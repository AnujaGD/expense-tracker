from flask import Flask, request, jsonify
import pytesseract
import cv2
import numpy as np
from werkzeug.utils import secure_filename
from PIL import Image
import re
from PyDictionary import PyDictionary

import nltk
from nltk.corpus import wordnet as wn

# Set the Tesseract executable path (if not in your PATH environment variable)
# On macOS/Linux, it's usually `/usr/local/bin/tesseract`
# On Windows, it could be `C:\\Program Files\\Tesseract-OCR\\tesseract.exe`
pytesseract.pytesseract.tesseract_cmd = r'/opt/homebrew/bin/tesseract'  # Adjust path for your system

app = Flask(__name__)
dictionary = PyDictionary()

# Health Check API
@app.route("/health", methods=["GET"])
def health_check():
    return jsonify({"status": "Server is running"}), 200


@app.route('/upload-receipt', methods=['POST'])
def upload_receipt():
    print("Received a request to /upload-receipt")
    if 'file' not in request.files:
        print("No file part in request")
        return jsonify(error="No file part"), 400
    
    file = request.files['file']
    if file.filename == '':
        print("No selected file")
        return jsonify(error="No selected file"), 400
    
    print(f"Processing file: {file.filename}")
    image = Image.open(file)
    text = pytesseract.image_to_string(image)
    print("OCR extraction complete")
    
    matching_strings = [s for s in text.split("\n") if contains_number_and_word(s)]
    food = []
    for iten in matching_strings:
        if(if_food(iten)):
            food.append(iten)
    print(food)
    item_price_map = extract_items_and_prices(food)
    print("Extracted item-price map:", item_price_map)

    return jsonify(receipt_text=text.split("\n"), items=item_price_map)

def extract_items_and_prices(food):
    item_price_map = {}
    
    for line in food:
        # match = re.search(r'^(.*?)[\s]+(\$\d+\.\d{2})$', line)
        line = re.sub(r'[^\w\s.,-]', '', line)
        match = re.search(r'([a-zA-Z\s]+)(\s+\d+(\.\d+)?)$', line)

        if match:
            item = match.group(1).strip()
            price = str(match.group(2))
            price = re.sub(r'[^0-9.]', '', price)  
            item_price_map[item] = price
    
    return item_price_map
from PyDictionary import PyDictionary

def if_food(query):

    words = query.split()
    
    for word in words:
        syns = wn.synsets(str(word), pos = wn.NOUN)
        for syn in syns:
            if 'food' in syn.lexname():
                return 1
    return 0
# Function to check if a string contains a number and a word
def contains_number_and_word(string):
    # Split the string using regex to match words and numbers
    words_and_numbers = re.findall(r'\b\w+\b', string)

    # Check if there's at least one number and one word
    has_number = any(char.isdigit() for char in words_and_numbers)
    has_word = any(char.isalpha() for char in words_and_numbers)

    return has_number and has_word
# Run the server
if __name__ == "__main__":
    app.run(host="0.0.0.0", port=8081, debug=True)
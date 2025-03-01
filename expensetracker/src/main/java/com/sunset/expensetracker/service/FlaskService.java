package com.sunset.expensetracker.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sunset.expensetracker.ReceiptResponse;
import com.sunset.expensetracker.entity.Expense;
import com.sunset.expensetracker.entity.Receipt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class FlaskService {

    @Autowired
    private RestTemplate restTemplate;
    ObjectMapper objectMapper = new ObjectMapper();

    public Receipt callFlaskEndpoint(MultipartFile file) throws IOException {
        String flaskUrl = "http://localhost:8081/upload-receipt"; // Flask server URL
        ByteArrayResource fileAsResource = new ByteArrayResource(file.getBytes()) {
            @Override
            public String getFilename() {
                return file.getOriginalFilename();
            }
        };
        // Create the multipart request
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", fileAsResource);

        // Create the HTTP headers with the correct content type
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Create the HttpEntity with the body and headers
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send the request to the Flask endpoint
        ResponseEntity<String> response = restTemplate.exchange(flaskUrl, HttpMethod.POST, requestEntity, String.class);

        return createReceipt(response);
    }

    private Receipt createReceipt(ResponseEntity<String> response) throws JsonProcessingException {
        ReceiptResponse receiptResponse = objectMapper.readValue(response.getBody(), ReceiptResponse.class);

        Receipt receipt = new Receipt();
        List<Expense> expenses= new ArrayList<>();
        receipt.setDate(LocalDate.now());
        receipt.setVendor("Not Available");
        receiptResponse.getItems().forEach((item, price) -> {
            Expense expense = new Expense(item,Double.parseDouble(price));
            expenses.add(expense);
        });
        receipt.setExpenses(expenses);
        return receipt;
    }
}
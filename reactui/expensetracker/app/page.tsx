"use client";
import { useState ,useEffect } from "react";
import { Trash2 } from "lucide-react"; // Import delete icon
import { Pencil, FileText, Plus  } from "lucide-react";

export default function ExpenseTracker() {
  const [expenses, setExpenses] = useState([]);
  const [description, setDescription] = useState("");
  const [amount, setAmount] = useState("");
  const [id, setId] = useState("");
  const [newExpense, setNewExpense] = useState({ description: "", amount: "" ,id: "" });
  const totalExpense = expenses.reduce((acc, expense) => acc + expense.amount, 0);
  const [isEditing, setIsEditing] = useState(false);
    // Receipt States
    const [receipts, setReceipts] = useState([]);
    const [newReceipt, setNewReceipt] = useState({ vendor: "", id: "", receiptNumber: "", expenses: [] });
    const [activeTab, setActiveTab] = useState("expenses"); // Tabs: expenses | receipts
    const [loading, setLoading] = useState(false);
    const [message, setMessage] = useState("");
    const [error, setError] = useState("");

    const deleteReceipt = async (receipt) => {
      if (!window.confirm("Are you sure you want to delete this receipt?")) return;
  
      try {
        const response = await fetch("http://localhost:8080/api/receipts", {
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify(receipt),
        });
  
        if (!response.ok) throw new Error("Failed to delete receipt");
  
        setReceipts(receipts.filter((r) => r.id !== receipt.id));
      } catch (error) {
        console.error("Error deleting receipt:", error);
        alert("Failed to delete receipt.");
      }
    };
  
    // Fetch Receipts from API
  const fetchReceipts = async () => {
    setLoading(true);
    setError("");
    try {
      const response = await fetch("http://localhost:8080/api/receipts");
      if (!response.ok) throw new Error("Failed to fetch receipts");

      const data = await response.json();
      
      setReceipts(data); // Set fetched receipts
    } catch (error) {
      console.error("Error fetching receipts:", error);
      setError("Failed to load receipts.");
    } finally {
      setLoading(false);
    }
  };

  // Calculate Total Expense for Each Receipt
  const calculateTotalAmount = (expenses) => {
    return expenses.reduce((sum, expense) => sum + (expense.amount || 0), 0);
  };

    // Handle Add Receipt
  const handleSaveReceipt = async () => {
    if (!newReceipt.vendor || !newReceipt.receiptNumber) {
      alert("Please fill all receipt fields");
      return;
    }

    setLoading(true);
    setMessage("");

    try {
      const response = await fetch("http://localhost:8080/api/receipts", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(newReceipt),
      });

      if (!response.ok) throw new Error("Failed to save receipt");

      setReceipts([...receipts, { ...newReceipt }]);
      setNewReceipt({ vendor: "",  receiptNumber: "", expenses: [] });
      setLoading(false);
      fetchReceipts();
    } catch (error) {
      setMessage("Error saving receipt.");
    }

    
  };
  const handleDeleteExpense = async (expense) => {
    try {
      const response = await fetch(`http://localhost:8080/api/expenses`, {
        method: "DELETE",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(expense), // Send full expense object
      });

      if (!response.ok) throw new Error(`HTTP Error! Status: ${response.status}`);

      console.log("Expense deleted successfully");
      fetchExpenses(); // Refresh list
    } catch (error) {
      console.error("Error deleting expense:", error);
    }
  };
  const handleUpdateExpense = async (expense) => {
    setNewExpense(expense); // Prefill form with existing data
    setIsEditing(true);

  };
   // Handle Add Expense to Receipt
   const handleAddExpenseToReceipt = () => {
    if (!newReceipt.expenses) newReceipt.expenses = [];
    setNewReceipt({
      ...newReceipt,
      expenses: [...newReceipt.expenses, {description: "", amount: "" }],
    });
  };
   // Handle Delete Expense from Receipt
   const handleDeleteExpenseFromReceipt = (index) => {
    const updatedExpenses = [...newReceipt.expenses];
    updatedExpenses.splice(index, 1);
    setNewReceipt({ ...newReceipt, expenses: updatedExpenses });
  };
  // Handle Expense Change Inside Receipt
  const handleReceiptExpenseChange = (index, field, value) => {
    const updatedExpenses = [...newReceipt.expenses];
    updatedExpenses[index][field] = value;
    setNewReceipt({ ...newReceipt, expenses: updatedExpenses });
  };
  const fetchExpenses = async () => {
    try {

      const response = await fetch("http://localhost:8080/api/expenses"); // Replace with real API
      const data = await response.json();
      console.log("Fetched data:", data); // Debugging line

      // Extracting relevant data (assuming API structure)
      const formattedExpenses = data.map((item) => ({
        description: item.description,  // Assuming API has 'title'
        amount: item.amount,    
        id : item.id   // Assuming API has 'price'
      }));

      setExpenses(formattedExpenses);
    } catch (error) {
      console.error("Error fetching expenses:", error);
    }
  };
 // Fetch Receipts Only When "Receipts" Tab is Active
 useEffect(() => {
  if (activeTab === "receipts") {
    fetchReceipts();
  }
  if (activeTab === "expenses") {
    fetchExpenses();
  }
}, [activeTab]); 
// Handle form submission (Add Expense)
const handleAddExpense = async () => {
  const url = isEditing
  ? `http://localhost:8080/api/expenses/update`
  : `http://localhost:8080/api/expenses`;
  if (!newExpense.description || !newExpense.amount) {
    alert("Please enter description and amount");
    return;
  }

  try {
    const response = await fetch("http://localhost:8080/api/expenses", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(newExpense),
    });

    if (!response.ok) throw new Error(`HTTP Error! Status: ${response.status}`);

    console.log("Expense added successfully");
    setNewExpense({ description: "", amount: "" ,id:""});
    setIsEditing(false);

    fetchExpenses(); // Refresh list after adding
  } catch (error) {
    console.error("Error adding expense:", error);
  }
};


  const handleChange = (e) => {
    setNewExpense({ ...newExpense, [e.target.name]: e.target.value });
  };
  const handleReceiptChange = (e) => {
    setNewReceipt({ ...newReceipt, [e.target.name]: e.target.value });
  };
  
  return (
    <div className="max-w-md mx-auto p-4 bg-gray-100 rounded-lg shadow-lg">
      {/* Tabs */}
      <div className="flex justify-between border-b-2 pb-2 mb-4">
        <button
          className={`flex-1 py-2 text-center ${
            activeTab === "expenses" ? "border-b-2 border-blue-500 text-blue-500" : "text-gray-600"
          }`}
          onClick={() => setActiveTab("expenses")}
        >
          Expenses
        </button>
        <button
          className={`flex-1 py-2 text-center ${
            activeTab === "receipts" ? "border-b-2 border-blue-500 text-blue-500" : "text-gray-600"
          }`}
          onClick={() => setActiveTab("receipts")}
        >
          Receipts
        </button>
      </div>

      {/* Expenses Tab */}
      {activeTab === "expenses" && (
        <>
          <h1 className="text-2xl font-bold text-black mb-4">Expense Tracker</h1>
          <input
            type="text"
            name="description"
            placeholder="Expense Description"
            value={newExpense.description}
            onChange={handleChange}
            className="w-full p-2 mb-2 border border-gray-300 rounded text-black"
          />
          <input
            type="number"
            name="amount"
            placeholder="Amount"
            value={newExpense.amount}
            onChange={handleChange}
            className="w-full p-2 mb-2 border border-gray-300 rounded text-black"
          />
          <button
            onClick={handleAddExpense}
            className={`w-full py-2 rounded text-white ${
              isEditing ? "bg-green-500 hover:bg-green-600" : "bg-blue-500 hover:bg-blue-600"
            }`}
          >
            {isEditing ? "Update Expense" : "Add Expense"}
          </button>
          <ul className="mt-4">
  {expenses.map((expense) => (
    <li
      key={expense.id}
      className="bg-white p-3 mb-2 border border-gray-300 rounded flex items-center justify-between"
    >
      {/* Expense Description */}
      <span className="flex-1 text-gray-800">{expense.description}</span>

      {/* Amount */}
      <span className="flex-1 text-gray-600 text-right">${expense.amount}</span>

      {/* Action Buttons (Edit & Delete) */}
      <div className="flex-1 flex justify-end space-x-3">
        {/* Edit Button */}
        <button onClick={() => handleUpdateExpense(expense)}>
          <Pencil className="text-blue-500 hover:text-blue-700" size={20} />
        </button>

        {/* Delete Button */}
        <button onClick={() => handleDeleteExpense(expense)}>
          <Trash2 className="text-red-500 hover:text-red-700" size={20} />
        </button>
      </div>
    </li>
  ))}
</ul>
        </>
      )}

      {/* Receipts Tab */}
      {activeTab === "receipts" && (
        <>
          <h1 className="text-2xl font-bold text-black mb-4">Receipts</h1>
          <input
            type="text"
            name="vendor"
            placeholder="Vendor Name"
            value={newReceipt.vendor}
            onChange={handleReceiptChange}
            className="w-full p-2 mb-2 border border-gray-300 rounded text-black"
          />
         
          <input
            type="text"
            name="receiptNumber"
            placeholder="Receipt Number"
            value={newReceipt.receiptNumber}
            onChange={handleReceiptChange}
            className="w-full p-2 mb-2 border border-gray-300 rounded text-black"
          />
         {/* Add Expense Button */}
         <button
            onClick={handleAddExpenseToReceipt}
            className="flex items-center space-x-2 text-white bg-gray-600 hover:bg-gray-700 px-4 py-2 rounded mb-2"
          >
            <Plus size={16} />
            <span>Add Expense</span>
          </button>

          {/* List of Expenses Under Receipt */}
          {newReceipt.expenses.length > 0 && (
            <div className="bg-gray-200 p-3 rounded mb-3">
              <h3 className="font-bold text-black mb-2">Receipt Expenses</h3>
              {newReceipt.expenses.map((expense, index) => (
                <div key={index} className="flex gap-2 mb-2">
                  <input
                    type="text"
                    placeholder="Expense Description"
                    value={expense.description}
                    onChange={(e) => handleReceiptExpenseChange(index, "description", e.target.value)}
                    className="flex-1 p-2 border border-gray-300 rounded text-black"
                  />
                  <input
                    type="number"
                    placeholder="Amount"
                    value={expense.amount}
                    onChange={(e) => handleReceiptExpenseChange(index, "amount", e.target.value)}
                    className="w-24 p-2 border border-gray-300 rounded text-black"
                  />
                  <button onClick={() => handleDeleteExpenseFromReceipt(index)} className="text-red-500 hover:text-red-700">
                    <Trash2 size={18} />
                  </button>
                </div>
              ))}
            </div>
          )}

          {/* Save Receipt */}
          <button onClick={handleSaveReceipt} className="w-full py-2 bg-blue-500 text-white rounded hover:bg-blue-600">
            Add Receipt
          </button>
          <>
          <h1 className="text-2xl font-bold text-black mb-4">Receipts</h1>

          {loading ? (
            <p className="text-center text-black">Loading receipts...</p>
          ) : message ? (
            <p className="text-center text-red-500">{message}</p>
          ) : receipts.length === 0 ? (
            <p className="text-center text-gray-500">No receipts found.</p>
          ) : (
            <ul className="space-y-3">
              {receipts.map((receipt) => (
                <li
                  key={receipt.id}
                  className="bg-white p-3 border border-gray-300 rounded flex justify-between items-center"
                >
                  <div>
                    <p className="text-lg font-semibold text-black">{receipt.vendor}</p>
                    <p className="text-sm text-gray-600">Receipt ID: {receipt.receiptId}</p>
                  </div>
                  <div className="text-right">
                    <span className="text-xl font-bold text-green-600">
                      ${calculateTotalAmount(receipt.expenses).toFixed(2)}
                    </span>
                  </div>
                  <button
                      onClick={() => deleteReceipt(receipt)}
                      className="p-2 bg-red-500 text-white rounded-lg hover:bg-red-700"
                    >
                      <Trash2 size={18} />
                    </button>
                </li>
              ))}
            </ul>
          )}
        </>
        </>
      )}
    </div>
  );
}
package com.sunset.expensetracker;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class DashboardHome extends Fragment {

    private ListView expenseListView;
    private Button createExpenseButton;
    private ArrayList<String> expenseList;

    public DashboardHome() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        expenseListView = view.findViewById(R.id.expenseListView);
//        createExpenseButton = view.findViewById(R.id.createExpenseButton);
        expenseList = new ArrayList<>();

        // Call the function to fetch expenses from the API
        fetchExpenses();

        // You can add an onClickListener to the button to create an expense
//        createExpenseButton.setOnClickListener(v -> {
//            // Handle create expense button click
//        });

        return view;
    }

    // Function to fetch expenses from the API
    private void fetchExpenses() {
        // Use AsyncTask to perform network operations on a background thread
        new FetchExpensesTask().execute("http://10.0.2.2:8080/api/expenses");
    }

    // AsyncTask class for making the API call on a background thread
    private class FetchExpensesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                // Set up the connection to the API
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setConnectTimeout(5000);
                urlConnection.setReadTimeout(5000);

                // Read the response from the API
                InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                int data = inputStreamReader.read();
                while (data != -1) {
                    response += (char) data;
                    data = inputStreamReader.read();
                }
                inputStreamReader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                // Parse the response JSON
                JSONArray expensesArray = new JSONArray(result);
                for (int i = 0; i < expensesArray.length(); i++) {
                    JSONObject expense = expensesArray.getJSONObject(i);
                    String expenseDetails = "Expense: " + expense.getString("description") +
                            ", Amount: " + expense.getDouble("amount");
                    expenseList.add(expenseDetails);
                }

                // Set the adapter to display the list in the ListView
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_list_item_1, expenseList);
                expenseListView.setAdapter(adapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
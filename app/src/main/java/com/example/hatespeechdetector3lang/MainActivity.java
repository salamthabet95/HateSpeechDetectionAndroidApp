package com.example.hatespeechdetector3lang;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    // UI Components
    private EditText editTextInput;
    private Button buttonAnalyze, buttonClear;
    private TextView textViewResult, textViewConfidence;
    private ProgressBar progressBar;
    private CardView cardResults;
    private RequestQueue requestQueue;

    // Constants
    private static final String TAG = "HateSpeechDetector";
    private static final String API_BASE_URL = "http://10.0.2.2:5000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        initializeViews();

        // Initialize Volley
        requestQueue = Volley.newRequestQueue(this);

        // Set button listeners
        setupButtonListeners();

        // Test API connection on startup
        testApiConnection();
    }

    private void initializeViews() {
        editTextInput = findViewById(R.id.editTextInput);
        buttonAnalyze = findViewById(R.id.buttonAnalyze);
        buttonClear = findViewById(R.id.buttonClear);
        textViewResult = findViewById(R.id.textViewResult);
        textViewConfidence = findViewById(R.id.textViewConfidence);
        progressBar = findViewById(R.id.progressBar);
        cardResults = findViewById(R.id.cardResults);
    }

    private void setupButtonListeners() {
        buttonAnalyze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                analyzeText();
            }
        });

        if (buttonClear != null) {
            buttonClear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clearResults();
                }
            });
        }
    }

    private void testApiConnection() {
        Log.d(TAG, "Testing API connection...");

        String url = API_BASE_URL + "/health";
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString("status");
                            boolean modelReady = response.getBoolean("model_ready");

                            if (modelReady) {
                                Log.d(TAG, "✅ API connected and model ready");
                                Toast.makeText(MainActivity.this, "✅ AI Model Ready", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.w(TAG, "⚠️ API connected but model not ready");
                                Toast.makeText(MainActivity.this, "⚠️ Model Loading...", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error parsing health response", e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "❌ API connection failed", error);
                        Toast.makeText(MainActivity.this, "❌ API Server Not Available", Toast.LENGTH_LONG).show();
                    }
                }
        );

        requestQueue.add(request);
    }

    private void analyzeText() {
        String text = editTextInput.getText().toString().trim();

        // Input validation
        if (text.isEmpty()) {
            showError("Please enter some text to analyze");
            return;
        }

        if (text.length() < 3) {
            showError("Text must be at least 3 characters long");
            return;
        }

        if (text.length() > 500) {
            showError("Text is too long (maximum 500 characters)");
            return;
        }

        // Show loading state
        setLoadingState(true);

        // Create JSON request
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("text", text);
        } catch (JSONException e) {
            Log.e(TAG, "Error creating request JSON", e);
            setLoadingState(false);
            showError("Error preparing request");
            return;
        }

        // Make API request
        String url = API_BASE_URL + "/predict";
        Log.d(TAG, "Making prediction request for text: " + text.substring(0, Math.min(text.length(), 50)) + "...");

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                url,
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        handleResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        handleError(error);
                    }
                }
        );

        // Set timeout
        request.setRetryPolicy(new com.android.volley.DefaultRetryPolicy(
                15000, // 15 seconds timeout
                1, // no retries
                com.android.volley.DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        ));

        requestQueue.add(request);
    }

    private void handleResponse(JSONObject response) {
        setLoadingState(false);

        try {
            boolean success = response.getBoolean("success");

            if (success) {
                // Extract main results only
                String prediction = response.getString("prediction");
                double confidence = response.getDouble("confidence");
                double hateProbability = response.getDouble("hate_probability");

                // Display simple results
                displaySimpleResults(prediction, confidence, hateProbability);

                // Log additional analysis if available
                if (response.has("analysis")) {
                    JSONObject analysis = response.getJSONObject("analysis");
                    logDetailedAnalysis(analysis);
                }

                Log.d(TAG, "✅ Analysis successful: " + prediction + " (" + String.format("%.3f", confidence) + ")");
                Toast.makeText(this, "✅ Analysis Complete!", Toast.LENGTH_SHORT).show();

            } else {
                String error = response.getString("error");
                Log.e(TAG, "API returned error: " + error);
                showError("Analysis failed: " + error);
            }

        } catch (JSONException e) {
            Log.e(TAG, "Error parsing response JSON", e);
            showError("Error parsing server response");
        }
    }

    private void displaySimpleResults(String prediction, double confidence, double hateProbability) {

        // Show results card
        if (cardResults != null) {
            cardResults.setVisibility(View.VISIBLE);
        }

        // Main prediction with emoji
        String predictionText = prediction.contains("Hate Speech") ? "🚨 " + prediction : "✅ " + prediction;
        textViewResult.setText(predictionText);

        // Set color based on prediction
        if (prediction.contains("Hate Speech")) {
            textViewResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            textViewResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        }

        // Simple confidence display
        if (textViewConfidence != null) {
            textViewConfidence.setText(String.format("🎯 Confidence: %.1f%% | 🔍 Hate Probability: %.1f%%",
                    confidence * 100, hateProbability * 100));
        }
    }

    private void logDetailedAnalysis(JSONObject analysis) {
        try {
            Log.d(TAG, "=== DETAILED ANALYSIS ===");

            if (analysis.has("text_length")) {
                Log.d(TAG, "📝 Text length: " + analysis.getInt("text_length"));
            }
            if (analysis.has("word_count")) {
                Log.d(TAG, "📊 Word count: " + analysis.getInt("word_count"));
            }
            if (analysis.has("sentence_count")) {
                Log.d(TAG, "📈 Sentences: " + analysis.getInt("sentence_count"));
            }
            if (analysis.has("language_detected")) {
                Log.d(TAG, "🌍 Language: " + analysis.getString("language_detected"));
            }

            Log.d(TAG, "========================");
        } catch (JSONException e) {
            Log.e(TAG, "Error logging detailed analysis", e);
        }
    }

    private void handleError(VolleyError error) {
        setLoadingState(false);

        String errorMsg = "Connection failed";

        // Detailed error analysis
        if (error.networkResponse != null) {
            int statusCode = error.networkResponse.statusCode;
            errorMsg = "Server error (Code: " + statusCode + ")";

            if (statusCode == 400) {
                errorMsg = "Invalid request - check your text";
            } else if (statusCode == 500) {
                errorMsg = "Server error - model may not be loaded";
            }
        } else {
            // Check specific error types
            if (error instanceof com.android.volley.TimeoutError) {
                errorMsg = "Request timeout - server took too long";
            } else if (error instanceof com.android.volley.NoConnectionError) {
                errorMsg = "No connection - check if API server is running";
            } else if (error instanceof com.android.volley.NetworkError) {
                errorMsg = "Network error - check your connection";
            }
        }

        Log.e(TAG, "❌ Request failed: " + errorMsg, error);
        showError(errorMsg);

        // Show troubleshooting hint
        Toast.makeText(this, "💡 Make sure the Python API server is running on port 5000", Toast.LENGTH_LONG).show();

        // Detailed logging for debugging
        logDetailedError(error);
    }

    private void logDetailedError(VolleyError error) {
        Log.e(TAG, "=== VOLLEY ERROR DETAILS ===");
        Log.e(TAG, "Error: " + error.toString());
        Log.e(TAG, "API URL: " + API_BASE_URL + "/predict");

        if (error.networkResponse != null) {
            Log.e(TAG, "Status Code: " + error.networkResponse.statusCode);
            if (error.networkResponse.data != null) {
                Log.e(TAG, "Response Data: " + new String(error.networkResponse.data));
            }
        }

        if (error.getCause() != null) {
            Log.e(TAG, "Cause: " + error.getCause().toString());
        }

        Log.e(TAG, "=== END ERROR DETAILS ===");
    }

    private void setLoadingState(boolean loading) {
        if (loading) {
            progressBar.setVisibility(View.VISIBLE);
            buttonAnalyze.setEnabled(false);
            buttonAnalyze.setText("🔄 Analyzing...");

            if (cardResults != null) {
                cardResults.setVisibility(View.GONE);
            }
        } else {
            progressBar.setVisibility(View.GONE);
            buttonAnalyze.setEnabled(true);
            buttonAnalyze.setText("🔍 Analyze Text");
        }
    }

    private void showError(String message) {
        if (cardResults != null) {
            cardResults.setVisibility(View.VISIBLE);
        }

        textViewResult.setText("❌ Error");
        textViewResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

        if (textViewConfidence != null) {
            textViewConfidence.setText(message);
        }
    }

    private void clearResults() {
        editTextInput.setText("");

        if (cardResults != null) {
            cardResults.setVisibility(View.GONE);
        }

        textViewResult.setText("");

        if (textViewConfidence != null) {
            textViewConfidence.setText("");
        }

        Toast.makeText(this, "Cleared", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}
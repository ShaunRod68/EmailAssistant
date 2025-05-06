package com.example.androidproject.fragments;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.androidproject.DatabaseHelper;
import com.example.androidproject.R;
import com.example.androidproject.emailmodel.SugModel;
import com.google.mediapipe.tasks.genai.llminference.LlmInferenceSession;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


public class HomeFragment extends Fragment {

    TextView sug1, sug2, sug3, sug4, display;
    EditText input;
    Button save, copy, email,load;
    private SugModel sugModel;
    Boolean loaded;
    String email_text;
    int email_length;
    DatabaseHelper databaseHelper;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        MainActivity activity = (MainActivity) requireActivity();
//        LlmInference llmInference = activity.llmInference;

        sugModel = new SugModel();
        loaded = false;


        display = view.findViewById(R.id.display);
        sug1 = view.findViewById(R.id.sug1);
        sug2 = view.findViewById(R.id.sug2);
        sug3 = view.findViewById(R.id.sug3);
        sug4 = view.findViewById(R.id.sug4);
        input = view.findViewById(R.id.input);
        save = view.findViewById(R.id.save);
        copy = view.findViewById(R.id.copy);
        email = view.findViewById(R.id.email);
        load = view.findViewById(R.id.load_email_model);
        databaseHelper = new DatabaseHelper(getContext());

        load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!loaded){
                    AlertDialog loadingDialog = new AlertDialog.Builder(requireContext())
                            .setTitle("Loading Model")
                            .setMessage("Please wait...")
                            .setCancelable(false)
                            .create();
                    loadingDialog.show();

                    new Thread(() -> {
                        // Run model loading in background
                        try {
                            sugModel.initializeModel(requireContext());
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                        requireActivity().runOnUiThread(() -> {
                            loaded = true;
                            loadingDialog.dismiss();
                            Toast.makeText(getContext(), "Model loaded!", Toast.LENGTH_SHORT).show();
                        });
                    }).start();
                }
            }
        });





        input.addTextChangedListener(new TextWatcher() {
            String previousText = "";
            StringBuilder records = new StringBuilder();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                previousText = s.toString();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!loaded){
                    AlertDialog NoModelDialog = new AlertDialog.Builder(requireContext())
                            .setTitle("Model Not Loaded")
                            .setMessage("Click On Load Model button before typing")
                            .create();
                    NoModelDialog.show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String currentText = s.toString();

                // Guard against invalid substring range
                if (previousText == null) {
                    previousText = "";
                }

                String added = "";
                if (currentText.length() >= previousText.length()) {
                    added = currentText.substring(previousText.length());
                }
                records.setLength(0);
                records.append(currentText);

                if (added.contains(" ")) {
                    sugModel.createNewSession();
                    LlmInferenceSession session = sugModel.getLlmInferenceSession();
                    String last200Chars = currentText.length() > 200
                            ? currentText.substring(currentText.length() - 200)
                            : currentText;
                    session.addQueryChunk(last200Chars);
                    String rawPredictions = session.generateResponse();

                    List<String> sortedWords = Arrays.stream(rawPredictions.split("\\s+"))  // Split by whitespace
                            .filter(word -> !word.isEmpty())  // Remove empty strings
                            .sorted((a, b) -> Integer.compare(b.length(), a.length()))  // Sort descending by length
                            .collect(Collectors.toList());

                    sug1.setText(sortedWords.get(0));
                    sug2.setText(sortedWords.get(1));
                    sug3.setText(sortedWords.get(2));
                    sug4.setText(sortedWords.get(3));
                }


                display.setText(currentText);

                previousText = currentText;
            }
        });
        sug1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = sug1.getText().toString();
                input.append(text);
            }
        });
        sug2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = sug2.getText().toString();
                input.append(text);
            }
        });
        sug3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = sug3.getText().toString();
                input.append(text);
            }
        });
        sug4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = sug4.getText().toString();
                input.append(text);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = display.getText().toString();
                if (!text.isEmpty()){
                    email_text = text;
                    email_length = text.length();
                    boolean result = databaseHelper.addEmail(email_text, email_length);
                    if(result){
                        Toast.makeText(getContext(), "Added to Private Database", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getContext(), "Did not add", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = display.getText().toString();
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(intent);


            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = display.getText().toString();
                ClipboardManager clipboard = (ClipboardManager) v.getContext().getSystemService(getContext().CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", text);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(v.getContext(), "Text copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
package com.example.androidproject.fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidproject.DatabaseHelper;
import com.example.androidproject.chatmodel.ChatAdapter;
import com.example.androidproject.chatmodel.ChatMessage;
import com.example.androidproject.chatmodel.ChatViewModel;
import com.example.androidproject.R;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {
    private EditText userInput;
    private ImageButton sendButton;
    private Button load;
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messages = new ArrayList<>();
    private ChatViewModel viewModel;
    private TextView tokenView;
    SeekBar seekBar;
    Boolean loaded;
    String message;
    String sender;
    int message_lenght;
    DatabaseHelper databaseHelper;


    public ChatFragment() {
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
        return inflater.inflate(R.layout.fragment_chat, container, false);



    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseHelper = new DatabaseHelper(getContext());

        viewModel= new ViewModelProvider(requireActivity()).get(ChatViewModel.class);

        loaded = false;



        userInput = view.findViewById(R.id.user_input);
        sendButton = view.findViewById(R.id.send_button);
        chatRecyclerView = view.findViewById(R.id.chat_recycler);
        load = view.findViewById(R.id.load);
        tokenView = view.findViewById(R.id.tokenView);
        seekBar = view.findViewById(R.id.seekBar);
        seekBar.setMax(4000);


        int tokens = seekBar.getProgress();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tokenView.setText(String.valueOf(progress)
                );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


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
                        viewModel.initializeModel(requireContext(),tokens);

                        requireActivity().runOnUiThread(() -> {
                            loaded = true;
                            loadingDialog.dismiss();
                            Toast.makeText(getContext(), "Model loaded!", Toast.LENGTH_SHORT).show();
                        });
                    }).start();
                }
            }
        });
        chatAdapter = new ChatAdapter(messages);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        chatRecyclerView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loaded){
                    String message = userInput.getText().toString();
                    if (!message.isEmpty()) {
                        addUserMessage(message);
                        userInput.setText("");
                        // TODO: Send to model
                        viewModel.sendMessage(message, ChatFragment.this);
                    }
                }else {
                    Toast.makeText(getContext(), "Load the LLM First", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void addUserMessage(String text) {
        databaseHelper.addChat(text,"user",text.length());
        ChatMessage message = new ChatMessage(text, "user");
        messages.add(message);
        chatAdapter.notifyItemInserted(messages.size() - 1);
        chatRecyclerView.scrollToPosition(messages.size() - 1);
    }
    public void addBotMessage(String text) {
        ChatMessage message = new ChatMessage(text, "bot");
        messages.add(message);
        chatAdapter.notifyItemInserted(messages.size() - 1);
        chatRecyclerView.scrollToPosition(messages.size() - 1);
    }

    public void updateLastBotMessage(String text) {
        if (!messages.isEmpty()) {
            ChatMessage lastMessage = messages.get(messages.size() - 1);
            if ("bot".equals(lastMessage.getAuthor())) {
                lastMessage.setText(text);
                chatAdapter.notifyItemChanged(messages.size() - 1);
                chatRecyclerView.scrollToPosition(messages.size() - 1);
            }
        }
    }

    public void addChatToDatabase(StringBuilder fullResponse) {
        String text = fullResponse.toString();
        databaseHelper.addChat(text,"bot",text.length());
    }
}
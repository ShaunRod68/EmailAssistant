package com.example.androidproject.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.androidproject.DatabaseHelper;
import com.example.androidproject.R;
import com.example.androidproject.modal.ChatHistoryAdapter;
import com.example.androidproject.modal.ChatItem;
import com.example.androidproject.modal.EmailAdapter;
import com.example.androidproject.modal.EmailItem;
import com.google.android.material.tabs.TabLayout;

import java.util.List;


public class HistoryFragment extends Fragment {
    private TabLayout tabLayout;
    private RecyclerView recyclerView;
    private Button btnPrevious, btnNext;
    private DatabaseHelper databaseHelper;
    private List<String> emailDates, chatDates;
    private int currentEmailDateIndex = 0, currentChatDateIndex = 0;
    private boolean showingEmails = true;


    public HistoryFragment() {
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
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());

        // Initialize UI components
        tabLayout = view.findViewById(R.id.tabLayout);
        recyclerView = view.findViewById(R.id.historyRecycle);
        btnPrevious = view.findViewById(R.id.button);
        btnNext = view.findViewById(R.id.button2);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load dates
        emailDates = databaseHelper.getAllEmailDates();
        chatDates = databaseHelper.getAllChatDates();


        // Set up tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    showingEmails = true;
                    showCurrentDateItems();
                } else {
                    showingEmails = false;
                    showCurrentDateItems();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
        btnPrevious.setOnClickListener(v -> {
            if (showingEmails) {
                if (currentEmailDateIndex < emailDates.size() - 1) {
                    currentEmailDateIndex++;
                    showCurrentDateItems();
                }
            } else {
                if (currentChatDateIndex < chatDates.size() - 1) {
                    currentChatDateIndex++;
                    showCurrentDateItems();
                }
            }
        });

        btnNext.setOnClickListener(v -> {
            if (showingEmails) {
                if (currentEmailDateIndex > 0) {
                    currentEmailDateIndex--;
                    showCurrentDateItems();
                }
            } else {
                if (currentChatDateIndex > 0) {
                    currentChatDateIndex--;
                    showCurrentDateItems();
                }
            }
        });

        // Show emails by default
        showingEmails = true;
        showCurrentDateItems();
    }

    private void showCurrentDateItems() {
        if (showingEmails) {
            if (!emailDates.isEmpty()) {
                String currentDate = emailDates.get(currentEmailDateIndex);
                List<EmailItem> emails = databaseHelper.getEmailsOnDate(currentDate);
                // You'll need to create an EmailAdapter similar to the ChatAdapter
                recyclerView.setAdapter(new EmailAdapter(emails));
            }
        } else {
            if (!chatDates.isEmpty()) {
                String currentDate = chatDates.get(currentChatDateIndex);
                List<ChatItem> chats = databaseHelper.getChatsOnDate(currentDate);
                // You'll need to create a ChatHistoryAdapter
                recyclerView.setAdapter(new ChatHistoryAdapter(chats));
            }
        }

    }
}
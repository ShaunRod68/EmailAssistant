package com.example.androidproject.chatmodel;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.ViewModel;

import com.example.androidproject.fragments.ChatFragment;
import com.google.mediapipe.tasks.genai.llminference.LlmInference;
import com.google.mediapipe.tasks.genai.llminference.LlmInferenceSession;
import com.google.mediapipe.tasks.genai.llminference.ProgressListener;


public class ChatViewModel extends ViewModel {

    private LlmInference llmInference;
    private LlmInferenceSession llmInferenceSession;
    private boolean isGenerating = false;
    private StringBuilder fullResponse = new StringBuilder();

    public void initializeModel(Context context,int tokens) {
        LlmInference.LlmInferenceOptions options = LlmInference.LlmInferenceOptions.builder()
                .setModelPath("/data/local/tmp/llm/gemma-2b-it-gpu-int8.bin")
                .setMaxTokens(tokens)
                .build();

        llmInference = LlmInference.createFromOptions(context, options);
        createNewSession();
    }

    private void createNewSession() {
        // Close previous session if exists
        if (llmInferenceSession != null) {
            llmInferenceSession.close();
        }

        // Create new session
        LlmInferenceSession.LlmInferenceSessionOptions sessionOptions =
                LlmInferenceSession.LlmInferenceSessionOptions.builder()
                        .setTopK(40)
                        .build();

        llmInferenceSession = LlmInferenceSession.createFromOptions(llmInference, sessionOptions);
    }

    public void sendMessage(String message, ChatFragment fragment) {
        if (isGenerating) return;

        isGenerating = true;
        fullResponse.setLength(0); // Clear previous response

        // Create new session for this message
        createNewSession();

        // Add loading message
        fragment.addBotMessage("Generating...");

        llmInferenceSession.addQueryChunk(message);
        llmInferenceSession.generateResponseAsync(new ProgressListener<String>() {
            @Override
            public void run(String partialResult, boolean done) {
                new Handler(Looper.getMainLooper()).post(() -> {
                    fullResponse.append(partialResult);

                    if (done) {
                        // Replace the loading message with final response
                        fragment.updateLastBotMessage(fullResponse.toString());
                        isGenerating = false;
                        fragment.addChatToDatabase(fullResponse);
                    } else {
                        // Update the loading message with streaming text
                        fragment.updateLastBotMessage(fullResponse.toString());
                    }
                });
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (llmInferenceSession != null) {
            llmInferenceSession.close();
        }
        if (llmInference != null) {
            llmInference.close();
        }
    }

}

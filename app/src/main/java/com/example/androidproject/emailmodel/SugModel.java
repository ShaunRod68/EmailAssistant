package com.example.androidproject.emailmodel;

import android.content.Context;

import androidx.lifecycle.ViewModel;

import com.google.mediapipe.tasks.genai.llminference.LlmInference;
import com.google.mediapipe.tasks.genai.llminference.LlmInferenceSession;

public class SugModel extends ViewModel {
    private LlmInference llmInference;
    private LlmInferenceSession llmInferenceSession;
    private boolean isInitialized = false;

    public void initializeModel(Context context) throws Exception {
        try {
            LlmInference.LlmInferenceOptions options = LlmInference.LlmInferenceOptions.builder()
                    .setModelPath("/data/local/tmp/llm/gemma3-1b-it-int4.task")
                    .setPreferredBackend(LlmInference.Backend.CPU)
                    .setMaxTokens(50)
                    .build();

            llmInference = LlmInference.createFromOptions(context, options);
            createNewSession();
            isInitialized = true;
        } catch (Exception e) {
            isInitialized = false;
            throw new Exception("Model initialization failed: " + e.getMessage());
        }
    }

    public void createNewSession() {
        if (llmInference == null) return;

        if (llmInferenceSession != null) {
            llmInferenceSession.close();
        }

        LlmInferenceSession.LlmInferenceSessionOptions sessionOptions =
                LlmInferenceSession.LlmInferenceSessionOptions.builder()
                        .setTopK(40)
                        .build();

        llmInferenceSession = LlmInferenceSession.createFromOptions(llmInference, sessionOptions);
    }

    public boolean isInitialized() {
        return isInitialized;
    }

    public LlmInferenceSession getLlmInferenceSession() {
        if (!isInitialized) {
            throw new IllegalStateException("Model not initialized");
        }
        return llmInferenceSession;
    }

    @Override
    protected void onCleared() {
        if (llmInferenceSession != null) {
            llmInferenceSession.close();
        }
        if (llmInference != null) {
            llmInference.close();
        }
        super.onCleared();
    }
}

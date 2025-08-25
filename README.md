# Email Assistant App  

A powerful, offline-first Email Assistant application for Android, powered by on-device AI.  
This app helps you draft emails with smart suggestions and provides a private chat interface, all while working completely offline.  

---

##Key Features  

- **Smart Email Suggestions**: Get intelligent, 4-word email continuations as you type to speed up your writing process.  
- **Private AI Chat**: A private and secure chat interface to interact with the AI model.  
- **100% Offline & Private**: No internet connection is needed. All your data and interactions are stored locally and never leave your device.  
- **Fast & Responsive**: Optimized for performance on flagship Android devices, with a response time of ~500ms on a Pixel 8.  
- **History**: Keep track of your past emails and chats, organized by date.  
- **Multi-language Support**: The app supports both English and Hindi.  
- **Customizable AI**: Adjust the AI model's token limit for the chat feature to control the length of the responses.  
- **Easy Export**: Copy your generated text to the clipboard or send it directly to your email client.  

---

## How It Works  

The app leverages the power of a **Gemma 3B Large Language Model** running directly on your device.  
This is made possible by the **MediaPipe tasks-genai** library, which allows for efficient on-device inference.  

- Email suggestions and chat responses are generated locally, ensuring your data remains private.  
- All email and chat history is stored in a **local SQLite database** on your device.  

---

##  Technical Details  

- **AI Model**: 4-bit quantized Gemma 3B model (email suggestions) + Gemma 2B model (chat feature).  
- **On-Device AI**: Google MediaPipe's `tasks-genai` library.  
- **Language**: Java  
- **Database**: SQLite (local storage).  
- **UI**: Android XML layouts with Material Design components.  
- **Architecture**: Fragment-based architecture with ViewModel to manage AI model and UI logic.  

---

##  Setup and Installation  

### Model Installation (Required)  

1. Download the **4-bit quantized Gemma model**.  
2. Push the model to your device using ADB:  

```bash
adb shell mkdir -p /data/local/tmp/llm/
adb push gemma-3b-it.task /data/local/tmp/llm/
```
## Privacy  

Your privacy is a top priority. This app is designed to be secure and private:  

- **Offline by Design**: Works entirely without an internet connection.  
- **On-Device Processing**: All AI processing happens locally.  
- **Local Storage**: All emails and chats are stored in a local database.  

---

## Future Roadmap  

- Implement **model training** to personalize suggestions.  
- Introduce more **customization options** for the AI model.  

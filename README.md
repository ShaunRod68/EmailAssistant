# Email Assistant App ✉️🤖

Offline AI-powered email drafting assistant using Gemma 3B on-device LLM.

## Key Features
- 📝 **Smart suggestions** - Get 4-word email continuations as you type  
- 🔒 **100% offline** - No internet needed  
- ⚡ **Fast** - Optimized for flagship Android devices

- 
## Technical Details
- Uses MediaPipe's `tasks-genai` library
- Quantized Gemma 3B model (4-bit)
- ~500ms response time on Pixel 8

## Setup Instructions

### Model Installation (Required)
1. Download the 4-bit quantized Gemma model
2. Push to device via ADB:
   ```bash
   adb shell mkdir -p /data/local/tmp/llm/
   adb push gemma-3b-it-quantized.task /data/local/tmp/llm/

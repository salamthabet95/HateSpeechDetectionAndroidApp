# Hate Speech Detection Android App

Mobile client application for real-time hate speech detection in Arabic, Turkish, and English text.

## Overview

This Android app provides an intuitive interface for analyzing text content for hate speech. It communicates with a Flask API backend to deliver accurate multilingual text classification with detailed analysis results.

## Features

- **Clean Material Design Interface**
- **Multi-language Support**: Arabic, Turkish, English
- **Real-time Analysis** with progress indicators
- **Detailed Results Display**:
  - Classification result (Hate Speech/Normal)
  - Confidence percentage 
  - Detected language
  - Color-coded risk levels
- **Text Input Validation** (up to 500 characters)
- **Clear and Analyze Actions**

## Screenshots

### Main Interface
- Text input area with multi-line support
- Prominent analysis and clear buttons
- Progress indicator during processing

### Results Display
- Organized card-based result presentation
- Risk level indicators with color coding
- Comprehensive analysis information

## Prerequisites

### API Backend
This app requires the Flask API backend to be running:

**API Repository**: [HateSpeechDetectionAPI-ArTrEn](https://github.com/salamthabet95/HateSpeechDetectionAPI-ArTrEn)

Follow the API setup instructions to:
1. Install Python dependencies
2. Configure the ML model
3. Start the Flask server

### Android Development
- Android Studio
- Android SDK API 21+
- Device or emulator for testing

## Installation

1. **Setup API Backend First**:
   ```bash
   git clone https://github.com/salamthabet95/HateSpeechDetectionAPI-ArTrEn.git
   cd HateSpeechDetectionAPI-ArTrEn
   pip install -r requirements.txt
   python app.py
   ```

2. **Clone Android App**:
   ```bash
   git clone https://github.com/salamthabet95/HateSpeechDetectionAndroidApp.git
   ```

3. **Open in Android Studio**:
   - Import the project
   - Sync with Gradle files
   - Configure API endpoint URL

4. **Build and Run**:
   - Connect device or start emulator
   - Build and install the app

## Configuration

Update the API endpoint in your app configuration:

```java
// For Android Emulator
String API_BASE_URL = "http://10.0.2.2:5000";

// For physical device (use your computer's IP)
String API_BASE_URL = "http://192.168.1.XXX:5000";

// For production
String API_BASE_URL = "https://your-api-domain.com";
```

## Usage

1. **Launch the app**
2. **Enter text** in the input field (Arabic, Turkish, or English)
3. **Tap "Analyze Text"** to process the content
4. **View results** including:
   - Classification status
   - Confidence score
   - Language detection
   - Risk assessment
5. **Use "Clear"** to reset for new analysis

## API Integration

The app communicates with the backend API using HTTP requests:

### Request Format
```json
{
  "text": "User input text"
}
```

### Response Processing
The app handles the API response to display:
- Risk level colors (SAFE to CRITICAL)
- Confidence percentages
- Language detection results
- Detailed analysis metrics

## UI Components

- **ScrollView**: Main container for responsive layout
- **CardView**: Material Design cards for organized sections
- **EditText**: Multi-line text input with character limit
- **Button**: Primary and secondary action buttons
- **ProgressBar**: Loading indicator during API calls
- **TextView**: Dynamic result display with formatting

## Network Requirements

- **Internet Connection**: Required for API communication
- **Network Permissions**: Configured in AndroidManifest.xml
- **API Availability**: Backend server must be accessible

## Development Notes

- Text input is limited to 500 characters (API constraint)
- Network calls should be handled on background threads
- Error handling for API connectivity issues
- Input validation before sending requests

## Testing

### API Connection Test
Verify the API is accessible:
```bash
curl -X POST http://your-api-url:5000/predict \
  -H "Content-Type: application/json" \
  -d '{"text": "Test message"}'
```

### App Testing
Test with various scenarios:
- Different languages (Arabic, Turkish, English)
- Various text lengths
- Network connectivity issues
- API server offline scenarios

## Troubleshooting

### Common Issues

**App can't connect to API**:
- Verify API server is running
- Check network permissions
- Confirm correct API URL configuration

**Analysis not working**:
- Ensure text is between 3-1000 characters
- Check API server logs for errors
- Verify JSON request format

### Network Configuration
- Use `10.0.2.2:5000` for Android Emulator
- Use computer's IP address for physical devices
- Ensure firewall allows connections on API port

## Architecture

```
┌─────────────────┐    HTTP POST    ┌──────────────┐
│   Android App   │ ──────────────► │  Flask API   │
│  (UI Interface) │                 │ (ML Backend) │
│                 │ ◄────────────── │              │
└─────────────────┘   JSON Response └──────────────┘
```

## Related Repository

- **Backend API**: [HateSpeechDetectionAPI-ArTrEn](https://github.com/salamthabet95/HateSpeechDetectionAPI-ArTrEn)

## Contributing

1. Fork the repository
2. Create your feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## License

This project is licensed under the MIT License.

## Developer

**Salam Thabet**  
GitHub: [@salamthabet95](https://github.com/salamthabet95)

## Support

For mobile app issues, please open an issue in this repository.  
For API-related problems, check the [API repository](https://github.com/salamthabet95/HateSpeechDetectionAPI-ArTrEn).

---

**Note**: This app is a client interface that requires the Flask API backend to be running for text analysis functionality.

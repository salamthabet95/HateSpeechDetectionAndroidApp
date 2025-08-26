# Hate Speech Detection Android App

Android application for hate speech detection that connects to a machine learning API backend.

## Overview

This is an Android client app that provides a user interface for text analysis. The app communicates with a separate Flask API that handles the machine learning processing.

## Related Repository

- **API Backend**: [HateSpeechDetectionAPI-ArTrEn](https://github.com/salamthabet95/HateSpeechDetectionAPI-ArTrEn)

## User Interface Features

Based on the app layout, the interface includes:

- Text input field with 500 character limit
- Multi-line text support
- "Analyze Text" and "Clear" buttons
- Results display area with cards
- Progress indicator during analysis
- Sections for displaying confidence scores and language detection

## Supported Languages

The app interface indicates support for English, Arabic, and Turkish text analysis.

## Prerequisites

1. **API Backend**: The Flask API server must be running
   - Setup instructions in the [API repository](https://github.com/salamthabet95/HateSpeechDetectionAPI-ArTrEn)

2. **Android Development Environment**:
   - Android Studio
   - Android SDK
   - Network connectivity for API calls

## Installation

1. **Setup the API first**:
   ```bash
   git clone https://github.com/salamthabet95/HateSpeechDetectionAPI-ArTrEn.git
   # Follow the API setup instructions
   ```

2. **Clone this Android app**:
   ```bash
   git clone https://github.com/salamthabet95/HateSpeechDetectionAndroidApp.git
   ```

3. **Open in Android Studio**:
   - Import the project
   - Sync with Gradle
   - Configure API endpoint if needed

## Usage

1. Ensure the API backend is running
2. Launch the Android app
3. Enter text in the input field
4. Tap "Analyze Text"
5. View the analysis results

## UI Components

The app uses:
- ScrollView for the main layout
- CardView for organized sections
- EditText for multi-line text input
- Buttons for actions
- ProgressBar for loading states
- TextViews for results display

## Development Notes

- The app requires network permissions for API communication
- API endpoint configuration may need to be updated based on your server setup
- The UI is designed with Material Design principles

## Developer

**Salam Thabet**
- GitHub: [@salamthabet95](https://github.com/salamthabet95)

## Architecture

This is a client-server architecture where:
- This Android app handles the user interface
- The separate Flask API handles machine learning processing
- Communication happens through HTTP requests

**Note**: This app requires the API backend to be running for functionality. See the API repository for setup instructions.

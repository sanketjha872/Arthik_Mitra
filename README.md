# 💰 Arthik Mitra (अर्थिक मित्र)

**Your personal finance companion — track expenses, manage budgets, and get AI-powered money advice in your own language.**

[![Kotlin](https://img.shields.io/badge/Kotlin-100%25-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?logo=android&logoColor=white)](https://developer.android.com)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-FFCA28?logo=firebase&logoColor=black)](https://firebase.google.com)
[![License](https://img.shields.io/badge/License-Not%20Specified-lightgrey)](#license)

*"Arthik Mitra" translates to "Financial Friend" in Hindi — an Android app built to make personal finance management simple, voice-friendly, and accessible in **English and Hindi** alike.*

---

## 📖 Table of Contents

- [About](#about)
- [Screenshots](#screenshots)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [Getting Started](#getting-started)
- [Configuration](#configuration)
- [Roadmap](#roadmap)
- [Contributing](#contributing)
- [License](#license)

---

## 📱 About

**Arthik Mitra** is a native Android application (built entirely in **Kotlin** with **Jetpack Compose**) designed to help everyday users — especially Hindi-speaking users — take control of their personal finances. It combines expense tracking, budgeting, debt management, and an on-device/AI-powered chatbot into a single, voice-enabled experience.

The app is bilingual by design: users pick their preferred language (English or हिन्दी) at launch, and core flows like budgeting come with **text-to-speech** support for a more accessible, conversational experience.

## 📸 Screenshots

| Sign In | Expense Tracker | HisabAI Chatbot |
|:---:|:---:|:---:|
| <img src="./login (1).png" width="220"/> | <img src="./screenshots/expense-tracker.png" width="220"/> | <img src="./screenshots/chatbot.png" width="220"/> |

| Monthly Trends (Spendings) | Monthly Trends (Category Breakdown) |
|:---:|:---:|
| <img src="./screenshots/monthly-trends.png" width="220"/> | <img src="./screenshots/monthly-trends-compact.png" width="220"/> |

## ✨ Features

- 🧾 **Expense Tracker** — Log transactions by category and sub-category, backed by a local **Room** database for fast, offline-first access.
- 📊 **Budget Management (Hindi-first flow)** — A dedicated budgeting screen with animated progress indicators, per-category **speaker/voice playback**, and mic-based amount entry for accessibility.
- 📈 **Monthly Trends Dashboard** — Visual donut chart breaking down Spendings vs. Earnings by category (Groceries, Transportation, Medical, Personal, etc.) for a selected date range.
- 💳 **Debt Tracker** — Keep tabs on money owed and money lent, grouped and totaled automatically.
- 🤖 **HisabAI Chatbot Assistant** — Ask questions about your spending in natural language via the built-in **HisabAI** assistant, powered by an on-device **TensorFlow Lite** model alongside the **Google Generative AI (Gemini)** SDK.
- 🎙️ **Voice Input & Output** — Speech-to-text queries and text-to-speech responses throughout the app (including the mic-enabled sign-in form) using Android's native speech APIs.
- 📷 **Bill Scanner** — Capture bills/receipts directly via **CameraX** for quick expense logging.
- 🌐 **Multi-language Support** — Seamless switching between English and Hindi, with **ML Kit Translate** for on-device translation.
- ☁️ **Cloud Sync** — Firebase **Firestore**, **Realtime Database**, and **Storage** integration for persisting and syncing user data.
- 🎨 **Modern UI** — Fully built with Jetpack Compose, Material 3, and animated navigation transitions (Accompanist Navigation Animation).

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Kotlin |
| **UI Toolkit** | Jetpack Compose, Material 3 |
| **Navigation** | Navigation Compose + Accompanist Animated Nav Host |
| **Local Storage** | Room Database |
| **Cloud / Backend** | Firebase (Firestore, Realtime Database, Storage) |
| **AI / ML** | TensorFlow Lite, Google Generative AI (Gemini), ML Kit Translate |
| **Camera** | CameraX (bill/receipt scanning) |
| **Networking** | Retrofit, OkHttp, Gson |
| **Async** | Kotlin Coroutines, LiveData, ViewModel |
| **Speech** | Android SpeechRecognizer & TextToSpeech APIs |

## 📂 Project Structure

```
Arthik_Mitra/
├── app/
│   └── src/main/java/com/jhainusa/arthik_/
│       ├── BackendFiles/         # Room database, DAOs, entities
│       ├── BottomNavPages/       # Budget, Chatbot, Debt, Expense Tracker screens
│       ├── uiComponents/         # Reusable Compose components (cards, mic input, etc.)
│       ├── ui/theme/             # App theming (colors, typography)
│       ├── BiilsScanner.kt       # CameraX-based bill/receipt scanner
│       ├── LanguageSelect.kt     # English / Hindi language picker
│       ├── Login.kt              # Authentication screen
│       ├── Splash_Screen.kt      # App splash screen
│       └── MainActivity.kt       # App entry point & navigation graph
│   └── src/main/assests/
│       └── chatbot_model.tflite  # On-device TensorFlow Lite chatbot model
├── build.gradle.kts
└── settings.gradle.kts
```

## 🚀 Getting Started

### Prerequisites

- [Android Studio](https://developer.android.com/studio) (latest stable version recommended)
- JDK 17+
- An Android device/emulator running a recent Android API level
- A Firebase project (for Firestore/Realtime Database/Storage features)

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/sanketjha872/Arthik_Mitra.git
   cd Arthik_Mitra
   ```

2. **Open in Android Studio**
   - Launch Android Studio → `Open` → select the cloned `Arthik_Mitra` folder.

3. **Add your Firebase configuration**
   - Create a project on the [Firebase Console](https://console.firebase.google.com/).
   - Download your own `google-services.json` and place it inside the `app/` directory (replacing the sample one).

4. **Sync & Build**
   - Let Gradle sync all dependencies, then build the project via `Build > Make Project`.

5. **Run the app**
   - Select an emulator or connected device and hit **Run ▶**.

## ⚙️ Configuration

The app requests the following runtime permissions:

| Permission | Purpose |
|---|---|
| `INTERNET` | Firebase sync & AI chatbot API calls |
| `RECORD_AUDIO` | Voice input for the chatbot and budget screens |
| `CAMERA` | Bill/receipt scanning |

If you're using the **Google Generative AI (Gemini)** chatbot integration, make sure to supply your own API key rather than committing it to source control (e.g. via `local.properties` or a secure secrets manager).

## 🗺️ Roadmap

- [ ] Expand language support beyond English & Hindi
- [ ] Add data visualizations (charts) for spending trends
- [ ] Export reports (PDF/CSV) of transactions
- [ ] Add unit/UI test coverage
- [ ] Publish to the Google Play Store

## 🤝 Contributing

Contributions are welcome! To contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature/your-feature`)
5. Open a Pull Request

Please open an issue first to discuss significant changes.

## 📄 License

No license has been specified for this repository yet. Consider adding one (e.g. via [choosealicense.com](https://choosealicense.com/)) so others know how they can use this project.

---

<p align="center">Made with ❤️ using Kotlin & Jetpack Compose</p>

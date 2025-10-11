# Sky Weather ☁️🌦️

A modern Android weather application built with **Jetpack Compose** and structured around **Clean Architecture**. The app demonstrates scalable engineering practices, dependency injection, reactive UI, and offline-first design.

<img width="920" alt="play_store_graphic (1)" src="https://github.com/Octagon-Technologies/Sky_Weather/assets/62815445/3eb4a5ce-d11a-4a20-a69b-268b3269cd17">

---

## Features 🚀

- **Location-based Forecasts**: Integrates with Google Play Services Location API for real-time, GPS-aware weather updates.
- **Offline-First Design**: Combines **Room** (persistent storage) with **DataStore** (key-value preferences) to ensure availability even without network connectivity.
- **Modern Declarative UI**: Built entirely with **Jetpack Compose**, styled with Material 3, and powered by reactive state management.
- **Background Workflows**: Uses **WorkManager** to periodically refresh forecasts in the background, ensuring up-to-date data without draining the battery.
- **Dependency Injection**: Fully modularized with **Hilt**, promoting testability and clean separation of concerns.
- **Networking Layer**: Built on **Retrofit + Moshi**, with OkHttp logging for debugging complex network interactions.

---

## Architecture 🏗️

The project follows a **Clean Architecture + MVVM** pattern:

- **UI Layer** → Jetpack Compose screens & state management.
- **Domain Layer** → Business logic encapsulated in use cases.
- **Data Layer** → Repository abstraction backed by Retrofit (remote) and Room (local).
- **Dependency Injection** → Hilt modules wiring all layers together.

This design ensures **testability**, **scalability**, and **separation of concerns**.

---

## App Screens 📱

<img src="https://github.com/Octagon-Technologies/Sky_Weather/assets/62815445/d581d793-93da-4de2-a524-67a8003a4510" width="250">
<img src="https://github.com/Octagon-Technologies/Sky_Weather/assets/62815445/4e7b8eaa-3728-41d6-af0a-e8f859008ce4" width="250">
<img src="https://github.com/Octagon-Technologies/Sky_Weather/assets/62815445/7107ede8-a6e5-4dcb-a111-5a64d6d039fc" width="250">
<img src="https://github.com/Octagon-Technologies/Sky_Weather/assets/62815445/92dbf7ca-b3ae-4ea0-9c48-d9f00b11d92c" width="250">

---

## Getting Started ⚡

### Requirements
- **Android Studio Ladybug (2024.2+)**
- **JDK 17**
- **Android device/emulator running API 24+**

### Build & Run
Clone the repository:

```bash
git clone https://github.com/yourusername/sky-weather.git
cd sky-weather
```

### Why This Project is Interesting 💡
This project is designed not just as a weather app, but as a demonstration of production-ready Android development:
- Uses Hilt DI to handle complex dependency graphs.
- Leverages WorkManager for resilient background scheduling.
- Implements offline-first caching using Room and DataStore.
- Fully embraces Compose for UI, showing modern declarative Android practices.


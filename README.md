# PlantCare

PlantCare is an Android mobile application that helps users take care of their plants. The app provides tools for tracking watering schedules, monitoring plant health, and learning about different plant species.

## Features

- **User Authentication** - Login and Registration screens
- **Plant Tracking** - Keep track of your plants and their care needs
- **Reminders** - Get notified when it's time to water your plants

## Tech Stack

- **Language:** Kotlin
- **UI Framework:** Jetpack Compose (Material 3)
- **Navigation:** Jetpack Compose Navigation
- **Architecture:** Clean Architecture (UI / Domain / Data layers)
- **IDE:** Android Studio

## Project Structure

```
com.example.plantcare/
├── ui/
│   ├── login/          # Login screen
│   ├── signup/         # Registration screen
│   ├── home/           # Home screen
│   ├── navigation/     # App navigation graph and routes
│   └── theme/          # Colors, typography, theming
├── domain/
│   ├── model/          # Data models
│   └── repository/     # Repository interfaces
└── data/
    ├── repository/     # Repository implementations
    └── remote/         # API services
```

## Development Model

We are using **Kanban** as our development model. Kanban is a simple and flexible approach that works well for our small team and project scope.

### Why Kanban?

- **Visual workflow** - We use a Kanban board (GitHub Projects) to track tasks across columns: To Do → In Progress → Done
- **No fixed sprints** - Team members pick up tasks when they are ready, which fits our varying schedules
- **Continuous delivery** - We can release updates as soon as features are completed and tested
- **Easy to manage** - Minimal overhead, no need for sprint planning meetings

## Team Members

| Name | GitHub Username |
|------|----------------|
| Dima Allikvee | [@DimaAllikvee](https://github.com/DimaAllikvee) |
| Juri Allikvee | [@JuriAllikvee](https://github.com/JuriAllikvee) |



## Persoona
<img width="1136" height="826" alt="image" src="https://github.com/user-attachments/assets/f7009c24-7821-4e8a-9858-5befbd6f13b9" />
<img width="1134" height="857" alt="image" src="https://github.com/user-attachments/assets/1c4696b7-5383-4459-b9d1-d3c5233e23f8" />



## How to Run



1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle dependencies
4. Run the app on an emulator or physical device

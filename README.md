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


## User Scenarios

### Scenario 1 - Sofia (The Beginner)
Sofia comes home after a long day at work and notices that her palm leaf has turned yellow. She can't remember when she last watered it. Sofia opens the **PlantCare** app and sees a notification on the home screen: *"2 plants need watering today."* She taps on the palm leaf image, sees simple instructions (*"water with 200 ml of lukewarm water"*), and marks the task as done with a single click. The app automatically sets the next reminder for 7 days later.

### Scenario 2 - Mark (The Collector)
On Saturday morning, Mark performs his weekly maintenance for his 32 indoor plants. He opens the **PlantCare** app, where he sees all his plants in a list with their recent care history. Mark notices that one orchid hasn't bloomed for a month. Looking at the history, he sees that he changed the fertilizer three weeks ago. He adds a note to the plant's profile, uploads a new photo, and adjusts the fertilization frequency. All actions are saved in the history for future comparison.

## User Stories

1. **As a user**, I want to receive notifications before watering my plants so that I don't forget to care for them.
2. **As a user**, I want to see simple care instructions for each plant so that I can look after them without prior knowledge.
3. **As a user**, I want to mark a plant as watered with a single button press so that the next reminder is scheduled automatically.
4. **As a user**, I want to add multiple plants and group them to manage my entire collection in one place.
5. **As a user**, I want to see the care history of each plant with photos to track how changes have affected the plant's health.


## Persoona
<img width="1136" height="826" alt="image" src="https://github.com/user-attachments/assets/f7009c24-7821-4e8a-9858-5befbd6f13b9" />
<img width="1134" height="857" alt="image" src="https://github.com/user-attachments/assets/1c4696b7-5383-4459-b9d1-d3c5233e23f8" />

figma - https://www.figma.com/design/YExjZgzFM9L891UqobFcvF/User-Personas?node-id=0-1&t=Rzon2YKzTWBBPAbz-1

## How to Run



1. Clone the repository
2. Open the project in Android Studio
3. Sync Gradle dependencies
4. Run the app on an emulator or physical device

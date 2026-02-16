# compose-production-template

Production-ready Android template built with Kotlin, Jetpack Compose, and modular Clean Architecture.

## Tech Stack
- Kotlin + Coroutines + Flow
- Jetpack Compose (Material 3)
- Hilt (DI)
- Retrofit + OkHttp
- Room
- WorkManager

## Repository Structure

```text
.
├── app/
├── core/
│   ├── common/
│   ├── database/
│   └── network/
├── features/
│   └── samplefeature/
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

## Architecture

```text
app (entrypoint, nav host, theme, work scheduling)
  -> features:samplefeature
       -> presentation (Compose + ViewModel)
       -> domain (use cases + repository contracts)
       -> data (repository impl + mappers + sync)
            -> core:network (Retrofit API + network monitor)
            -> core:database (Room DAO + entity + DB)
  -> core:common (AppResult, UiState, BaseViewModel)
```

## What’s Included
- Single-activity Compose app with navigation
- Feature module wired end-to-end (API -> Room -> UI)
- Offline-first caching (Room is source of truth)
- Pull-to-refresh on list screen
- Periodic background sync with WorkManager
- Unified result and UI state wrappers
- Loading/error handling in UI

## Sync Strategy
- **No network:** keep showing cached Room data
- **Remote error:** worker returns retry
- **Conflict resolution:** remote wins by ID (`REPLACE` in Room)

## Build and Run

### Prerequisites
- Android Studio (latest stable)
- JDK 17
- Android SDK 36

### Commands
```bash
./gradlew :app:assembleDebug
./gradlew :app:installDebug
```

## Why This Template Is Production-Quality

This template mirrors real-world Android product architecture:

- Room as single source of truth for predictable offline behavior
- Repository pattern separating data sources cleanly
- Explicit domain use cases to isolate business logic
- Centralized Result & UiState modeling for consistent error handling
- Background sync using WorkManager with retry strategy
- Modular feature-first structure ready for scale
- Testable ViewModel with coroutine test rule and fake repository

## Testing Strategy

- ViewModel tested with coroutine Main dispatcher rule
- Fake repository used to isolate business logic
- Error and success flows covered
- Repository behavior validated with in-memory fake implementation

The structure allows:
- Easy replacement of data layer for integration tests
- Deterministic ViewModel testing

## Extension Ideas

- Add feature modules dynamically
- Replace JSONPlaceholder with real backend
- Add DataStore for user preferences
- Integrate logging abstraction
- Add pagination strategy

## Notes
- Compose Navigation routes are used; Safe Args is intentionally not used.
- AGP 9 compatibility flags are present in `gradle.properties` to keep kapt-based processors stable in this template.

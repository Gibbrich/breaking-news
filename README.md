# Breaking News

## General information
App allows user to look through top headlines in the US. On article click user can read short description of the article and jump to the source. After implementation discussing, we decided, that app will not cache dowloaded articles (due to simplicity), so user can use it online only.

## Architecture:
- App divided into several layers: general Android framework (UI, ViewModel, Activities, Fragments) - app, data fetching/caching - data and common code - core.
- App is built using recommended Google architecture: UI - ViewModel - Repository (local and remote). Basic architecture pattern is MVVM, implemented by using Architecture components.
- Dependencies and build logic are written in Kotlin script and should be placed in buildSrc module, which improves readability/comprehensibility of custom build logic.

## What can be improved from product perspective:
- Currently app can fetch and display articles, that are relevant for the US. We can add some settings mechanism to fetch news from other countries
- Add filtering by source
- Add search articles by keyword

## What can be improved/added from technical perspective:
- Add storing data mechanism to allow offline work
- More unit tests
- Add UI and integration tests

## How to build and install app
Installs the Debug build

    ./gradlew installDebug

## How to test

- Run unit tests

      ./gradlew test

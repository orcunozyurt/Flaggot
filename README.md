# Flaggot - An Android Flag App
Simple Flag Glossary App with a simple quiz feature added.
The application uses the REST countries API (https://restcountries.eu).
All country names and flag images in the app are sourced from that API.

## Installation
Download:
```
$ git clone https://github.com/orcunozyurt/Flaggot.git
```

Import Project by Android Studio Menu > File > Import Project...

## Features

The App containes two parts.  Flags part and Quiz part. Retrofit2 with Gson was chosen for REST API communications.


### 1. Flags Part

Onboarding activity has a button(Flags Button) to direct users to this path.
This part is consisted of an activity(CardsActivity) which uses a recyclerview and a recyclerview adapter.

Flags are combined with the country information. For the purpose, view flipper was used. It gives a nice modern vibe to the app. Clicking on flag card will trigget the flipping.

This part also includes a search view to be able to find the particular flag in 100s of them.

### 2. Quiz Part

QuizActivity is a simple activity with a unswipeable viewpager and a placeholder fragment.
Counts for 3 wrong answers to end the game.
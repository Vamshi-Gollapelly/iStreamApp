# iStreamApp

A personal video playlist app built for Android using Java as part of a university assignment.

## What the app does

The app lets users create an account, log in, and build their own YouTube 
playlist. They can paste a YouTube link, watch the video inside the app, 
and save it to their personal playlist for later.

## Screens

- Login Screen - enter username and password to log in
- Sign Up Screen - create a new account with full name, username, 
  password and confirm password
- Home Screen - paste a YouTube URL, play the video, add it to playlist, 
  or go to your playlist
- Playlist Screen - see all your saved videos and tap any to play it

## Features

- User registration and login
- Passwords and user data stored securely in a local database
- YouTube video playback using WebView and the iFrame embed method
- Add any valid YouTube URL to your personal playlist
- Each user only sees their own playlist
- Invalid URLs show a helpful error message
- Logout button on both Home and Playlist screens

## How it was built

- Language: Java
- Room Database for storing users and playlists
- SharedPreferences for keeping the user logged in between sessions
- WebView with YouTube iFrame API for video playback
- Multiple Activities for each screen

## How to run

1. Clone or download this repository
2. Open it in Android Studio
3. Run it on an emulator or physical Android device
4. Create an account on the Sign Up screen to get started

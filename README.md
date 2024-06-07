# Password Manager Application

## Overview

The Password Manager application is designed to securely store and manage passwords in an organized manner. It provides functionalities to add, view, edit, and delete passwords, along with features like biometric authentication and PIN authentication for added security.

## Functionalities

### 1. Add Password
   - Users can securely add new passwords by providing details such as account type, username/email, and password.

### 2. View/Edit Password
   - Users can view and edit existing passwords, including account details like username/email and password.

### 3. Show List of Passwords on Home Screen
   - The home screen displays a list of all saved passwords, showing essential details for each entry.

### 4. Delete Password
   - Users can delete passwords they no longer need.

### 5. Biometric Authentication
   - Users are prompted to authenticate using biometric (e.g., fingerprint, face ID) when opening the app for added security.

### 6. PIN Authentication
   - Users can authenticate using a PIN screen if biometric authentication is not available or preferred.

## Technical Details

### 1. Encryption
   - Strong encryption algorithm (AES) is implemented to secure password data.

### 2. Database
   - Room Database is used to manage encrypted passwords locally on the device.

### 3. User Interface
   - The application is built using Jetpack Compose to create a clean and intuitive user interface for managing passwords.

### 4. Input Validation
   - Validation is implemented to ensure that mandatory fields are not empty when adding or editing passwords.

### 5. Error Handling
   - Proper error handling is implemented to ensure a smooth user experience and handle edge cases effectively.

## Building and Running the Application

### Prerequisites
   - Android Studio installed on your development machine.
   - Android SDK with the required APIs installed.

### Steps
To build and run this Android app project in Android Studio on your local machine, follow these steps:

1. Clone this repository using Android Studio:

   - Open Android Studio.
   - Click on "File" in the top menu.
   - Select "New" and then "Project from Version Control."
   - Choose "Git" and enter the repository URL: `https://github.com/tushant-akar/Password-Manager`.
   - Click "Clone" to download the project.

2. Open the Project:

   - After cloning, Android Studio will automatically detect and open the project.

3. Configure Dependencies:

   - Ensure that you have the required dependencies and SDK versions installed as specified in the project's `build.gradle` files.

4. Build and Run the App:

   - Once the project is opened in Android Studio, click the "Run" button (usually a green triangle) in the top toolbar.
   - Select your target device (emulator or physical device) and click "OK."

5. Wait for the app to build and launch on your selected device.

That's it! You should now have the app running in Android Studio on your PC.

## Using the Application

### 1. Adding a Password:
  - Click on the add icon on the main screen to open the Add Password screen.
  - Enter the account name, username/email, and password.
  - Click on the save button to add the password securely.

### 2. Viewing/Editing a Password:
  - Click on a password entry on the home screen to view its details.
  - Click on the edit button to edit the password details if needed.

### 3. Deleting a Password:
  - Click on the delete option to remove the password.

### 4. Biometric Authentication:
  - When opening the app, users will be prompted to authenticate using biometric (e.g., fingerprint, face ID).
  - Follow the on-screen instructions to complete the biometric authentication process.

### 5. PIN Authentication:
  - If biometric authentication is not available or preferred, users can choose to authenticate using a PIN screen.
  - Enter the correct PIN to access the application.

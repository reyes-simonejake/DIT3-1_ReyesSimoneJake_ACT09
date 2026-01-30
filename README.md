# SimoneChat - Modern Firebase Chat Application

A beautifully designed mobile chat application with real-time messaging, built with Firebase Authentication and Firestore. Features a clean, modern UI with Telegram-inspired design.

![SimoneChat]

---

## ✨ Features

### 🔐 User Authentication
- **Email/Password authentication** - Secure login system
- **Anonymous guest login** - Chat without registration
- **Firebase Auth integration** - Industry-standard security
- **Password visibility toggle** - Better user experience

### 💬 Real-time Chat
- **Instant messaging** - Send and receive messages in real-time
- **Message bubbles** - Telegram-style design (blue for sent, gray for received)
- **Sender information** - Display sender name and email
- **Timestamps** - Show message time in 12-hour format
- **Auto-scrolling** - Automatically scroll to latest messages
- **Message tails** - Modern asymmetric bubble corners

### 🎨 Modern UI/UX
- **Clean Material Design** - Following Material Design 3 guidelines
- **Telegram-inspired colors** - Modern blue (#0088CC) theme
- **Gradient backgrounds** - Beautiful soft gradients
- **Rounded corners** - Smooth 20dp radius on bubbles
- **Responsive layout** - Works on all screen sizes
- **Smooth animations** - Polished user experience

---

## 🛠 Technologies Used

- **Kotlin** - Modern Android programming language
- **Firebase Authentication** - Secure user authentication
- **Cloud Firestore** - Real-time NoSQL database
- **Material Design 3** - Latest UI components
- **ViewBinding** - Type-safe view access
- **RecyclerView** - Efficient message list display

---

## 📱 Setup Instructions

### 1. Firebase Project Setup

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Create a new project or select existing one
3. Add an Android app to your Firebase project
   - **Package name**: `com.example.firebase_basedchatapplication`
   - **App nickname**: `SimoneChat`
4. Download `google-services.json`
5. Place `google-services.json` in the `app/` directory

### 2. Enable Firebase Services

#### Enable Authentication:
1. In Firebase Console, go to **Authentication**
2. Click **Get Started**
3. Click **Sign-in method** tab
4. Enable **Email/Password** provider
5. Enable **Anonymous** provider (for guest login)
6. Click **Save**

#### Enable Firestore:
1. In Firebase Console, go to **Firestore Database**
2. Click **Create Database**
3. Select **Start in test mode** (for development)
4. Choose location: **asia-southeast1 (Singapore)** for Philippines
5. Click **Enable**

#### Configure Firestore Rules:

**For Testing:**
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /messages/{message} {
      allow read, write: if request.auth != null;
    }
  }
}
```

**For Production (Recommended):**
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /messages/{message} {
      allow read: if request.auth != null;
      allow create: if request.auth != null 
                    && request.resource.data.senderId == request.auth.uid;
      allow update, delete: if request.auth != null 
                           && request.auth.uid == resource.data.senderId;
    }
  }
}
```

### 3. Build and Run

1. Open project in **Android Studio**
2. Click **File** → **Sync Project with Gradle Files**
3. Connect your Android device via USB or start an emulator
4. Click the green **Run** button (▶️)
5. Select your device
6. Wait for installation and enjoy! 🎉

---

## 📂 Project Structure

```
app/
├── src/main/
│   ├── java/com/example/firebase_basedchatapplication/
│   │   ├── LoginActivity.kt          # Login/Register screen
│   │   ├── MainActivity.kt           # Chat screen
│   │   ├── Message.kt                # Message data model
│   │   └── MessageAdapter.kt         # RecyclerView adapter
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_login.xml    # Login screen layout
│   │   │   ├── activity_main.xml     # Chat screen layout
│   │   │   └── item_message.xml      # Message bubble layout
│   │   ├── drawable/
│   │   │   ├── ios_blur_background.xml
│   │   │   ├── ios_message_sent.xml
│   │   │   └── ios_message_received.xml
│   │   ├── menu/
│   │   │   └── menu_main.xml         # Logout menu
│   │   └── values/
│   │       ├── strings.xml           # String resources
│   │       ├── colors.xml            # Color palette
│   │       └── styles.xml            # Custom styles
│   └── AndroidManifest.xml
└── build.gradle.kts
```

---

## 🎯 How to Use

### First Time Users:
1. Enter your **email** and **password**
2. Click **"Sign Up"** to create a new account
3. Or click **"Continue as Guest"** for anonymous login

### Returning Users:
1. Enter your **email** and **password**
2. Click **"Sign In"**

### Chatting:
1. Type your message in the input field at the bottom
2. Click the **send button** (✈️) to post your message
3. Messages appear in real-time
4. **Your messages** appear on the right with **blue background**
5. **Other users' messages** appear on the left with **gray background**

### Logout:
1. Click the **three-dot menu** (⋮) in the toolbar
2. Select **"Logout"**

---

## 📸 Screenshots

Place your screenshots in the `screenshots/` folder:
- `login_screen.png` - Login/Register screen
- `chat_screen.png` - Chat interface with messages
- `message_bubbles.png` - Message bubble design
- `app_preview.png` - App overview

---

## 🔧 Firebase Configuration

### Required Dependencies
- **Firebase BOM**: 33.7.0
- **Firebase Authentication**
- **Cloud Firestore**
- **Google Play Services Auth**

### Firestore Collection Structure

**Collection**: `messages`

```json
{
  "text": "Hello, World!",
  "senderEmail": "simone@example.com",
  "senderName": "Simone",
  "timestamp": 1706567890000,
  "senderId": "firebase_user_id"
}
```

---

## 🐛 Common Issues & Solutions

### Issue: App crashes on startup
**Solution**: Make sure `google-services.json` is in the `app/` directory (not `app/src/`)

### Issue: Authentication fails
**Solution**: 
- Enable Email/Password provider in Firebase Console
- Enable Anonymous provider for guest login
- Check internet connection

### Issue: Messages not appearing / "Failed to send message"
**Solution**:
- Check Firestore rules allow authenticated users to read/write
- Verify internet connection
- Make sure Firestore Database is enabled in Firebase Console
- Check that you're logged in (not logged out)

### Issue: "Missing google-services.json"
**Solution**: 
- Download from Firebase Console → Project Settings → Your apps
- Place in `app/` folder (same level as `build.gradle.kts`)

### Issue: Can't login anonymously
**Solution**: Enable Anonymous provider in Firebase Console → Authentication → Sign-in method

### Issue: Build errors
**Solution**:
- File → Sync Project with Gradle Files
- Build → Clean Project
- Build → Rebuild Project

---

## 🔒 Security Notes

⚠️ **Important for Production:**

- ✅ Update Firestore security rules (use production rules above)
- ✅ **Don't commit** `google-services.json` to public repositories
- ✅ Add `google-services.json` to `.gitignore`
- ✅ Implement proper user validation
- ✅ Add rate limiting for messages
- ✅ Enable email verification
- ✅ Use HTTPS for all connections
- ✅ Regularly update Firebase SDK

---

## 🎨 Design Features

### Color Palette
- **Primary**: `#0088CC` (Telegram Blue)
- **Accent**: `#FF3B5C` (Coral Pink)
- **Background**: Soft gradient (Blue → Purple → Pink)
- **Message Sent**: `#0088CC` (Blue)
- **Message Received**: `#F0F0F0` (Light Gray)

### UI Components
- Modern gradient backgrounds
- Telegram-style message bubbles
- Rounded corners (12-20dp)
- Material Design 3 components
- Smooth shadows and elevations
- Responsive layouts

---

## 💡 Key Takeaway

Cloud and Firebase integration enable mobile apps to scale beyond the device. By using Firebase Authentication and Firestore, developers can build secure, real-time, and collaborative applications—such as chat systems—without managing their own servers.

---

## 📄 License

This project is for educational purposes as part of a Mobile Development laboratory activity.

---

## 👨‍💻 Author

**Simone Jake Reyes**

Built with ❤️ using Kotlin and Firebase

---

## 🙏 Acknowledgments

- Firebase for backend services
- Material Design for UI guidelines
- Telegram for design inspiration
- Android community for support

---

## 📞 Support

If you encounter any issues:
1. Check the **Common Issues & Solutions** section above
2. Review Firebase Console for configuration
3. Check Android Studio Build output for errors
4. Verify internet connection



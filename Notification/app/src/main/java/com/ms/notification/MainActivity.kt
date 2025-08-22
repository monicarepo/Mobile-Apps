package com.ms.notification

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import com.ms.notification.ui.theme.NotificationTheme

class MainActivity : ComponentActivity() {

    companion object {
        val FIREBASE_TAG = "FirebaseMessagingService"
        val DB_NAME = "users"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        enableEdgeToEdge()
        setContent {
            NotificationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        getFirebaseToken()
    }

//    fun getDBReference(userEmail: String, token: String) {
//        //we will get this from firebase authentication
//        val email = auth.getCurrentUser().getEmail()
//        val user = User(email, token)
//        val db = FirebaseDatabase.getInstance().getReference(DB_NAME)
//        db.child(auth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener {
//            object : OnCompleteListener<Void> {
//                override fun onComplete(task: Task<Void?>) {
//                    if(task.isSuccessful) {
//                        //Token Saved
//                    }
//                }
//
//            }
//        }
//    }

    private fun subscribeToTopics() {
        // While subscribing the updates it will throw the notification to the all devices
        FirebaseMessaging.getInstance().subscribeToTopic("updates")
    }

    private fun getFirebaseToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result
                Log.d(FIREBASE_TAG," New Token: $token")
            } else {
                Log.e("FCM", "Failed to fetch token", task.exception)
            }
        }
    }

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NotificationTheme {
        Greeting("Android")
    }
}
package co.kezzi.cst

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import co.kezzi.cst.ui.theme.CamScanTalkTheme
import co.kezzi.cst.Speaker

class MainActivity : ComponentActivity() {
//    private lateinit var textToSpeech: TextToSpeech
//
    private val tag = "LEE"

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        // Check camera permission
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            val cameraIntent = Intent(this, CameraActivity::class.java)
            startActivity(cameraIntent)
        }

//        if (requestCode == PERMISSION_REQUEST_CODE) {
//            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, handle accordingly
//                // You can start the camera activity or perform other operations that require the permissions
//            } else {
//                // Permission denied, handle accordingly
//                // You can show an explanation dialog or disable features that require the permissions
//            }
//        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        // Check camera permission
        if (cameraPermission == PackageManager.PERMISSION_GRANTED) {
            val cameraIntent = Intent(this, CameraActivity::class.java)
            startActivity(cameraIntent)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA),
                CameraActivity.CAMERA_PERMISSION_REQUEST_CODE)
        }

        Voice.speaker = Speaker(this, Voice)
//        textToSpeech = TextToSpeech(this, object : TextToSpeech.UtteranceListener {
//            override fun onInitialized() {
//                Log.d(tag, "Initialized")
//// Check if the synthesizer is initialized
//                if (textToSpeech.isInitialized()) {
//                    // Synthesize the utterance
//                    textToSpeech.synthesizeUtterance("Text to speech initialized")
//
//                }
//            }
//
//            override fun onStart() {
//                Log.d(tag, "onStart")
//            }
//
//            override fun onCompleted() {
//                Log.d(tag, "onCompleted")
//
//                textToSpeech.synthesizeUtterance("Point the camera, and I will read the text that I see")
//
//// Release the synthesizer when no longer needed
////                textToSpeech.release()
//
//            }
//
//            override fun onError() {
//                Log.d(tag, "onError")
//            }
//        })

        setContent {
            CamScanTalkTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
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
    CamScanTalkTheme {
        Greeting("Android")
    }
}


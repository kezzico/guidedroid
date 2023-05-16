package co.kezzi.cst

import android.Manifest
import android.content.pm.PackageManager
import android.hardware.Camera
import android.media.ImageReader
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException
import java.util.*

class CameraActivity : AppCompatActivity(), SurfaceHolder.Callback {

    private lateinit var camera: Camera
    private lateinit var surfaceHolder: SurfaceHolder
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    private var isPreviewing = false
    private var zoomLevel = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_camera)
        Log.d("LEE","CAMERA")

        camera = Camera.open()
//        val cameraPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
//        // Check camera permission
//        if (cameraPermission != PackageManager.PERMISSION_GRANTED
//        ) {
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.CAMERA),
//                CAMERA_PERMISSION_REQUEST_CODE
//            )
//        } else {
//            Log.d("LEE","START CAMERA")
//            camera = Camera.open()
//        }

        // Setup surface view and holder
        val surfaceView = findViewById<SurfaceView>(R.id.surfaceView)
        surfaceHolder = surfaceView.holder
        surfaceHolder.addCallback(this)

        // Setup scale gesture detector
        scaleGestureDetector = ScaleGestureDetector(this, ZoomGestureListener())
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        scaleGestureDetector.onTouchEvent(event)
        return super.onTouchEvent(event)
    }

    override fun surfaceCreated(holder: SurfaceHolder) {
        // Start the preview when the surface is created
        if (!isPreviewing) {
            startPreview()
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
        // Update the preview when the surface dimensions change
        if (isPreviewing) {
            stopPreview()
            startPreview()
        }
    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        // Stop the preview when the surface is destroyed
        camera.stopPreview()

        isPreviewing = false
    }

    private fun startPreview() {
        try {
            camera.setPreviewDisplay(surfaceHolder)
            camera.setDisplayOrientation(90) // Set the orientation to portrait
            camera.startPreview()
            isPreviewing = true
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun stopPreview() {
        camera.stopPreview()
        isPreviewing = false
    }

    private inner class ZoomGestureListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val zoomParams = camera.parameters
            val maxZoom = zoomParams.maxZoom

            val scaleFactor = detector.scaleFactor
            if (scaleFactor > 1.0f) {
                // Zoom in
                if (zoomLevel < maxZoom) {
                    Voice.speak("Zoom")
                    zoomLevel++
                }
            } else {
                // Zoom out
                if (zoomLevel > 0) {
                    zoomLevel--
                }
            }

            zoomParams.zoom = zoomLevel
            camera.parameters = zoomParams

            return true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        camera.release()
    }

    companion object {
        const val CAMERA_PERMISSION_REQUEST_CODE = 100
    }
}
//
//    private fun releaseCamera() {
//        camera.release()
//    }

//    private fun createCaptureSession() {
//        val surfaceView = findViewById<SurfaceView>(R.id.surfaceView)
//
//        val previewSurface = surfaceView.holder.surface
//
//        // Create an ImageReader to receive camera frames
//
//        val imageReader = ImageReader.newInstance(
//            200,
//            400,
//            ImageFormat.YUV_420_888,
//            2
//        )
//        imageReader.setOnImageAvailableListener(imageAvailableListener, null)
//
//        // Create a list of target surfaces
//        val surfaces = listOf(previewSurface, imageReader.surface)
//
//        // Create a capture session with the target surfaces
//        cameraDevice.createCaptureSession(surfaces, captureSessionStateCallback, null)
//    }

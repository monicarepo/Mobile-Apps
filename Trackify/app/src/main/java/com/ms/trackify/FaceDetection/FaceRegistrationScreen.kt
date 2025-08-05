package com.ms.trackify.FaceDetection

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.Face
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetector
import com.google.mlkit.vision.face.FaceDetectorOptions
import kotlinx.coroutines.launch
import kotlin.collections.isNotEmpty

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun FaceRegistrationScreen(
    onRegistrationComplete: (Uri?) -> Unit,
    viewModel: FaceRegistrationViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    // Camera permission state
    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    // UI state
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        when {
            // Permission granted - show main content
            cameraPermissionState.status.isGranted -> {
                if (state.faceImageUri == null) {
                    // Face detection camera preview
                    FaceDetectionCamera(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(3f / 4f)
                            .clip(RoundedCornerShape(16.dp)),
                        onFaceDetected = viewModel::onFaceDetected,
                        lifecycleOwner = lifecycleOwner,
                        onImageCaptureAvailable = viewModel::setImageCapture,
                        onState = state
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Detection status
                    DetectionStatus(state.isFaceDetected)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Registration form
                    OutlinedTextField(
                        value = state.username,
                        onValueChange = viewModel::onUsernameChanged,
                        label = { Text("Username") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.email,
                        onValueChange = viewModel::onEmailChanged,
                        label = { Text("Email") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Register button
                    Button(
                        onClick = { viewModel.captureFace() },
                        enabled = state.isFaceDetected &&
                                state.username.isNotBlank() &&
                                state.email.isNotBlank(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Register Face",
                                fontSize = 16.sp
                            )
                        }
                    }
                } else {
                    // Registration complete screen
                    RegistrationCompleteScreen(
                        faceImageUri = state.faceImageUri,
                        onComplete = { onRegistrationComplete(state.faceImageUri) }
                    )
                }
            }

            // Permission not granted - show permission UI
            else -> {
                PermissionRequestContent(
                    permissionState = cameraPermissionState,
                    permissionText = "Camera access is required for face registration"
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionRequestContent(
    permissionState: PermissionState,
    permissionText: String = "Camera access is required for face registration"
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val showRationale = permissionState.status.shouldShowRationale

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Camera icon
        Icon(
            imageVector = Icons.Outlined.AccountCircle,
            contentDescription = "Camera permission",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Permission text
        Text(
            text = permissionText,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Additional explanation if permission was denied before
        if (showRationale) {
            Text(
                text = "You previously denied this permission. Please grant access in settings.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Request permission button
        Button(
            onClick = {
                scope.launch {
                    if (showRationale) {
                        // Open app settings if permission was denied before
                        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                            data = Uri.fromParts("package", context.packageName, null)
                        }
                        context.startActivity(intent)
                    } else {
                        // Request permission normally
                        permissionState.launchPermissionRequest()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(0.8f),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (showRationale) "Open Settings" else "Allow Access",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }

    // Automatically request permission when first shown
    if (!permissionState.status.isGranted && !showRationale) {
        LaunchedEffect(Unit) {
            permissionState.launchPermissionRequest()
        }
    }
}

@Composable
private fun FaceDetectionCamera(
    modifier: Modifier = Modifier,
    onFaceDetected: (Boolean) -> Unit,
    lifecycleOwner: LifecycleOwner,
    onImageCaptureAvailable: (ImageCapture) -> Unit,
    onState: FaceRegistrationState
) {
    val context = LocalContext.current
    val faceDetector = remember { buildFaceDetector() }

    Box(modifier = modifier) {
        val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
        var preview by remember { mutableStateOf<Preview?>(null) }
        var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                }
            },
            modifier = Modifier.fillMaxSize(),
            update = { previewView ->
                cameraProviderFuture.addListener({
                    val cameraProvider = cameraProviderFuture.get()

                    // Set up image analysis for face detection
                    val imageAnalysis = ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build()
                        .also {
                            it.setAnalyzer(ContextCompat.getMainExecutor(context)) { imageProxy ->
                                detectFaces(imageProxy, faceDetector) { faces ->
                                    val hasFace = faces.isNotEmpty()
                                    onFaceDetected(hasFace)
                                    imageProxy.close()
                                }
                            }
                        }

                    // Set up preview use case
                    preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    // Set up image capture use case
                    imageCapture = ImageCapture.Builder().build().also {
                        onImageCaptureAvailable(it)
                    }

                    try {
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            CameraSelector.DEFAULT_FRONT_CAMERA,
                            preview,
                            imageAnalysis,
                            imageCapture
                        )
                    } catch(exc: Exception) {
                        Log.e("Camera", "Use case binding failed", exc)
                    }
                }, ContextCompat.getMainExecutor(context))
            }
        )

        // Face detection indicator
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(12.dp)
                .background(
                    color = if (onState.isFaceDetected) Color.Green.copy(alpha = 0.7f)
                    else Color.Red.copy(alpha = 0.7f),
                    shape = CircleShape
                )
                .size(20.dp)
        )
    }
}

@Composable
private fun DetectionStatus(isFaceDetected: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = if (isFaceDetected) Icons.Default.CheckCircle else Icons.Default.Warning,
            contentDescription = "Detection status",
            tint = if (isFaceDetected) Color.Green else Color.Red,
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = if (isFaceDetected) "Face detected" else "No face detected",
            color = if (isFaceDetected) Color.Green else Color.Red,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun RegistrationCompleteScreen(
    faceImageUri: Uri?,
    onComplete: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "Registration Complete!",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        faceImageUri?.let { uri ->
            AsyncImage(
                model = uri,
                contentDescription = "Registered face",
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onComplete,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(52.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Continue")
        }
    }
}

private fun buildFaceDetector(): FaceDetector {
    val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
        .setContourMode(FaceDetectorOptions.CONTOUR_MODE_NONE)
        .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_NONE)
        .setMinFaceSize(0.15f)
        .build()
    return FaceDetection.getClient(options)
}

private fun detectFaces(
    imageProxy: ImageProxy,
    detector: FaceDetector,
    onFacesDetected: (List<Face>) -> Unit
) {
    try {
        val mediaImage = imageProxy.image ?: run {
            onFacesDetected(emptyList())
            return
        }

        val image = InputImage.fromMediaImage(
            mediaImage,
            imageProxy.imageInfo.rotationDegrees
        )

        // Process the image for face detection
        detector.process(image)
            .addOnSuccessListener { faces ->
                onFacesDetected(faces)
            }
            .addOnFailureListener { e ->
                Log.e("FaceDetection", "Face detection failed", e)
                onFacesDetected(emptyList())
            }
    } finally {
        imageProxy.close()
    }
}
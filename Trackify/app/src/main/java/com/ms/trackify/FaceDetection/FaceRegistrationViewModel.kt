package com.ms.trackify.FaceDetection

import android.content.Context
import android.net.Uri
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FaceRegistrationViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val database: DatabaseReference,
    @ApplicationContext private val appContext: Context
) : ViewModel() {

    private val _uiState = MutableStateFlow(FaceRegistrationState())
    val uiState: StateFlow<FaceRegistrationState> = _uiState.asStateFlow()

    private var imageCapture: ImageCapture? = null

    fun onUsernameChanged(value: String) {
        _uiState.update { it.copy(username = value) }
    }

    fun onEmailChanged(value: String) {
        _uiState.update { it.copy(email = value) }
    }

    fun onFaceDetected(isDetected: Boolean) {
        _uiState.update { it.copy(isFaceDetected = isDetected) }
    }

    fun setImageCapture(imageCapture: ImageCapture?) {
        this.imageCapture = imageCapture
    }

    fun captureFace() {
        val context = appContext
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val photoFile = createTempFile(context.toString(), "face_")
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                imageCapture?.takePicture(
                    outputOptions,
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                            val uri = Uri.fromFile(photoFile)
                            _uiState.update {
                                it.copy(
                                    faceImageUri = uri,
                                    isLoading = false
                                )
                            }
                        }

                        override fun onError(exc: ImageCaptureException) {
                            _uiState.update {
                                it.copy(
                                    error = "Failed to capture image: ${exc.message}",
                                    isLoading = false
                                )
                            }
                        }
                    }
                )
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        error = "Error: ${e.message}",
                        isLoading = false
                    )
                }
            }
        }
    }
}

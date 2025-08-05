package com.ms.trackify.FaceDetection

import android.net.Uri

data class FaceData(
    val userId: String = "",
    val username: String = "",
    val email: String = "",
    val faceImageUrl: String = "",
    val faceEmbedding: List<Float> = emptyList(), // Facial features vector
    val detectionDate: Long = System.currentTimeMillis(),
    val faceBounds: FaceBounds? = null // Optional: store face position
)

data class FaceBounds(
    val left: Float,
    val top: Float,
    val right: Float,
    val width: Float,
    val height: Float
)

data class FaceRegistrationState(
    val username: String = "",
    val email: String = "",
    val isFaceDetected: Boolean = false,
    val faceImageUri: Uri? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
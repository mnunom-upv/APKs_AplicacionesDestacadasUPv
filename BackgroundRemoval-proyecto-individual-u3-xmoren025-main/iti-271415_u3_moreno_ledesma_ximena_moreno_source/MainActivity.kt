package upvictoria.pm_may_ago_2025.iti_271415.pi3u3.moreno_ledesma

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.segmentation.Segmentation
import com.google.mlkit.vision.segmentation.selfie.SelfieSegmenterOptions
import java.util.concurrent.Executors
import androidx.activity.compose.setContent
import android.graphics.Color
import android.graphics.Matrix
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.unit.dp


import upvictoria.pm_may_ago_2025.iti_271415.pi3u3.moreno_ledesma.utils.YuvToRgbConverter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                SegmentationApp()
            }
        }
    }
}

@Composable
fun SegmentationApp() {
    var selectedImage by remember { mutableStateOf<Bitmap?>(null) }
    var showCamera by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            selectedImage = loadBitmapFromUri(context, it)
            showCamera = true
        }
    }

    val activity = context as ComponentActivity
    val permissionGranted = remember {
        mutableStateOf(
            android.content.pm.PackageManager.PERMISSION_GRANTED ==
                    context.checkSelfPermission(android.Manifest.permission.CAMERA)
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        permissionGranted.value = granted
    }

    if (!permissionGranted.value) {
        LaunchedEffect(Unit) {
            permissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
        // Muestra una pantalla de espera o explicación mientras se concede
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text("Solicitando permiso de cámara...")
        }
    } else if (!showCamera) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { launcher.launch("image/*") },
                modifier = Modifier.align(alignment = androidx.compose.ui.Alignment.CenterHorizontally)
            ) {
                Text("Seleccionar imagen de fondo")
            }
        }
    } else {
        selectedImage?.let { CameraWithSegmentation(backgroundBitmap = it) }
    }
}

fun loadBitmapFromUri(context: android.content.Context, uri: Uri): Bitmap {
    return if (Build.VERSION.SDK_INT < 28) {
        MediaStore.Images.Media.getBitmap(context.contentResolver, uri).copy(Bitmap.Config.ARGB_8888, true)
    } else {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        ImageDecoder.decodeBitmap(source).copy(Bitmap.Config.ARGB_8888, true)
    }
}


@Composable
fun CameraWithSegmentation(backgroundBitmap: Bitmap) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    // Eliminamos el PreviewView, ya no lo necesitamos.
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    var resultBitmap by remember { mutableStateOf<Bitmap?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }

    val segmenter = remember {
        val options = SelfieSegmenterOptions.Builder()
            .setDetectorMode(SelfieSegmenterOptions.STREAM_MODE)
            .build()
        Segmentation.getClient(options)
    }

    LaunchedEffect(Unit) {
        val cameraProvider = ProcessCameraProvider.getInstance(context).get()

        // --- CAMBIO CLAVE AQUÍ ---
        // Eliminamos el uso del Preview.Builder()
        // y el preview.setSurfaceProvider(previewView.surfaceProvider).

        val imageAnalysis = ImageAnalysis.Builder()
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()
            .also {
                it.setAnalyzer(cameraExecutor) { imageProxy ->
                    processImage(context, imageProxy, backgroundBitmap, segmenter) { processedBitmap ->
                        resultBitmap = processedBitmap
                    }
                }
            }

        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        try {
            cameraProvider.unbindAll()
            // Ahora solo vinculamos el imageAnalysis a la cámara.
            cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, imageAnalysis)
        } catch (e: Exception) {
            Toast.makeText(context, "Error al iniciar la cámara: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Ahora solo tenemos un Image que se muestra,
        // no hay ningún PreviewView ni AndroidView.
        if (resultBitmap != null) {
            Image(
                bitmap = resultBitmap!!.asImageBitmap(),
                contentDescription = "Vista de la cámara con fondo segmentado",
                modifier = Modifier.fillMaxSize()
            )
        } else {
            CircularProgressIndicator()
        }
    }
}


@Suppress("UnsafeOptInUsageError")
fun processImage(
    context: Context,
    imageProxy: ImageProxy,
    background: Bitmap,
    segmenter: com.google.mlkit.vision.segmentation.Segmenter,
    onBitmapReady: (Bitmap) -> Unit
) {
    val mediaImage = imageProxy.image ?: run {
        imageProxy.close()
        return
    }

    val rotationDegrees = imageProxy.imageInfo.rotationDegrees
    val inputImage = InputImage.fromMediaImage(mediaImage, rotationDegrees)

    segmenter.process(inputImage)
        .addOnSuccessListener { mask ->
            try {
                val maskBuffer = mask.buffer
                val width = mask.width
                val height = mask.height

                val scaledBg = Bitmap.createScaledBitmap(background, width, height, true)
                val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

                maskBuffer.rewind()

                // ✅ Convertir cámara de YUV a RGB
                val yuvToRgbConverter = YuvToRgbConverter(context)
                val cameraBitmap = Bitmap.createBitmap(imageProxy.width, imageProxy.height, Bitmap.Config.ARGB_8888)
                yuvToRgbConverter.yuvToRgb(mediaImage, cameraBitmap)

                // ✅ Rotar imagen
                val matrix = Matrix().apply { postRotate(rotationDegrees.toFloat()) }
                val rotatedCameraBitmap = Bitmap.createBitmap(cameraBitmap, 0, 0, cameraBitmap.width, cameraBitmap.height, matrix, true)

                // ✅ Escalar a tamaño de máscara
                val scaledCameraBitmap = Bitmap.createScaledBitmap(rotatedCameraBitmap, width, height, true)

                // ✅ Combinar tipo Zoom/Meet
                for (y in 0 until height) {
                    for (x in 0 until width) {
                        val maskVal = maskBuffer.float
                        if (maskVal > 0.5f) {
                            outputBitmap.setPixel(x, y, scaledCameraBitmap.getPixel(x, y))
                        } else {
                            outputBitmap.setPixel(x, y, scaledBg.getPixel(x, y))
                        }
                    }
                }

                onBitmapReady(outputBitmap)

            } catch (e: Exception) {
                Log.e("Segmentation", "Error al aplicar máscara: ${e.message}")
            }
        }
        .addOnFailureListener {
            Log.e("Segmentation", "Falló segmentación: ${it.message}")
        }
        .addOnCompleteListener {
            imageProxy.close()
        }
}
package com.jhainusa.arthik_

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.jhainusa.arthik_.BackendFiles.LoanViewModel
import com.jhainusa.arthik_.ui.theme.AppBack
import com.jhainusa.arthik_.ui.theme.purpleBack
import com.jhainusa.arthik_.uiComponents.UpperFlapper
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.util.concurrent.Executors

@Composable
fun startCapture(type:String,item_name: String){
    // Create a ViewModelProvider.Factory
    val factory = object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GeminiViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return GeminiViewModel() as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

    // Use the factory when getting the ViewModel
    val viewModel: GeminiViewModel = viewModel(factory = factory)
    GeminiVisionApp(viewModel, type = type, item_name = item_name, onTransactionAdded = {" d"})
}

class GeminiViewModel : ViewModel() {
    // Replace with your actual Gemini API key
    private val apiKey = "AIzaSyAa4ox7hRfpy3GCDLc05IHrTt79eWD8DH4"
    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = apiKey
    )

    var capturedImage by mutableStateOf<Bitmap?>(null)
    var userQuestion by mutableStateOf("")
    var aiResponse by mutableStateOf("")
    var isLoading by mutableStateOf(false)

    // Add this function to clean and validate JSON
    private fun extractValidJson(response: String): String {

        val jsonPattern = "\\[\\s*\\{.*?\\}\\s*\\]".toRegex(RegexOption.DOT_MATCHES_ALL)
        val matchResult = jsonPattern.find(response)

        return matchResult?.value ?: ""
    }

    suspend fun sendImageWithQuestion(bitmap: Bitmap, question: String) {
        isLoading = true
        aiResponse = "Processing..."

        try {
            val inputContent = content {
                text("Please analyze this bill image and extract each product/item name along with its corresponding price. Format the results as a structured JSON array with item_name and price fields for each product. Maintain the exact spelling and formatting of item names as they appear on the bill. For prices, include only the numerical value (without currency symbols). Return ONLY the JSON array with no additional text.")
                image(bitmap)
            }

            val response = generativeModel.generateContent(inputContent)
            val rawText = response.text ?: ""

            // Try to extract valid JSON from the response
            val jsonText = extractValidJson(rawText)

            if (jsonText.isNotEmpty()) {
                aiResponse = jsonText
            } else {
                // If no valid JSON was found, return the raw text
                aiResponse = rawText
                Log.w("GeminiViewModel", "Could not extract valid JSON from response: $rawText")
            }
        } catch (e: Exception) {
            aiResponse = "Error: ${e.message}"
            Log.e("GeminiViewModel", "Error sending image to Gemini", e)
        } finally {
            isLoading = false
        }
    }
}

@Composable
fun GeminiVisionApp(viewModel: GeminiViewModel , mainViewModel:LoanViewModel = viewModel(),
                    type:String,item_name: String,  onTransactionAdded: () -> Unit,
) {
    var btn by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val coroutineScope = rememberCoroutineScope()

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) ==
                    PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasCameraPermission = isGranted
    }

    LaunchedEffect(key1 = true) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }
    var total = 0.0
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.background(Color(0xFFF1F0F0))
    ) {
    UpperFlapper("बिल स्कैनर")
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 10.dp, horizontal = 10.dp)
                    .background(Color(0xFFF1F0F0))
                    .clip(RoundedCornerShape(20.dp))
                    .shadow(15.dp)
                    .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text("बिल की फोटो खींचकर राशि जोड़ें",
                fontFamily = intern,
                fontSize = 19.sp,
                color = Color(0xFF6A0DAD),
                fontWeight = FontWeight.W600,
                modifier = Modifier.padding(top = 16.dp))
            if (hasCameraPermission) {
                CameraPreview(
                    onImageCaptured = { bitmap ->
                        viewModel.capturedImage = bitmap
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp)
                        .height(300.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp))
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(2.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(12.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Camera permission is required")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            viewModel.capturedImage?.let { bitmap ->
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = "Captured image",
                    modifier = Modifier
                        .size(200.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        coroutineScope.launch {
                            viewModel.sendImageWithQuestion(bitmap, "Analyze this bill")
                        }
                    },
                    enabled = !viewModel.isLoading
                ) {
                    Text("स्कैन करे")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (viewModel.isLoading) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(8.dp))
                Text("Processing...")
            }
        }

        // Conditionally show bill analysis results
        if (viewModel.aiResponse.isNotEmpty() && !viewModel.isLoading) {
            item {
                Text(
                    text = "बिल विश्लेषण",
                    fontSize = 24.sp,
                    fontFamily = intern,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                    .padding(14.dp)
                )
            }

            when {
                viewModel.aiResponse.startsWith("Error:") -> {
                    item {
                        Text(
                            text = viewModel.aiResponse,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                else -> {
                    val repository = ProductRepository()
                    val products = repository.parseProductsFromJson(viewModel.aiResponse)

                    if (products.isEmpty()) {
                        item {
                            Text(
                                text = "No items found. Raw response: ${viewModel.aiResponse}",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        // Display each product as a separate item
                        items(products) { product ->
                            ProductItem(product)
                        }

                        // Display total at the bottom
                        item {
                             total = products.sumOf { it.price }
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp)
                                    .padding(vertical = 16.dp),
                                elevation = CardDefaults.elevatedCardElevation(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFa29bfe))
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "टोटल",
                                        fontFamily = intern,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                    Text(
                                        text = "₹%.2f".format(total),
                                        fontFamily = intern,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                        item{
                            Button(
                                colors = ButtonColors(
                                    contentColor = Color.White,
                                    disabledContentColor = Color.White,
                                    containerColor = if(!btn) purpleBack else Color.LightGray,
                                    disabledContainerColor = AppBack
                                ),
                                onClick = {
                                    mainViewModel.addTransaction(
                                       amount = total,
                                       category = type,
                                       subCategory = item_name
                                   )
                                    onTransactionAdded()
                                    btn = !btn
                                }
                            ){
                                Text(text = if(!btn) "बिल सेव करें" else "बिल सेव्ड")
                            }
                            Spacer(modifier = Modifier.height(32.dp))

                        }
                    }
                }
            }
            }
        }
    }
}

@Composable
fun CameraPreview(
    onImageCaptured: (Bitmap) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    val previewView = remember { PreviewView(context) }

    // Store the ImageCapture instance so we can access it from the button click
    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }

    DisposableEffect(key1 = Unit) {
        onDispose {
            cameraExecutor.shutdown()
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            factory = { previewView },
            modifier = Modifier.matchParentSize()
        ) { view ->
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(view.surfaceProvider)
                }

                // Create and store the ImageCapture use case
                imageCapture = ImageCapture.Builder()
                    .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                    .build()

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )
                } catch (e: Exception) {
                    Log.e("CameraPreview", "Use case binding failed", e)
                }
            }, ContextCompat.getMainExecutor(context))
        }

        FloatingActionButton(
            onClick = {
                // Use the stored imageCapture instance
                val localImageCapture = imageCapture
                if (localImageCapture != null) {
                    localImageCapture.takePicture(
                        cameraExecutor,
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(image: ImageProxy) {
                                // Process the captured image
                                val buffer = image.planes[0].buffer
                                val bytes = ByteArray(buffer.capacity())
                                buffer.get(bytes)
                                val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                                onImageCaptured(bitmap)
                                image.close()
                            }

                            override fun onError(exception: ImageCaptureException) {
                                Log.e("CameraPreview", "Image capture failed", exception)
                            }
                        }
                    )
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(painter = painterResource(R.drawable.baseline_camera_alt_24), contentDescription = "Take photo")
        }
    }
}

// Product data class
@Serializable
data class Product(
    val item_name: String,
    val price: Double
)

// Repository class for parsing the JSON
class ProductRepository {
    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true  // Add this to be more forgiving with JSON parsing
    }

    fun parseProductsFromJson(jsonString: String): List<Product> {
        return try {
            // Trim whitespace and check if the string looks like a JSON array
            val trimmed = jsonString.trim()
            if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
                json.decodeFromString(trimmed)
            } else {
                Log.e("ProductRepository", "Invalid JSON format: $jsonString")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ProductRepository", "JSON parsing error: ${e.message}", e)
            emptyList()
        }
    }
}

// Composable to display the bill analysis results
@Composable
fun BillScreen(jsonString: String) {
    val repository = ProductRepository()

    val products = if (jsonString == "Processing..." || jsonString.startsWith("Error:")) {
        emptyList()
    } else {
        repository.parseProductsFromJson(jsonString)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Bill Analysis",
            fontSize = 24.sp,
            fontFamily = intern,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        when {
            jsonString == "Processing..." -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Processing bill...")
                    }
                }
            }
            jsonString.startsWith("Error:") -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = jsonString,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            products.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "No items found. Raw response: $jsonString",
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
            else -> {
                ProductList(products)
            }
        }
    }
}

// Composable to display the product list
@Composable
fun ProductList(products: List<Product>) {
    LazyColumn(
        modifier = Modifier .fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            ProductItem(product)
        }

        item {
            // Display the total at the bottom
            val total = products.sumOf { it.price }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                elevation = CardDefaults.elevatedCardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "TOTAL",
                        fontFamily = intern,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "$%.2f".format(total),
                        fontFamily = intern,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

// Composable to display a single product item
@Composable
fun ProductItem(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 3.dp),
        elevation = CardDefaults.elevatedCardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = product.item_name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = intern,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "₹%.2f".format(product.price),
                fontFamily = intern,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }
}
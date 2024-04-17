package com.rajendra.photogrid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.rajendra.photogrid.data.ImageLoader
import com.rajendra.photogrid.data.UnsplashApi
import com.rajendra.photogrid.data.repository.PhotoRepositoryImpl
import com.rajendra.photogrid.presentation.ImageViewModel
import com.rajendra.photogrid.presentation.MainScreen
import com.rajendra.photogrid.presentation.PhotoGrid
import com.rajendra.photogrid.ui.theme.PhotoGridTheme

class UnSplash : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PhotoGridTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel = ImageViewModel(
                        photoRepository = PhotoRepositoryImpl(UnsplashApi.create()),
                        imageLoader = ImageLoader(),
                        clientId = Config.AccessKey
                    )

                    MainScreen(viewModel = viewModel)
                    
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
    PhotoGridTheme {
        Greeting("Android")
    }
}
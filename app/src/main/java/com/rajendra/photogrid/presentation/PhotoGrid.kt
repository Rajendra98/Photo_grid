package com.rajendra.photogrid.presentation

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.FlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PhotoGrid(
    viewModel:ImageViewModel,
    modifier: Modifier
) {
    // Get ViewModel

    val state by viewModel.state.collectAsState()

    val lazyGridState = rememberLazyStaggeredGridState()


    // Lazy vertical grid to display photos
    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),  // Define the number of columns (e.g., 2 columns)
        modifier = modifier.padding(16.dp),
        state = lazyGridState,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)  // Remove horizontal space between columns
    )  {
        items(state.photos.size) { index ->
            var image by remember { mutableStateOf<Bitmap?>(null) }
            var isLoading by remember { mutableStateOf(true) }
            var loadError by remember { mutableStateOf<String?>(null) }

            val photo=state.photos.get(index)
            // Load image using ViewModel
            LaunchedEffect(photo) {
                isLoading = true
                loadError = null
                image = viewModel.loadImage(photo.urls.small)
                isLoading = false
                if (image == null) {
                    loadError = "Failed to load image"
                }
            }

            // Display image or error message
            if (image != null) {
                Image(
                    bitmap = image!!.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxWidth().border(2.dp, Color.Black,
                        RoundedCornerShape(6.dp)
                    ),
                    contentScale = ContentScale.FillWidth
                )
            } else if (loadError != null) {
                Text(loadError!!, Modifier.fillMaxWidth().padding(8.dp), color = Color.Black)
            } else if (isLoading) {
                Text(" Image Loading...", Modifier.fillMaxWidth().padding(8.dp), color = Color.Blue, fontSize = 8.sp)

            }
        }

        // Check if more photos need to be loaded

    }




    val lastItemVisible by remember {
        derivedStateOf {
            lazyGridState.layoutInfo.visibleItemsInfo.lastOrNull()?.let { lastItem ->
                lastItem.index == state.photos.size - 1
                    // Load the next page of photos
                }        }
    }
    LaunchedEffect(lastItemVisible )
    {
        viewModel.handleEvent(MainEvent.ScrollDown)
    }

}

package com.rajendra.photogrid.presentation

import android.graphics.Bitmap
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rajendra.photogrid.data.ImageLoader
import com.rajendra.photogrid.data.UnsplashApi
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: ImageViewModel)
{
    var showIcon:Boolean by remember {
        mutableStateOf(true)
    }

    val state by viewModel.state.collectAsState()
    Scaffold(

        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black

                ),
                navigationIcon = {
                    Icon( Icons.Default.Info, contentDescription =null )
                },
                title = {
                    Text(
                        text = "Photo Grid",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                actions = {
                    if(showIcon)
                    {
                        IconButton(
                            onClick = {
                                      showIcon=!showIcon

                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon( Icons.Default.Search, contentDescription =null , tint = Color.Black )
                        }
                    }

                }
            )
        },
        content = {
            Column(modifier = Modifier
                .padding(it)
                .background(Color.White)) {
                var query by remember { mutableStateOf("") }
                if(!showIcon)
                {
                    Row(verticalAlignment = Alignment.CenterVertically) {


                        TextField(
                            value = query,
                            onValueChange = { query = it },
                            label = { Text("Search Query") },
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(1f),
                            colors = TextFieldDefaults.textFieldColors(
                                textColor  = Color.Black,
                                containerColor = Color.White

                            )
                        )

                        IconButton(
                            onClick = {
                                showIcon=!showIcon
                                viewModel.handleEvent(MainEvent.Search(query))
                                query=""
                            },
                            modifier = Modifier.padding(8.dp)
                        ) {
                            Icon( Icons.Default.Search, contentDescription =null , tint = Color.Black )
                        }
                    }

                }




                PhotoGrid(viewModel ,modifier=Modifier.weight(1f))
                // Display error message if any
                state.error?.let {
                    Text(it,
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp))
                }

                if (state.loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(60.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(40.dp)
                                .align(Alignment.Center),
                            color = Color.Blue
                        )
                    }
                }


            }
        }, contentColor = Color.Black,
        containerColor = Color.White,
        )


}







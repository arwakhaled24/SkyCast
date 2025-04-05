package com.example.skycast.Favourits.view

import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.Favourits.viewModel.FavouritsViewModel
import com.example.skycast.R
import com.example.skycast.data.dataClasses.LocationDataClass
import com.example.skycast.ui.theme.PrimaryContainer
import kotlinx.coroutines.delay

@Composable
fun OnError(e: Throwable) {
    val context = LocalContext.current
    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
}

@Composable
fun OnLoading() {
    Column(
        Modifier.fillMaxSize(),
        Arrangement.Center,
        Alignment.CenterHorizontally

    ) { CircularProgressIndicator() }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OnSuccess(
    locations: List<LocationDataClass>,
    favViewModel: FavouritsViewModel,
    onFabClick: () -> Unit,
    onItemClick: (LocationDataClass) -> Unit,
    isConnected: Boolean
) {
    val showSnackbar = remember { mutableStateOf(false) }
    val deletedItem = remember { mutableStateOf<LocationDataClass?>(null) }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = Color.Transparent
        , snackbarHost = {

            if (showSnackbar.value && deletedItem.value != null) {
                LaunchedEffect(showSnackbar.value) {
                    delay(2000)
                    showSnackbar.value = false
                    deletedItem.value = null
                }
                Snackbar(
                    action = {
                        TextButton(
                            onClick = {
                                deletedItem.value?.let { favViewModel.addFavLocation(it) }
                                showSnackbar.value = false
                                deletedItem.value = null
                            }
                        ) {
                            Text(stringResource(R.string.undo), color = Color.Gray.copy(.5f) )
                        }
                    },
                    modifier = Modifier.padding(bottom = 80.dp)
                ) {
                    Text(stringResource(R.string.location_removed_from_favorites))
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (!isConnected) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = Color.Red.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = stringResource(R.string.offline_mode),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            Text(
                                text = stringResource(R.string.limited_functionality_available),
                                fontSize = 14.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
            if (locations.isEmpty()) {
                Column(
                    Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.cart_free),
                        contentDescription = "Empty List",
                        Modifier.size(300.dp)
                    )

                    Text(text = stringResource(R.string.no_selected_location_yet),fontWeight = FontWeight.Bold, fontSize = 30.sp, color = Color.White)
                }
            } else {

                Spacer(modifier = Modifier.height(25.dp))
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 80.dp,
                            start = 5.dp,
                            end = 5.dp,
                            bottom = 5.dp
                        ),
                    verticalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    itemsIndexed(locations) { _, location ->
                        FavItem(
                            location = location,
                            onItemClick = { onItemClick(location) },
                            onDeleteClick = {
                                deletedItem.value = location
                                favViewModel.deleteFavLocation(location)
                                showSnackbar.value = true
                            }
                        )
                    }
                }
            }
            FloatingActionButton(
                onClick = onFabClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 120.dp, end = 35.dp)
                    .width(50.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(20.dp),
               // containerColor = PrimaryContainer,
                containerColor = Color.Gray.copy(alpha = 0.9f),
                contentColor = Color.White

                ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "Add Location", tint = Color.White)
            }
        }
    }
}
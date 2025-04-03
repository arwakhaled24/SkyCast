package com.example.skycast.alarm.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.example.skycast.R
import com.example.skycast.alarm.AlarmViewModel
import com.example.skycast.alarm.workManager.MyWorker
import com.example.skycast.data.dataClasses.NotificationDataClass
import com.example.skycast.ui.theme.PrimaryContainer
import kotlinx.coroutines.delay
import java.security.Permission
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Alert(alrmViewModel: AlarmViewModel, currentLocation: Location) {
    AlertScreen(alrmViewModel, currentLocation)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertScreen(alrmViewModel: AlarmViewModel, currenTocation: Location) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                val todayMillis = Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)
                }.timeInMillis
                return utcTimeMillis >= todayMillis
            }

            override fun isSelectableYear(year: Int): Boolean {
                val currentYear = Calendar.getInstance().get(Calendar.YEAR)
                return year >= currentYear
            }
        }
    )
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = false,
    )

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    alrmViewModel.getAllNotification()
    val notificationList = alrmViewModel.notificationLis.collectAsState().value
    val showSnackbar = remember { mutableStateOf(false) }
    val deletedItem = remember { mutableStateOf<NotificationDataClass?>(null) }
    Scaffold(modifier = Modifier
        .fillMaxSize()
        .background(Color(0xFFA5BFCC)),
        snackbarHost = {
            if (showSnackbar.value && deletedItem.value != null) {
                LaunchedEffect(showSnackbar.value) {
                    delay(2000) // Show for 3 seconds
                    showSnackbar.value = false
                    deletedItem.value = null
                }
                Snackbar(
                    action = {
                        TextButton(
                            onClick = {
                                deletedItem.value?.let { alrmViewModel.addNotidication(it) }
                                showSnackbar.value = false
                                deletedItem.value = null
                            }
                        ) {
                            Text(stringResource(R.string.undo))
                        }
                    },
                    modifier = Modifier.padding(bottom = 80.dp)
                ) {
                    Text(stringResource(R.string.alarm_canceled))
                }
            }
        }) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFA5BFCC)),
        ) {
            if (notificationList.isEmpty()) {
                val context = LocalContext.current
                Column(
                    Modifier
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.empity_box_backgroundless),
                        contentDescription = "Empity List",
                        Modifier.size(300.dp)
                    )

                    Text(text = stringResource(R.string.no_recorded_alerts_yet), fontSize = 30.sp)

                }
            } else {
                val context = LocalContext.current
                LazyColumn {
                    items(notificationList.size) { index ->
                        NotificationRow(
                            notificationList[index],
                            onDeleteClick = {
                                deletedItem.value = notificationList[index]
                                showSnackbar.value = true
                                alrmViewModel.deleteNotification(notificationList[index].id)
                                WorkManager.getInstance(context)
                                    .cancelWorkById(notificationList[index].id)
                            }
                        )
                    }
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 120.dp)
            ) {
                val context = LocalContext.current
                var hasNotidicationPermission = remember {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        mutableStateOf(
                            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS)
                                    == PackageManager.PERMISSION_GRANTED
                        )
                    } else {
                        mutableStateOf(true)
                    }
                }

                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        hasNotidicationPermission.value = isGranted
                        if (!isGranted){
                           // shouldShow
                            //////////////////////////////////////////on permission denaied
                        }
                    }
                )

                FloatingActionButton(
                    onClick = {

                        permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        if (hasNotidicationPermission.value){
                            showBottomSheet = true
                        }

                    },
                    modifier = Modifier
                        .padding(bottom = 0.dp, end = 35.dp)
                        .align(Alignment.BottomEnd)
                        .width(50.dp)
                        .height(50.dp),
                    shape = RoundedCornerShape(20.dp),
                    containerColor = PrimaryContainer
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = "FAB")
                }
            }
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {

                    showBottomSheet = false
                },
                sheetState = sheetState
            ) {
                val dateRangePickerState = rememberDateRangePickerState()

                Column(horizontalAlignment = AbsoluteAlignment.Right) {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedDate,
                            onValueChange = { },
                            label = { Text("Date") },
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = { showDatePicker = !showDatePicker }) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = "Set date"
                                    )
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp)
                        )

                        if (showDatePicker) {
                            Popup(
                                onDismissRequest = { showDatePicker = false },
                                alignment = Alignment.TopStart
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .offset(y = 64.dp)
                                        .shadow(elevation = 0.dp)
                                        .background(MaterialTheme.colorScheme.surface)
                                        .padding(bottom = 100.dp),
                                    horizontalAlignment = AbsoluteAlignment.Right
                                ) {
                                    DatePicker(
                                        state = datePickerState,
                                        showModeToggle = false,
                                    )
                                    Row(
                                        modifier = Modifier,
                                        horizontalArrangement = Arrangement.Absolute.Right
                                    ) {
                                        TextButton(onClick = {
                                            showDatePicker = false
                                            showBottomSheet = false
                                        })
                                        {
                                            Text(
                                                text = stringResource(R.string.cancel),
                                                color = Color.Red.copy(alpha = 0.5f)
                                            )
                                        }
                                        TextButton(onClick = {
                                            showDatePicker = false
                                        }) {
                                            Text(text = stringResource(R.string.okay))
                                        }
                                        Spacer(modifier = Modifier.fillMaxWidth(0.1F))
                                    }
                                }
                            }
                        }
                    }

                    Spacer(Modifier.fillMaxHeight(.05f))

                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        TimeInput(
                            state = timePickerState,
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.Absolute.Right
                    ) {
                        val context = LocalContext.current
                        TextButton(onClick = {
                            showBottomSheet = false
                        }) {
                            Text(
                                text = stringResource(R.string.cancel),
                                color = Color.Red.copy(alpha = 0.5f)
                            )
                        }
                        TextButton(onClick = {
                            val currentTimeMillis = System.currentTimeMillis()
                            val selectedCalendar = Calendar.getInstance().apply {
                                timeInMillis =
                                    datePickerState.selectedDateMillis ?: currentTimeMillis
                                set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                                set(Calendar.MINUTE, timePickerState.minute)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }
                            val selectedTimeMillis = selectedCalendar.timeInMillis
                            if (selectedTimeMillis <= currentTimeMillis) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.please_select_a_future_time),
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                showBottomSheet = false
                                var request = OneTimeWorkRequestBuilder<MyWorker>()
                                    .setInitialDelay(
                                        (selectedTimeMillis - currentTimeMillis),
                                        TimeUnit.MILLISECONDS
                                    )
                                    .setInputData(
                                        workDataOf(
                                            "latitude" to currenTocation.latitude,
                                            "longitude" to currenTocation.longitude
                                        ),
                                    ).build()
                                WorkManager.getInstance(context).enqueue(request)
                                alrmViewModel.addNotidication(
                                    NotificationDataClass(
                                        request.id,
                                        time = selectedDate,
                                        date = "${timePickerState.hour} : ${timePickerState.minute}"
                                    )
                                )
                            }
                        }) {
                            Text(stringResource(R.string.add_notification))
                        }
                        Spacer(Modifier.fillMaxWidth(.1f))
                    }
                    Spacer(Modifier.fillMaxHeight(.1f))

                }


            }
        }
    }

}


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}
/*

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}

@Composable
fun RequestPermissionScreen() {
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "Permission Granted!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied!", Toast.LENGTH_SHORT).show()
        }
    }

    Button(onClick = {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }) {
        Text("Request Notification Permission")
    }
}


@Composable
fun checkPermission(context: Context) {


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Button({ permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }) {
            Text("RequestPermission")
        }

    }

}
*/

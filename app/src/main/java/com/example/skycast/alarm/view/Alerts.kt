package com.example.skycast.alarm.view

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimeInput
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.skycast.R
import com.example.skycast.alarm.workManager.MyWorker
import com.example.skycast.ui.theme.PrimaryContainer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit


@Composable
fun Alert() {
    AlertScreen()
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertScreen() {
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
        is24Hour = true,
    )

    val selectedDate = datePickerState.selectedDateMillis?.let {
        convertMillisToDate(it)
    } ?: ""

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA5BFCC)),
    ) {
        if (true) {
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

                Text(text = "No Recorded Alerts Yet", fontSize = 30.sp)

            }
        } else {

        }

            Box(
                modifier = Modifier
                    .fillMaxSize().padding(bottom = 120.dp)
            ) {

                val context = LocalContext.current
                FloatingActionButton(
                    onClick = {
                        showBottomSheet = true
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
             /*   Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("hi arwaa", textAlign = TextAlign.Center)
                }*/

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
                                    }) {
                                        Text(
                                            text = "cancel",
                                            color = Color.Red.copy(alpha = 0.5f)
                                        )
                                    }

                                    TextButton(onClick = {
                                        showDatePicker = false
                                    }) {
                                        Text(text = "okay")
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
                            text = "cancel",
                            color = Color.Red.copy(alpha = 0.5f)
                        )
                    }
                    TextButton(onClick = {
                        showBottomSheet = false
                        var request = OneTimeWorkRequestBuilder<MyWorker>()
                            /* .setInputMerger(
                                 workDataOf(
                                    Data.LocationDataClass
                                 )
                             )*/
                            .setInitialDelay(3, TimeUnit.SECONDS)//have to set the right delay
                            .build()
                        WorkManager.getInstance(context).enqueue(request)

                    }) {
                        Text("add Notification")
                    }
                    Spacer(Modifier.fillMaxWidth(.1f))
                }

                Spacer(Modifier.fillMaxHeight(.1f))

            }


        }
    }
}


fun convertMillisToDate(millis: Long): String {
    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

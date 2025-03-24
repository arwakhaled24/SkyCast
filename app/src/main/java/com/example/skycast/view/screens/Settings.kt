package com.example.skycast.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.skycast.R

@Preview
@Composable
fun Setting() {
    Column(modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)) {
        Spacer(modifier = Modifier.height(25.dp))
        CardView(
            R.string.language, listOf(
                stringResource(R.string.english),
                stringResource(R.string.arabic)
            ), R.drawable.baseline_language_24
        )
        Spacer(modifier = Modifier.height(25.dp))

        CardView(
            R.string.temperture_unit, listOf(
                stringResource(R.string.celsius_c),
                stringResource(R.string.kelvin_k),
                stringResource(R.string.fahrenheit_f)
            ), R.drawable.temperature_svgrepo_com
        )
        Spacer(modifier = Modifier.height(25.dp))

        CardView(
            R.string.edit_location, listOf(
                stringResource(R.string.gps),
                stringResource(R.string.map)
            ), R.drawable.baseline_edit_location_alt_24
        )
        Spacer(modifier = Modifier.height(25.dp))

        CardView(
            R.string.wind_spean_unit, listOf(
                stringResource(R.string.m_sec),
                stringResource(R.string.mile_houre)
            ), R.drawable.wind_svgrepo_com
        )

    }
}

@Composable
fun CardView(title: Int, options: List<String>, icon: Int) {
    ElevatedCard(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 5.dp,
            hoveredElevation = 25.dp
        ),
        modifier = Modifier.height(height = 100.dp)
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Row(modifier = Modifier.padding(start = 5.dp)) {
            Icon(painter = painterResource(icon), "Language Icon")
            Spacer(modifier = Modifier.padding(3.dp))
            Text(text = stringResource(title), fontSize = 22.sp)
        }
        Row {
            RadioButtonSingleSelection(options)
        }
    }
}


@Composable
fun RadioButtonSingleSelection(listOptions: List<String>) {
    val radioOptions = listOptions
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
    Row(
        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth()
    ) {
        radioOptions.forEach { text ->
            Row(
                Modifier
                    .height(56.dp)
                    .selectable(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) },
                        role = Role.RadioButton
                    )
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = (text == selectedOption),
                    onClick = null
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}
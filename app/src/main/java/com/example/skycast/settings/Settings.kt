package com.example.skycast.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skycast.R
import com.example.skycast.utils.Constant
import com.example.skycast.utils.SharedPrefrances

@Preview
@Composable
fun Setting() {
    val context = LocalContext.current
    var selectedLanguage by remember {
        mutableStateOf(
            SharedPrefrances.getInstance(context).getLanguage()
        )
    }
    var selectedTempretureUnit by remember {
        mutableStateOf(
            SharedPrefrances.getInstance(context).getTemperature()
        )
    }
    var selectedLocationSource by remember {
        mutableStateOf(
            SharedPrefrances.getInstance(context).getLocationSource()
        )
    }
    var selectedWindSpeedUnit by remember {
        mutableStateOf(
            SharedPrefrances.getInstance(context).getWindSpeed()
        )
    }
    Column(
        modifier = Modifier
            .padding(top = 5.dp, bottom = 5.dp)
            .background(Color(0xFFA5BFCC))
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        CardView(
            R.string.language,
            listOf(
                stringResource(R.string.english),
                stringResource(R.string.arabic)
            ),
            R.drawable.baseline_language_24,
            selectedOption =selectedLanguage ,
            onOptionSelected = { language ->
                SharedPrefrances.getInstance(context).setLanguage(language)
                selectedLanguage = language
            },
        )
        Spacer(modifier = Modifier.height(25.dp))

        CardView(
            R.string.temperture_unit, listOf(
                stringResource(R.string.celsius_c),
                stringResource(R.string.kelvin_k),
                stringResource(R.string.fahrenheit_f)
            ), R.drawable.temperature_svgrepo_com,
            onOptionSelected = { unit ->
                SharedPrefrances.getInstance(context).setTemperature(unit)
                selectedTempretureUnit = unit
            },
            selectedOption = selectedTempretureUnit
        )
        Spacer(modifier = Modifier.height(25.dp))

        CardView(
            R.string.edit_location, listOf(
                stringResource(R.string.gps),
                stringResource(R.string.map)
            ), R.drawable.baseline_edit_location_alt_24,
            onOptionSelected = { locatiosSource ->
                SharedPrefrances.getInstance(context).setLocationSource(locatiosSource)
                selectedLocationSource = locatiosSource
            },
            selectedOption = selectedLocationSource
        )
        Spacer(modifier = Modifier.height(25.dp))

        CardView(
            R.string.wind_spean_unit, listOf(
                stringResource(R.string.m_sec),
                stringResource(R.string.mile_houre)
            ), R.drawable.wind_svgrepo_com,
            onOptionSelected = {windSpeedUnit->
                SharedPrefrances.getInstance(context).setWindSpeed(windSpeedUnit)
                selectedWindSpeedUnit= windSpeedUnit
            },
            selectedOption =selectedWindSpeedUnit
        )
    }
}

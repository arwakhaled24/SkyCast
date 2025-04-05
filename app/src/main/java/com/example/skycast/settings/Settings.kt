
package com.example.skycast.settings

import android.app.Activity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skycast.Favourits.view.Map
import com.example.skycast.R
import com.example.skycast.home.viewModel.WeatherViewModel
import com.example.skycast.utils.Constant
import com.example.skycast.utils.LanguageChangeHelper
import com.example.skycast.utils.NetworkConnectivityObserver
import com.example.skycast.utils.SharedPreferences

@Composable
fun Setting() {
    val context = LocalContext.current
    val showMap = remember { mutableStateOf(false) }
    val connectivityObserver = remember { NetworkConnectivityObserver(context) }
    val isConnected by connectivityObserver.networkStatus.collectAsState(initial = false)
    AnimatedVisibility(
        visible = showMap.value,
        enter = fadeIn(animationSpec = tween(500)) +
                slideInVertically(animationSpec = tween(700)),

    ) {
        if (isConnected) {
            Map(
                onPlaceSelected = { locationDataClass ->
                    SharedPreferences.getInstance(context).inMap(locationDataClass)
                },
                onLocationAdded = {
                    showMap.value = false
                   (context as Activity).recreate()
                },
                buttonText = stringResource(R.string.set_this_location)
            )
        }
    }
    if(!showMap.value) {
        OnSettings(showMap)
    }
}
@Composable
fun OnSettings(showMap: MutableState<Boolean>) {
    val languageHelber by lazy {
        LanguageChangeHelper()
    }
    val context = LocalContext.current
    var currentLanguageCode = languageHelber.getLanguageCode(context)
    val onCurrentLanguageChange: (String) -> Unit = { code ->
        currentLanguageCode = code
        languageHelber.changelanguage(context, code)
        (context as Activity).recreate()
    }
    val pref = SharedPreferences.getInstance(context)
    val prefConstant = Constant.Companion.sharedPrefrances(context)
    var selectedLanguage by remember {
        mutableStateOf(
            pref.getLanguage()
        )
    }
    var selectedTempretureUnit by remember {
        mutableStateOf(
            pref.getTemperature()
        )
    }
    var selectedLocationSource by remember {
        mutableStateOf(pref.getLocationSource())
    }
    var selectedWindSpeedUnit by remember {
        mutableStateOf(
            pref.getWindSpeed()
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        CardView(
            R.string.language,
            listOf(
                stringResource(R.string.english),
                stringResource(R.string.arabic)
            ),
            R.drawable.baseline_language_24,
            onOptionSelected = { language ->
                selectedLanguage = language
                val code = when {
                    language == context.getString(R.string.english) -> {
                        pref.setLanguage("english")
                        "en"
                    }
                    else -> {
                        pref.setLanguage("arabic")
                        "ar"
                    }
                }
                onCurrentLanguageChange(code)
            },
            selectedOption = when (pref.getLanguage()) {
                "english" -> stringResource(R.string.english)
                "arabic" -> stringResource(R.string.arabic)
                else -> stringResource(R.string.english)
            }
        )
        Spacer(modifier = Modifier.height(25.dp))
        selectedTempretureUnit?.let {
            CardView(
                R.string.temperature_unit,
                listOf(
                    stringResource(R.string.celsius_c),
                    stringResource(R.string.kelvin_k),
                    stringResource(R.string.fahrenheit_f)
                ),
                R.drawable.temperature_svgrepo_com,
                onOptionSelected = { unit ->
                    selectedTempretureUnit = unit
                    when {
                        unit == context.getString(R.string.celsius_c) -> pref.setTemperature(prefConstant.CELSIUS)
                        unit == context.getString(R.string.fahrenheit_f) -> pref.setTemperature(prefConstant.FAHRENHEIT)
                        else -> pref.setTemperature(prefConstant.KELVIN)
                    }
                },
                selectedOption = when (pref.getTemperature()) {
                    prefConstant.CELSIUS -> stringResource(R.string.celsius_c)
                    prefConstant.FAHRENHEIT -> stringResource(R.string.fahrenheit_f)
                    prefConstant.KELVIN -> stringResource(R.string.kelvin_k)
                    else -> stringResource(R.string.celsius_c)
                }
            )
        }
        Spacer(modifier = Modifier.height(25.dp))
        CardView(
            R.string.edit_location,
            listOf(stringResource(R.string.gps), stringResource(R.string.map)),
            R.drawable.baseline_edit_location_alt_24,
            onOptionSelected = { locationSource ->
                selectedLocationSource = locationSource
                if (locationSource == context.getString(R.string.gps)) {
                    pref.setLocationSource(prefConstant.GPS)
                    pref.selectedLocation(false)
                    (context as Activity).recreate()
                } else {
                    pref.setLocationSource(prefConstant.MAP)
                    pref.selectedLocation(true)
                    showMap.value = true
                }
            },
            selectedOption = when (pref.getLocationSource()) {
                prefConstant.GPS -> stringResource(R.string.gps)
                prefConstant.MAP -> stringResource(R.string.map)
                else -> stringResource(R.string.gps)
            }
        )
        Spacer(modifier = Modifier.height(25.dp))
        CardView(
            R.string.wind_speed_unit,
            listOf(
                stringResource(R.string.m_sec),
                stringResource(R.string.mile_hour)
            ),
            R.drawable.wind_svgrepo_com,
            onOptionSelected = { windSpeedUnit ->
                selectedWindSpeedUnit = if (windSpeedUnit == context.getString(R.string.m_sec)) {
                    prefConstant.METER_Sec
                } else {
                    prefConstant.MILE_HOURE
                }
                pref.setWindSpeed(selectedWindSpeedUnit)
            },
            selectedOption = when(selectedWindSpeedUnit) {
                prefConstant.METER_Sec -> stringResource(R.string.m_sec)
                prefConstant.MILE_HOURE -> stringResource(R.string.mile_hour)
                else -> stringResource(R.string.m_sec)
            }
        )
    }
}


/*
package com.example.skycast.settings

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skycast.R
import com.example.skycast.utils.Constant
import com.example.skycast.utils.LanguageChangeHelper
import com.example.skycast.utils.SharedPrefrances

@Preview
@Composable
fun Setting() {
    val languageHelper by lazy {
        LanguageChangeHelper()
    }
    val context = LocalContext.current
    var currentLanguageCode= languageHelper.getLanguageCode(context)
    var currentLanguage = rememberSaveable { mutableStateOf(currentLanguageCode) }
    var onCurrentLanguageChange:(String)->Unit={
            code ->
        currentLanguageCode=code
        languageHelper.changelanguage(context,code)
        ( context as Activity).recreate()
    }


    var selectedLanguage by rememberSaveable {
        mutableStateOf(
            SharedPrefrances.getInstance(context).getLanguage()
        )
    }
    var selectedTempretureUnit by rememberSaveable {
        mutableStateOf(
            SharedPrefrances.getInstance(context).getTemperature()
        )
    }
    var selectedLocationSource by rememberSaveable  {
        mutableStateOf(
            SharedPrefrances.getInstance(context).getLocationSource()
        )
    }
    var selectedWindSpeedUnit by rememberSaveable {
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
            selectedOption =selectedLanguage  ,
            onOptionSelected = { language ->
                SharedPrefrances.getInstance(context).setLanguage(language)
                selectedLanguage = language
              val code:String=  if (language==Constant.Companion.sharedPrefrances(context).ENGLISH) {
                   "en"
                } else
                {
                   "ar"
                }
                onCurrentLanguageChange(code)

            },
        )
        Spacer(modifier = Modifier.height(25.dp))

        CardView(
            R.string.temperature_unit, listOf(
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
            R.string.wind_speed_unit, listOf(
                stringResource(R.string.m_sec),
                stringResource(R.string.mile_hour)
            ), R.drawable.wind_svgrepo_com,
            onOptionSelected = {windSpeedUnit->
                SharedPrefrances.getInstance(context).setWindSpeed(windSpeedUnit)
                selectedWindSpeedUnit= windSpeedUnit
            },
            selectedOption =selectedWindSpeedUnit
        )
    }
}

*/
/*
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
            R.string.temperature_unit, listOf(
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
            R.string.wind_speed_unit, listOf(
                stringResource(R.string.m_sec),
                stringResource(R.string.mile_hour)
            ), R.drawable.wind_svgrepo_com,
            onOptionSelected = {windSpeedUnit->
                SharedPrefrances.getInstance(context).setWindSpeed(windSpeedUnit)
                selectedWindSpeedUnit= windSpeedUnit
            },
            selectedOption =selectedWindSpeedUnit
        )
    }
}*/
package com.example.skycast.settings

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.skycast.Favourits.view.Map
import com.example.skycast.R
import com.example.skycast.utils.Constant
import com.example.skycast.utils.LanguageChangeHelper
import com.example.skycast.utils.SharedPrefrances

@Preview
@Composable
fun Setting() {
    val showMap = remember { mutableStateOf(false) }
    if (showMap.value) {
        val context = LocalContext.current
        Map(
            onPlaceSelected = { locationDataClass ->
                SharedPrefrances.getInstance(context).inMap(locationDataClass)
            },
            onLocationAdded = {
                showMap.value = false
                (context as Activity).recreate()
            },
            buttontext = stringResource(R.string.set_this_location)
        )
    } else {
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
    var currentLanguage = rememberSaveable { mutableStateOf(currentLanguageCode) }
    var onCurrentLanguageChange: (String) -> Unit = { code ->
        currentLanguageCode = code
        languageHelber.changelanguage(context, code)
        (context as Activity).recreate()
    }
    val pref = SharedPrefrances.getInstance(context)
    val prefConstant = Constant.Companion.sharedPrefrances(context)

    var selectedLanguage by remember {
        mutableStateOf(
            pref.getLanguage()
            //  SharedPrefrances.getInstance(context).getLanguage()
        )
    }
    var selectedTempretureUnit by remember {
        mutableStateOf(
            pref.getTemperature()
            //    SharedPrefrances.getInstance(context).getTemperature()
        )
    }
    var selectedLocationSource by remember {
        mutableStateOf(
            pref.getLocationSource()
        )
    }
    var selectedWindSpeedUnit by remember {
        mutableStateOf(
            pref.getWindSpeed()
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
            onOptionSelected = { language ->
                selectedLanguage = language
                val code: String = if (language == context.getString(R.string.english)) {
                    pref.setLanguage(prefConstant.ENGLISH)
                    "en"

                } else {
                    pref.setLanguage(prefConstant.ARABIC)
                    "ar"
                }
                onCurrentLanguageChange(code)
            },
            selectedOption = selectedLanguage,
        )
        Spacer(modifier = Modifier.height(25.dp))

        selectedTempretureUnit?.let {
            CardView(
                R.string.temperature_unit, listOf(
                    stringResource(R.string.celsius_c),
                    stringResource(R.string.kelvin_k),
                    stringResource(R.string.fahrenheit_f)
                ), R.drawable.temperature_svgrepo_com,
                onOptionSelected = { unit ->
                    selectedTempretureUnit = unit
                    if (unit == context.getString(R.string.celsius_c)) {
                        pref.setTemperature(context.getString(R.string.celsius_c))

                    } else if (unit == context.getString(R.string.fahrenheit_f)) {
                        pref.setTemperature(context.getString(R.string.fahrenheit_f))

                    } else {
                        pref.setTemperature(context.getString(R.string.kelvin_k))
                    }
                    (context as Activity).recreate()
                },
                selectedOption = it
            )
        }
        Spacer(modifier = Modifier.height(25.dp))

        CardView(
            R.string.edit_location,
            listOf(
                stringResource(R.string.gps),
                stringResource(R.string.map)
            ), R.drawable.baseline_edit_location_alt_24,
            onOptionSelected = { locatiosSource ->

                selectedLocationSource = locatiosSource

                if (locatiosSource == context.getString(R.string.gps)) {
                    pref.setLocationSource(context.getString(R.string.gps))
                    pref.selectedLocation(false)
                    (context as Activity).recreate()
                } else {
                    pref.setLocationSource(locatiosSource)
                    pref.selectedLocation(true)
                    showMap.value = true
                }
            },
            selectedOption = selectedLocationSource
        )

        Spacer(modifier = Modifier.height(25.dp))

        CardView(
            R.string.wind_speed_unit, listOf(
                stringResource(R.string.m_sec),
                stringResource(R.string.mile_hour)
            ), R.drawable.wind_svgrepo_com,
            onOptionSelected = { windSpeedUnit ->
                selectedWindSpeedUnit = windSpeedUnit
                if (windSpeedUnit == context.getString(R.string.m_sec)) {
                    pref.setWindSpeed(context.getString(R.string.m_sec))

                } else {
                    pref.setWindSpeed(context.getString(R.string.mile_hour))

                }
                (context as Activity).recreate()
            },
            selectedOption = selectedWindSpeedUnit
        )
    }
}


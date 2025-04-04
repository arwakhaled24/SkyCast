package com.example.skycast.settings

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CardView(title: Int, options: List<String>, icon: Int, onOptionSelected:  (String) -> Unit, selectedOption:String) {
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
            RadioButtonGroup(options,onOptionSelected,selectedOption)
        }
    }
}


@Composable
fun RadioButtonGroup(listOptions: List<String>,onOptionSelected:(String) -> Unit,selectedOption:String=listOptions[0]) {
//    var (selectedOption,optionClicked) = remember { mutableStateOf(defauilSelectedOption) }


    val radioOptions = listOptions
    Row(

        modifier = Modifier
            .selectableGroup()
            .fillMaxWidth()
    ) {
       // selectedOption=defauilSelectedOption


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
                    onClick = {onOptionSelected(text)}
                )
                Text(
                    text = text,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}

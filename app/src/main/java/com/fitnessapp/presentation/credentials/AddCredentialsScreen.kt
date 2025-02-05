package com.fitnessapp.presentation.credentials

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.fitnessapp.R
import com.fitnessapp.utils.Constants


@Composable
fun AddCredentialsScreen(
    event: (AddCredentialsEvent) -> Unit
) {
    val context = LocalContext.current
    var weight by remember { mutableStateOf("") } // Store the weight input
    var isBtnEnabled by remember {
        mutableStateOf(false)
    }
    var isError by remember { mutableStateOf(false) } // Track if the input is invalid
    val errorMessage = "Please enter a valid weight (e.g., 50.5)" // Error message
    fun saveUserSharePref(selectedOption: String, toFloat: Float) {
        val SharedPref = context.getSharedPreferences(Constants.U_PREF, Context.MODE_PRIVATE)
        val editor = SharedPref.edit()
        editor.putString(Constants.GENDER, selectedOption)
        editor.putFloat(Constants.WEIGHT, toFloat )
        editor.apply() //commit changes
    }
    // radio Button
    val radioOptions = listOf("Male", "Female")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf<String?>(null) }
    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(50.dp))
        Text(text = "Welcome to Fitness App",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = colorResource(id = R.color.display_small
        ))
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedTextField(value = weight, onValueChange ={newValue ->
            weight = newValue
            isError = !isValidWeight(newValue)
        },
            label = { Text(text = "Enter your weight (kg)")},
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number
            ),
            isError = isError
        )
        if(isError){
            Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(top = 8.dp))
        }
        Spacer(modifier = Modifier.height(50.dp))
//        RadioBtn
        Row(modifier = Modifier
            .padding(horizontal = 15.dp)
            .fillMaxWidth()
            .heightIn(200.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
            ) {
            Image(painter = painterResource(id = R.drawable.mans), contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(180.dp)
                    .padding(end = 10.dp)
                    .weight(0.5f)
                    .clip(RoundedCornerShape(20.dp)),
                )
            Image(painter = painterResource(id = R.drawable.woman), contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(180.dp)
                    .weight(0.5f)
                    .clip(RoundedCornerShape(20.dp)))
        }
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(end = 20.dp,),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            radioOptions.forEach{ text,  ->
                Row(
                    modifier = Modifier
                        .weight(0.5f)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = { onOptionSelected(text) }
                        ), horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = ( text == selectedOption ), onClick = { onOptionSelected(text) })
                    Text(
                        text = text,
                        color = colorResource(id = R.color.display_small),
                        style = MaterialTheme.typography.bodyMedium.merge(),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
        if(isValidWeight(weight) && selectedOption != null){
            isBtnEnabled = true
        }
        //btn
        Button(
           enabled = isBtnEnabled,
            onClick = {
                if (selectedOption != null) {
                    if (isValidWeight(weight)) {
                        saveUserSharePref(selectedOption.toString(), weight.toFloat())
                        event(AddCredentialsEvent.SaveAppEntry)
                    } else {
                        Toast.makeText(context,"Please fill the Required fields,", Toast.LENGTH_SHORT).show()
                        isError = true
                    }
                }
            },
            modifier = Modifier
                .padding(top = 16.dp)
                .width(180.dp)
                .height(50.dp)
        ) {
            Text(text = "Next", style = MaterialTheme.typography.titleLarge, )
        }
        Spacer(modifier = Modifier.height(120.dp))
    }
}
fun isValidWeight(newValue: String): Boolean {
    return try{
        val weightValue = newValue.toDouble()
        weightValue in 30.0..300.0 // weight Range
        
    }catch (e: NumberFormatException){
        false // input isnt a valid number
    }

}


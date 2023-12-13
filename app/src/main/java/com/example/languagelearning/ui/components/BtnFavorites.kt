//package com.example.languagelearning.ui.components
//
//import android.annotation.SuppressLint
//import androidx.compose.animation.animateColor
//import androidx.compose.animation.core.FastOutLinearInEasing
//import androidx.compose.animation.core.LinearOutSlowInEasing
//import androidx.compose.animation.core.animateDp
//import androidx.compose.animation.core.keyframes
//import androidx.compose.animation.core.updateTransition
//import androidx.compose.foundation.layout.size
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.FavoriteBorder
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconToggleButton
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.dp
//
//@SuppressLint("UnusedTransitionTargetStateParameter")
//@Composable
//fun BtnFavorites() {
//    var checkState = remember { mutableStateOf(false) }
//
//
//
//    IconToggleButton(
//        checked = checkState.value,
//        onCheckedChange = {
//            checkState.value = !checkState.value
//    }) {
//        val transition = updateTransition(targetState = checkState.value, label = "")
//
//        val myTint by transition.animateColor(label = "") {isChecked ->
//            if (isChecked){
//                Color.Red
//            } else {
//                Color.Black
//            }
//        }
//
//        val mySize by transition.animateDp(
//            label = "",
//            transitionSpec = {
//                keyframes {
//                    durationMillis = 250
//                    45.dp at 0 with LinearOutSlowInEasing
//                    35.dp at 50 with FastOutLinearInEasing
//                }
//            }
//        ) {35.dp}
//
//        Icon(
//            imageVector = if (checkState.value) {
//                Icons.Filled.Favorite
//            } else {
//                Icons.Filled.FavoriteBorder
//            },
//            contentDescription = "",
//            modifier = Modifier.size(mySize),
//            tint = myTint
//
//        )
//    }
//}
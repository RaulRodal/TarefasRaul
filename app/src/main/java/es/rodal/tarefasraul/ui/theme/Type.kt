package es.rodal.tarefasraul.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import es.rodal.tarefasraul.R


val Silence = FontFamily(
    Font(R.font.silence)
)

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = Silence,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp
    )
)
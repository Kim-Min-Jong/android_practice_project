package com.pr.unit_conversion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnitConverter() {

    // 버튼 및 드롭다운 클릭을 위한 상태 변수
    var inputValue by remember { mutableStateOf("") }
    var outputValue by remember { mutableStateOf("") }
    var inputUnit by remember {
        mutableStateOf("Centimeters")
    }
    var outputUnit by remember {
        mutableStateOf("Meters")
    }
    var isInputExpanded by remember {
        mutableStateOf(false)
    }
    var isOutputExpanded by remember {
        mutableStateOf(false)
    }
    // 변환 승수
    var conversionFactor by remember {
        mutableStateOf(1.0)
    }
    var outputConversionFactor by remember {
        mutableStateOf(1.0)
    }

    fun convertUnits() {
        val inputValueDouble = inputValue.toDoubleOrNull() ?: 0.0
        val result =
            (inputValueDouble * conversionFactor * 100.0 / outputConversionFactor).roundToInt() / 100.0
        outputValue = result.toString()
    }

    val customTextStyle = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontSize = 32.sp,
        color = Color.Black
    )

    // UI 열 형태로 쌓기
    Column(
        // 요소 정렬
        modifier = Modifier.fillMaxSize(), // 전체 채우기
        verticalArrangement = Arrangement.Center, // 수직 중앙 정렬
        horizontalAlignment = Alignment.CenterHorizontally // 수평 중앙 정렬
    ) {
        Text(
            text = "Unit Converter", modifier = Modifier.padding(8.dp),
            style = customTextStyle
        )
        // 간격 컴포저블
        Spacer(modifier = Modifier.height(16.dp))
        // (있어야하는 파라미터) 텍스트 및 텍스트가 입력되었을 때 실행될 콜백 등록
        OutlinedTextField(
            value = inputValue,
            onValueChange = {
                // 익명함수 스코프
                inputValue = it
                convertUnits()
            },
            // placeholder ?
            label = { Text(text = "Enter Value") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        // 그 안에서 Row 가로행 컴포넌트 생성
        Row {
            // modifier에 조정가능한 빈 상자
            Box {
                Button(onClick = { isInputExpanded = !isInputExpanded }) {
                    Text(text = inputUnit)
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
                }
                DropdownMenu(
                    // 열려있는지? state를 잘 활용해야함
                    expanded = isInputExpanded,
                    // 드롭다운이 닫히면 실행 될 콜백
                    onDismissRequest = { isInputExpanded = false }
                ) {
                    // 드롭다운 메뉴 추가
                    DropdownMenuItem(
                        text = {
                            Text(text = "Centimeter")
                        },
                        onClick = {
                            isInputExpanded = false
                            inputUnit = "Centimeter"
                            conversionFactor = 0.01
                            convertUnits()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Meters")
                        },
                        onClick = {
                            isInputExpanded = false
                            inputUnit = "Meter"
                            conversionFactor = 1.0
                            convertUnits()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Feet")
                        },
                        onClick = {
                            isInputExpanded = false
                            inputUnit = "Feet"
                            conversionFactor = 0.3048
                            convertUnits()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "MilliMeters")
                        },
                        onClick = {
                            isInputExpanded = false
                            inputUnit = "Millimeter"
                            conversionFactor = 0.001
                            convertUnits()
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Box {
                Button(onClick = { isOutputExpanded = !isOutputExpanded }) {
                    Text(text = outputUnit)
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = "")
                }
                DropdownMenu(
                    // 열려있는지? state를 잘 활용해야함
                    expanded = isOutputExpanded,
                    // 드롭다운이 닫히면 실행 될 콜백
                    onDismissRequest = { isOutputExpanded = false }
                ) {
                    // 드롭다운 메뉴 추가
                    DropdownMenuItem(
                        text = {
                            Text(text = "Centimeter")
                        },
                        onClick = {
                            isOutputExpanded = false
                            outputUnit = "Centimeter"
                            outputConversionFactor = 0.01
                            convertUnits()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Meters")
                        },
                        onClick = {
                            isOutputExpanded = false
                            outputUnit = "Meter"
                            outputConversionFactor = 1.0
                            convertUnits()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "Feet")
                        },
                        onClick = {
                            isOutputExpanded = false
                            outputUnit = "Feet"
                            outputConversionFactor = 0.3048
                            convertUnits()
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(text = "MilliMeters")
                        },
                        onClick = {
                            isOutputExpanded = false
                            outputUnit = "Millimeter"
                            outputConversionFactor = 0.001
                            convertUnits()
                        }
                    )
                }
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Result:",
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

// 전체 빌드 방지를 위한 프리뷰
@Preview(showBackground = true)
@Composable
fun UnitConverterPreview() {
    UnitConverter()
}


package com.pr.unit_conversion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.pr.unit_conversion.ui.theme.Unit_conversionTheme

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
    // UI 열 형태로 쌓기
    Column {
        Text(text = "Unit Converter")
        // (있어야하는 파라미터) 텍스트 및 텍스트가 입력되었을 때 실행될 콜백 등록
        OutlinedTextField(value = "", onValueChange = {
            // 익명함수 스코프
        })
        // 그 안에서 Row 가로행 컴포넌트 생성
        Row {

        }
    }
}

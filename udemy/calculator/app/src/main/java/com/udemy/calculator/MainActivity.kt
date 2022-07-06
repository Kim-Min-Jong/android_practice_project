package com.udemy.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private var tvInput: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        tvInput = findViewById(R.id.tvInput)
    }
    fun onDigit(view: View){
        // 뷰를 버튼으로 취급해 text를 가져옴
        tvInput?.append((view as Button).text)
    }
    fun onOperator(view: View){
        Toast.makeText(this,"op clicked",Toast.LENGTH_SHORT).show()
    }
    fun onEqual(view: View){
        Toast.makeText(this,"eq clicked",Toast.LENGTH_SHORT).show()
    }
    fun onClear(view: View){
        tvInput?.text = ""
    }
    fun onDecimalPoint(view: View){
        Toast.makeText(this,"button clicked",Toast.LENGTH_SHORT).show()
    }

}
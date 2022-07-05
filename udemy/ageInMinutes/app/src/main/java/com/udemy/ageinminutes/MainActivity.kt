package com.udemy.ageinminutes

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private var tvSelectedDate: TextView? = null
    private var tvAgeInMinutes: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDatePicker: Button = findViewById(R.id.btnDatePicker)
        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        tvAgeInMinutes = findViewById(R.id.tvTime)
        btnDatePicker.setOnClickListener {
            clickDatePicker()
        }
    }
    private fun clickDatePicker(){
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(this,
                { _, selectedYear, selectedMonth, selectedDayOfMonth ->
//                    Toast.makeText(this,"Year was $selectedYear, month was ${selectedMonth+1}, day of Month was $selectedDayOfMonth",Toast.LENGTH_SHORT).show()

                    val selectedDate = "$selectedDayOfMonth/${selectedMonth+1}/${selectedYear}"
                    tvSelectedDate?.text = selectedDate


                    val sdf = SimpleDateFormat("dd/MM/yyyy",Locale.KOREA)
                    val theDate = sdf.parse(selectedDate)

                    //let 날짜 접근 여부를 확인하고 클릭이 선택되면 실행
                    theDate?.let{
                        // 지난 시간을 ms 단위로 출력하기 때문에 60000으로 나눔 --> 분단위로
                        // 1970/1/1 부터 지정한 날까지 지난 시간(ms단위)
                        val selectedDateInMinutes = theDate.time / 60000

                        // 1970/1/1 부터 현재까지 지난 시간(ms단위)
                        val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))

                        //let 날짜 접근 여부를 확인하고 클릭이 선택되면 실행
                        currentDate?.let{
                            val currentDateInMinutes =  currentDate.time / 60000

                            val differenceInMinutes = currentDateInMinutes - selectedDateInMinutes
                            tvAgeInMinutes?.text = "$differenceInMinutes (day:${differenceInMinutes/1440})"
                        }
                    }
                },
                year, month, day
            )
        // 오늘 날짜까지만 선택가능하게
        dpd.datePicker.maxDate = System.currentTimeMillis() - 86400000
        dpd.show()
    }
}
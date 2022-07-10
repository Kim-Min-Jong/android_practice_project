package com.udemy.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import com.udemy.a7minutesworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding: ActivityExerciseBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExerciseBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarExercise)
        //액션바가 있으면
        if(supportActionBar != null){
            // 버튼 보여주게 한다
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        // 액션바 설정
        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }
        setUpRestView()
    }

    // 타이머 충돌 방지를 위해 따로 설정(타이머 있을 때, 없을 떄)
    private fun setUpRestView(){
        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        setRestProgressBar()
    }
    // 프로그레스바 진행 메소드
    private fun setRestProgressBar(){
        binding?.progressBar?.progress = restProgress

        restTimer = object: CountDownTimer(10000, 1000){
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBar?.progress =  10 - restProgress
                binding?.tvTimer?.text = (10 -restProgress).toString()
            }

            override fun onFinish() {
                Toast.makeText(this@ExerciseActivity,"start next exercise",Toast.LENGTH_LONG).show()
            }

        }.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        binding = null
    }
}
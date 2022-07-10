package com.udemy.a7minutesworkout

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.Toast
import com.udemy.a7minutesworkout.databinding.ActivityExerciseBinding

class ExerciseActivity : AppCompatActivity() {
    private var binding: ActivityExerciseBinding? = null

    // 대기용
    private var restTimer: CountDownTimer? = null
    private var restProgress = 0

    // 운동용
    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0

    // 운동 리스트
    private var exerciseList: ArrayList<ExerciseModel>? = null
    private var currentExercisePosition = -1

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

        exerciseList = Constants.defaultExerciseList()

        // 액션바 설정
        binding?.toolbarExercise?.setNavigationOnClickListener {
            onBackPressed()
        }

        setUpRestView()
    }

    // 타이머 충돌 방지를 위해 따로 설정(타이머 있을 때, 없을 떄)
    private fun setUpRestView(){
        // 대기 화면은 보이게하고
        binding?.flRestView?.visibility = View.VISIBLE
        binding?.tvTitle?.visibility = View.VISIBLE
        binding?.upcoming?.visibility = View.VISIBLE
        binding?.kindOfExercise?.visibility = View.VISIBLE

        // 운동화면은 안보이게 하고
        binding?.tvExerciseName?.visibility = View.INVISIBLE
        binding?.flExerciseView?.visibility = View.INVISIBLE
        binding?.ivImage?.visibility = View.INVISIBLE
        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }
        binding?.kindOfExercise?.text = exerciseList!![currentExercisePosition+1].name
        setRestProgressBar()
    }

    private fun setUpExerciseView(){
        // 대기 화면은 가리고
        binding?.flRestView?.visibility = View.INVISIBLE
        binding?.tvTitle?.visibility = View.INVISIBLE
        binding?.upcoming?.visibility = View.INVISIBLE
        binding?.kindOfExercise?.visibility = View.INVISIBLE

        // 운동화면은 보이게 하고
        binding?.tvExerciseName?.visibility = View.VISIBLE
        binding?.flExerciseView?.visibility = View.VISIBLE
        binding?.ivImage?.visibility = View.VISIBLE


        binding?.flExerciseView?.visibility = View.VISIBLE

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        // 데이터 바인딩
        binding?.ivImage?.setImageResource(exerciseList!![currentExercisePosition].image)
        binding?.tvExerciseName?.text = exerciseList!![currentExercisePosition].name

        setExerciseProgressBar()
    }

    // 프로그레스바 진행 메소드 (대기용)
    private fun setRestProgressBar(){
        binding?.progressBar?.progress = restProgress

        restTimer = object: CountDownTimer(3000, 1000){
            override fun onTick(p0: Long) {
                restProgress++
                binding?.progressBar?.progress =  10 - restProgress
                binding?.tvTimer?.text = (10 -restProgress).toString()
            }

            override fun onFinish() {
                currentExercisePosition++
                setUpExerciseView()
            }

        }.start()
    }

    // 프로그레스바 진행 메소드 (운동용)
    private fun setExerciseProgressBar(){
        binding?.progressBar?.progress = exerciseProgress

        exerciseTimer = object: CountDownTimer(3000, 1000){
            override fun onTick(p0: Long) {
                exerciseProgress++
                binding?.progressBarExercise?.progress =  30 - exerciseProgress
                binding?.tvTimerExercise?.text = (30 -exerciseProgress).toString()
            }

            override fun onFinish() {
                if(currentExercisePosition < exerciseList?.size!! - 1){
                    setUpRestView()
                }else{
                    Toast.makeText(this@ExerciseActivity,"운동끘!",Toast.LENGTH_SHORT).show()
                }
            }

        }.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        if(restTimer != null){
            restTimer?.cancel()
            restProgress = 0
        }

        if(exerciseTimer != null){
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }


        binding = null
    }
}
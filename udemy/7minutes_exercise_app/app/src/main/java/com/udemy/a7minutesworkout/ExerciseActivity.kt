package com.udemy.a7minutesworkout

import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.udemy.a7minutesworkout.databinding.ActivityExerciseBinding
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
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

    //tts
    private var tts: TextToSpeech? = null

    // rest view 중간 음성
    private var player: MediaPlayer? = null

    //recycler adapter
    private var exerciseAdapter: ExerciseStatusAdapter? = null

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
        exerciseList = Constants.defaultExerciseList()

        tts = TextToSpeech(this, this)

        setUpRestView()
        setUpExerciseStatusRecyclerView()
    }

    // recyler view function
    private fun setUpExerciseStatusRecyclerView(){
        binding?.rvExerciseStatus?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    // 타이머 충돌 방지를 위해 따로 설정(타이머 있을 때, 없을 떄)
    private fun setUpRestView(){
        // 알림 음성 적용
        try {
            val soundURI = Uri.parse(
             "android.resource://com.udemy.a7minutesworkout/"
            + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        } catch(e:Exception) {
            e.printStackTrace()
        }

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
        //tts 설정
        speakOut(exerciseList!![currentExercisePosition].name)

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
                exerciseList!![currentExercisePosition].isSelected = true
                exerciseAdapter!!.notifyDataSetChanged()
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
                exerciseList!![currentExercisePosition].isCompleted = true
                exerciseList!![currentExercisePosition].isSelected = false
                exerciseAdapter!!.notifyDataSetChanged()

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

        if(tts != null){
            tts?.stop()
            tts?.shutdown()
        }
        if(player != null){
            player!!.stop()
        binding = null
        }
    }

    override fun onInit(status: Int) {
        if(status == TextToSpeech.SUCCESS){
            val result = tts!!.setLanguage(Locale.KOREA)
            if(result == TextToSpeech.LANG_MISSING_DATA
                || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Log.e("TTS","not supported")
            }else{
                Log.e("TTS","init failed")
            }
        }
    }
    private fun speakOut(text:String){
        // queue-flush -- 이전  입력 문장 삭제  add -- 이전문장에 이어서 추가
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH,null,"")
    }
}

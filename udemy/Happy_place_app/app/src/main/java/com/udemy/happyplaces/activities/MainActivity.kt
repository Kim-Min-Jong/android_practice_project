package com.udemy.happyplaces.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.udemy.happyplaces.database.DatabaseHandler
import com.udemy.happyplaces.databinding.ActivityMainBinding
import com.udemy.happyplaces.models.HappyPlaceModel

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.fabAddaHappyPlace?.setOnClickListener{
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivity(intent)
        }
    }

    // happy place를 가져오는 함수
    private fun getHappyPlacesListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        // 읽어온 것을 리스트로 저장
        val getHappyPlaceList: ArrayList<HappyPlaceModel> =
            dbHandler.getHappyPlacesList()


    }
}
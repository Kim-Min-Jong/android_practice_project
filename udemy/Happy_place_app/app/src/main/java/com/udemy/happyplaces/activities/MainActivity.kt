package com.udemy.happyplaces.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.happyplaces.adapters.HappyPlacesAdapter
import com.udemy.happyplaces.database.DatabaseHandler
import com.udemy.happyplaces.databinding.ActivityMainBinding
import com.udemy.happyplaces.models.HappyPlaceModel
import com.udemy.happyplaces.utils.SwipeToEditCallback

class MainActivity : AppCompatActivity() {
    private var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)


        getHappyPlacesListFromLocalDB()

        binding?.fabAddaHappyPlace?.setOnClickListener{
            val intent = Intent(this, AddHappyPlaceActivity::class.java)
            startActivity(intent)
        }
        binding?.swipeLl?.setOnRefreshListener {
            getHappyPlacesListFromLocalDB()
            binding?.swipeLl?.isRefreshing = false
        }
    }

    // happy place를 가져오는 함수
    private fun getHappyPlacesListFromLocalDB(){
        val dbHandler = DatabaseHandler(this)
        // 읽어온 것을 리스트로 저장
        val getHappyPlaceList: ArrayList<HappyPlaceModel> =
            dbHandler.getHappyPlacesList()

        if(getHappyPlaceList.size > 0){
            binding?.rvHappyPlacesList?.visibility = View.VISIBLE
            binding?.tvNoRecordsAvailable?.visibility = View.GONE
            setupHappyPlaceRecyclerView(getHappyPlaceList)
        } else{
            binding?.rvHappyPlacesList?.visibility = View.GONE
            binding?.tvNoRecordsAvailable?.visibility = View.VISIBLE
        }

    }

    private fun setupHappyPlaceRecyclerView(happyPlaceList: ArrayList<HappyPlaceModel>){
        binding?.rvHappyPlacesList?.layoutManager = LinearLayoutManager(this)
        binding?.rvHappyPlacesList?.setHasFixedSize(true)
        val adapter = HappyPlacesAdapter(this, happyPlaceList)
        binding?.rvHappyPlacesList?.adapter = adapter
        adapter.setOnClickListener(object:HappyPlacesAdapter.OnClickListener{
            override fun onClick(position: Int, model: HappyPlaceModel) {
                val intent = Intent(this@MainActivity, HappyPlaceDetailActivity::class.java)
                intent.putExtra(EXTRA_PLACE_DETAILS, model)
                startActivity(intent)

            }

        })
        // 스와이프 핸들러
        val editSwipeHandler = object : SwipeToEditCallback(this){
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val adapter = binding?.rvHappyPlacesList?.adapter as HappyPlacesAdapter
                adapter.notifyEditItem(this@MainActivity, viewHolder.adapterPosition, ADD_PLACE_ACTIVITY_REQUEST_CODE)
            }
        }

        val editItemTouchHelper= ItemTouchHelper(editSwipeHandler)
        editItemTouchHelper.attachToRecyclerView(binding?.rvHappyPlacesList)
    }
    companion object {
        var EXTRA_PLACE_DETAILS = "extra_place_details"
        var ADD_PLACE_ACTIVITY_REQUEST_CODE = 1
    }
}
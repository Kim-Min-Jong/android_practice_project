package com.udemy.drawing_app

import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.get

class MainActivity : AppCompatActivity() {
    private var drawingView: DrawingView? = null
    private var mImageButtonCurrentPaint: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawingView = findViewById(R.id.drawing_view)
        drawingView?.setSizeForBrush(10.toFloat())

        //ll를 가져옴
        val llPaintColors = findViewById<LinearLayout>(R.id.ll_paint_colors)

        // 이미지 버튼을 가져옴  --> 이미지버튼에 테두리효과 적용(기본)
        mImageButtonCurrentPaint = llPaintColors[1] as ImageButton
        mImageButtonCurrentPaint!!.setImageDrawable(
            ContextCompat.getDrawable(this, R.drawable.palette_pressed)
        )

        val brush: ImageButton = findViewById(R.id.ib_brush)
        brush.setOnClickListener {
            showBrushSizeChooserDialog()

        }
    }

    private fun showBrushSizeChooserDialog() {
        val brushDialog = Dialog(this)
        brushDialog.setContentView(R.layout.dialog_brush_size)
        brushDialog.setTitle("Brush Size: ")
        val smallBtn = brushDialog.findViewById<ImageButton>(R.id.ib_small_brush)
        val mediumBtn = brushDialog.findViewById<ImageButton>(R.id.ib_medium_brush)
        val largeBtn = brushDialog.findViewById<ImageButton>(R.id.ib_large_brush)

        smallBtn.setOnClickListener {
            drawingView?.setSizeForBrush(5.toFloat())
            brushDialog.dismiss()
        }
        mediumBtn.setOnClickListener {
            drawingView?.setSizeForBrush(10.toFloat())
            brushDialog.dismiss()
        }
        largeBtn.setOnClickListener {
            drawingView?.setSizeForBrush(15.toFloat())
            brushDialog.dismiss()
        }

        brushDialog.show()
    }

    //이미지버튼 클릭 시
    fun paintClicked(view: View){
        //currentPaint가 내가 누른 뷰(기본 검정)가 아닌 다른 뷰일때 (다른 버튼)
       if(view !== mImageButtonCurrentPaint){
           // 버튼과 태그 설정 후
           val imageBtn = view as ImageButton
           val colorTag = imageBtn.tag.toString()
           // 뷰에 대입하여 변경
           drawingView?.setColor(colorTag)
           imageBtn.setImageDrawable(
               ContextCompat.getDrawable(this, R.drawable.palette_pressed)
           )
           mImageButtonCurrentPaint?.setImageDrawable(
               ContextCompat.getDrawable(this, R.drawable.palette_normal)
           )
           // 이전 뷰(버튼)는 새로 설정하여 계속 클릭되있는거 방지
            mImageButtonCurrentPaint = view

       }
    }
}
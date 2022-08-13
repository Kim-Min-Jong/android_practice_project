package com.udemy.drawing_app

import android.Manifest
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.app.Dialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.media.MediaScannerConnection
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.*

class MainActivity : AppCompatActivity() {
    private var drawingView: DrawingView? = null
    private var mImageButtonCurrentPaint: ImageButton? = null
    var customProgressDialog: Dialog? = null

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            result ->
            if(result.resultCode == Activity.RESULT_OK &&result.data != null){
                val imageBackground:ImageView = findViewById(R.id.iv_background)
                imageBackground.setImageURI(result.data?.data)
            }
        }

    var requestPermission: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){
            permission ->
                permission.entries.forEach{
                    val permissionName = it.key
                    val isGranted = it.value
                    if(isGranted){
                        Toast.makeText(applicationContext, "Permission granted now  you can read the storage files", Toast.LENGTH_LONG).show()
                        // 권한 확인후 갤러리 열기
                        val pickIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                        openGalleryLauncher.launch(pickIntent)
                    }else {
                        if(permissionName == Manifest.permission.READ_EXTERNAL_STORAGE){
                            Toast.makeText(applicationContext, "oops! denied storage permission ", Toast.LENGTH_LONG).show()
                        }
                    }
                }
        }

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

        val gallery: ImageButton = findViewById(R.id.ib_gallery)
        gallery.setOnClickListener {
            requestStoragePermission()
        }

        val undo: ImageButton = findViewById(R.id.ib_undo)
        undo.setOnClickListener {
            drawingView?.onClickUndo()
        }
        val redo: ImageButton = findViewById(R.id.ib_redo)
        redo.setOnClickListener {
            drawingView?.onClickRedo()
        }
        val save :ImageButton = findViewById(R.id.ib_save)
        save.setOnClickListener {
            // 비동기 실행
            if(isReadStorageAllowed() && isWriteStorageAllowed()){
                showCustomProgressDialog()
                lifecycleScope.launch {
                    val flDrawingView:FrameLayout = findViewById(R.id.fl_drawing_container)
                    saveBitmapFile(getBitmapFromView(flDrawingView))
                    imageExternalSave(applicationContext, getBitmapFromView(flDrawingView))
                }
            }
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

    // 읽기 권한 확인
    private fun isReadStorageAllowed(): Boolean{
        val result =  ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }
    private fun isWriteStorageAllowed(): Boolean{
        val result =  ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }
    private fun requestStoragePermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
        )){
            showRationaleDialog(
                "Kids Drawing App",
                "Kids Drawing App needs to Access your external storage"
            )
        } else{
            requestPermission.launch(
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
            )
        }
    }

    private fun getBitmapFromView(view: View): Bitmap {
        // 저장할 이미지의 너비 높이, 투명도 설정
        val returnedBitmap = Bitmap.createBitmap(
            view.width, view.height, Bitmap.Config.ARGB_8888)
        // 캔버스에 비트뱅을 넣음
        val canvas = Canvas(returnedBitmap)
        // 배경이 있을 수 있으니 view의 배경을 일단 가져옴
        val bgDrawable = view.background
        // 있으면
        if(bgDrawable != null){
            //캔버스 + 배경
            bgDrawable.draw(canvas)
        }else{
            // 없으면 흰배경
            canvas.drawColor(Color.WHITE)
        }
        // 캔버스를 다시 뷰로 적용
        view.draw(canvas)
        // 그 뷰룰 가진 비트맵을 반환환
        return returnedBitmap
    }
    private suspend fun saveBitmapFile(mBitmap: Bitmap?):String{
        // 결과(이미지) 담을 변수
        var result = ""
        //async task
        withContext(Dispatchers.IO){
            if(mBitmap != null){
                // 오류가 날 수 있기때문에 출력스트림 이용
                try{
                    // 바이트 배열 출력 스트림으로 이미지 출력
                    val bytes = ByteArrayOutputStream()
                    // 비트맵 압축 (압축형식 ,90% 수준, 출력 스트림)
                    mBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes)
                    // 저장할 파일  (파일을 저장할 경로 및 파일 이름 설정
                    val f = File(externalCacheDir?.absoluteFile.toString()
                            + File.separator + "DrawingApp_"+ System.currentTimeMillis() / 1000 + ".png")

                    // 출력 스트림 -- 실제 쓰기 과정
                    val fo = FileOutputStream(f)
                    fo.write(bytes.toByteArray())
                    fo.close()

                    result = f.absolutePath

                    // 위는 백그라운드실행
                    // 이제는 ui 쓰레드에서 실행행
                   runOnUiThread {
                       cancelProgressDialog()
                        if(result.isNotEmpty()){
                            Toast.makeText(applicationContext,"File saved successfully: $result",Toast.LENGTH_LONG).show()
                            showShareDialog(result)
                        }else{
                            Toast.makeText(applicationContext,"File saved Failed..", Toast.LENGTH_LONG).show()
                        }
                    }
                } catch(e:Exception) {  // 실패하면
                    result = ""
                    e.printStackTrace()
                }
            }
        }
        return result
    }
    private suspend fun imageExternalSave(context: Context, bitmap: Bitmap?) {
        withContext(Dispatchers.IO){
            val fileName = "${System.currentTimeMillis().toString()}.png"
            val cr = context.contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/drawingApp")
                put(MediaStore.Images.Media.IS_PENDING, 1)
            }
            println(contentValues)
            val item = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            try{
                val pdf = item?.let{
                    it -> cr.openFileDescriptor(it, "w", null)
                }
                if(pdf != null){
                    val fos = FileOutputStream(pdf.fileDescriptor)
                    // 바이트 배열 출력 스트림으로 이미지 출력
                    val bytes = ByteArrayOutputStream()
                    // 비트맵 압축 (압축형식 ,90% 수준, 출력 스트림)
                    bitmap?.compress(Bitmap.CompressFormat.PNG, 90, bytes)
                    fos.write(bytes.toByteArray())
                    fos.flush()
                    fos.close()
                    pdf.close()
                    cr.update(item, contentValues, null, null)


                }
            } catch (e: IOException) {}
              catch (e:FileNotFoundException){}
            contentValues.clear()
            contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
            item?.let{ it -> cr.update(it, contentValues, null, null) }

        }
    }
    private fun showRationaleDialog(
        title: String,
        message: String,
    ) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(title)
            .setMessage(message)
            .setPositiveButton("Cancel") { dialog, _ ->
                requestPermission.launch(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                )
                dialog.dismiss()
            }
        builder.create().show()
    }

    private fun showShareDialog(result:String){
        val builder: AlertDialog.Builder = AlertDialog.Builder(this@MainActivity)
        builder.setTitle("share the image?")
            .setMessage("Are you share the Image??? if you want CLICK YES!!")
            .setPositiveButton("YES") { dialog, _ ->
                shareImage(result)
                dialog.dismiss()
            }.setNegativeButton("NO"){_,_->}
        builder.create().show()
    }

    // progress dialog
    private fun showCustomProgressDialog() {
        customProgressDialog = Dialog(this@MainActivity)

        /*Set the screen content from a layout resource.
        The resource will be inflated, adding all top-level views to the screen.*/
        customProgressDialog?.setContentView(R.layout.dialog_custom_progress)

        //Start the dialog and display it on screen.
        customProgressDialog?.show()
    }
    private fun cancelProgressDialog() {
        if(customProgressDialog != null){
            customProgressDialog?.dismiss()
            customProgressDialog = null
        }
    }

    // 이미지 공유기능
    private fun shareImage(result: String){
        // 미디어 파일을 미디어스캐너 서비스에 보냄
        // 미디어스캐너 서비스는 메타데이터를 읽고 media content provider에 파일을 추가함
        // 그리고 연결 클라이언트를 열어 파일의 uri를 반환
        // (context, 내보낼 결과, 통과시키지 않을 타입
        MediaScannerConnection.scanFile(this@MainActivity, arrayOf(result),null){
            // path , uri
            path, uri ->
            val shareIntent = Intent()
            // intent의 보내는 액션  -- uri를 보냄
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent.type = "image/png"
            // 선택화면 시작
            startActivity(Intent.createChooser(shareIntent,"Share"))
        }
    }
}
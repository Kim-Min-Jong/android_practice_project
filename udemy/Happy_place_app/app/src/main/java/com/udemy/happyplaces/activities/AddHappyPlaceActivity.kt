package com.udemy.happyplaces.activities

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Matrix
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.google.android.gms.location.*
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.udemy.happyplaces.R
import com.udemy.happyplaces.database.DatabaseHandler
import com.udemy.happyplaces.databinding.ActivityAddHappyPlaceBinding
import com.udemy.happyplaces.models.HappyPlaceModel
import net.daum.mf.map.api.MapPoint
import net.daum.mf.map.api.MapReverseGeoCoder
import net.daum.mf.map.api.MapReverseGeoCoder.ReverseGeoCodingResultListener
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddHappyPlaceActivity : AppCompatActivity(), View.OnClickListener{
    private var binding: ActivityAddHappyPlaceBinding? = null
    private var cal = Calendar.getInstance()
    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener
    // 이미지 위치 uri
    private var saveImageToInternalStorage: Uri? = null

    private var mLatitude: Double = 0.0
    private var mLongitude: Double = 0.0

    private var mHappyPlaceDetails: HappyPlaceModel? = null
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    // deprecated 된 startActivityForResult 대체 수단
    val getGalleryImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                saveImageToInternalStorage = result.data?.data // 선택한 이미지의 주소(상대경로)

                binding?.ivPlaceImage?.setImageURI(saveImageToInternalStorage)
            }
        }
    val takeCameraImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                var bitmap = result.data?.extras?.get("data") as Bitmap // 선택한 이미지의 주소(상대경로)
                val matrix = Matrix()
                matrix.postRotate(90f)
                val rotatedBitmap = Bitmap.createBitmap(bitmap,0,0,
                        bitmap.width, bitmap.height, matrix, true)
                bitmap.recycle()
                saveImageToInternalStorage = saveImageToInternalStorage(rotatedBitmap)
                binding?.ivPlaceImage?.setImageBitmap(rotatedBitmap)
            }
        }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddHappyPlaceBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbarAddPlace)
        //액션바가 있으면
        if(supportActionBar != null){
            // 버튼 보여주게 한다
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        // 액션바 설정
        binding?.toolbarAddPlace?.setNavigationOnClickListener {
            onBackPressed()
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this@AddHappyPlaceActivity)

        // edit할 때의 모델 객체 생성
        if(intent.hasExtra(MainActivity.EXTRA_PLACE_DETAILS)){
            mHappyPlaceDetails = intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS)
        }

        dateSetListener = DatePickerDialog.OnDateSetListener {
                _ , year, month, dayOfMonth ->
                    cal.set(Calendar.YEAR, year)
                    cal.set(Calendar.MONTH, month)
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    updateDateInView()
        }
        updateDateInView()
        if(!isLocationEnabled()){
            Toast.makeText(this@AddHappyPlaceActivity, "provider is turned off. please turn on GPS", Toast.LENGTH_SHORT).show()
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(intent)
        } else{
            Dexter.withActivity(this).withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ).withListener(object: MultiplePermissionsListener {
                // 권한이 확인되면
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                    if(report!!.areAllPermissionsGranted()){

                        requestLocationData()

                    }
                    if(report.isAnyPermissionPermanentlyDenied){
                        Toast.makeText(this@AddHappyPlaceActivity, "you have denied location permission. Please all enable permission", Toast.LENGTH_SHORT).show()
                    }
                }
                // 권한이 필요한지 보여줘야함
                override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) {
                    showRationaleDialogForPermissions()
                }
            }).onSameThread().check()
        }

        // 작성이 아닌 글 수정 시 로직
        if(mHappyPlaceDetails != null){
            supportActionBar?.title = "Edit Happy Place"
            binding?.etTitle?.setText(mHappyPlaceDetails!!.title)
            binding?.etDescription?.setText(mHappyPlaceDetails!!.description)
            binding?.etDate?.setText(mHappyPlaceDetails!!.date)
            binding?.etLocation?.setText(mHappyPlaceDetails!!.location)
            mLatitude = mHappyPlaceDetails!!.latitude
            mLongitude = mHappyPlaceDetails!!.longitude
            saveImageToInternalStorage = Uri.parse(mHappyPlaceDetails!!.image)
            binding?.ivPlaceImage?.setImageURI(saveImageToInternalStorage)
            binding?.btnSave?.text = "UPDATE"
        }

        binding?.etDate?.setOnClickListener(this)
        binding?.ivPlaceImage?.setOnClickListener(this)
        binding?.btnSave?.setOnClickListener(this)
        binding?.etLocation?.setOnClickListener(this)
        binding?.tvSelectAnotherLocation?.setOnClickListener(this)
    }

    // a모든 클릭 리스너
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.et_date -> {
                DatePickerDialog(this@AddHappyPlaceActivity, dateSetListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH))
                    .show()
            }
            R.id.iv_place_image ->{
                println(1)
                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItems = arrayOf("Select photo from Gallery", "Capture photo from Camera")
                pictureDialog.setItems(pictureDialogItems){
                        _, which ->
                        when(which){
                            0 -> choosePhotoFroGallery()
                            1 -> takePhotoFromCamera()
                        }
                }
                pictureDialog.create().show()
            }
            R.id.btn_save -> {
                when{
                    binding?.etTitle?.text.isNullOrEmpty() ->{
                        Toast.makeText(this,"please enter title", Toast.LENGTH_LONG).show()
                    }
                    binding?.etDescription?.text.isNullOrEmpty() ->{
                        Toast.makeText(this,"please enter a description", Toast.LENGTH_LONG).show()
                    }
                    binding?.etLocation?.text.isNullOrEmpty() ->{
                        Toast.makeText(this,"please enter a location", Toast.LENGTH_LONG).show()
                    }
                    saveImageToInternalStorage == null ->{
                        Toast.makeText(this,"please select an image", Toast.LENGTH_LONG).show()
                    } else ->{
                        val happyPlaceModel = HappyPlaceModel(
                            if(mHappyPlaceDetails == null ) 0 else mHappyPlaceDetails!!.id,
                                binding?.etTitle?.text.toString(),
                                saveImageToInternalStorage.toString(),
                                binding?.etDescription?.text.toString(),
                                binding?.etDate?.text.toString(),
                                binding?.etLocation?.text.toString(),
                                mLatitude,
                                mLongitude
                            )
                        val dbHandler = DatabaseHandler(this)
                        if(mHappyPlaceDetails == null){
                            val addHappyPlace = dbHandler.addHappyPlace(happyPlaceModel)
                            if(addHappyPlace > 0){
                                setResult(Activity.RESULT_OK)
                                finish()
                                 Toast.makeText(this, "Swipe to Refresh",Toast.LENGTH_LONG).show()
                            }
                        } else{
                            val updateHappyPlace = dbHandler.updateHappyPlace(happyPlaceModel)
                            if(updateHappyPlace > 0){
                                setResult(Activity.RESULT_OK)
                                finish()
                                Toast.makeText(this, "Swipe to Refresh",Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                }
            }
            R.id.et_location -> {
                try{

                } catch(e: Exception){
                    e.printStackTrace()
                }
            }
            R.id.tv_select_another_location -> {
                val coords = doubleArrayOf(mLatitude,mLongitude)
                val intent = Intent(this, MapActivity::class.java)
                intent.putExtra("coords",coords)
                startActivity(intent)
            }
        }
    }
    private fun openMap(latitude: Double, longitude: Double) {
        val point = MapPoint.mapPointWithGeoCoord(latitude, longitude)
        // 카카오 맵 내장 함수는 rest api 키 값으로 접근해야함
        val mapView = MapReverseGeoCoder(resources.getString(R.string.rest_api_key),
                point, object: ReverseGeoCodingResultListener{
                override fun onReverseGeoCoderFoundAddress(p0: MapReverseGeoCoder?, p1: String?) {
                    Log.e("addressSuccess",p1!!)
                    updateLocationInView(p1)
                }
                override fun onReverseGeoCoderFailedToFindAddress(p0: MapReverseGeoCoder?) {
                    Log.e("addressFail", p0.toString())
                }
            }, this
            )
        mapView.startFindingAddress()
    }
    @SuppressLint("MissingPermission")
    fun requestLocationData() {
        // 받을 위치 콜백
        val mLocationCallback = object: LocationCallback(){
            override fun onLocationResult(p0: LocationResult) {
                val mLastLocation: Location? = p0.lastLocation
                val latitude = mLastLocation?.latitude
                val longitude = mLastLocation?.longitude
                Log.e("latitude","latitude: $latitude")
                Log.e("longitude","longitude: $longitude")
                if (latitude != null && longitude != null) {
                    mLatitude = latitude
                    mLongitude = longitude
                    openMap(latitude, longitude)
                }
            }
        }
        val mLocationRequest = LocationRequest.create().apply{
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback , Looper.myLooper()
        )
    }
    private fun takePhotoFromCamera(){
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        ).withListener(object: MultiplePermissionsListener {
            // 권한이 확인되면
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if(report!!.areAllPermissionsGranted()){
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                    startActivityForResult(intent, GALLERY)
                    takeCameraImageLauncher.launch(intent)
                }
            }
            // 권한이 필요한지 보여줘야함
            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) {
                showRationaleDialogForPermissions()
            }
        }).onSameThread().check()
    }

    private fun choosePhotoFroGallery(){
        // 권한 설정 서드파티 라이브러리
        Dexter.withActivity(this).withPermissions(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ).withListener(object: MultiplePermissionsListener {
            // 권한이 확인되면
            override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                if(report!!.areAllPermissionsGranted()){
                    val intent = Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                    startActivityForResult(intent, GALLERY)
                    getGalleryImageLauncher.launch(intent)
                }
            }
            // 권한이 필요한지 보여줘야함
            override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>, token: PermissionToken) {
                showRationaleDialogForPermissions()
            }
        }).onSameThread().check()
    }

    // 권한 없을 시 왜 필요한 지 보여주며 설정으로
    private fun showRationaleDialogForPermissions(){
        AlertDialog.Builder(this).setMessage("It looks like you have turned off permission required for this feature. It can be enabled under the Application Settings")
            .setPositiveButton("GO TO SETTINGS"){ _, _ ->
                try{
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }.setNegativeButton("Cancel"){ dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }

    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri{
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")

        try{
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,stream)
            stream.flush()
            stream.close()
        } catch (e: Exception){
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    private fun updateDateInView(){
        val format = "yyyy.MM.dd"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        binding?.etDate?.setText(sdf.format(cal.time).toString())
    }
    private fun updateLocationInView(location: String){
        binding?.etLocation?.setText(location)
    }

    private fun isLocationEnabled(): Boolean{
        val locationMgr: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationMgr.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    companion object {
        private const val GALLERY = 1
        private const val CAMERA = 2
        private const val IMAGE_DIRECTORY = "HappyPlacesImages"
        private const val PLACE_AUTOCOMPLETE_REQUEST_CODE = 3
    }
}
package com.example.link.ui.main.detail.fragmentlist

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.SystemClock
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.link.R
import com.example.link.databinding.FragmentDetailWalkBinding
import com.example.link.model.RecordModel
import com.example.link.ui.base.BaseFragment
import com.example.link.ui.main.MainSharedViewModel
import com.example.link.ui.main.MainToolbarViewModel
import com.example.link.ui.main.detail.fragmentlist.DetailWalkViewModel.Companion.ANALYSIS
import com.example.link.ui.main.detail.fragmentlist.DetailWalkViewModel.Companion.GPS
import com.example.link.ui.main.detail.fragmentlist.DetailWalkViewModel.Companion.STEP
import com.example.link.util.DeviceUtil
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt


@AndroidEntryPoint
class DetailWalkFragment : BaseFragment<FragmentDetailWalkBinding, DetailWalkViewModel>(),
    SensorEventListener {

    @Inject
    lateinit var sharedViewModel: MainSharedViewModel

    @Inject
    lateinit var toolbarViewModel: MainToolbarViewModel

    override val viewModel: DetailWalkViewModel by viewModels()

    override fun getViewBinding(): FragmentDetailWalkBinding =
        FragmentDetailWalkBinding.inflate(layoutInflater)


    private val stepPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
                initStepSensor()
            } else {
                showSystemSettingDialog(requireActivity())
            }
        }


    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val responsePermission = permissions.entries.filter {
                (it.key == Manifest.permission.ACCESS_COARSE_LOCATION)
                        || (it.key == Manifest.permission.ACCESS_FINE_LOCATION)
            }

            if (responsePermission.filter { it.value == true }.size == locationPermission.size) {
                initMyLocationManger()
            }
        }


    override fun initViews() {
        initChronometer()
    }


    override fun bindViews() {
        bindTopMenu()
        bindButton()
    }


    override fun observeData() {
        viewModel.menu.observe(viewLifecycleOwner) {
            changeMenu(it)
        }

        viewModel.isWalk.observe(viewLifecycleOwner) { isWalk ->
            isWalk?.let { setWalk(it) }
        }


        viewModel.isBottomViewVisible.observe(viewLifecycleOwner) { isVisible ->
            isVisible?.let { setBottomView(it) }
        }

        sharedViewModel.todayRecord.observe(viewLifecycleOwner){
            setRecordData(it)
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        sensorManager?.unregisterListener(this)
        removeLocationListener()
    }


    /**
     * TopMenuClick
     */

    private var before = STEP

    private fun bindTopMenu() = with(binding) {
        topMenuStepTextView.setOnClickListener { viewModel.setTopMenu(STEP) }
        topMenuGpsTextView.setOnClickListener { viewModel.setTopMenu(GPS) }
        topMenuAnalysisTextView.setOnClickListener { viewModel.setTopMenu(ANALYSIS) }
    }


    private fun changeMenu(menu: String) = with(binding) {
        val green = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green))
        val gray = ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.gray_e0))

        val black = ContextCompat.getColor(requireContext(), R.color.text_gray)
        val white = ContextCompat.getColor(requireContext(), R.color.white)

        //이전뷰 변경
        when (before) {
            STEP -> {
                topMenuStepTextView.apply {
                    backgroundTintList = gray
                    setTextColor(black)
                }
                binding.stepContainer.isGone = true
            }
            GPS -> {
                topMenuGpsTextView.apply {
                    backgroundTintList = gray
                    setTextColor(black)
                }
                binding.mapSampleImageView.isGone = true
                binding.walkButton.isGone = true
                binding.bottomWalkDataButton.isGone = true
            }
            ANALYSIS -> {
                topMenuAnalysisTextView.apply {
                    backgroundTintList = gray
                    setTextColor(black)
                }
            }
        }


        //클릭한 뷰 변경
        when (menu) {
            STEP -> {
                topMenuStepTextView.apply {
                    backgroundTintList = green
                    setTextColor(white)
                }
                binding.stepContainer.visibility = View.VISIBLE
                toolbarViewModel.onChangeBottomNavigation(true)
                toolbarViewModel.setGps(false)
            }
            GPS -> {
                topMenuGpsTextView.apply {
                    backgroundTintList = green
                    setTextColor(white)
                }
                binding.walkButton.visibility = View.VISIBLE
                binding.mapSampleImageView.visibility = View.VISIBLE
                binding.bottomWalkDataButton.visibility = View.VISIBLE
                toolbarViewModel.onChangeBottomNavigation(false)
                toolbarViewModel.setGps(true)
            }
            ANALYSIS -> {
                topMenuAnalysisTextView.apply {
                    backgroundTintList = green
                    setTextColor(white)
                }
            }
        }

        before = menu
    }


    private fun bindButton() {
        binding.walkButton.setOnClickListener {
            if (DeviceUtil.isAndroid10Later()) {
                stepPermissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
            } else {
                initStepSensor()
            }

            viewModel.clickWalkButton()
        }

        binding.bottomWalkDataButton.setOnClickListener {
            viewModel.clickBottomButton()
        }
    }


    /**
     *  Set Walk Data
     */

    private fun setWalk(it: Boolean) {
        if (it) {
            binding.walkButton.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.walk_pause_button))
            startWalk()
            viewModel.setBottomButton(true)
        } else {
            binding.walkButton.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.walk_start_button))
            stopWalk()
            viewModel.setBottomButton(true)
        }
    }


    private fun setBottomView(it: Boolean) {
        binding.bottomWalkDataContainer.isVisible = it

        if (it) {
            binding.bottomWalkDataButton.apply {
                val lp = layoutParams as ConstraintLayout.LayoutParams
                lp.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
                lp.bottomToTop = binding.bottomWalkDataContainer.id
                layoutParams = lp
                setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_keyboard_arrow_down_24
                    )
                )
            }
        } else {
            binding.bottomWalkDataButton.apply {
                val lp = layoutParams as ConstraintLayout.LayoutParams
                lp.bottomToBottom = binding.mapSampleImageView.id
                lp.bottomToTop = ConstraintLayout.LayoutParams.UNSET
                layoutParams = lp
                setImageDrawable(
                    ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_baseline_keyboard_arrow_up_24
                    )
                )
            }
        }
    }


    private fun setRecordData(model: RecordModel) =with(binding){
        val m = model.walkTime.sum()
        val h = m / 60

        countTextView.text = getString(R.string.amount_with_int, model.walkLength.size)
        activityTimeTextView.text =
            if (h > 0) {
                getString(R.string.hour_and_minutes_with_double, h,  (m - h * 60) )
            } else{
                getString(R.string.minutes_with_double, m)
            }
        lengthTextView.text = getString(R.string.km_with_double, model.walkLength.sum())


        val percent = model.walkCount.sum().toDouble() / 10000 * 100
        walkCenterGraphCountTextView.text = model.walkCount.sum().toString()
        walKPercentTextView.text = getString(R.string.percent_with_double, percent)
    }


    /**
     * Start Walk Stop Walk
     */

    private fun startWalk() {
        isWalking = true

        startTimer()
        getMyLocation()
    }


    private fun stopWalk() {
        val walk = binding.bottomStepCountTextView.text.toString().toInt()
        val length = walk * DEFAULT_WALK_LENGTH
        val time = binding.bottomActivityChronometer.text.toString()

        val timeTemp = time.split(':')
        try {
            val h = timeTemp[0].toInt()
            val m = timeTemp[1].toInt()


            val totalMinutes = if (h > 0) {
                h * 60 + m
            } else m

            sharedViewModel.saveWalk(walk, length, totalMinutes)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        isWalking = false
        stopTimer()

        Toast.makeText(requireContext(), "걷기를 중단합니다!!!", Toast.LENGTH_SHORT).show()
    }


    /**
     * Chronometer
     */
//    private var timeWhenStopped: Long = 0

    private fun initChronometer() {
        binding.bottomActivityChronometer.base = SystemClock.elapsedRealtime();
        binding.bottomActivityChronometer.text = getString(R.string.default_time)

        binding.bottomActivityChronometer.setOnChronometerTickListener { chron ->
            val time: Long = SystemClock.elapsedRealtime() - chron.base
            val h = (time / 3600000).toInt()
            val m = (time - h * 3600000).toInt() / 60000
            val s = (time - h * 3600000 - m * 60000).toInt() / 1000
            val t =
                (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
            chron.text = t
        }
    }

    private fun startTimer() {
//        binding.bottomActivityChronometer.base = SystemClock.elapsedRealtime() + timeWhenStopped;
        binding.bottomActivityChronometer.base = SystemClock.elapsedRealtime()
        binding.bottomActivityChronometer.start()
    }


    private fun stopTimer() {
//        timeWhenStopped = binding.bottomActivityChronometer.base - SystemClock.elapsedRealtime();
        binding.bottomActivityChronometer.stop()
    }


    private fun showSystemSettingDialog(activity: Activity) {
        AlertDialog.Builder(activity)
            .setMessage(getString(R.string.please_check_storage_permission))
            .setPositiveButton(getString(R.string.detail_setting)) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri: Uri = Uri.fromParts("package", activity.packageName, null)
                intent.data = uri
                activity.startActivity(intent)
            }
            .setNegativeButton("취소", null)
            .show()
    }

    /**
     * step dectector
     * TYPE_STEP_DETECTOR:  리턴 값이 무조건 1, 앱이 종료되면 다시 0부터 시작
     * TYPE_STEP_COUNTER : 앱 종료와 관계없이 계속 기존의 값을 가지고 있다가 1씩 증가한 값을 리턴
     */


    private var sensorManager: SensorManager? = null

    private var isWalking = false
    private var step = 0f


    private fun initStepSensor() {
        sensorManager = requireContext().getSystemService(Context.SENSOR_SERVICE) as SensorManager

        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR)

        if (sensorManager == null) {
            Toast.makeText(requireContext(), "기기에서 걸음 센서를 발견하지 못했습니다", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_FASTEST)
        }
    }


    override fun onSensorChanged(event: SensorEvent) {
        if (isWalking) {
            if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {

                if (event.values[0] == 1.0f) {
                    // 센서 이벤트가 발생할때 마다 걸음수 증가
                    step++;
                    binding.bottomStepCountTextView.text = step.toString();
                    binding.bottomWalkLengthTextView.text =
                        ((step * DEFAULT_WALK_LENGTH * 100).roundToInt() / 100).toString();
                }
            }
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}


    /**
     * * locatoin
     */
    private lateinit var myLocationListener: MyLocationListener
    private lateinit var locationManager: LocationManager


    private fun getMyLocation() {
        if (::locationManager.isInitialized.not()) {
            locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        }

        val isGpsUnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (isGpsUnabled) {
            locationPermissionLauncher.launch(locationPermission)
        }
    }

    @SuppressLint("MissingPermission")
    private fun initMyLocationManger() {
        val minTime = 1500L
        val minDistance = 100f

        if (::myLocationListener.isInitialized.not()) {
            myLocationListener = MyLocationListener()
        }

        with(locationManager) {
            requestLocationUpdates(
                LocationManager.GPS_PROVIDER, minTime, minDistance, myLocationListener
            )
            requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                minTime,
                minDistance,
                myLocationListener
            )
        }
    }


    inner class MyLocationListener : LocationListener {
        override fun onLocationChanged(location: Location) {
            Log.e("location", "${location.latitude},  ${location.longitude}")
            viewModel.setLocation(location)
            removeLocationListener()
        }
    }


    private fun removeLocationListener() {
        if (::locationManager.isInitialized && ::myLocationListener.isInitialized) {
            locationManager.removeUpdates(myLocationListener)
        }
    }


    companion object {
        fun newInstance() = DetailWalkFragment()

        const val DEFAULT_WALK_LENGTH = 0.00065

        val locationPermission = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}
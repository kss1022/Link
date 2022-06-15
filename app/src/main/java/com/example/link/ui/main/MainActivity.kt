package com.example.link.ui.main

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.ProgressDialog
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.nfc.NdefMessage
import android.nfc.NdefRecord
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.Ndef
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.link.R
import com.example.link.databinding.ActivityMainBinding
import com.example.link.ui.base.BaseActivity
import com.example.link.ui.main.detail.DetailFragment
import com.example.link.ui.main.home.HomeFragment
import com.example.link.ui.main.home.HomeRecordFragment
import com.example.link.ui.main.map.MapFragment
import com.example.link.ui.main.my.MyFragment
import com.example.link.util.DeviceUtil
import com.google.android.material.navigation.NavigationBarView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.StandardCharsets.*
import java.util.*
import kotlin.experimental.and
import kotlin.math.pow


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(),
    NavigationBarView.OnItemSelectedListener {


    override val viewModel: MainViewModel by viewModels()

    override fun getViewBinding(): ActivityMainBinding =
        ActivityMainBinding.inflate(layoutInflater)

    private val sharedViewModel: MainSharedViewModel by viewModels()

    private val toolbarViewModel: MainToolbarViewModel by viewModels()


    override fun initViews() {
        initToolbar()
        initViewModel()
        initNfc()

        binding.bottomNavigationView.setOnItemSelectedListener(this@MainActivity)
        showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
    }


    override fun observeData() {
        toolbarViewModel.navClickEvent.observe(this) {
            showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
            binding.bottomNavigationView.menu.getItem(0).isChecked = true
        }

        toolbarViewModel.navChangeEvent.observe(this) {
            setToolbar()
        }

        toolbarViewModel.bottomNavigationIsVisible.observe(this) {
            it?.let { setBottomNavigation(it) }
        }

        viewModel.profileImage.observe(this) {
            Glide.with(binding.mainProfileImageView)
                .load(it)
                .circleCrop()
                .into(binding.mainProfileImageView)
        }

        sharedViewModel.updateStartEvent.observe(this) {
            binding.progressBar.visibility = View.VISIBLE
        }

        sharedViewModel.petModel.observe(this) {
            Toast.makeText(this, "${it.name}의 데이터를 가져왔습니다!", Toast.LENGTH_SHORT).show()
        }


        lifecycleScope.launch {
            sharedViewModel.updateEndEvent.collect {
                binding.progressBar.isGone = true
            }
        }


        viewModel.nfcMessage.observe(this) {
            sharedViewModel.changePetId(it)
        }

        sharedViewModel.getSleepDataEvent.observe(this){
            if(bluetoothAdapter == null){
                initBluetooth()
            }else{
                if(connetDevice){
                    sendBtData()
                    beginListenForData()
                }else{
                    selectDevice()
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        nfcAdapter?.enableForegroundDispatch(
            this,
            nfcPendingIntent,
            null,
            null
        )

    }

    override fun onPause() {
        super.onPause()
        nfcAdapter?.disableForegroundDispatch(this)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        nfcAdapter?.let {
            //Tag 를 발견한 경우
            if (intent!!.action === NfcAdapter.ACTION_TAG_DISCOVERED) {
                tag = intent!!.getParcelableExtra(NfcAdapter.EXTRA_TAG)
                if (tag != null) {
                    val tagId = getHexString(tag!!.id)
                    viewModel.setTag(tagId)
                    setNfcTag()
                }
            } else {
                //메세지가 존재하는 경우
                val messages =
                    intent!!.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES) ?: return

                for (i in messages.indices) getNfcTextMessage(messages[i] as NdefMessage)
            }
        }
    }


    private fun initViewModel() {
        sharedViewModel.fetchData()
    }


    /**
     * Fragment Change
     */


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_home -> {
                showFragment(HomeFragment.newInstance(), HomeFragment.TAG)
                true
            }

            R.id.menu_detail -> {
                showFragment(DetailFragment.newInstance(), DetailFragment.TAG)
                true
            }

//            R.id.menu_map -> {
//                showFragment(MapFragment.newInstance() , MapFragment.TAG)
//                true
//            }

            R.id.menu_my -> {
                showFragment(MyFragment.newInstance(), MyFragment.TAG)
                true
            }

            else -> false
        }
    }


    fun showFragment(fragment: Fragment, tag: String) {
        val findFragment = supportFragmentManager.findFragmentByTag(tag)

        supportFragmentManager.fragments.forEach {
            supportFragmentManager.beginTransaction().hide(it).commitAllowingStateLoss()
        }

        findFragment?.let {
            supportFragmentManager.beginTransaction().show(it).commitAllowingStateLoss()
            it.changeActionBar()
        } ?: kotlin.run {
            supportFragmentManager.beginTransaction().add(R.id.fragmentContainer, fragment, tag)
                .commitAllowingStateLoss()
        }
    }


    private fun Fragment.changeActionBar() {
        when (this) {
            is HomeFragment -> this.initActionBar()
            is DetailFragment -> this.initActionBar()
            is MapFragment -> this.initActionBar()
            is HomeRecordFragment -> this.initActionBar()
            is MyFragment -> this.initActionBar()
        }
    }


    /**
     * Toolbar Setting
     */

    private fun initToolbar() {
        setSupportActionBar(binding.toolbar)
        binding.toolbar.apply {
            setNavigationOnClickListener { toolbarViewModel.onClick() }
            setOnClickListener { view ->
                Toast.makeText(this@MainActivity, "Toolbar CLick", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setToolbar() {
        binding.toolbar.apply {
            toolbarViewModel.backgroundColor.value?.let { setBackgroundColor(it) }
            toolbarViewModel.navIconRes.value?.let { setNavigationIcon(it) }
                ?: kotlin.run { navigationIcon = null }
            toolbarViewModel.navIconTint.value?.let { navigationIcon?.setTint(it) }
            toolbarViewModel.titleColor.value?.let { setTitleTextColor(it) }
            toolbarViewModel.visible.value?.let { isVisible = it }
        }

        binding.toolbarTitleTextView.text = toolbarViewModel.title.value
    }

    private fun setBottomNavigation(it: Boolean) {
        binding.bottomNavigationView.isVisible = it
    }


    /**
     * Nfc
     *  [setNfcTag] [createTagMessage] [writeTag] Save Tag -Text
     * [getNfcTextMessage] Get Tag -Text
     */

    private var nfcAdapter: NfcAdapter? = null
    private lateinit var nfcPendingIntent: PendingIntent
    private var tag: Tag? = null

    private fun initNfc() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        nfcAdapter?.let {
            if (it.isEnabled.not()) {
                Log.e("NFC Error", "NFC_INACTIVE")
                nfcAdapter = null
                return
            }
            Log.e("NFC", "ACTIVE")

            nfcPendingIntent = if (DeviceUtil.isAndroid12Later()) {
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    PendingIntent.FLAG_MUTABLE
                )
            } else {
                PendingIntent.getActivity(
                    this,
                    0,
                    Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    0
                )
            }
        } ?: kotlin.run {
            Log.e("NFC Error", "NFC DISABLE")
        }
    }


    private fun setNfcTag() {
        val id = sharedViewModel.userId.value
        val message: NdefMessage = createTagMessage(id!!);
        writeTag(message, tag!!)
    }

    private fun createTagMessage(msg: String): NdefMessage {
        return NdefMessage(NdefRecord.createTextRecord("en", msg))
    }

    private fun writeTag(message: NdefMessage, tag: Tag) {
        val size = message.toByteArray().size
        try {
            val ndef = Ndef.get(tag)
            if (ndef != null) {
                ndef.connect()
                if (!ndef.isWritable) {
                    Toast.makeText(applicationContext, "NFC 가 읽기 전용입니다.", Toast.LENGTH_SHORT)
                        .show()
                }
                if (ndef.maxSize < size) {
                    Toast.makeText(applicationContext, "NFC 의 최대 용량이 넘었습니다.", Toast.LENGTH_SHORT)
                        .show()
                }
                ndef.writeNdefMessage(message)
                Toast.makeText(applicationContext, "NFC 에 데이터를 저장했습니다.", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun getNfcTextMessage(mMessage: NdefMessage) {
        val recs = mMessage.records
        for (i in recs.indices) {
            val record = recs[i]

            if (Arrays.equals(record.type, NdefRecord.RTD_TEXT)) {
                val payload = record.payload

                val textEncoding = if ((payload[0] and 128.toByte()).toInt() == 0) UTF_8 else UTF_16
                val langCodeLen = (payload[0] and 63.toByte()).toInt()


                val result =
                    String(payload, langCodeLen + 1, payload.size - langCodeLen - 1, textEncoding)
                viewModel.setNfcMessage(result)
                Log.e("result", " $result")
            } else {
                Log.e("not text", "${getHexString(record.type)} ")
            }
        }
    }


    //TAG ID to HEX
    private val hexArray = byteArrayOf(
        '0'.toByte(), '1'.toByte(),
        '2'.toByte(), '3'.toByte(), '4'.toByte(), '5'.toByte(), '6'.toByte(),
        '7'.toByte(), '8'.toByte(), '9'.toByte(), 'A'.toByte(), 'B'.toByte(),
        'C'.toByte(), 'D'.toByte(), 'E'.toByte(), 'F'.toByte()
    )

    private fun getHexString(raw: ByteArray): String {
        val len = raw.size
        val hex = ByteArray(2 * len)
        var index = 0
        var pos = 0

        for (b in raw) {
            if (pos >= len) break
            pos++
            val v = (b and 0xFF00.toByte()).toInt()
            hex[index++] = hexArray[v ushr 4]
            hex[index++] = hexArray[v and 0xF000]
        }
        return String(hex)
    }




    /**
     *  bluethooth
     */

    private var bluetoothAdapter: BluetoothAdapter? = null

    private var pairedDevices = mutableSetOf<BluetoothDevice>()
    var mDevices: Set<BluetoothDevice>? = null

    private var bSocket: BluetoothSocket? = null
    private var mOutputStream: OutputStream? = null
    private var mInputStream: InputStream? = null

    private var mRemoteDevice: BluetoothDevice? = null
    var onBT = false
    var sendByte = ByteArray(4)

    private lateinit var asyncDialog : ProgressDialog
    private var connetDevice = false


    @Volatile
    var stopWorker = false
    var workerThread: Thread? = null
    lateinit var readBuffer: ByteArray
    var readBufferPosition = 0


    private val blueToothPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) {
            } else {
            }
        }


    private val requestBluetoothLauncher =  registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result->
    }




    @SuppressLint("MissingPermission")
    fun initBluetooth() {
        blueToothPermissionLauncher.launch(Manifest.permission.BLUETOOTH)
        bluetoothAdapter = (getSystemService(BLUETOOTH_SERVICE) as BluetoothManager).adapter

        bluetoothAdapter?.let {
            if (!it.isEnabled) {
                Toast.makeText(this, "블루투스를 사용할수 없습니다!", Toast.LENGTH_SHORT).show()
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                requestBluetoothLauncher.launch( enableBtIntent)
            }else{
                 pairedDevices = it.bondedDevices
                if (pairedDevices.isNotEmpty()) {
                    // 페어링 된 장치가 있는 경우.
                    selectDevice()
                } else {
                    // 페어링 된 장치가 없는 경우.
                    Toast.makeText(applicationContext, "먼저 Bluetooth 설정에 들어가 페어링을 진행해 주세요.", Toast.LENGTH_SHORT).show()
                }
            }
        } ?: kotlin.run {
            btSendThread.interrupt();   // 데이터 송신 쓰레드 종료
            mInputStream?.close();
            mOutputStream?.close();
            bSocket?.close();
            onBT = false;
        }
    }




    @SuppressLint("MissingPermission")
    private fun selectDevice() {
        mDevices = bluetoothAdapter!!.bondedDevices
        val mPairedDeviceCount = mDevices!!.size
        if (mPairedDeviceCount == 0) {
            //  페어링 된 장치가 없는 경우
            Toast.makeText(applicationContext, "장치를 페어링 해주세요!", Toast.LENGTH_SHORT).show()
        }
        val builder = AlertDialog.Builder(this)
        builder.setTitle("블루투스 장치 선택")


        // 페어링 된 블루투스 장치의 이름 목록 작성
        val listItems: MutableList<String> = ArrayList()
        for (device in mDevices!!) {
            listItems.add(device.name)
        }
        listItems.add("취소") // 취소 항목 추가
        val items = listItems.toTypedArray<CharSequence>()
        builder.setItems(items, DialogInterface.OnClickListener { dialog, item ->
            if (item == mPairedDeviceCount) {
                // 연결할 장치를 선택하지 않고 '취소'를 누른 경우
                //finish();
            } else {
                // 연결할 장치를 선택한 경우
                // 선택한 장치와 연결을 시도함
                connectToSelectedDevice(items[item].toString())

            }
        })
        builder.setCancelable(false) // 뒤로 가기 버튼 사용 금지
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    @SuppressLint("MissingPermission")
    fun connectToSelectedDevice(selectedDeviceName: String) {
        mRemoteDevice = getDeviceFromBondedList(selectedDeviceName)

        //Progress Dialog
        asyncDialog = ProgressDialog(this@MainActivity)
        asyncDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        asyncDialog.setMessage("블루투스 연결중..")
        asyncDialog.show()
        asyncDialog.setCancelable(false)
        val BTConnect = Thread {
            try {
                val uuid = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb") //HC-06 UUID
                // 소켓 생성
                bSocket = mRemoteDevice!!.createRfcommSocketToServiceRecord(uuid)


                // RFCOMM 채널을 통한 연결
                bSocket?.connect()

                // 데이터 송수신을 위한 스트림 열기
                mOutputStream = (bSocket as BluetoothSocket ).outputStream
                mInputStream = (bSocket as BluetoothSocket ).inputStream
                runOnUiThread {
                    Toast.makeText(
                        applicationContext,
                        "$selectedDeviceName 연결 완료",
                        Toast.LENGTH_LONG
                    ).show()
                    asyncDialog.dismiss()

                    connetDevice = true
                    sendBtData()
                    beginListenForData()
                }
                onBT = true
            } catch (e: java.lang.Exception) {
                // 블루투스 연결 중 오류 발생
                runOnUiThread {
                    asyncDialog.dismiss()
                    Toast.makeText(applicationContext, "블루투스 연결 오류", Toast.LENGTH_SHORT).show()
                }
            }
        }
        BTConnect.start()
    }

    @SuppressLint("MissingPermission")
    fun getDeviceFromBondedList(name: String): BluetoothDevice? {
        var selectedDevice: BluetoothDevice? = null
        for (device in mDevices!!) {
            if (name == device.name) {
                selectedDevice = device
                break
            }
        }
        return selectedDevice
    }


    var btSendThread = Thread {
        try {
            mOutputStream?.write(sendByte) // 프로토콜 전송
        } catch (e: java.lang.Exception) {
            // 문자열 전송 도중 오류가 발생한 경우.
        }
    }

    // 데이터 전송
    private fun sendBtData() {
        //sendBuffer.order(ByteOrder.LITTLE_ENDIAN);
        val bytes = ByteArray(4)
        bytes[0] = 0xa5.toByte()
        bytes[1] = 0x5a.toByte()
        bytes[2] = 1 //command
        bytes[3] = 10.toByte()
        sendByte = bytes
        btSendThread.run()
    }



    private fun beginListenForData() {
        val handler = Handler()
        val delimiter: Byte = 10 //This is the ASCII code for a newline character
        stopWorker = false
        readBufferPosition = 0
        readBuffer = ByteArray(1024)
        workerThread = Thread {
            while (!Thread.currentThread().isInterrupted && !stopWorker) {
                try {
                    val bytesAvailable: Int = mInputStream!!.available()
                    if (bytesAvailable > 0) {
                        val packetBytes = ByteArray(bytesAvailable)
                        mInputStream!!.read(packetBytes)


                        for (i in 0 until bytesAvailable) {
                            val b = packetBytes[i]
                            if (b == delimiter) {
                                val encodedBytes = ByteArray(readBufferPosition)
                                System.arraycopy(
                                    readBuffer,
                                    0,
                                    encodedBytes,
                                    0,
                                    encodedBytes.size
                                )
                                val data = String(encodedBytes, US_ASCII)
                                readBufferPosition = 0
                                handler.post(Runnable {
                                    Toast.makeText(this, data, Toast.LENGTH_SHORT).show()
                                    Log.e("HeartBeat", binaryStringToInt(data).toString() )
                                    sharedViewModel.setSleepData(binaryStringToInt(data))

                                })
                            } else {
                                readBuffer[readBufferPosition++] = b;
                            }
                        }
                    }
                } catch (e : Exception) {
                    e.printStackTrace()
                    stopWorker = true
                }
            }
        }
        workerThread!!.start()
    }

    private fun binaryStringToInt(binaryString : String) : Int{
        var convertedDouble = 0.0

        for (i in 0 until binaryString.length) {
            if (binaryString[i] == '1') {
                val len: Int = binaryString.length - 2 - i
                convertedDouble += 2.0.pow(len.toDouble())
            }
        }

        return  convertedDouble.toInt()
    }

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java)

        const val REQUEST_ENABLE_BT = 1
    }

}
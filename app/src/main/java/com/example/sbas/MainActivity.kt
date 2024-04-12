package com.example.sbas

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.sbas.data.WarningModel
import com.example.sbas.databinding.ActivityMainBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var backgroundState = 0
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var sp: SharedPreferences
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sp = getSharedPreferences("AppSettings", Context.MODE_PRIVATE)


        retrieveAndSendFCMToken()
        onNewIntent(intent)
        setupToggleListeners()
        setupTextClickListeners()

    }
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent) // 새로운 인텐트를 현재 인텐트로 설정
        var data = intent.getStringExtra("warningId")
        data?.let {
            data  = handleWarningIdData(it)
            Log.d(TAG,"let data $data")
        }

        Log.d(TAG,"onNewIntent 호출 데이터 $data")

        updateBackgroundAndText(data ?: "0")

    }

    private fun handleWarningIdData(data: String) : String{
        val warningIdFromNotification = data.toInt()
        Log.d(TAG, warningIdFromNotification.toString())
        val fireToggleOn = getToggleState(applicationContext, "fireToggle")
        val gasToggleOn = getToggleState(applicationContext, "gasToggle")
        val eqToggleOn = getToggleState(applicationContext, "eqToggle")
        Log.d(TAG,"$fireToggleOn + $gasToggleOn + $eqToggleOn")
        val warningId = if(!fireToggleOn && warningIdFromNotification == 1){
            0
        }else if(!gasToggleOn && warningIdFromNotification == 2){
            0
        }else if(!eqToggleOn && warningIdFromNotification == 3){
            0
        }else{
            warningIdFromNotification
        }

        return warningId.toString()
    }

    private fun retrieveAndSendFCMToken() {
        val isTokenSent = sharedPreferences.getBoolean("isTokenSent", false)
        if (!isTokenSent) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.d("token 실패", "실패.")
                    return@OnCompleteListener
                }

                val token: String = task.result
                sendRegistrationToServer(token)
                Log.d("Get Token", token)
            })
        }
    }
    private fun setupToggleListeners() {
        binding.mainSc.setOnCheckedChangeListener { _, isChecked ->
            binding.fireToggle.isChecked = isChecked
            binding.gasToggle.isChecked = isChecked
            binding.eqToggle.isChecked = isChecked
        }

        // 각 Toggle on/off 현황 SharedPreferences에 저장
        binding.fireToggle.setOnCheckedChangeListener { _, isChecked ->
            saveToggleState(applicationContext, "fireToggle", isChecked)
            Log.d(TAG, sp.getBoolean("fireToggle",false).toString())
            if(!isChecked){
                updateBackgroundAndText("0")
            }
        }

        binding.gasToggle.setOnCheckedChangeListener { _, isChecked ->
            saveToggleState(applicationContext, "gasToggle", isChecked)
            Log.d(TAG, sp.getBoolean("gasToggle",false).toString())
            updateBackgroundAndText("0")

        }

        binding.eqToggle.setOnCheckedChangeListener { _, isChecked ->
            saveToggleState(applicationContext, "eqToggle", isChecked)
            Log.d(TAG, sp.getBoolean("eqToggle",false).toString())
            updateBackgroundAndText("0")

        }
    }


    //StateAcitivity로 넘어가는 Intent 메소드
    private fun setupTextClickListeners() {
        binding.fireText.setOnClickListener {
            startState(binding.fireText.text)
        }
        binding.gasText.setOnClickListener {
            startState(binding.gasText.text)
        }
        binding.eqText.setOnClickListener {
            startState(binding.eqText.text)
        }
    }
    private fun startState(msg: CharSequence) {
        val intent = Intent(this, StateActivity::class.java)
        intent.putExtra("msg", msg)
        startActivity(intent)
    }

    //앱 처음 실행 시 FCM Token을 서버로 전송하는 메소드
    private fun sendRegistrationToServer(token: String) {
        val warningCall: Call<WarningModel> = MyApplication.networkService.sendToken(WarningModel(token))

        warningCall?.enqueue(object : Callback<WarningModel> {
            override fun onResponse(call: Call<WarningModel>, response: Response<WarningModel>) {
                Log.d("Response 성공 :", response.body().toString())
                with(sharedPreferences.edit()) {
                    putBoolean("isTokenSent", true)
                    apply()
                }
            }

            override fun onFailure(call: Call<WarningModel>, t: Throwable) {
                Log.d("Failure", "exception: $t")
            }
        })

    }
    //WarningId에 따른 배경색 & 텍스트 변경
    private fun updateBackgroundAndText(data: String) {
        Log.d(TAG,"backgroundState $data")
        backgroundState = data.toInt()
        when (backgroundState) {
            0 -> {
                binding.alertText.text = SAFE_MESSAGE
                binding.MainLinear.setBackgroundColor(Color.parseColor("#FFFFFFFF")) // White
            }
            1 -> {
                binding.alertText.text = "$DANGER 화재 경보기 $MESSAGE"
                binding.MainLinear.setBackgroundColor(Color.parseColor("#FFFF0000")) // RED
            }
            2 -> {
                binding.alertText.text = "$DANGER 가스 누출 경보기 $MESSAGE"
                binding.MainLinear.setBackgroundColor(Color.parseColor("#000000")) // Black
            }
            3 -> {
                binding.alertText.text = "$DANGER 지진 감지기 $MESSAGE"
                binding.MainLinear.setBackgroundColor(Color.parseColor("#009900")) // Green
            }
            else -> {
                // 예외 상황 처리
                binding.alertText.text = "알 수 없는 경고"
                binding.MainLinear.setBackgroundColor(Color.parseColor("#FFA500")) // Orange
            }
        }
    }

    fun saveToggleState(context: Context, toggleName: String, isOn: Boolean) {
        val editor = sp.edit()
        editor.putBoolean(toggleName, isOn)
        Log.d(TAG,"toggle -> $isOn")
        editor.apply()
    }

    fun getToggleState(context: Context, toggleName: String): Boolean {
        val sharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        // 기본값으로 false를 반환합니다. 토글 버튼이 처음 사용될 때 OFF 상태라고 가정합니다.
        return sharedPreferences.getBoolean(toggleName, true)
    }


    companion object {
        const val SAFE_MESSAGE = "감지된 위협 없음"
        const val DANGER = "경고"
        const val MESSAGE = "가 실행중입니다."
        const val TAG = "MainActivityLogTest"
    }
}
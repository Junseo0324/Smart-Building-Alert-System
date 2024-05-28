package com.example.sbas

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
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
        setupSwitchListeners()
        setupTextClickListeners()

    }

    //warningID를 notification으로 부터 가져오기 위한 onNewIntent
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

    // Switch(On/Off) 상태에 따라 받은 메시지를 어떻게 처리할지에 대한 메소드
    private fun handleWarningIdData(data: String) : String{
        val warningIdFromNotification = data.toInt()
        Log.d(TAG, warningIdFromNotification.toString())
        val fireScOn = getSwitchState(applicationContext, "fireSc")
        val gasScOn = getSwitchState(applicationContext, "gasSc")
        val eqScOn = getSwitchState(applicationContext, "eqSc")
        Log.d(TAG,"$fireScOn + $gasScOn + $eqScOn")
        val warningId = if(!fireScOn && warningIdFromNotification == 1){
            0
        }else if(!gasScOn && warningIdFromNotification == 2){
            0
        }else if(!eqScOn && warningIdFromNotification == 3){
            0
        }else{
            warningIdFromNotification
        }

        return warningId.toString()
    }

    // token 가져오기 및 서버로 토큰 보내기
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

    // 스위치 버튼 상태 변경 및 배경색 변경
    private fun setupSwitchListeners() {
        binding.mainSc.setOnCheckedChangeListener { _, isChecked ->
            Log.d(TAG,"mainSwicth 변환 값 : $isChecked")
            if(isChecked){
                binding.fireSc.isChecked = true
                binding.gasSc.isChecked = true
                binding.eqSc.isChecked = true
            }
            else{
                binding.fireSc.isChecked = false
                binding.gasSc.isChecked = false
                binding.eqSc.isChecked = false
            }

        }

        // 각 Sc on/off 현황 SharedPreferences에 저장
        binding.fireSc.setOnCheckedChangeListener { _, isChecked ->
            saveSwitchState(applicationContext, "fireSc", isChecked)
            Log.d(TAG, sp.getBoolean("fireSc",false).toString())
            if(!isChecked){
                updateBackgroundAndText("0")
            }
        }

        binding.gasSc.setOnCheckedChangeListener { _, isChecked ->
            saveSwitchState(applicationContext, "gasSc", isChecked)
            Log.d(TAG, sp.getBoolean("gasSc",false).toString())
            if(!isChecked){
                updateBackgroundAndText("0")
            }
        }

        binding.eqSc.setOnCheckedChangeListener { _, isChecked ->
            saveSwitchState(applicationContext, "eqSc", isChecked)
            Log.d(TAG, sp.getBoolean("eqSc",false).toString())
            if(!isChecked){
                updateBackgroundAndText("0")
            }
        }
    }


    //센서 텍스트를 클릭 시 센서 로그를 볼 수 있도록 넘어가면서 텍스트에 존재하는 애니메이션 제거
    private fun setupTextClickListeners() {
        binding.fireText.setOnClickListener {
            startState(binding.fireText.text)
            binding.fireText.clearAnimation()
            updateBackgroundAndText("0")

        }
        binding.gasText.setOnClickListener {
            startState(binding.gasText.text)
            binding.gasText.clearAnimation()
            updateBackgroundAndText("0")

        }
        binding.eqText.setOnClickListener {
            startState(binding.eqText.text)
            binding.eqText.clearAnimation()
            updateBackgroundAndText("0")
        }
    }

    //StateActivity로 넘어가는 메소드
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
    //WarningId에 따른 배경색 & 텍스트 변경 -> 경고 메시지에 해당하는 값에 애니메이션 적용으로 변경 & 텍스트 변경은 유지.
    private fun updateBackgroundAndText(data: String) {
        val anim = AlphaAnimation(0.0f,1.0f).apply {
            duration = 100
            startOffset = 20
            repeatMode = Animation.REVERSE
            repeatCount = Animation.INFINITE
        }
        Log.d(TAG,"backgroundState $data")
        backgroundState = data.toInt()
        when (backgroundState) {
            0 -> {
                binding.alertText.text = SAFE_MESSAGE
            }
            1 -> {
                binding.alertText.text = "$DANGER 화재 경보기 $MESSAGE"

                binding.fireText.startAnimation(anim)
                binding.gasText.clearAnimation()
                binding.eqText.clearAnimation()

            }
            2 -> {
                binding.alertText.text = "$DANGER 가스 누출 경보기 $MESSAGE"

                binding.fireText.clearAnimation()
                binding.gasText.startAnimation(anim)
                binding.eqText.clearAnimation()
            }
            3 -> {
                binding.alertText.text = "$DANGER 지진 감지기 $MESSAGE"

                binding.fireText.clearAnimation()
                binding.gasText.clearAnimation()
                binding.eqText.startAnimation(anim)

            }
            else -> {
                // 예외 상황 처리
                binding.alertText.text = "알 수 없는 경고"

            }
        }
    }

    //sharedPreferences에 Switch(On/Off)의 값을 저장
    private fun saveSwitchState(context: Context, ScName: String, isOn: Boolean) {
        val editor = sp.edit()
        editor.putBoolean(ScName, isOn)
        Log.d(TAG,"$ScName Switch -> $isOn")
        editor.apply()
    }

    // 저장된 Switch(On/Off) 값을 반환
    private fun getSwitchState(context: Context, ScName: String): Boolean {
        val sharedPreferences = context.getSharedPreferences("AppSettings", Context.MODE_PRIVATE)
        // 기본값으로 true를 반환하도록 설정. Switch(On/Off) 버튼이 처음 사용될 때 On 상태라고 가정합니다.
        return sharedPreferences.getBoolean(ScName, true)
    }


    companion object {
        const val SAFE_MESSAGE = "감지된 위협 없음"
        const val DANGER = "경고"
        const val MESSAGE = "가 실행중입니다."
        const val TAG = "MainActivityLogTest"
    }
}
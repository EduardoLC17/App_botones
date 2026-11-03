package com.example.app_botones

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_botones.databinding.ActivityConfBinding

class ConfActivity : AppCompatActivity() {

    private lateinit var confBinding: ActivityConfBinding
    private lateinit var sharedFich: SharedPreferences
    private lateinit var nameSharedPhone: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        confBinding = ActivityConfBinding.inflate(layoutInflater)
        setContentView(confBinding.root)
        initPreferentShared()
        start()
    }

    private fun initPreferentShared() {
        val nameSharedFich = getString(R.string.name_preferen_shared_fich)
        nameSharedPhone = getString(R.string.name_shared_phone)
        sharedFich = getSharedPreferences(nameSharedFich, Context.MODE_PRIVATE)
    }

    override fun onResume() {
        super.onResume()
        val back = intent.getBooleanExtra("back", false)
        if (back) {
            confBinding.editPhone.setText("")
            Toast.makeText(this, R.string.msg_new_phone, Toast.LENGTH_LONG).show()
            intent.removeExtra("back")
        }
    }

    private fun start() {
        val sharedPhone: String? = sharedFich.getString(nameSharedPhone, null)

        sharedPhone?.let {
            startMainActivity(it)
        }

        confBinding.btnConf.setOnClickListener {
            val numberPhone = confBinding.editPhone.text.toString()
            if (numberPhone.isEmpty())
                Toast.makeText(this, R.string.msg_empty_phone, Toast.LENGTH_LONG).show()
            else if (!isValidPhoneNumber(numberPhone))
                Toast.makeText(this, R.string.msg_not_valid_phone, Toast.LENGTH_LONG).show()
            else{
                val edit = sharedFich.edit()
                edit.putString(nameSharedPhone, numberPhone)
                edit.apply()
                Log.d("ConfActivity", "telefono guardado " + numberPhone)
                //Toast.makeText(this, "guardado", Toast.LENGTH_SHORT).show()
                startMainActivity(numberPhone)
            }
        }
    }

    private fun startMainActivity(phone: String) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(getString(R.string.string_phone), phone)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        }
        startActivity(intent)
    }

    private fun isValidPhoneNumber(phone: String): Boolean {
        return PhoneNumberUtils.isGlobalPhoneNumber(phone);
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}
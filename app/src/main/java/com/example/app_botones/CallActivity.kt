package com.example.app_botones

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.app_botones.databinding.ActivityCallBinding

class CallActivity : AppCompatActivity() {

    private lateinit var callBinding: ActivityCallBinding
    private var phone: String? = null

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            call()
        }
        else {
            Toast.makeText(this, "Necesitas habilitar los permisos", Toast.LENGTH_LONG).show()
            goToConfiguracionApp()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        callBinding = ActivityCallBinding.inflate(layoutInflater)
        setContentView(callBinding.root)

        phone = intent.getStringExtra(getString(R.string.string_phone))
        callBinding.txtNumero.text = phone
        Log.d("CallActivity", "numero recibido: " + phone)

        callBinding.ivCall.setOnClickListener {
            requestPermissions()
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionPhone() )
                call()
            else
                requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
        }else {
            call()
        }
    }

    private fun PermissionPhone(): Boolean =
        ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED

    private fun call() {
        val intent = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:" + phone!!)
        }
        startActivity(intent)
    }

    private fun goToConfiguracionApp()
    {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", packageName, null)
        }
        startActivity(intent)
    }
}
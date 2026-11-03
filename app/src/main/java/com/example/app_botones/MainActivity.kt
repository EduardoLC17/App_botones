package com.example.app_botones

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.AlarmClock
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app_botones.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var mainBinding: ActivityMainBinding
    private var phone: String? = null
    private var contador = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)

        initEvent()
    }

    override fun onResume() {
        super.onResume()
        phone = intent.getStringExtra(getString(R.string.string_phone))
        mainBinding.txtPhone.setText("Teléfono: " + phone)
    }

    private fun initEvent() {
        mainBinding.ivLlamar.setOnClickListener {
            val intent = Intent(this@MainActivity, CallActivity::class.java).apply {
                putExtra(getString(R.string.string_phone), phone)
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            startActivity(intent)
        }

        mainBinding.ivWeb.setOnClickListener {
            abrirWeb()
        }


        mainBinding.ivAlarma.setOnClickListener {
            ponerAlarma()
        }
        mainBinding.ivCorreo.setOnClickListener {
            mandarCorreo()
        }

        mainBinding.ivConf.setOnClickListener {
            volverConfiguracion()
        }
    }

    private fun abrirWeb() {
        var url = mainBinding.editUrl.text.toString()
        if(url.isEmpty())
            url = "https://www.google.es"
        if (!url.contains("http"))
            url = "https://" + url
        Log.d("MainActivity", "abriendo web: " + url)

        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse(url)
        }
        startActivity(intent);
    }

    private fun ponerAlarma() {
        val calendar = Calendar.getInstance()
        var hora = calendar.get(Calendar.HOUR_OF_DAY)
        var minutos = calendar.get(Calendar.MINUTE) + 2
        if (minutos >= 60){
            minutos = minutos - 60
            hora = hora + 1
        }
        Log.d("prueba", "alarma " + hora + ":" + minutos)

        val intent = Intent(AlarmClock.ACTION_SET_ALARM).apply {
            putExtra(AlarmClock.EXTRA_MESSAGE, "Eduardo: entregar la practica de PMDM")
            putExtra(AlarmClock.EXTRA_HOUR, hora)
            putExtra(AlarmClock.EXTRA_MINUTES, minutos)
            putExtra(AlarmClock.EXTRA_SKIP_UI, true)
        }
        startActivity(intent)
        Toast.makeText(this, "Alarma puesta a las $hora:$minutos", Toast.LENGTH_LONG).show()
    }

    private fun mandarCorreo() {
        val intent=Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("eduardo.lopezcozar@gmail.com"))
            putExtra(Intent.EXTRA_SUBJECT, "Practica Tema 1 y 2")
            putExtra(Intent.EXTRA_TEXT, "Hola, te envio la practica del tema 1 y 2.\nEduardo")
        }
        //Toast.makeText(this, "correo", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }

    private fun volverConfiguracion() {
        val sharedFich = getSharedPreferences("edu_shared_fich", Context.MODE_PRIVATE)
        val edit = sharedFich.edit()
        edit.remove("phone_number")
        edit.apply()

        val intent = Intent(this, ConfActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            putExtra("back", true)
        }
        startActivity(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }
}
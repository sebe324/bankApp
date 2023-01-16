package com.example.testapp1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.testapp1.databinding.ActivityMainBinding
import org.json.JSONException
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var url = "http://192.168.1.125:8080"

    override fun onCreate(savedInstanceState: Bundle?) {
        var login=""
        var password=""
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val requestQueue = Volley.newRequestQueue(this)
        binding.buttonLogin.setOnClickListener {
            login = binding.Login.getText().toString()
            password = binding.Password.getText().toString()
            val myRequest = StringRequest(
                Request.Method.POST, url + "/account/login/" + login + "/" + password,
                { response: String? ->
                    try {
                        val intent = Intent(this, BlikActivity::class.java)
                        intent.putExtra("num",login)
                        intent.putExtra("password", password)
                        startActivity(intent)

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            ) { volleyError: VolleyError ->
                var message = Toast.makeText(applicationContext,"Złe hasło lub login",Toast.LENGTH_SHORT)
                message.show()
            }
           requestQueue.add(myRequest)
        }
    }
}
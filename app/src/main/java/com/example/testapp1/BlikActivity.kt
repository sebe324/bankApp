package com.example.testapp1

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.testapp1.databinding.ActivityBlikBinding
import org.json.JSONException
import org.json.JSONObject


class BlikActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBlikBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        var url="http://192.168.1.103:8080"
        var num: String = intent.getStringExtra("num").toString()

        super.onCreate(savedInstanceState)
        binding=ActivityBlikBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val requestQueue = Volley.newRequestQueue(this)

        binding.button.setOnClickListener{
            val myRequest = StringRequest(
                Request.Method.POST, url + "/blik/generate-blik/" + num,
                { response: String? ->
                    try {
                        val myJsonObject = JSONObject(response)
                        binding.kodBlik.setText(myJsonObject.getString("blik_num"))

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }
            ) { volleyError: VolleyError ->
                var message = Toast.makeText(applicationContext,"cos sie popsulo xd", Toast.LENGTH_SHORT)
                message.show()
            }
            requestQueue.add(myRequest)
        }
    }
}
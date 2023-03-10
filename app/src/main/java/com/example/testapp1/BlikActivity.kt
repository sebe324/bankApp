package com.example.testapp1

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
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
        var url="http://192.168.1.125:8080"
        var num: String = intent.getStringExtra("num").toString()
        var password: String = intent.getStringExtra("password").toString()
        var blik: String =""
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
                        blik=myJsonObject.getString("blik_num")
                        binding.kodBlik.setText(blik)
                        binding.Text.setText("czas trwania kodu")
                        val timer2 = object: CountDownTimer(120000,1000){

                            override fun onTick(milisUntilFinished: Long){

                                binding.Text.setText("Zaakceptuj kod blik")
                                binding.Seconds.setText((milisUntilFinished/1000).toString());

                            }
                            override fun onFinish() {
                                binding.Text.setText("Kod nie zostal zaakceptowany!")
                                binding.Seconds.setText("");
                            }
                        }
                        val timer = object: CountDownTimer(120000,1000){

                            override fun onTick(milisUntilFinished: Long){
                                val checkConfirm=StringRequest(Request.Method.GET, url+"/blik/check-status/"+blik,{
                                        response: String? -> try{
                                    var status :String = response.toString()
                                    Log.d("message",status)
                                    if(status=="active"){binding.Seconds.setText((milisUntilFinished/1000).toString())}
                                    if(status=="to_confirm"){
                                        binding.button2.setText("Potwierd?? kod")
                                        binding.button2.setVisibility(View.VISIBLE)
                                        timer2.start();
                                    }
                                    if(status=="used"){
                                        binding.Seconds.setText("")
                                        timer2.cancel();
                                    }
                                } catch(e: JSONException){
                                    e.printStackTrace()
                                }
                                } ){ volleyError: VolleyError ->
                                    var message = Toast.makeText(applicationContext,"cos sie popsulo xd", Toast.LENGTH_SHORT)
                                    message.show()
                                }
                                requestQueue.add(checkConfirm)
                            }
                            override fun onFinish() {
                                binding.button2.setText("Kod wygas??")
                            }
                        }
                        timer.start();

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
        binding.button2.setOnClickListener(){
            val myRequest = StringRequest(
                Request.Method.POST, url + "/blik/confirm/" + binding.kodBlik.getText()+"/"+password,
                { response: String? -> try {
                    binding.button2.setText("Potwierdzono");

                }catch(e: JSONException){
                    e.printStackTrace()
                }
                } ){ volleyError: VolleyError ->
                var message = Toast.makeText(applicationContext,"cos sie popsulo xd", Toast.LENGTH_SHORT)
                message.show()
            }
            val myRequest2 = StringRequest(
                Request.Method.GET, url + "/blik/confirm/" + binding.kodBlik.getText()+"/"+password,
                { response: String? -> try {
                    binding.button2.setText("Potwierdzono");

                }catch(e: JSONException){
                    e.printStackTrace()
                }
                } ){ volleyError: VolleyError ->
                var message = Toast.makeText(applicationContext,"cos sie popsulo xd", Toast.LENGTH_SHORT)
                message.show()
            }
            requestQueue.add(myRequest2);
            requestQueue.add(myRequest);
        }
    }
}
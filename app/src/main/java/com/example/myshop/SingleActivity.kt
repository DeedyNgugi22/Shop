package com.example.myshop

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import cz.msebera.android.httpclient.entity.StringEntity
import org.json.JSONArray
import org.json.JSONObject

class SingleActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single)
        val prefs: SharedPreferences = this.getSharedPreferences("shop",Context.MODE_PRIVATE)

        //Find the views from the singleactivity.xml
        val prodname = findViewById(R.id.p_name) as TextView
        val prodesc = findViewById(R.id.p_desc) as TextView
        val prodcost = findViewById(R.id.p_cost) as TextView
        val img = findViewById(R.id.img_url) as ImageView

        //Get the data from the preference
        val flashname = prefs.getString("prod_name","")
        val flashdesc = prefs.getString("prod_desc","")
        val flashcost = prefs.getString("prod_cost","")
        val flashing = prefs.getString("img_url","")

        //replace the current
        prodname.text = flashname
        prodesc.text = flashdesc
        prodcost.text = flashcost

        Glide.with(applicationContext).load( flashing)
             .apply(RequestOptions().centerCrop())
             .into(img)


        val progressbar = findViewById(R.id.progressbar) as ProgressBar

        progressbar.visibility = View.GONE
        val phone = findViewById<EditText>(R.id.phone)
        val pay = findViewById<Button>(R.id.pay)

        pay.setOnClickListener {
            progressbar.visibility = View.VISIBLE
            //Initialize loop
            val client = AsyncHttpClient(true,80,443)
            //Create a json object
            val json =JSONObject()
            //Convert the data you are sending to json
            json.put("amount","1")
            json.put("phone",phone.text.toString())
            //Create the body as json entity
            val body = StringEntity(json.toString())
            //Use loop j to post a request
            client.post(this,"https://modcom.pythonanywhere.com/mpesa_payment",
            body,
            "application/json",
            object :JsonHttpResponseHandler(){
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    response: JSONObject?
                ) {
                    Toast.makeText(applicationContext,"Paid Successfully",Toast.LENGTH_LONG).show()
                    progressbar.visibility = View.GONE
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    throwable: Throwable?,
                    errorResponse : JSONObject?
                ) {
                    Toast.makeText(applicationContext,"Error During payment",Toast.LENGTH_LONG).show()
                }

            })


        }
}
}
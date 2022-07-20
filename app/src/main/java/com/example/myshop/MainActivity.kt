package com.example.myshop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.model.Product
import com.example.myshop.adapter.RecyclerAdapter
import com.google.gson.GsonBuilder
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray

class MainActivity : AppCompatActivity() {


        lateinit var productlist: ArrayList<Product>
        lateinit var recyclerAdapter: RecyclerAdapter
        lateinit var progressBar: ProgressBar
        lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Find the recycler bin and the progressbar from the mainActivity XML
        recyclerView = findViewById(R.id.recycler)
        progressBar = findViewById(R.id.progressbar)
        progressBar.visibility = View.VISIBLE

        val client = AsyncHttpClient(true,80,443)
        //pass the list of products to the adapter
        recyclerAdapter = RecyclerAdapter(applicationContext)
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.setHasFixedSize(true)

        client.get(this,"https://modcom.pythonanywhere.com/api/all",null,
            "application/json",
        object :JsonHttpResponseHandler(){
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                response: JSONArray?
            ) {
               val gson = GsonBuilder().create()
                val list = gson.fromJson(response.toString(),
                Array<Product>::class.java).toList()
                //NOW PASS THE CONVERTED LIST TO ADAPTER
                recyclerAdapter.setProductListItems(list)
                progressBar.visibility = View.GONE
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseString: String?,
                throwable: Throwable?
            ) {
                Toast.makeText(applicationContext, "no products on sale",Toast.LENGTH_LONG).show()
                progressBar.visibility = View.GONE
            }
            //convert json array of product from api to a lost
        })//End of getting data
        //now put the adapter to recycler view
        recyclerView.adapter = recyclerAdapter








    }
}
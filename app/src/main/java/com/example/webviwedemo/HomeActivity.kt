package com.example.webviwedemo

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class HomeActivity : AppCompatActivity() {
    private lateinit var myWebView: WebView
    //private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var context: Context
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        context = this
        //swipeRefreshLayout = findViewById(R.id.swip_page)
        myWebView = findViewById(R.id.webview)
        progressBar = findViewById(R.id.pageLoadProgress)


        if (isNetworkAvailable()){
            showWebView()

        }else{
            showErrorDialog()
            //swipeRefreshLayout.setOnRefreshListener { showErrorDialog() }
        }

    }


    private fun isNetworkAvailable(): Boolean {
        val conManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = conManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }

    private fun showWebView() {
        myWebView.apply {
            this.settings.loadsImagesAutomatically = true
            this.settings.javaScriptEnabled = false
            this.settings.useWideViewPort = true
            this.settings.loadWithOverviewMode = true
            this.settings.supportZoom()
            this.settings.builtInZoomControls = true
            this.settings.displayZoomControls = false
            this.webViewClient = WebViewClient()
            myWebView.loadUrl("https://b-ok.africa")
        }

        myWebView.webViewClient = object : WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                progressBar.visibility = View.VISIBLE
                //swipeRefreshLayout.isRefreshing = false
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                progressBar.visibility = View.GONE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(
                view: WebView?,
                request: WebResourceRequest?,
                error: WebResourceError?
            ) {
                super.onReceivedError(view, request, error)
                try {
                    myWebView.stopLoading()
                }catch (e : Exception){

                }

                showErrorDialog()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.action_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //handle refresh
        return when(item.itemId){
            R.id.refresh -> {
                val intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && myWebView.canGoBack()){
            myWebView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    fun showErrorDialog() {
        progressBar.visibility = View.GONE
        myWebView.loadUrl("about:blank")
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle("Error")
        alertDialog.setMessage("Check your Internet Connection and try again.")
        alertDialog.setPositiveButton("Try again") { _, _ ->
            finish()
            startActivity(intent)
        }
        alertDialog.create()
        alertDialog.show()
    }
}
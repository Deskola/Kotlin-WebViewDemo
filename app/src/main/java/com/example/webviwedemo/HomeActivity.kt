package com.example.webviwedemo

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class HomeActivity : AppCompatActivity() {
    private lateinit var myWebView: WebView
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var context: Context
    private lateinit var progressBar: ProgressBar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        context = this
        swipeRefreshLayout = findViewById(R.id.swip_page)
        myWebView = findViewById(R.id.webview)
        progressBar = findViewById(R.id.pageLoadProgress)


        if (isNetworkAvailable()){
            showWebView()
            swipeRefreshLayout.setOnRefreshListener { showWebView() }
        }else{
            showErrorDialog()
            swipeRefreshLayout.setOnRefreshListener { showErrorDialog() }
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
            this.settings.javaScriptEnabled = true
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
            }

            override fun onPageCommitVisible(view: WebView?, url: String?) {
                super.onPageCommitVisible(view, url)
                progressBar.visibility = View.GONE
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                swipeRefreshLayout.isRefreshing = false
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
package com.example.webviwedemo

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.DialogFragment
import java.lang.IllegalStateException

class NetworkAlert(val intent: Intent) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                this.setTitle("Error")
                this.setMessage("Check your Internet connection and try again")
                this.setPositiveButton("Try again"){_,_ ->
                    startActivity(intent)
                }
            }
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }
}
package edu.washington.bennyn.arewethereyet

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    class CustomReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //add in function to stop listening for intents
    }
}

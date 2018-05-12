package edu.washington.bennyn.arewethereyet

import android.Manifest
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import android.telephony.SmsManager


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textMessage : EditText = findViewById(R.id.textMessage)
        val phoneNum: EditText = findViewById(R.id.phoneNum)
        val minuteBox: EditText = findViewById(R.id.minutesBox)
        val startStopBtn: Button = findViewById(R.id.startStopBtn)

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, CustomReceiver::class.java)

        if (checkCallingOrSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.SEND_SMS), 1)
        }

        startStopBtn.setOnClickListener {
            if (startStopBtn.text == "Start") {
                if (!textMessage.text.isEmpty() &&
                        phoneNum.text.length == 10 &&
                        !minuteBox.text.isBlank() &&
                        minuteBox.text[0] != '0') {
                    Toast.makeText(this, "Alarm initiated!", Toast.LENGTH_SHORT).show()
                    intent.putExtra("message", textMessage.text.toString())
                    intent.putExtra("phoneNum", phoneNum.text.toString())
                    val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                    val minutes = minuteBox.text.toString().toInt() * 60 * 1000
                    alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), minutes.toLong(), pendingIntent)
                    startStopBtn.text = "Stop"
                }
            } else { //If stop button is clicked
                val pendingIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT)
                alarmManager.cancel(pendingIntent)
                startStopBtn.text = "Start"
            }
        }
    }

    class CustomReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val message = intent!!.getStringExtra("message")
            val phoneNum = intent!!.getStringExtra("phoneNum")
            val properNum = "(${phoneNum.substring(0, 3)}) ${phoneNum.substring(3, 6)}-${phoneNum.substring(6)}"
            val fullMessage = "${properNum}: ${message}"
            Toast.makeText(context, fullMessage, Toast.LENGTH_SHORT).show()
            SmsManager.getDefault().sendTextMessage(phoneNum, null, message, null, null)
        }
    }
}

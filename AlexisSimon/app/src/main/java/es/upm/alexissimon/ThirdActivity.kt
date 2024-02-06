package es.upm.alexissimon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ThirdActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)
        val buttonNextActivity: Button = findViewById(R.id.goto_second_activity1)
        buttonNextActivity.setOnClickListener{
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }
}
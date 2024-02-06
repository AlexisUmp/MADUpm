package es.upm.alexissimon

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val buttonThirdActivity: Button = findViewById(R.id.goto_third_activity)
        buttonThirdActivity.setOnClickListener{
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }
        val buttonMainActivity: Button = findViewById(R.id.goto_main_activity)
        buttonMainActivity.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
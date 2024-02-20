package es.upm.btb.helloworldkt

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.app.Activity

class SecondActivity : AppCompatActivity() {
    private val TAG = "btaSecondActivity"

    data class User(val name: String?, val email: String?, val score: Int)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        var sr : SharedPreferences = getSharedPreferences("label", 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        val user = getUserData(sr)
        findViewById<TextView>(R.id.textViewName).text = user.name
        findViewById<TextView>(R.id.textViewEmail).text = user.email
        findViewById<Button>(R.id.editProfile).setOnClickListener {
            val editIntent = Intent(this, EditProfileActivity::class.java)
            editIntent.putExtra("name", user.name)
            editIntent.putExtra("email", user.email)
            startActivity(editIntent)
        }
        val buttonNext: Button = findViewById(R.id.secondNextButton)
        buttonNext.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }

        val buttonPrevious: Button = findViewById(R.id.secondPreviousButton)
        buttonPrevious.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUserData(sr : SharedPreferences): User {
        val score = sr.getInt("score", 0)
        val name = sr.getString("name", "User")
        val email = sr.getString("email", "No email provided")
        return User(name, email, score)
    }
    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        Log.d(TAG, "onCreate: The activity is being created.");

        val bundle = intent.getBundleExtra("locationBundle")
        val location: Location? = bundle?.getParcelable("location")

        if (location != null) {
            Log.i(TAG, "onCreate: Location["+location.altitude+"]["+location.latitude+"]["+location.longitude+"][")
        };

        val buttonNext: Button = findViewById(R.id.secondNextButton)
        buttonNext.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }

        val buttonPrevious: Button = findViewById(R.id.secondPreviousButton)
        buttonPrevious.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    */
}
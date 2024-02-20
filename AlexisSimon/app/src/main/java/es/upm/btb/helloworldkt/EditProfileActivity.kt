package es.upm.btb.helloworldkt

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class EditProfileActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val currentName = intent.getStringExtra("name")
        val currentEmail = intent.getStringExtra("email")

        val nameEditText = findViewById<EditText>(R.id.editTextName)
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)

        // Only set the initial text if it's not already set
        if (nameEditText.text.isNullOrBlank()) {
            nameEditText.setText(currentName)
        }
        if (emailEditText.text.isNullOrBlank()) {
            emailEditText.setText(currentEmail)
        }

        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val newName = nameEditText.text.toString()
            val newEmail = emailEditText.text.toString()
            val sr: SharedPreferences = getSharedPreferences("label", 0)
            val editor = sr.edit()
            newName.let { editor.putString("name", it) }
            newEmail.let { editor.putString("email", it) }
            editor.apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
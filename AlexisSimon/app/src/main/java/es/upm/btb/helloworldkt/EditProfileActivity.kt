package es.upm.btb.helloworldkt

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EditProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val sr: SharedPreferences = getSharedPreferences("label", Context.MODE_PRIVATE)

        val nameEditText = findViewById<EditText>(R.id.editTextName)
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)

        nameEditText.setText(sr.getString("name", "Name:"))
        emailEditText.setText(sr.getString("email", "Email:"))

        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val newName = nameEditText.text.toString()
            val newEmail = emailEditText.text.toString()
            val newPassword = passwordEditText.text.toString()

            val editor = sr.edit()
            editor.putString("name", newName)
                .putString("email", newEmail)
                .putString("password", newPassword)
                .apply()

            startActivity(Intent(this, SecondActivity::class.java))
        }
    }
}

package es.upm.btb.helloworldkt

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Debug
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val sr: SharedPreferences = getSharedPreferences("label", Context.MODE_PRIVATE)
        val storedPassword = sr.getString("password", "")

        findViewById<TextView>(R.id.textViewName).text = sr.getString("name", "User")
        findViewById<TextView>(R.id.textViewEmail).text = sr.getString("email", "")

        askForUserIdentifier(sr, storedPassword)
        findViewById<Button>(R.id.secondNextButton).setOnClickListener {
            startActivity(Intent(this, ThirdActivity::class.java))
        }

        findViewById<Button>(R.id.secondPreviousButton).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun askForUserIdentifier(sr: SharedPreferences, storedPassword: String?) {
        val input = EditText(this)
        val passwordInput = EditText(this)
        val layout = LinearLayout(this)
        layout.orientation = LinearLayout.VERTICAL
        layout.addView(input)
        layout.addView(passwordInput)

        AlertDialog.Builder(this)
            .setTitle("Enter User Identifier")
            .setIcon(R.mipmap.ic_launcher)
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val userInput = input.text.toString()
                val password = passwordInput.text.toString()
                if (userInput.isNotBlank() && password.isNotBlank() && userInput == sr.getString("name", "") && password == storedPassword) {
                    Toast.makeText(this, "Successfully registered.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Wrong Information.", Toast.LENGTH_LONG).show()
                    askForUserIdentifier(sr, storedPassword)
                }
            }
            .setNegativeButton("Register ?") { _, _ ->
                startActivity(Intent(this, EditProfileActivity::class.java))
            }
            .show()
    }
}

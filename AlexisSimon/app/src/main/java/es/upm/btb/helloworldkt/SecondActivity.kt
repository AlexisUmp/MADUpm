package es.upm.btb.helloworldkt

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Debug
import android.text.InputFilter
import android.text.InputType
import android.text.Spanned
import android.util.Base64
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import android.text.method.PasswordTransformationMethod
import com.google.android.material.bottomnavigation.BottomNavigationView
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val menuItem = navView.menu.findItem(R.id.navigation_dashboard)
        menuItem.isChecked = true
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    navView.menu.findItem(R.id.navigation_home)?.isChecked = true
                    true
                }

                R.id.navigation_notifications -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    navView.menu.findItem(R.id.navigation_home)?.isChecked = true
                    true
                }

                R.id.navigation_dashboard -> {
                    val intent = Intent(this, SecondActivity::class.java)
                    startActivity(intent)
                    navView.menu.findItem(R.id.navigation_home)?.isChecked = true
                    true
                }

                else -> false
            }
        }

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

        if (getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("dark_theme", false))
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    /*
    @SuppressLint("SetTextI18n")
    private fun askForUserIdentifier(sr: SharedPreferences, storedPassword: String?) {
        val input = EditText(this)
        input.hint = "Name / Email:"
        val passwordInput = EditText(this)
        passwordInput.hint = "Password:"
        passwordInput.inputType = 16
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
                if (userInput.isNotBlank() && password.isNotBlank() && (userInput == sr.getString("name", "") || userInput == sr.getString("email", ""))  && password == storedPassword) {
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
    */


    private fun askForUserIdentifier(sr: SharedPreferences, storedPassword: String?) {
        val input = EditText(this)
        input.hint = "Name / Email:"
        val passwordInput = EditText(this)
        passwordInput.hint = "Password:"
        passwordInput.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
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
                if (userInput.isNotBlank() && password.isNotBlank() && (userInput == sr.getString("name", "") || userInput == sr.getString("email", "")) && password == storedPassword) {
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
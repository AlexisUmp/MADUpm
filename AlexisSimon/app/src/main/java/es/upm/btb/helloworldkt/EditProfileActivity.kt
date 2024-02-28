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
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        nameEditText.hint = "Name: (" + sr.getString("name", "") + ")"
        emailEditText.hint = "Email: (" + sr.getString("email", "") + ")"
        passwordEditText.hint = "Password: "

        // ButtomNavigationMenu
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_profile -> {
                    val intent = Intent(this, EditProfileActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.navigation_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

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

        if (getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("dark_theme", false))
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}

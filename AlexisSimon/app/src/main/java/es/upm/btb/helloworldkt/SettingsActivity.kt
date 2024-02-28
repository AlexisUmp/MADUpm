package es.upm.btb.helloworldkt

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.NightMode
import com.google.android.material.bottomnavigation.BottomNavigationView

class SettingsActivity : AppCompatActivity() {

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val sr: SharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)

        val soundSwitch = findViewById<Switch>(R.id.soundSwitch)
        soundSwitch.isChecked = sr.getBoolean("sound", true)

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

        soundSwitch.setOnCheckedChangeListener { _, isChecked ->
            sr.edit().putBoolean("sound", isChecked).apply()
        }

        val vibrationSwitch = findViewById<Switch>(R.id.vibrationSwitch)
        vibrationSwitch.isChecked = sr.getBoolean("vibration", true)

        vibrationSwitch.setOnCheckedChangeListener { _, isChecked ->
            sr.edit().putBoolean("vibration", isChecked).apply()
        }

        val darkThemeSwitch = findViewById<Switch>(R.id.darkThemeSwitch)
        if (sr.getBoolean("dark_theme", false)) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            darkThemeSwitch.isChecked = true
        } else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            darkThemeSwitch.isChecked = false
        }

        darkThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            sr.edit().putBoolean("dark_theme", isChecked).apply()
            if (isChecked)
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        /*
        if (sr.getBoolean("dark_theme", false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        */
    }
}
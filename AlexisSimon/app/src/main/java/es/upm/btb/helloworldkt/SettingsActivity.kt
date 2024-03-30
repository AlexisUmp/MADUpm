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

    @SuppressLint("UseSwitchCompatOrMaterialCode", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        val menuItem = navView.menu.findItem(R.id.navigation_notifications)
        menuItem.isChecked = true
        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    navView.menu.findItem(R.id.navigation_home)?.isChecked = true
                    true
                }

                R.id.navigation_notifications -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    navView.menu.findItem(R.id.navigation_home)?.isChecked = true
                    startActivity(intent)
                    finish()
                    true
                }

                R.id.navigation_dashboard -> {
                    val intent = Intent(this, SecondActivity::class.java)
                    navView.menu.findItem(R.id.navigation_home)?.isChecked = true
                    startActivity(intent)
                    finish()
                    true
                }

                else -> false
            }
        }

        val sr: SharedPreferences = getSharedPreferences("settings", Context.MODE_PRIVATE)

        val soundSwitch = findViewById<Switch>(R.id.soundSwitch)
        soundSwitch.isChecked = sr.getBoolean("sound", true)

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

        val qrButton = findViewById<Button>(R.id.buttonQR)
        qrButton.setOnClickListener {
            val intent = Intent(this, QrCodeActivity::class.java)
            startActivity(intent)
        }

        val backButton = findViewById<Button>(R.id.buttonBack)
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
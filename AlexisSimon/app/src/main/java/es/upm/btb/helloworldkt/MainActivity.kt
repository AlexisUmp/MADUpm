package es.upm.btb.helloworldkt

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.TextView
import androidx.core.app.ActivityCompat
import android.content.Intent
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.io.FileWriter


class MainActivity : AppCompatActivity(), LocationListener {
    private val TAG = "btaMainActivity"
    private lateinit var locationManager: LocationManager
    private lateinit var latestLocation: Location
    private val locationPermissionCode = 2
    private var isLoading = false
    private lateinit var loader: ProgressBar
    private  lateinit var buttonOsm: Button
    private  lateinit var playAsGuestButton: Button
    private  lateinit var buttonNext: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: The activity is being created.");
        println("Hello world!")

        val navView: BottomNavigationView = findViewById(R.id.nav_view)
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
                    startActivity(intent)
                    finish()
                    navView.menu.findItem(R.id.navigation_home)?.isChecked = true
                    true
                }

                R.id.navigation_dashboard -> {
                    val intent = Intent(this, SecondActivity::class.java)
                    startActivity(intent)
                    finish()
                    navView.menu.findItem(R.id.navigation_home)?.isChecked = true
                    true
                }

                else -> false
            }
        }

        buttonNext = findViewById(R.id.logInButton)
        playAsGuestButton = findViewById(R.id.playAsGuestButton)
        buttonOsm = findViewById(R.id.osmButton)
        loader = findViewById(R.id.loader)

        loader.visibility = View.VISIBLE

        buttonNext.isEnabled = isLoading
        playAsGuestButton.isEnabled = isLoading
        buttonOsm.isEnabled = isLoading

        val root = File(this.filesDir, "Hello_World")
        val file = File(root, "gps_coordinates.csv")
        if (!root.exists() || !file.exists())
        {
            root.mkdir()
            val fw = FileWriter(file)
            fw.append("La_Tabacalera 800 40.405966 -3.701864 1\n" +
                    "Chimpancé 900 40.408355 -3.704700 1\n" +
                    "El_Fabuloso_Cuento_De_Ser_Diferente 700 40.417683 -3.702142 1\n" +
                    "Una_Mirada_A_Otro_Futuro 500 40.343073 -3.697710 1\n" +
                    "Entre_Dos_Universos 600 40.499686 -3.708817 1\n" +
                    "Mo_Evolution 1000 40.336272 -3.713952 1\n" +
                    "Athena_And_Minerva 800 40.526678 -3.661393 1\n" +
                    "Byte_The_Candy 700 40.412371 -3.700932 1\n" +
                    "Selfie_Wall_Fiction 1000 40.436516 -3.706739 1\n" +
                    "Smoking_Graciela 900 40.400396 -3.663107 1\n" +
                    "Offset 800 40.422612 -3.699013 1\n" +
                    "Libres 600 40.409783 -3.706127 1\n" +
                    "Transformation_In_Motion 700 40.453444 -3.704322 1\n" +
                    "La_Chulapa 500 40.394283 -3.728198 1\n" +
                    "Tokyo_Loves_Nairobi 500 40.423284 -3.698346 1\n" +
                    "Boa_Mistura 600 40.408153 -3.700580 1\n" +
                    "Paisaje_Urbano 700 40.463018 -3.702512 1\n" +
                    "Run_Away 900 40.409141 -3.705540 1\n" +
                    "Still_Life 600 40.409211 -3.705597 1\n" +
                    "Moratalaz_Market 800 40.405172 -3.645620 1\n" +
                    "Fouquet_Street 700 40.407252 -3.698490 1\n" +
                    "El_Beso_Multicolor 900 40.392358 -3.701966 1\n" +
                    "The_Rhyme_Of_Things 1000 40.411302 -3.698738 1\n" +
                    "Argumosa_Street 600 40.407851 -3.697047 1\n" +
                    "Episodios_Nacionales 700 40.42207 -3.70091 1")
            fw.flush()
            fw.close()
        }

        buttonNext.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            val bundle = Bundle()
            bundle.putParcelable("location", latestLocation)
            intent.putExtra("locationBundle", bundle)
            startActivity(intent)
        }

        playAsGuestButton.setOnClickListener {
            val intent = Intent(this, OpenStreetMapActivity::class.java)
            val bundle = Bundle()

            bundle.putParcelable("location", latestLocation)
            intent.putExtra("locationBundle", bundle)
            startActivity(intent)
        }

        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // Check for location permissions
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                locationPermissionCode
            )
        } else {
            // The location is updated every 5000 milliseconds (or 5 seconds) and/or if the device moves more than 5 meters,
            // whichever happens first
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
        }

        buttonOsm.setOnClickListener {
            if (latestLocation != null) {
                val intent = Intent(this, OpenStreetMapActivity::class.java)
                val bundle = Bundle()
                bundle.putParcelable("location", latestLocation)
                intent.putExtra("locationBundle", bundle)
                startActivity(intent)
            }else{
                Log.e(TAG, "Location not set yet.")
            }
        }
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        if (getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("dark_theme", false))
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
                }
            }
        }
    }

    override fun onLocationChanged(location: Location) {
        latestLocation = location
        val textView: TextView = findViewById(R.id.mainTextView)
        textView.text = "Latitude: ${location.latitude}, Longitude: ${location.longitude}"

        isLoading = true
        buttonNext.isEnabled = true
        playAsGuestButton.isEnabled = true
        buttonOsm.isEnabled = true

        loader.visibility = View.GONE
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}
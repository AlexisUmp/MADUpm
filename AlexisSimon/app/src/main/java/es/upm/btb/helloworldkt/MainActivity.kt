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
    private  lateinit var settingsButton: Button
    private  lateinit var buttonNext: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate: The activity is being created.");
        println("Hello world!")

        buttonNext = findViewById(R.id.logInButton)
        playAsGuestButton = findViewById(R.id.playAsGuestButton)
        settingsButton = findViewById(R.id.settingsButton)
        buttonOsm = findViewById(R.id.osmButton)
        loader = findViewById(R.id.loader)

        loader.visibility = View.VISIBLE

        buttonNext.isEnabled = isLoading
        playAsGuestButton.isEnabled = isLoading
        buttonOsm.isEnabled = isLoading
        settingsButton.isEnabled = isLoading

        val root = File(this.filesDir, "Hello_World")
        val file = File(root, "gps_coordinates.csv")
        if (!root.exists() || !file.exists())
        {
            root.mkdir()
            val fw = FileWriter(file)
            fw.append("Test 500 10.2790086756443 22.12451430541 1\n" +
                    "Test2 1000 21.951605144653 125.546852358 0")
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

        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
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
        settingsButton.isEnabled = true

        loader.visibility = View.GONE
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}
}
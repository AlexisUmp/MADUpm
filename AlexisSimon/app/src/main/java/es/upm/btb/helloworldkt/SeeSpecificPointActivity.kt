package es.upm.btb.helloworldkt

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class SeeSpecificPointActivity : AppCompatActivity()  {
    private val TAG = "btaSpecificPointActivity"
    private lateinit var map: MapView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_specific_point)
        Log.d(TAG, "onCreate: The activity is being created.")

        val buttonNext: Button = findViewById(R.id.quit_activity)
        buttonNext.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }
        val text_score : TextView = findViewById(R.id.text_score)
        val text_name : TextView = findViewById(R.id.text_name)
        val check_visited : CheckBox = findViewById(R.id.check_visited)
        check_visited.isClickable = false
        val bundle = intent.getBundleExtra("bundle")
        val latitude = bundle?.getDouble("latitude")
        val longitude = bundle?.getDouble("longitude")
        val name = bundle?.getString("name")
        val score = bundle?.getInt("score")
        val found = bundle?.getBoolean("found")
        if (score != null && found != null && name != null)
        {
            text_score.text = score.toString()
            text_name.text = name
            check_visited.isChecked = found
        }

        if (latitude != null && longitude != null && name != null) {
            Configuration.getInstance().load(applicationContext, getSharedPreferences("osm", MODE_PRIVATE))
            map = findViewById(R.id.map)
            map.setTileSource(TileSourceFactory.MAPNIK)
            map.controller.setZoom(15.0)
            val startPoint = GeoPoint(latitude, longitude)
            map.controller.setCenter(startPoint)
            addMarker(startPoint, name)

            if (getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("dark_theme", false))
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            Log.e(TAG, "Latitude, longitude, or name is null.")
            // Handle the case where latitude, longitude, or name is null.
            // For example, show an error message or finish the activity.
            finish()
        }
        if (getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("dark_theme", false))
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }



    private fun addMarker(point: GeoPoint, title: String) {
        val marker = Marker(map)
        marker.position = point
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = title
        map.overlays.add(marker)
        map.invalidate() // Reload map
    }
    override fun onResume() {
        super.onResume()
        map.onResume()
    }
    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}
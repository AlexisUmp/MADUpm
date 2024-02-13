package es.upm.btb.helloworldkt

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.Polyline

class OpenStreetMapActivity : AppCompatActivity() {
    private val TAG = "btaOpenStreetMapActivity"
    private lateinit var map: MapView
    val gymkhanaCoords = listOf(
        GeoPoint(40.42015608319648, -3.6912447228630003), // James Joyce Irish Pub
        GeoPoint(40.416815496450475, -3.698427970319809), // Glass Bar by SIPS
        GeoPoint(40.41683904048266, -3.701515742953153), // La Fontana de Oro
        GeoPoint(40.41451918184525, -3.699927875238618), // El Imperfecto
        GeoPoint(40.4155647618362, -3.7024598805131466), // The Room Madrid
        GeoPoint(40.41710042799128, -3.706965991594933), // Bar Yambala
        GeoPoint(40.42193591373181, -3.7033611027295037), // Santamaría Coctelería
        GeoPoint(40.422883366836174, -3.6982541768368113) // Twist & Shout Bar
    )
    val gymkhanaNames = listOf(
        "James Joyce Irish Pub",
        "Glass Bar by SIPS",
        "La Fontana de Oro",
        "El Imperfecto",
        "The Room Madrid",
        "Bar Yambala",
        "Santamaría Coctelería",
        "Twist & Shout Bar"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_open_street_map)
        Log.d(TAG, "onCreate: The activity is being created.");
        val bundle = intent.getBundleExtra("locationBundle")
        val location: Location? = bundle?.getParcelable("location")
        if (location != null) {
            Log.i(TAG, "onCreate: Location["+location.altitude+"]["+location.latitude+"]["+location.longitude+"][")
            Configuration.getInstance().load(applicationContext, getSharedPreferences("osm", MODE_PRIVATE))
            map = findViewById(R.id.map)
            map.setTileSource(TileSourceFactory.MAPNIK)
            map.controller.setZoom(15.0)
            val startPoint = GeoPoint(location.latitude, location.longitude)
//val startPoint = GeoPoint(40.416775, -3.703790) in case you want to test it mannualy
            map.controller.setCenter(startPoint)
            addMarker(startPoint, "My current location")
            addMarkersAndRoute(map, gymkhanaCoords, gymkhanaNames)
            //addMarkers(map, gymkhanaCoords, gymkhanaNames)
        };

    }

    private fun addMarkers(mapView: MapView, locationsCoords: List<GeoPoint>, locationsNames: List<String>) {
        for (location in locationsCoords) {
            val marker = Marker(mapView)
            marker.position = location
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            mapView.overlays.add(marker)
            marker.icon = ContextCompat.getDrawable(this, com.google.android.material.R.drawable.ic_m3_chip_checked_circle)
            mapView.overlays.add(marker)
        }
        mapView.invalidate() // Refresh the map to display the new markers
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

    fun addMarkersAndRoute(mapView: MapView, locationsCoords: List<GeoPoint>, locationsNames: List<String>) {
        if (locationsCoords.size != locationsNames.size) {
            Log.e("addMarkersAndRoute", "Locations and names lists must have the same number of items.")
            return
        }
        val route = Polyline()
        route.setPoints(locationsCoords)
        route.color = ContextCompat.getColor(this, R.color.teal_700)
        mapView.overlays.add(route)
        for (location in locationsCoords) {
            val marker = Marker(mapView)
            marker.position = location
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
            val locationIndex = locationsCoords.indexOf(location)
            marker.title = "Marker at ${locationsNames[locationIndex]} ${location.latitude}, ${location.longitude}"
            marker.icon = ContextCompat.getDrawable(this, org.osmdroid.library.R.drawable.ic_menu_compass)
            //marker.icon = ContextCompat.getDrawable(this, org.osmdroid.library.R.drawable.ic_menu_day)
            mapView.overlays.add(marker)
        }
        mapView.invalidate()
    }
}
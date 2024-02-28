package es.upm.btb.helloworldkt

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ThirdActivity : AppCompatActivity() {
    private val TAG = "btaThirdActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third)

        Log.d(TAG, "onCreate: The activity is being created.")

        val buttonPrevious: Button = findViewById(R.id.thirdButton)
        buttonPrevious.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }

        val listView: ListView = findViewById(R.id.lvCoordinates)
        val headerView = layoutInflater.inflate(R.layout.listview_header, listView, false)
        listView.addHeaderView(headerView, null, false)
        val adapter = CoordinatesAdapter(this, readFileContents())
        listView.adapter = adapter


        if (getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("dark_theme", false))
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)

    }

    private class CoordinatesAdapter(context: Context, private val coordinatesList: List<List<String>>) :
        ArrayAdapter<List<String>>(context, R.layout.listview_item, coordinatesList) {
        private val inflater: LayoutInflater = LayoutInflater.from(context)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = convertView ?: inflater.inflate(R.layout.listview_item, parent, false)
            val nameTextView: TextView = view.findViewById(R.id.tvName)
            val scoreTextView: TextView = view.findViewById(R.id.tvScore)
            val latitudeTextView: TextView = view.findViewById(R.id.tvLatitude)
            val longitudeTextView: TextView = view.findViewById(R.id.tvLongitude)
            val visitedCheckBox:CheckBox = view.findViewById(R.id.tvVisited)
            try{
                val item = coordinatesList[position]
                nameTextView.text = item[0]
                scoreTextView.text = item[1].toInt().toString()
                latitudeTextView.text = formatCoordinate(item[2].toDouble())
                longitudeTextView.text = formatCoordinate(item[3].toDouble())
                visitedCheckBox.isChecked = item[4] == "0"
                visitedCheckBox.isClickable = false
                view.setOnClickListener {
                    val intent = Intent(context, SeeSpecificPointActivity::class.java)
                    val bundle = Bundle()
                    bundle.putDouble("latitude", item[2].toDouble())
                    bundle.putDouble("longitude", item[3].toDouble())
                    bundle.putString("name", item[0])
                    intent.putExtra("bundle", bundle)
                    context.startActivity(intent)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("CoordinatesAdapter", "getView: Exception parsing coordinates.");
            }
            return view
        }

        private fun formatCoordinate(value: Double): String {
            return String.format("%.6f", value)
        }
    }
    private fun readFileContents(): List<List<String>> {
        val f = File(this.filesDir, "Hello_World")
        val fileName = File(f, "gps_coordinates.csv")
        return try {
            val fr = FileReader(fileName)
            fr.buffered().useLines {  lines ->
                lines.map { it.split(" ").map(String::trim) }.toList()
            }
        } catch (e: IOException) {
            listOf(listOf("Error reading file: ${e.message}"))
        }
    }
}



class MatrixFileHandler {
    fun readMatrixFromFile(filePath: String): Array<Quintuple<String, Int, Float, Float, Boolean>>? {
        val file = File(filePath)
        if (!file.exists()) {
            println("File $filePath does not exist.")
            return null
        }

        val matrix = mutableListOf<Quintuple<String, Int, Float, Float, Boolean>>()
        for (line in file.readLines()) {
            val elements = line.split(" ")
            if (elements.size != 5) {
                println("Invalid format in file $filePath")
                return null
            }
            val strElement = elements[0]
            val intElement = elements[1].toInt()
            val floatElement1 = elements[2].toFloat()
            val floatElement2 = elements[3].toFloat()
            val booleanElement = elements[4].toBoolean()
            val quintuple = Quintuple(strElement, intElement, floatElement1, floatElement2, booleanElement)
            matrix.add(quintuple)
        }
        return matrix.toTypedArray()
    }

    fun writeMatrixToFile(matrix: Array<Quintuple<String, Int, Float, Float, Boolean>>, filePath: String) {
        val file = File(filePath)
        val writer = file.bufferedWriter()
        for (quintuple in matrix) {
            val line = "${quintuple.first} ${quintuple.second} ${quintuple.third} ${quintuple.fourth} ${quintuple.fifth}"
            writer.write(line)
            writer.newLine()
        }
        writer.close()
    }
}

data class Quintuple<A, B, C, D, E>(val first: A, val second: B, val third: C, val fourth: D, val fifth: E)



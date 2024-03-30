package es.upm.btb.helloworldkt

import android.graphics.*
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.journeyapps.barcodescanner.BarcodeEncoder


class QrCodeActivity : AppCompatActivity() {
    lateinit var iv_qr: ImageView


    @SuppressLint("UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        iv_qr = findViewById(R.id.iv_qr)

        findViewById<Button>(R.id.bt_generate).setOnClickListener {
            generateQR()
        }

        findViewById<Button>(R.id.buttonBackQR).setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        if (getSharedPreferences("settings", Context.MODE_PRIVATE).getBoolean("dark_theme", false))
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun generateQR() {
        val sr : SharedPreferences = getSharedPreferences("label", Context.MODE_PRIVATE)
        val text: String = "Name: ${sr.getString("name", "Guest")}\nScore: ${sr.getInt("score", 0)}"
        val writer = MultiFormatWriter()
        try {
            val matrix: BitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 600, 600)
            val encoder = BarcodeEncoder()
            val bitmap: Bitmap = encoder.createBitmap(matrix)
            iv_qr.setImageBitmap(bitmap)
        } catch (e: WriterException) {
            e.printStackTrace()
        }
    }
}
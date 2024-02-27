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
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

class EditProfileActivity : AppCompatActivity() {

    private val key = "iamakey"
    private fun String.encrypt(password: String): String {
        val secretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
        val iv = ByteArray(16)
        val charArray = password.toCharArray()
        for (i in 0 until charArray.size){
            iv[i] = charArray[i].toByte()
        }
        val ivParameterSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)

        val encryptedValue = cipher.doFinal(this.toByteArray())
        return Base64.encodeToString(encryptedValue, Base64.DEFAULT)
    }
    @SuppressLint("MissingInflatedId", "CommitPrefEdits")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val sr : SharedPreferences = getSharedPreferences("label", Context.MODE_PRIVATE)
        val currentName = sr.getString("name", "User:")
        val currentEmail = sr.getString("email", "Email:")

        val nameEditText = findViewById<EditText>(R.id.editTextName)
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        nameEditText.setText(currentName)
        emailEditText.setText(currentEmail)

        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            val newName = nameEditText.text.toString()
            val newEmail = emailEditText.text.toString()
            val newPassword = passwordEditText.text.toString()
            val editor = sr.edit()
            editor.putString("name", newName)
                .putString("email", newEmail)
                .putString("password", newPassword.encrypt(key))
                .apply()
            val intent = Intent(this, SecondActivity::class.java)
            val bundle = Bundle()
            bundle.putInt("should", 1)
            intent.putExtra("locationBundle", bundle)
            startActivity(intent)
        }
    }
}
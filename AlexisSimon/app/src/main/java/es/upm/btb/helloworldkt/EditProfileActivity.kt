package es.upm.btb.helloworldkt

import android.annotation.SuppressLint
import android.app.Activity
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
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val currentName = intent.getStringExtra("name")
        val currentEmail = intent.getStringExtra("email")

        val nameEditText = findViewById<EditText>(R.id.editTextName)
        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        nameEditText.setText(currentName)
        emailEditText.setText(currentEmail)

        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            if (nameEditText.text.isNullOrBlank()) {
                nameEditText.setText(currentName)
            }
            if (emailEditText.text.isNullOrBlank()) {
                emailEditText.setText(currentEmail)
            }
            if (passwordEditText.text.isNullOrBlank()) {
                passwordEditText.setText(currentName)
            }
            val newName = nameEditText.text.toString()
            val newEmail = emailEditText.text.toString()
            val newPassword = passwordEditText.text.toString()
            val sr: SharedPreferences = getSharedPreferences("label", 0)
            val editor = sr.edit()
            newName.let { editor.putString("name", it) }
            newEmail.let { editor.putString("email", it) }
            newPassword.let {editor.putString("password", it.encrypt(it))}
            editor.apply()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
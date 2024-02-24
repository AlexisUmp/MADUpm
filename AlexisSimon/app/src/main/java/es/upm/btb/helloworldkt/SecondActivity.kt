package es.upm.btb.helloworldkt

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.app.Activity
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class SecondActivity : AppCompatActivity() {
    private val TAG = "btaSecondActivity"


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
    private fun askForUserIdentifier(sr : SharedPreferences) {
        val input = EditText(this)
        val passwordInput = EditText(this)
        AlertDialog.Builder(this)
            .setTitle("Enter User Identifier")
            .setIcon(R.mipmap.ic_launcher)
            .setView(input)
            .setView(input)
            .setPositiveButton("Save") { dialog, which ->
                val userInput = input.text.toString()
                val password = passwordInput.text.toString()
                if (userInput.isNotBlank() && password.isNotBlank() && userInput == sr.getString("name", "") && password.encrypt(password) == sr.getString("password", "")) {
                    Toast.makeText(this, "Successfully registered.", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Wrong Information.", Toast.LENGTH_LONG).show()
                    askForUserIdentifier(sr)
                }
            }
            .setNegativeButton("Register ?") { dialog, which ->
                val intent = Intent(this, ThirdActivity::class.java)
                startActivity(intent)
            }
            .show()
    }


    private fun String.decrypt(password: String): String {
        val secretKeySpec = SecretKeySpec(password.toByteArray(), "AES")
        val iv = ByteArray(16)
        val charArray = password.toCharArray()
        for (i in 0 until charArray.size){
            iv[i] = charArray[i].toByte()
        }
        val ivParameterSpec = IvParameterSpec(iv)

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec)

        val decryptedByteValue = cipher.doFinal(Base64.decode(this, Base64.DEFAULT))
        return String(decryptedByteValue)
    }

    data class User(val name: String?, val email: String?, val score: Int, val password: String)
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        var sr : SharedPreferences = getSharedPreferences("label", 0)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        askForUserIdentifier(sr)
        val user = getUserData(sr)
        findViewById<TextView>(R.id.textViewName).text = user.name
        findViewById<TextView>(R.id.textViewEmail).text = user.email
        findViewById<Button>(R.id.editProfile).setOnClickListener {
            val editIntent = Intent(this, EditProfileActivity::class.java)
            editIntent.putExtra("name", user.name)
            editIntent.putExtra("email", user.email)
            startActivity(editIntent)
        }
        val buttonNext: Button = findViewById(R.id.secondNextButton)
        buttonNext.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }

        val buttonPrevious: Button = findViewById(R.id.secondPreviousButton)
        buttonPrevious.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getUserData(sr : SharedPreferences): User {
        val score = sr.getInt("score", 0)
        val name = sr.getString("name", "User")
        val email = sr.getString("email", "No email provided")
        val password = sr.getString("password", "").toString()
        return if (password != "")
            User(name, email, score, password.decrypt(password))
        else
            User(name, email, score, "")
    }
    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        Log.d(TAG, "onCreate: The activity is being created.");

        val bundle = intent.getBundleExtra("locationBundle")
        val location: Location? = bundle?.getParcelable("location")

        if (location != null) {
            Log.i(TAG, "onCreate: Location["+location.altitude+"]["+location.latitude+"]["+location.longitude+"][")
        };

        val buttonNext: Button = findViewById(R.id.secondNextButton)
        buttonNext.setOnClickListener {
            val intent = Intent(this, ThirdActivity::class.java)
            startActivity(intent)
        }

        val buttonPrevious: Button = findViewById(R.id.secondPreviousButton)
        buttonPrevious.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
    */
}
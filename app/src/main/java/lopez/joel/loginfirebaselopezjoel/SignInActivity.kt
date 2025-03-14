package com.example.app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import lopez.joel.loginfirebaselopezjoel.MainActivity
import lopez.joel.loginfirebaselopezjoel.R

class SignInActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth  // Variable global de autenticación

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        // Inicializar Firebase Auth
        auth = Firebase.auth

        // Referencias a los elementos del layout
        val etEmail = findViewById<EditText>(R.id.etrEmail)
        val etPassword = findViewById<EditText>(R.id.etrPassword)
        val etConfirmPassword = findViewById<EditText>(R.id.etrConfirmPassword)
        val tvError = findViewById<TextView>(R.id.tvrError)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        tvError.visibility= View.INVISIBLE

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                tvError.text = "Todos los campos son obligatorios"
                tvError.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                tvError.text = "Las contraseñas no coinciden"
                tvError.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            signIn(email,password)
            // Registrar usuario con Firebase Authentication

        }
    }

    private fun signIn(email: String, password: String) {
        Log.d("INFO","email: ${email}, password: ${password}")
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d("INFO","singInWithEmail:success")
                    val user= auth.currentUser
                    val intent= Intent(this,MainActivity::class.java)
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    Log.d("ERROR","singInWithEmail:failure")
                    Toast.makeText(baseContext,"El registro fallo.", Toast.LENGTH_SHORT).show()
                }
            }

    }
}

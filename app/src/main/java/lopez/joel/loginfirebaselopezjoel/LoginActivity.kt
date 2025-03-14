package com.example.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import lopez.joel.loginfirebaselopezjoel.MainActivity
import lopez.joel.loginfirebaselopezjoel.R

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth  // Variable global para Firebase Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        // Inicializar Firebase Auth
        auth = Firebase.auth

        // Referencias a los elementos del layout
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val tvError = findViewById<TextView>(R.id.tvError)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val btnRegister = findViewById<Button>(R.id.btnRegistrar)

        tvError.visibility= View.INVISIBLE


        btnLogin.setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                tvError.text = "Por favor, ingrese su correo y contraseña"
                tvError.visibility = TextView.VISIBLE
                return@setOnClickListener
            }

            login(email,password)
        }
        // Redirigir a la pantalla de registro
        btnRegister.setOnClickListener {
            startActivity(Intent(this, SignInActivity::class.java))
        }
    }

    fun login(email:String,password:String){
        // Iniciar sesión con Firebase Authentication
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    showError(visible = false)
                    goToMain(user!!)
                } else {
                    showError("Usuario y/o contraseña equivocadas", true )
                }
            }
    }

    public override fun onStart(){
        super.onStart()
        val currentUser= auth.currentUser
        if (currentUser!=null){
            goToMain(currentUser)
        }
    }


     fun showError(text: String = "", visible:  Boolean) {
        val tvError: TextView = findViewById(R.id.tvError)
        tvError.text=text
        tvError.visibility=if (visible) View.VISIBLE else View.INVISIBLE
    }


     fun goToMain(user: FirebaseUser) {
        val intent= Intent(this, MainActivity::class.java)
        intent.putExtra("user", user.email)
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}

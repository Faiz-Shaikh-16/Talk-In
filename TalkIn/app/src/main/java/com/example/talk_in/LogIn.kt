package com.example.talk_in

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth


class LogIn : AppCompatActivity() {
  private lateinit var edtEmail: EditText
  private lateinit var edtPassword: EditText
  private lateinit var btnLogIn: Button
  private lateinit var btnSignUp: Button
  private lateinit var mAuth: FirebaseAuth

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_log_in)
    mAuth = FirebaseAuth.getInstance()

    edtEmail = findViewById(R.id.edt_email)
    edtPassword = findViewById(R.id.edt_password)
    btnLogIn = findViewById(R.id.btnLogin)
    btnSignUp = findViewById(R.id.btnSignup)

    btnSignUp.setOnClickListener {
      val intent = Intent(this, SignUp::class.java)
      startActivity(intent)
    }
    btnLogIn.setOnClickListener {
      val email = edtEmail.text.toString()
      val password = edtPassword.text.toString()
      if (email.isBlank() || password.isBlank()) {
        Toast.makeText(this, "Please enter details.", Toast.LENGTH_SHORT).show()
      } else
        login(email, password)
    }


  }

  private fun login(email: String, password: String) {
    //login for logging user

    try {
      mAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
          if (task.isSuccessful) {
            val verified = mAuth.currentUser?.isEmailVerified
            if (verified == true){
              val intent = Intent(this@LogIn, MainActivity::class.java)
              finish()
              startActivity(intent)

            } else{
              Toast.makeText(this,"Please verify your email first.",Toast.LENGTH_LONG).show()
            }

            // code for logging



          } else {
            Toast.makeText(
              this@LogIn,
              "User doesn't exist..! Please Sign-Up..",
              Toast.LENGTH_SHORT
            ).show()
          }
        }
    } catch (e: Exception) {
      Log.e("#", e.message.toString())
    }

  }

}
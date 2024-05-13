package com.example.talk_in

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp : AppCompatActivity() {

  private lateinit var edtName: com.google.android.material.textfield.TextInputEditText
  private lateinit var edtEmail: com.google.android.material.textfield.TextInputEditText
  private lateinit var edtPassword: com.google.android.material.textfield.TextInputEditText

  private lateinit var btnSignUp: Button
  private lateinit var mAuth: FirebaseAuth
  private lateinit var mDbRef: DatabaseReference

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_sign_up)

    supportActionBar?.hide()

    mAuth = FirebaseAuth.getInstance()

    edtName = findViewById(R.id.edt_name)
    edtEmail = findViewById(R.id.edt_email)
    edtPassword = findViewById(R.id.edt_password)
    val backbtn = findViewById<ImageView>(R.id.btnBack)
    btnSignUp = findViewById(R.id.btnSignup)



    btnSignUp.setOnClickListener {
      val name = edtName.text.toString()
      val email = edtEmail.text.toString()
      val password = edtPassword.text.toString()
      if (name.isBlank() || email.isBlank() || password.isBlank()) {
        Toast.makeText(this, "Please enter details.", Toast.LENGTH_SHORT).show()
      } else
        signUp(name, email, password)
      backbtn.setOnClickListener{
        val intent = Intent(this@SignUp,EntryActivity::class.java)
        startActivity(intent)
        finish()
      }

    }
  }

  private fun signUp(name: String, email: String, password: String) {
    //logic of creating user
    try {
      mAuth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) { task ->
          if (task.isSuccessful) {
            //for verification of email address


            //code for jumping home activity
            addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
            val intent = Intent(this@SignUp, MainActivity::class.java)
            finish()
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in_from_right, R.anim.slide_out_to_right);

          } else {
            Toast.makeText(this@SignUp, "Please Try Again,Some Error Occurred", Toast.LENGTH_SHORT)
              .show()
          }
        }
    } catch (e: Exception) {
      Log.e("#", e.message.toString())
    }

  }

  private fun addUserToDatabase(name: String, email: String, uid: String) {
    mDbRef = FirebaseDatabase.getInstance().getReference()
    mDbRef.child("user").child(uid).setValue(User(name, email, uid))

  }
}
package com.project.seaprochegue.seaprochegue

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

private const val TAG = "MainActivity"

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btnSignUp.setOnClickListener {
            registrar()
        }

        hasAccount.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        inputProfileImage.setOnClickListener{
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            selectedPhoto.setImageBitmap(bitmap)

            inputProfileImage.alpha = 0f
        }
    }

    private fun registrar(){
        val userName = inputName.text.toString()
        val userPhone = inputPhone.text.toString()
        val userEmail = inputEmail.text.toString()
        val userPassword = inputPassword.text.toString()

        if (userName.isEmpty() || userPhone.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os dados", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmail, userPassword).addOnCompleteListener {
            if (!it.isSuccessful) return@addOnCompleteListener
            uploadImageToDatabase()
        }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }

        Log.d(TAG, "Name is $userName, Phone is $userPhone, Email is $userEmail, Password is $userPassword")

    }

    private fun uploadImageToDatabase() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/userImages/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    saveUserToDatabase(it.toString())
                }
            }
    }

    private fun saveUserToDatabase(profileImageUrl: String){
        val uid = FirebaseAuth.getInstance().uid?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, profileImageUrl, inputName.text.toString(), inputPhone.text.toString())

        ref.setValue(user)
            .addOnSuccessListener {
                val intent = Intent(this, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
    }
}

@Parcelize
class User(val uid: String, val profileImageUrl: String, val name: String, val phone: String) : Parcelable {
    constructor() : this("", "", "", "")
}



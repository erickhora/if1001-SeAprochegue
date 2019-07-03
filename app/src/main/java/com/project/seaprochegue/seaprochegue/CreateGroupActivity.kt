package com.project.seaprochegue.seaprochegue

import android.app.Activity
import android.content.ClipDescription
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.project.seaprochegue.seaprochegue.models.Group
import kotlinx.android.synthetic.main.activity_create_group.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.*

class CreateGroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_group)

        btnOnCreate.setOnClickListener {
            registrarGrupo()
        }

        inputGroupImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedImageUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            selectedImageUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedImageUri)

            selectedImage.setImageBitmap(bitmap)

            inputGroupImage.alpha = 0f
        }
    }

    private fun registrarGrupo(){
        val groupName = inputGroupName.text.toString()
        val groupDescription = inputGroupDescription.text.toString()

        if (groupName.isEmpty() || groupDescription.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os dados", Toast.LENGTH_SHORT).show()
            return
        }

        uploadImageToDatabase()

    }

    private fun uploadImageToDatabase(){
        if(selectedImageUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/groupImages/$filename")

        ref.putFile(selectedImageUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    saveGroupToDatabase(it.toString())
                }
            }
    }

    companion object {
        val GROUP_KEY = "GROUP_KEY"
    }

    private fun saveGroupToDatabase(groupImageUrl: String){
        val uid = UUID.randomUUID().toString()
        val ref = FirebaseDatabase.getInstance().getReference("/groups/$uid")

        val creatorUid = FirebaseAuth.getInstance().uid.toString()

        val group = Group(uid, creatorUid, groupImageUrl, inputGroupName.text.toString(), inputGroupDescription.text.toString(), mutableListOf(creatorUid))

        ref.setValue(group)
            .addOnSuccessListener {
                val intent = Intent(this, GroupActivity::class.java)
                intent.putExtra(GROUP_KEY, group)
                startActivity(intent)
            }
    }
}

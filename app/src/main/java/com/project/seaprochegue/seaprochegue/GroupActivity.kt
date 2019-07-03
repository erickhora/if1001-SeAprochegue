package com.project.seaprochegue.seaprochegue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.project.seaprochegue.seaprochegue.R.*
import com.project.seaprochegue.seaprochegue.models.Group
import com.project.seaprochegue.seaprochegue.models.GroupItemChat
import com.project.seaprochegue.seaprochegue.models.GroupMessage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_group.*

class GroupActivity : AppCompatActivity() {

    val group: Group? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_group)

        fetchUserData()

        fetchMessages()

        val groupHome = intent.getParcelableExtra<Group>(HomeActivity.GROUP_KEY)

        supportActionBar?.title = groupHome.groupName
        groupName.text = groupHome.groupName
        groupDescription.text = groupHome.groupDescription

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        Picasso.get().load(groupHome.groupImageUrl).into(groupImage)



        if(groupHome.creatorUid == uid) {
            btnJoin.visibility = View.GONE

            /*listenForMessages()*/

            btnSend.setOnClickListener {
                performSendMessage()
            }
        } else {
            groupMsg.visibility = View.INVISIBLE
            messageBox.visibility = View.GONE
            btnSend.visibility = View.GONE

            btnJoin.setOnClickListener {
                groupMsg.visibility = View.VISIBLE
                btnJoin.visibility = View.GONE
            }

        }

    }

    var userUri = ""

    private fun fetchUserData(){
        val group = intent.getParcelableExtra<Group>(HomeActivity.GROUP_KEY)
        val uid = group.creatorUid
        val ref = FirebaseDatabase.getInstance().getReference()
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val userProfileImage = p0.child("users").child(uid).child("profileImageUrl").getValue().toString()

                val uri = userProfileImage
                userUri = uri
                Picasso.get().load(uri).into(groupCreator)
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    private fun fetchMessages(){
        val adapter = GroupAdapter<ViewHolder>()

        adapter.add(GroupItemChat())
        adapter.add(GroupItemChat())
        adapter.add(GroupItemChat())
        adapter.add(GroupItemChat())
        adapter.add(GroupItemChat())
        adapter.add(GroupItemChat())
        adapter.add(GroupItemChat())
        adapter.add(GroupItemChat())

        groupMsg.adapter = adapter

//        val ref = FirebaseDatabase.getInstance().getReference("messages")
//        ref.addListenerForSingleValueEvent(object: ValueEventListener {
//            override fun onDataChange(p0: DataSnapshot) {
//                val adapter = GroupAdapter<ViewHolder>()
//
//                val group = intent.getParcelableExtra<Group>(HomeActivity.GROUP_KEY)
//                val creatorId = group.creatorUid
//                val groupId = group.uid
//
//                p0.children.forEach {
//                    val message = it.getValue(GroupMessage::class.java)
//                    val fromId = message?.fromId
//                    if(message != null && fromId == creatorId && message.groupId == groupId) {
//                        adapter.add(GroupItemChat(message.text, userUri))
//                    }
//                }
//
//                groupMessages.adapter = adapter
//            }
//
//            override fun onCancelled(p0: DatabaseError) {
//
//            }
//        })
    }

    /*private fun listenForMessages(){
        val ref = FirebaseDatabase.getInstance().getReference("/messages")
        
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val groupMessage = p0.getValue(GroupMessage::class.java)

                val fromId = groupMessage?.fromId
                val group = intent.getParcelableExtra<Group>(HomeActivity.GROUP_KEY)
                val creatorId = group.creatorUid
                val groupId = group.uid

                if(groupMessage != null && fromId == creatorId && groupMessage.groupId == groupId ) {
                   adapter.add(GroupItemChat(groupMessage.text, userUri))

                    if(groupMessage.fromId == FirebaseAuth.getInstance().uid){
                        adapter.add(GroupItemChat(groupMessage.text, userUri))
                    } else {
                        //DESABILITAR BOTÂO DE ENVIAR MENSAGEM.v+
                    }
                }
            }
            
            override fun onCancelled(p0: DatabaseError) {
                
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                
            }
        })
    }*/

    private fun performSendMessage(){
        val text = messageBox.text.toString()
        if(text == "") return
        val fromId = FirebaseAuth.getInstance().uid
        val group = intent.getParcelableExtra<Group>(HomeActivity.GROUP_KEY)
        val groupId = group.uid

        if(fromId == null) return

        val ref = FirebaseDatabase.getInstance().getReference("/messages").push()

        val groupMessage = GroupMessage(ref.key!!, text, fromId, groupId)
        ref.setValue(groupMessage)
            .addOnSuccessListener {
                Toast.makeText(this, "Mensagem enviada!", Toast.LENGTH_SHORT).show()
            }
    }
}


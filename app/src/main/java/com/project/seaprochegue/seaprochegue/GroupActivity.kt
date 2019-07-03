package com.project.seaprochegue.seaprochegue

import android.content.Intent
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

    val adapter = GroupAdapter<ViewHolder>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_group)

        groupMsg.adapter = adapter

        val groupHome = intent.getParcelableExtra<Group>(HomeActivity.GROUP_KEY)

        val uid = FirebaseAuth.getInstance().currentUser?.uid

        supportActionBar?.title = groupHome.groupName
        groupName.text = groupHome.groupName
        groupDescription.text = groupHome.groupDescription

        Picasso.get().load(groupHome.groupImageUrl).into(groupImage)

        fetchUserData()

        if(groupHome.creatorUid == uid) {
            btnJoin.visibility = View.GONE

            btnSend.setOnClickListener {
                performSendMessage()
            }

            fetchMessages()

        } else {
            groupMsg.visibility = View.INVISIBLE
            messageBox.visibility = View.GONE
            btnSend.visibility = View.GONE
            btnJoin.visibility = View.VISIBLE

            btnJoin.setOnClickListener {
                groupMsg.visibility = View.VISIBLE
                btnJoin.visibility = View.GONE
                listenForMessages()
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

    private fun fetchMessages() {
        val ref = FirebaseDatabase.getInstance().getReference("messages")

        ref.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val groupMessage = p0.getValue(GroupMessage::class.java)

                val fromId = FirebaseAuth.getInstance().uid

                val groupId = intent.getParcelableExtra<Group>(HomeActivity.GROUP_KEY).uid
                val creatorUid = intent.getParcelableExtra<Group>(HomeActivity.GROUP_KEY).creatorUid

                if (groupMessage != null && fromId == creatorUid && groupMessage.groupId == groupId) {
                    adapter.add(GroupItemChat(groupMessage.text, userUri))
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        })
    }

    private fun listenForMessages() {
        val ref = FirebaseDatabase.getInstance().getReference("messages")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    val groupMessage = it.getValue(GroupMessage::class.java)
                    val groupSelected = intent.getParcelableExtra<Group>(HomeActivity.GROUP_KEY).creatorUid

                    if (groupMessage != null && groupMessage.fromId == groupSelected){
                        adapter.add(GroupItemChat(groupMessage.text, userUri))
                    }
                }
                groupMsg.adapter = adapter
            }
        })
    }

    private fun performSendMessage(){
        val fromId = FirebaseAuth.getInstance().uid

        if(fromId == null) return

        val groupId = intent.getParcelableExtra<Group>(HomeActivity.GROUP_KEY).uid

        val text = messageBox.text.toString()
        if(text == "") return

        val ref = FirebaseDatabase.getInstance().getReference("messages").push()

        val groupMessage = GroupMessage(ref.key!!, text, fromId, groupId)

        ref.setValue(groupMessage)
            .addOnSuccessListener {
                Toast.makeText(this, "Mensagem enviada!", Toast.LENGTH_SHORT).show()
                messageBox.text.clear()
                groupMsg.scrollToPosition(adapter.itemCount - 1)
            }
    }
}


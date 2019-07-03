package com.project.seaprochegue.seaprochegue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.project.seaprochegue.seaprochegue.R.*
import com.project.seaprochegue.seaprochegue.models.Group
import com.project.seaprochegue.seaprochegue.models.GroupMessage
import com.project.seaprochegue.seaprochegue.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_group.*
import kotlinx.android.synthetic.main.chat_group_row.view.*

class GroupActivity : AppCompatActivity() {
    
    val adapter = GroupAdapter<ViewHolder>()

    val group: Group? = null
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_group)

        fetchUserData()
        
        groupMessages.adapter = adapter

        val groupHome = intent.getParcelableExtra<Group>(HomeActivity.GROUP_KEY)

        supportActionBar?.title = groupHome.groupName
        groupName.text = groupHome.groupName
        groupDescription.text = groupHome.groupDescription

        Picasso.get().load(groupHome.groupImageUrl).into(groupImage)

        listenForMessages()

        btnSend.setOnClickListener {
            performSendMessage()
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

    private fun listenForMessages(){
        val ref = FirebaseDatabase.getInstance().getReference("/messages")
        
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val groupMessage = p0.getValue(GroupMessage::class.java)

                val fromId = groupMessage?.fromId
                val group = intent.getParcelableExtra<Group>(HomeActivity.GROUP_KEY)
                val creatorId = group.creatorUid
                val groupId = group.uid

                if(groupMessage != null && fromId == creatorId && groupMessage.groupId == groupId ) {
                   adapter.add(GroupItems(groupMessage.text, userUri))

                    if(groupMessage.fromId == FirebaseAuth.getInstance().uid){
                        adapter.add(GroupItems(groupMessage.text, userUri))
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
    }

    private fun performSendMessage(){
        val text = messageBox.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
        val group = intent.getParcelableExtra<Group>(HomeActivity.GROUP_KEY)
        val groupId = group.uid

        if(fromId == null) return

        val ref = FirebaseDatabase.getInstance().getReference("/messages").push()

        val groupMessage = GroupMessage(ref.key!!, text, fromId, groupId)
        ref.setValue(groupMessage)
            .addOnSuccessListener {

            }
    }
}
class GroupItems(val text: String, val userUri: String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.msgSent.text = text

        val target = viewHolder.itemView.msgCreator
        Picasso.get().load(userUri).into(target)
    }

    override fun getLayout(): Int {
        return R.layout.chat_group_row
    }
}

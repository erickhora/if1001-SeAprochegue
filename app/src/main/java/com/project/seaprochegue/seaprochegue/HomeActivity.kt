package com.project.seaprochegue.seaprochegue

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.seaprochegue.seaprochegue.models.Group
import com.project.seaprochegue.seaprochegue.models.GroupItemHome
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        verifyIfUserIsLoggedIn()

        fetchUserData()

        fetchGroups()

        btnOnCreateGroup.setOnClickListener{
            val intent = Intent(this, CreateGroupActivity::class.java)
            startActivity(intent)
        }

        btnOnSearch.setOnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun verifyIfUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid

        if(uid == null){
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    companion object {
        val GROUP_KEY = "GROUP_KEY"
    }

    //TODO("FAZER DISSO ABAIXO UM SERVIÇO")
    private fun fetchUserData(){
        val uid = FirebaseAuth.getInstance().uid.toString()
        val ref = FirebaseDatabase.getInstance().getReference()
        ref.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val path = p0.child("users").child(uid)

                val userName = path.child("name").getValue().toString()
                val userPhone = path.child("phone").getValue().toString()
                val userProfileImage = path.child("profileImageUrl").getValue().toString()

                homeUserName.text = userName
                homeUserPhone.text = userPhone

                val uri = userProfileImage
                Picasso.get().load(uri).into(homeUserImage)

                Log.d("HOME", "username is ${userName}, phone is ${userPhone} and image is ${userProfileImage}")
            }

            override fun onCancelled(p0: DatabaseError) {

            }

        })
        Log.d("HOME", "uid is ${uid}")
    }



    private fun fetchGroups(){
        val ref = FirebaseDatabase.getInstance().getReference("/groups")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    val uid = FirebaseAuth.getInstance().uid
                    val group = it.getValue(Group::class.java)
                    if (group != null && group.creatorUid == uid){
                        adapter.add(GroupItemHome(group))
                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    val groupItem = item as GroupItemHome

                    val intent = Intent(view.context, GroupActivity::class.java)
                    intent.putExtra(GROUP_KEY, groupItem.group)
                    startActivity(intent)

                }

                homeUserGroups.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            /*R.id.profileMenu -> {
                val intent = Intent(this, EditProfileActivity::class.java)
                startActivity(intent)
            }
*/
            R.id.logoutMenu -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navbar, menu)
        return super.onCreateOptionsMenu(menu)
    }
}


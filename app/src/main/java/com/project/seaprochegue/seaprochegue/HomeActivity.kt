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
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.user_row_group.view.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        verifyIfUserIsLoggedIn()

        /*val adapter = GroupAdapter<ViewHolder>()

        homeUserGroups.adapter = adapter*/
        fetchUserData()

        fetchGroups()

        btnOnCreateGroup.setOnClickListener{
            val intent = Intent(this, CreateGroupActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun fetchUserData(){
        val uid = FirebaseAuth.getInstance().uid.toString()
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val imagePath = FirebaseDatabase.getInstance().getReference("users").child(uid).child("profileImageUrl")
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })

    }

    companion object {
        val GROUP_NAME_KEY = "GROUP_NAME_KEY"
        val GROUP_DESCRIPTION_KEY = "GROUP_DESCRIPTION_KEY"
        val GROUP_IMAGE_KEY = "GROUP_IMAGE_KEY"
        val GROUP_CREATOR_KEY = "GROUP_CREATOR_KEY"
    }

    private fun fetchGroups(){
        val ref = FirebaseDatabase.getInstance().getReference("/groups")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    val group = it.getValue(Group::class.java)
                    if (group != null){
                        adapter.add(UserGroups(group))
                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    val groupItem = item as UserGroups

                    val intent = Intent(view.context, GroupActivity::class.java)
                    //Mnadar um objeto com as propriedades para outra tela
                    intent.putExtra(GROUP_NAME_KEY , item.group.groupName)
                    intent.putExtra(GROUP_DESCRIPTION_KEY, item.group.groupDescription)
                    intent.putExtra(GROUP_IMAGE_KEY, item.group.groupImageUrl)
                    intent.putExtra(GROUP_CREATOR_KEY, item.group.creatorUid)
                    startActivity(intent)

                }

                homeUserGroups.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun verifyIfUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid

        if(uid == null){
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.profileMenu -> {
                val intent = Intent(this, EditProfileActivity::class.java)
                startActivity(intent)
            }

            R.id.configMenu -> {
                val intent = Intent(this, ConfigActivity::class.java)
                startActivity(intent)
            }

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

class UserGroups(val group: Group): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.groupRowName.text = group.groupName

        Picasso.get().load(group.groupImageUrl).into(viewHolder.itemView.groupRowImage)
    }

    override fun getLayout(): Int {
       return R.layout.user_row_group
    }
}


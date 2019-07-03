package com.project.seaprochegue.seaprochegue

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.project.seaprochegue.seaprochegue.models.Group
import com.project.seaprochegue.seaprochegue.models.GroupItemHome
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        fetchGroups()
    }

    companion object {
        val GROUP_KEY = "GROUP_KEY"
    }

    private fun fetchGroups(){
        val ref = FirebaseDatabase.getInstance().getReference("/groups")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    val group = it.getValue(Group::class.java)
                    if (group != null){
                        adapter.add(GroupItemHome(group))
                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    val groupItem = item as GroupItemHome

                    val intent = Intent(view.context, GroupActivity::class.java)
                    intent.putExtra(GROUP_KEY, groupItem.group)
                    startActivity(intent)

                }

                viewSearchGroups.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}

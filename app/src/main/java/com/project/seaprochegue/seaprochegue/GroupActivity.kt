package com.project.seaprochegue.seaprochegue

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.project.seaprochegue.seaprochegue.R.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_group.*

class GroupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout.activity_group)

        //PEGA OS EXTRAS
        val groupnamebar = intent.getStringExtra(HomeActivity.GROUP_NAME_KEY)
        groupName.text = intent.getStringExtra(HomeActivity.GROUP_NAME_KEY)
        groupDescription.text = intent.getStringExtra(HomeActivity.GROUP_DESCRIPTION_KEY)

        supportActionBar?.title = groupnamebar

        val adapter = GroupAdapter<ViewHolder>()

        adapter.add(GroupItem())
        adapter.add(GroupItem())
        adapter.add(GroupItem())
        adapter.add(GroupItem())

        groupMessages.adapter = adapter

    }
}
class GroupItem: Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.chat_group_row
    }
}

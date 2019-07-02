package com.project.seaprochegue.seaprochegue.models

import com.project.seaprochegue.seaprochegue.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row_group.view.*

class GroupItem(val group: Group): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.groupRowName.text = group.groupName

        Picasso.get().load(group.groupImageUrl).into(viewHolder.itemView.groupRowImage)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_group
    }
}
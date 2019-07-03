package com.project.seaprochegue.seaprochegue.models

import com.project.seaprochegue.seaprochegue.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.group_chat_row.view.*

class GroupItemChat(val text: String, val userUri: String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.msgSent.text = text

        val target = viewHolder.itemView.msgCreator
        Picasso.get().load(userUri).into(target)
    }

    override fun getLayout(): Int {
        return R.layout.group_chat_row
    }
}
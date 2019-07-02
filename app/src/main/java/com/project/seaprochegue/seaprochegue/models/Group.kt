package com.project.seaprochegue.seaprochegue.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Group(val uid: String, val creatorUid: String, val groupImageUrl: String, val groupName: String, val groupDescription: String) : Parcelable {
    constructor() : this("", "", "", "","")
}
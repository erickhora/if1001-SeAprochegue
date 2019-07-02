package com.project.seaprochegue.seaprochegue.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(val uid: String, val profileImageUrl: String, val name: String, val phone: String) : Parcelable {
    constructor() : this("", "", "", "")
}

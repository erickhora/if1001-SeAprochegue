package com.project.seaprochegue.seaprochegue.models

class GroupMessage(val id: String, val text: String, val fromId: String, val groupId: String) {
    constructor() : this("","","","")
}
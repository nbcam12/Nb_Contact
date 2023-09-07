package com.example.contact_nb12.models

import android.support.v4.os.IResultReceiver.Stub

data class Contact(val Img:Int, val name:String, val phonenumber:String, val email: String, val birth:String, val nickname: String, var isMark: Boolean = false)

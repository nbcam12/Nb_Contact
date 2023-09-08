package com.example.contact_nb12.models

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Contact(
    val Img: Uri?,
    val name: String,
    val phonenumber: String,
    val email: String,
    val birth: String,
    val nickname: String,
    val imageUri: String? = null,
    var isMark: Boolean = false
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Uri::class.java.classLoader),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?:"",
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(Img, flags)
        parcel.writeString(name)
        parcel.writeString(phonenumber)
        parcel.writeString(email)
        parcel.writeString(birth)
        parcel.writeString(nickname)
        parcel.writeString(imageUri)
        parcel.writeByte(if (isMark) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }
}

package com.example.myagenda.models

import android.os.Parcel
import android.os.Parcelable
import android.util.Log

data class Contact(var name: String, var address: String, var phone: Int, var mobile: Int, var email: String): Parcelable {

    constructor(parcel: Parcel) : this(parcel.readString()!!, parcel.readString()!!, parcel.readInt(), parcel.readInt(), parcel.readString()!!) {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeInt(phone)
        parcel.writeInt(mobile)
        parcel.writeString(email)
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
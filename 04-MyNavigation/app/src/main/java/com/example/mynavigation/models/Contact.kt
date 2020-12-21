package com.example.mynavigation.models

import android.os.Parcel
import android.os.Parcelable


data class Contact(var id: Long?, var name: String, var address: String, var phone: String, var mobile: String, var email: String): Parcelable {

    constructor(parcel: Parcel) : this(parcel.readLong(), parcel.readString()!!, parcel.readString()!!, parcel.readString()!!, parcel.readString()!!, parcel.readString()!!) {}

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id!!)
        parcel.writeString(name)
        parcel.writeString(address)
        parcel.writeString(phone)
        parcel.writeString(mobile)
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

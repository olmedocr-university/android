package com.example.myagenda.models

import android.os.Parcel
import android.os.Parcelable

data class Appointment (
        var id: Long?,
        var year: Int,
        var month: Int,
        var day: Int,
        var hour: String,
        var description: String
): Parcelable{
    constructor(parcel: Parcel) : this(
            parcel.readLong(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id!!)
        parcel.writeInt(year)
        parcel.writeInt(month)
        parcel.writeInt(day)
        parcel.writeString(hour)
        parcel.writeString(description)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Appointment> {
        override fun createFromParcel(parcel: Parcel): Appointment {
            return Appointment(parcel)
        }

        override fun newArray(size: Int): Array<Appointment?> {
            return arrayOfNulls(size)
        }
    }
}
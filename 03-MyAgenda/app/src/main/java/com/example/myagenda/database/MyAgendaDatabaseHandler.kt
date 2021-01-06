package com.example.myagenda.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.myagenda.models.Appointment
import com.example.myagenda.models.Contact


class MyAgendaDatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val TAG = "ContactsDatabaseHandler"

    override fun onCreate(db: SQLiteDatabase?) {
        val createContactsTable = """
            CREATE TABLE $CONTACTS_TABLE_NAME (
                $KEY_CONTACT_ID INTEGER PRIMARY KEY,
                $KEY_CONTACT_NAME TEXT,
                $KEY_CONTACT_ADDRESS TEXT,
                $KEY_CONTACT_PHONE INTEGER,
                $KEY_CONTACT_MOBILE INTEGER,
                $KEY_CONTACT_EMAIL TEXT );
        """.trimIndent()

        val createAppointmentsTable = """
            CREATE TABLE $APPOINTMENTS_TABLE_NAME (
                $KEY_APPOINTMENT_ID INTEGER PRIMARY KEY,
                $KEY_APPOINTMENT_YEAR INTEGER,
                $KEY_APPOINTMENT_MONTH INTEGER,
                $KEY_APPOINTMENT_DAY INTEGER,
                $KEY_APPOINTMENT_HOUR TEXT,
                $KEY_APPOINTMENT_DESCRIPTION TEXT );
        """.trimIndent()

        db?.execSQL(createContactsTable)
        db?.execSQL(createAppointmentsTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $CONTACTS_TABLE_NAME")
        db?.execSQL("DROP TABLE IF EXISTS $APPOINTMENTS_TABLE_NAME")
        onCreate(db)
    }

    fun addContact(contact: Contact): Long {
        val values = ContentValues().apply {
            put(KEY_CONTACT_NAME, contact.name)
            put(KEY_CONTACT_ADDRESS, contact.address)
            put(KEY_CONTACT_PHONE, contact.phone)
            put(KEY_CONTACT_MOBILE, contact.mobile)
            put(KEY_CONTACT_EMAIL, contact.email)
        }
        val db = this.writableDatabase
        val newRowId = db.insert(CONTACTS_TABLE_NAME, null, values)
        Log.d(TAG, "addContact: added new contact with id $newRowId")
        return newRowId
    }

    fun addAppointment(appointment: Appointment): Long {
        val values = ContentValues().apply {
            put(KEY_APPOINTMENT_ID, appointment.id)
            put(KEY_APPOINTMENT_YEAR, appointment.year)
            put(KEY_APPOINTMENT_MONTH, appointment.month)
            put(KEY_APPOINTMENT_DAY, appointment.day)
            put(KEY_APPOINTMENT_HOUR, appointment.hour)
            put(KEY_APPOINTMENT_DESCRIPTION, appointment.description)
        }
        val db = this.writableDatabase
        val newRowId = db.insert(APPOINTMENTS_TABLE_NAME, null, values)
        Log.d(TAG, "addAppointment: added new appointment with id $newRowId")
        return newRowId
    }

    fun updateContact(contact: Contact) {
        val values = ContentValues().apply {
            put(KEY_CONTACT_NAME, contact.name)
            put(KEY_CONTACT_ADDRESS, contact.address)
            put(KEY_CONTACT_PHONE, contact.phone)
            put(KEY_CONTACT_MOBILE, contact.mobile)
            put(KEY_CONTACT_EMAIL, contact.email)
        }
        val db = this.writableDatabase
        db.update(CONTACTS_TABLE_NAME, values, "$KEY_CONTACT_ID = ?", Array(1) { contact.id.toString() })
        Log.d(TAG, "updateContact: updated contact with id $contact.id")
    }

    fun updateAppointment(appointment: Appointment) {
        val values = ContentValues().apply {
            put(KEY_APPOINTMENT_YEAR, appointment.year)
            put(KEY_APPOINTMENT_MONTH, appointment.month)
            put(KEY_APPOINTMENT_DAY, appointment.day)
            put(KEY_APPOINTMENT_HOUR, appointment.hour)
            put(KEY_APPOINTMENT_DESCRIPTION, appointment.description)
        }
        val db = this.writableDatabase
        db.update(APPOINTMENTS_TABLE_NAME, values, "$KEY_APPOINTMENT_ID = ?", Array(1) { appointment.id.toString() })
        Log.d(TAG, "updateAppointment: updated appointment with id $appointment.id")
    }

    fun deleteContact(contactId: Long) {
        val db = this.writableDatabase
        db.delete(CONTACTS_TABLE_NAME, "$KEY_CONTACT_ID = ?", Array(1) { contactId.toString() })
    }

    fun deleteAppointment(appointmentId: Long) {
        val db = this.writableDatabase
        db.delete(APPOINTMENTS_TABLE_NAME, "$KEY_APPOINTMENT_ID = ?", Array(1) { appointmentId.toString() })
    }

    fun getAllContacts(): ArrayList<Contact> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $CONTACTS_TABLE_NAME", null)
        return getContactsFromCursor(cursor)
    }

    fun getAllAppointments(): ArrayList<Appointment> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $APPOINTMENTS_TABLE_NAME", null)
        return getAppointmentsFromCursor(cursor)
    }

    fun getDayAppointments(year: Int, month: Int, day: Int): ArrayList<Appointment> {
        val db = this.readableDatabase
        val cursor = db.rawQuery(
                "SELECT * FROM $APPOINTMENTS_TABLE_NAME WHERE year=? AND month=? AND day=?",
                arrayOf(year.toString(), month.toString(), day.toString()))
        return getAppointmentsFromCursor(cursor)
    }

    fun dropAllContacts() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $CONTACTS_TABLE_NAME")
        onCreate(db)
    }

    fun dropAllAppointments() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $APPOINTMENTS_TABLE_NAME")
        onCreate(db)
    }

    private fun getContactsFromCursor(cursor: Cursor): ArrayList<Contact> {
        val contacts: ArrayList<Contact> = ArrayList()

        if (!cursor.moveToFirst()) {
            return contacts
        }

        val idIndex = cursor.getColumnIndex(KEY_CONTACT_ID)
        val nameIndex = cursor.getColumnIndex(KEY_CONTACT_NAME)
        val addressIndex = cursor.getColumnIndex(KEY_CONTACT_ADDRESS)
        val phoneIndex = cursor.getColumnIndex(KEY_CONTACT_PHONE)
        val mobileIndex = cursor.getColumnIndex(KEY_CONTACT_MOBILE)
        val emailIndex = cursor.getColumnIndex(KEY_CONTACT_EMAIL)

        do {
            contacts.add(Contact(
                    cursor.getLong(idIndex),
                    cursor.getString(nameIndex),
                    cursor.getString(addressIndex),
                    cursor.getString(phoneIndex),
                    cursor.getString(mobileIndex),
                    cursor.getString(emailIndex)
            ))
        } while (cursor.moveToNext())

        cursor.close()

        return contacts
    }

    private fun getAppointmentsFromCursor(cursor: Cursor): ArrayList<Appointment> {
        val appointments: ArrayList<Appointment> = ArrayList()

        if (!cursor.moveToFirst()) {
            return appointments
        }

        val idIndex = cursor.getColumnIndex(KEY_APPOINTMENT_ID)
        val yearIndex = cursor.getColumnIndex(KEY_APPOINTMENT_YEAR)
        val monthIndex = cursor.getColumnIndex(KEY_APPOINTMENT_MONTH)
        val dayIndex = cursor.getColumnIndex(KEY_APPOINTMENT_DAY)
        val hourIndex = cursor.getColumnIndex(KEY_APPOINTMENT_HOUR)
        val descriptionIndex = cursor.getColumnIndex(KEY_APPOINTMENT_DESCRIPTION)

        do {
            appointments.add(Appointment(
                    cursor.getLong(idIndex),
                    cursor.getInt(yearIndex),
                    cursor.getInt(monthIndex),
                    cursor.getInt(dayIndex),
                    cursor.getString(hourIndex),
                    cursor.getString(descriptionIndex)
            ))
        } while (cursor.moveToNext())

        cursor.close()

        return appointments
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "myagenda.db"

        const val CONTACTS_TABLE_NAME = "contacts"
        const val KEY_CONTACT_ID = "id"
        const val KEY_CONTACT_NAME = "name"
        const val KEY_CONTACT_ADDRESS = "address"
        const val KEY_CONTACT_PHONE = "phone"
        const val KEY_CONTACT_MOBILE = "mobile"
        const val KEY_CONTACT_EMAIL = "email"

        const val APPOINTMENTS_TABLE_NAME = "appointments"
        const val KEY_APPOINTMENT_ID = "id"
        const val KEY_APPOINTMENT_DAY = "day"
        const val KEY_APPOINTMENT_MONTH = "month"
        const val KEY_APPOINTMENT_YEAR = "year"
        const val KEY_APPOINTMENT_HOUR = "hour"
        const val KEY_APPOINTMENT_DESCRIPTION = "description"


    }

}
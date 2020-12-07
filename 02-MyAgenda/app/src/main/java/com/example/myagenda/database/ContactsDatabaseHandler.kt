package com.example.myagenda.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.myagenda.models.Contact

class ContactsDatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    private val TAG = "ContactsDatabaseHandler"

    override fun onCreate(db: SQLiteDatabase?) {
        val createContactsTable = """
            CREATE TABLE $TABLE_NAME (
                $KEY_CONTACT_ID INTEGER PRIMARY KEY,
                $KEY_CONTACT_NAME TEXT,
                $KEY_CONTACT_ADDRESS TEXT,
                $KEY_CONTACT_PHONE INTEGER,
                $KEY_CONTACT_MOBILE INTEGER,
                $KEY_CONTACT_EMAIL TEXT );
        """.trimIndent()

        db?.execSQL(createContactsTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
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
        val newRowId = db.insert(TABLE_NAME, null, values)
        Log.d(TAG, "addContact: added new contact with id $newRowId")
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
        db.update(TABLE_NAME, values, "$KEY_CONTACT_ID = ?", Array(1){contact.id.toString()})
        Log.d(TAG, "updateContact: updated contact with id $contact.id")
    }

    fun deleteContact(contactId: Long) {
        val db = this.writableDatabase
        db.delete(TABLE_NAME, "$KEY_CONTACT_ID = ?", Array(1){contactId.toString()})
    }


    fun getAllContacts(): ArrayList<Contact> {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        return getDataFromCursor(cursor)
    }

    fun dropAllContacts() {
        val db = this.writableDatabase
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    private fun getDataFromCursor(cursor: Cursor): ArrayList<Contact> {
        var contacts: ArrayList<Contact> = ArrayList()

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


    companion object {
        private const val DATABASE_VERSION = 13
        private const val DATABASE_NAME = "contacts.db"
        const val TABLE_NAME = "contacts"
        const val KEY_CONTACT_ID = "id"
        const val KEY_CONTACT_NAME = "name"
        const val KEY_CONTACT_ADDRESS = "address"
        const val KEY_CONTACT_PHONE = "phone"
        const val KEY_CONTACT_MOBILE = "mobile"
        const val KEY_CONTACT_EMAIL = "email"
    }

}
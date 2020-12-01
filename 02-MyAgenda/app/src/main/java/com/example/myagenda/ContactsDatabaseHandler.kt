package com.example.myagenda

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.myagenda.ContactsDbSchema.ContactsTable.Columns.KEY_CONTACT_ADDRESS
import com.example.myagenda.ContactsDbSchema.ContactsTable.Columns.KEY_CONTACT_EMAIL
import com.example.myagenda.ContactsDbSchema.ContactsTable.Columns.KEY_CONTACT_ID
import com.example.myagenda.ContactsDbSchema.ContactsTable.Columns.KEY_CONTACT_MOBILE
import com.example.myagenda.ContactsDbSchema.ContactsTable.Columns.KEY_CONTACT_NAME
import com.example.myagenda.ContactsDbSchema.ContactsTable.Columns.KEY_CONTACT_PHONE
import com.example.myagenda.ContactsDbSchema.ContactsTable.TABLE_NAME
import com.example.myagenda.ContactsDbSchema.DATABASE_NAME
import com.example.myagenda.ContactsDbSchema.DATABASE_VERSION

class ContactsDatabaseHandler(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
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

}
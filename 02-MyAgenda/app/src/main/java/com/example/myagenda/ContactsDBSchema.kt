package com.example.myagenda

object ContactsDbSchema {

    const val DATABASE_VERSION = 1
    const val DATABASE_NAME = "contacts.db"

    object ContactsTable {
        const val TABLE_NAME = "contacts"
        object Columns {
            const val KEY_CONTACT_ID = "id"
            const val KEY_CONTACT_NAME = "name"
            const val KEY_CONTACT_ADDRESS = "address"
            const val KEY_CONTACT_PHONE = "phone"
            const val KEY_CONTACT_MOBILE = "mobile"
            const val KEY_CONTACT_EMAIL = "email"

        }
    }
}
package com.soulbrain.expense_tracker.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.net.Uri
import android.provider.BaseColumns

class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(AccountContract.SQL_CREATE_ENTRIES)
    }

    // TODO - redesign this function
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(AccountContract.SQL_DELETE_ENTRIES)
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "Account.db"
        const val AUTHORITY = "com.soulbrain.expense_tracker"
        const val ACCOUNT = "account"
        val BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
        val EXP_TRACKER_DB_LOC = Uri.withAppendedPath(BASE_CONTENT_URI, ACCOUNT);
    }

    object AccountContract {
        object AccountEntry : BaseColumns {

            // TODO date and category
            const val TABLE_NAME = "account"
            const val COLUMN_NAME_DATE = "date"
            const val COLUMN_NAME_DESC = "description"
            const val COLUMN_NAME_AMOUNT = "amount"
        }

        const val SQL_CREATE_ENTRIES =
                "CREATE TABLE ${AccountEntry.TABLE_NAME} (" +
                        "${BaseColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "${AccountEntry.COLUMN_NAME_DATE} DATETIME," +
                        "${AccountEntry.COLUMN_NAME_DESC} TEXT," +
                        "${AccountEntry.COLUMN_NAME_AMOUNT} FLOAT);"

        const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${AccountEntry.TABLE_NAME}"

    }
}



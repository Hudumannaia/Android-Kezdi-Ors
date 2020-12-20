package android.kezdi.ors.Repository

import android.content.ContentValues
import android.database.sqlite.*
import android.content.Context
import android.kezdi.ors.Models.Favourite
import android.kezdi.ors.Models.User
import android.util.Log


class DBHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VER) {
    companion object{
        private val DATABASE_NAME = "local.db"
        private val DATABASE_VER = 1

        private val USER_TABLE = "users"
        private val USER_ID = "id"
        private val USER_PIC = "photo"
        private val USER_NAME = "name"
        private val USER_EMAIL = "email"
        private val USER_ADDRESS = "address"
        private val USER_PHONE = "phone"

        private val FAV_TABLE = "favourites"
        private val FAV_ID = "id"
        private val FAV_NAME = "name"
        private val FAV_ADDRESS = "address"
        private val FAV_PRICE = "price"
        private val FAV_IMGURL = "imgurl"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_USER_TABLE = ("CREATE TABLE $USER_TABLE (" +
                "$USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$USER_PIC TEXT, " +
                "$USER_NAME TEXT, " +
                "$USER_EMAIL TEXT, " +
                "$USER_ADDRESS TEXT, " +
                "$USER_PHONE TEXT)")
        val CREATE_FAVOURITES_TABLE = ("CREATE TABLE $FAV_TABLE (" +
                "$FAV_ID  INTEGER PRIMARY KEY, " +
                "$FAV_NAME TEXT, " +
                "$FAV_ADDRESS  TEXT, " +
                "$FAV_PRICE TEXT, " +
                "$FAV_IMGURL TEXT)")

        db?.execSQL(CREATE_USER_TABLE)
        db?.execSQL(CREATE_FAVOURITES_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $USER_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $FAV_TABLE")
        this.onCreate(db)
    }


    val allFavourites:MutableList<Favourite>
        get() {
            val list = mutableListOf<Favourite>()
            val selectQuery = "SELECT * FROM $FAV_TABLE"
            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)
            if (cursor.moveToFirst()){
                do {
                    val restaurant = Favourite()
                    restaurant.id = cursor.getInt(cursor.getColumnIndex(FAV_ID))
                    restaurant.name = cursor.getString(cursor.getColumnIndex(FAV_NAME))
                    restaurant.address = cursor.getString(cursor.getColumnIndex(FAV_ADDRESS))
                    restaurant.price = cursor.getInt(cursor.getColumnIndex(FAV_PRICE))
                    restaurant.image_url = cursor.getString(cursor.getColumnIndex(FAV_IMGURL))

                    list.add(restaurant)
                } while (cursor.moveToNext())
            }
            db.close()
            return list
        }

    fun addFavourite( restaurant: Favourite){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put( FAV_ID, restaurant.id)
        values.put( FAV_NAME, restaurant.name)
        values.put( FAV_ADDRESS, restaurant.address)
        values.put( FAV_PRICE, restaurant.price)
        values.put( FAV_IMGURL, restaurant.image_url)

        db.insert(FAV_TABLE, null, values)
        db.close()
    }

    fun deleteFavourite( id: Int){
        val db = this.writableDatabase
        db.delete(FAV_TABLE, "$FAV_ID=?", arrayOf(id.toString()))
        db.close()
    }

    fun isFavourite(id: Int): Boolean {
        val db = this.writableDatabase
        val cursor = db.rawQuery("SELECT * FROM $FAV_TABLE WHERE $FAV_ID=$id", null)

        val res = cursor.count == 1
        db.close()
        return res
    }

    fun getUser(id: Int): User {
        val user = User(id=id)
        val selectQuery = "SELECT * FROM $USER_TABLE WHERE $USER_ID=$id"
        val db = this.writableDatabase
        val cursor = db.rawQuery(selectQuery, null)
        Log.d("database","GET USER ${cursor.columnCount} ${cursor.count}")
        if (cursor.moveToFirst()){
            user.id = cursor.getInt(cursor.getColumnIndex(USER_ID))
            user.photoPath = cursor.getString(cursor.getColumnIndex(USER_PIC))
            user.name = cursor.getString(cursor.getColumnIndex(USER_NAME))
            user.email =cursor.getString(cursor.getColumnIndex(USER_EMAIL))
            user.address = cursor.getString(cursor.getColumnIndex(USER_ADDRESS))
            user.phoneNumber = cursor.getString(cursor.getColumnIndex(USER_PHONE))
        }
        db.close()
        return user
    }

    fun updateUser(user: User) {
        Log.d("database","UPDATE USER: "+user.toString())
        val db = this.writableDatabase
        val values = ContentValues()
        values.put( USER_ID, user.id)
        values.put( USER_PIC, user.photoPath)
        values.put( USER_NAME, user.name )
        values.put( USER_EMAIL, user.email)
        values.put( USER_ADDRESS, user.address)
        values.put( USER_PHONE, user.phoneNumber)

        val cursor = db.rawQuery("SELECT * FROM $USER_TABLE WHERE $USER_ID=${user.id}", null)
        when (cursor.count) {
            0 -> db.insert(USER_TABLE, null, values)
            1 -> db.update(USER_TABLE, values, "$USER_ID=?", arrayOf(user.id.toString()))
            else -> { } //ERROR
        }
        db.close()
    }




}
package com.example.stormguard


import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stormguard.utiles.DataBaseHelper


class MainActivity : AppCompatActivity() {
    private var dbHelper: DataBaseHelper? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DataBaseHelper(this);
        val db = dbHelper?.writableDatabase



    }

}

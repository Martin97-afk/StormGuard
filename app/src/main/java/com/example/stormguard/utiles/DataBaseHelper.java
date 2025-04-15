package com.example.stormguard.utiles;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mi_base_de_datos";
    private static final String TABLE_UBICACION = "t_ubicacion";
    private static final String TABLE_CONDICIONES_ACTUALES = "t_condiciones_actuales";
    private static final String TABLE_DATOS_HISTORICOS = "t_datos_historicos";


    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_UBICACION + "(" +
                "idUbicacion INTEGER PRIMARY KEY," +
                "nombreCiudad TEXT," +
                "latitud REAL," +
                "longitud REAL," +
                "codigoPostal TEXT" + ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CONDICIONES_ACTUALES + "(" +
                "id INTEGER PRIMARY KEY," +
                "temperatura REAL," +
                "humedad INTEGER," +
                "velocidadViento REAL," +
                "direccionViento REAL," +
                "presionAtmosferica REAL," +
                "visibilidad REAL," +
                "indiceUV REAL" + ")");

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_DATOS_HISTORICOS + "(" +
                "id INTEGER PRIMARY KEY," +
                "fecha TEXT," +
                "temperatura REAL," +
                "humedad INTEGER," +
                "velocidadViento REAL," +
                "direccionViento REAL," +
                "presionAtmosferica REAL," +
                "visibilidad REAL," +
                "indiceUV REAL" + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_UBICACION);
        onCreate(sqLiteDatabase);

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_CONDICIONES_ACTUALES);
        onCreate(sqLiteDatabase);

        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_DATOS_HISTORICOS);
        onCreate(sqLiteDatabase);

    }
}
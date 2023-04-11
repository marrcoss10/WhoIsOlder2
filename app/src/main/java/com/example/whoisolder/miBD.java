package com.example.whoisolder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class miBD extends SQLiteOpenHelper {
    public miBD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version){
        super(context,name,factory,version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        //Creamos la tabla de famosos
        sqLiteDatabase.execSQL("CREATE TABLE Famosos('Codigo' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'Nombre' VARCHAR(30), 'Edad' INTEGER)");
        //Borramos la bd de famosos
        sqLiteDatabase.execSQL("DELETE FROM Famosos");
        //Rellenamos la bd de famosos
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Messi',35)");
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Cristiano',38)");
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Pen.Cruz',48)");
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Ant.Banderas',62)");
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Jen.Lopez',53)");
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Bel.Rueda',57)");
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Cr.Pedroche',34)");
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Pablo Motos',57)");
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Alej.Sanz',54)");
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Coronado',65)");
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Sanchez',51)");
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Rajoy',67)");
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Broncano',38)");
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Risto',48)");
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Bustamante',40)");
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Chenoa',47)");
        sqLiteDatabase.execSQL("INSERT INTO Famosos ('Nombre','Edad') VALUES ('Beckham',47)");
        //Creamos la tabla de puntuaciones
        sqLiteDatabase.execSQL("CREATE TABLE Puntuaciones('Codigo' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, 'Nombre' VARCHAR(30), 'Puntos' INTEGER)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        //No hay operaciones
    }
}

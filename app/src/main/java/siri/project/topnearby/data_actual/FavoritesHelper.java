package siri.project.topnearby.data_actual;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoritesHelper extends SQLiteOpenHelper {

    static String DATABASE_NAME = "favorite_place.db";
    static int DATABASE_VERSION = 1;

    public FavoritesHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            final String SQL_CREATE_TABLE = "CREATE TABLE " +
                    FavoritesContract.FavoritesEntry.TABLE_FAVPLACES + "(" +
                    FavoritesContract.FavoritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    FavoritesContract.FavoritesEntry.COLUMN_PLACEID + " TEXT UNIQUE NOT NULL , " +
                    FavoritesContract.FavoritesEntry.COLUMN_PLACENAME + " TEXT NOT NULL, " +
                    FavoritesContract.FavoritesEntry.COLUMN_PLACERATING + " TEXT NOT NULL, " +
                    FavoritesContract.FavoritesEntry.COLUMN_PLACEPHONE + " TEXT NOT NULL, " +
                    FavoritesContract.FavoritesEntry.COLUMN_PLACEADDRESS + " TEXT NOT NULL, " +
                    FavoritesContract.FavoritesEntry.COLUMN_PLACEWEBSITE + " TEXT NOT NULL, " +
                    FavoritesContract.FavoritesEntry.COLUMN_PLACEPHOTO_REFERENCE + ", " +
                    FavoritesContract.FavoritesEntry.COLUMN_OPENINGHOURS_WEEKDAYS + ", " +
                    FavoritesContract.FavoritesEntry.COLUMN_PHOTO_MAXHEIGHT + ", " +
                    FavoritesContract.FavoritesEntry.COLUMN_PLACECITY+ " TEXT NOT NULL " + ");";
            db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoritesEntry.TABLE_FAVPLACES);
        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                FavoritesContract.FavoritesEntry.TABLE_FAVPLACES + "'");

        // re-create database
        onCreate(db);
    }
}

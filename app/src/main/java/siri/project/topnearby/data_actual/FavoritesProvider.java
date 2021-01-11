package siri.project.topnearby.data_actual;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class FavoritesProvider extends ContentProvider {

    public static UriMatcher favUriMatcher = buildUriMatcher();
    FavoritesHelper favoritesHelper;

    public static final int FAVORITE_PLACE = 100;
    public static final int FAVORITE_PLACE_WITH_ID = 200;

    public static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoritesContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavoritesContract.FavoritesEntry.TABLE_FAVPLACES, FAVORITE_PLACE);
        matcher.addURI(authority, FavoritesContract.FavoritesEntry.TABLE_FAVPLACES+"/#",FAVORITE_PLACE_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {

        favoritesHelper = new FavoritesHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor returnCursor;
        switch (favUriMatcher.match(uri))
        {
            case FAVORITE_PLACE:{
                returnCursor = favoritesHelper.getReadableDatabase().query(FavoritesContract.FavoritesEntry.TABLE_FAVPLACES,
                        projection, selection, selectionArgs, null, null, sortOrder);
                return  returnCursor;
            }

            case FAVORITE_PLACE_WITH_ID:{
                returnCursor = favoritesHelper.getReadableDatabase().query(FavoritesContract.FavoritesEntry.TABLE_FAVPLACES,
                        projection, FavoritesContract.FavoritesEntry._ID + "=?", new String[] {String.valueOf(ContentUris.parseId(uri))},
                        null, null, sortOrder);
                return  returnCursor;
            }

            default:
                throw  new UnsupportedOperationException("Unknown Uri "+uri);
        }

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = favUriMatcher.match(uri);

        switch (match){
            case FAVORITE_PLACE:{
                return FavoritesContract.FavoritesEntry.CONTENT_DIR_TYPE;
            }
            case FAVORITE_PLACE_WITH_ID:{
                return FavoritesContract.FavoritesEntry.CONTENT_ITEM_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase db = favoritesHelper.getWritableDatabase();
        Uri returnUri;
        switch (favUriMatcher.match(uri)) {
            case FAVORITE_PLACE: {
                long _id = db.insert(FavoritesContract.FavoritesEntry.TABLE_FAVPLACES, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    //returnUri = MoviesContract.MovieEntry.buildMoviesUri(_id);
                    returnUri = ContentUris.withAppendedId(FavoritesContract.FavoritesEntry.CONTENT_URI,_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = favoritesHelper.getWritableDatabase();
        final int match = favUriMatcher.match(uri);
        int numDeleted;
        switch(match){
            case FAVORITE_PLACE:
                numDeleted = db.delete(
                        FavoritesContract.FavoritesEntry.TABLE_FAVPLACES, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        FavoritesContract.FavoritesEntry.TABLE_FAVPLACES + "'");
                break;
            case FAVORITE_PLACE_WITH_ID:
                numDeleted = db.delete(FavoritesContract.FavoritesEntry.TABLE_FAVPLACES,
                        FavoritesContract.FavoritesEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        FavoritesContract.FavoritesEntry.TABLE_FAVPLACES + "'");

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        return numDeleted;

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}

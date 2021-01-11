package siri.project.topnearby.data_actual;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class FavoritesContract {

    public static final String CONTENT_AUTHORITY = "siri.project.topnearby";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final class FavoritesEntry implements BaseColumns {

        public static final String TABLE_FAVPLACES = "fav_place";

        public static final String _ID = "_id";
        public static final String COLUMN_PLACEID = "placeId";
        public static final String COLUMN_PLACENAME = "placeName";
        public static final String COLUMN_PLACERATING = "placeRating";
        public static final String COLUMN_PLACEPHONE = "placePhone";
        public static final String COLUMN_PLACEADDRESS = "placeAddress";
        public static final String COLUMN_PLACEWEBSITE = "placeWebsite";
        public static final String COLUMN_PLACEPHOTO_REFERENCE = "placePhotoReference";
        public static final String COLUMN_OPENINGHOURS_WEEKDAYS = "placeOpeningHoursWeekdays";
        public static final String COLUMN_PHOTO_MAXHEIGHT = "photoMaxHeight";
        public static final String COLUMN_PLACECITY = "placeCity";



        public static Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(TABLE_FAVPLACES).build();

        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_FAVPLACES;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_FAVPLACES;

        // for building URIs on insertion
        public static Uri buildFavoritesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }



    }
}

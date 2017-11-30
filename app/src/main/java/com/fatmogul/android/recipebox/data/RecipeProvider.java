package com.fatmogul.android.recipebox.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by adam on 11/21/17.
 */

public class RecipeProvider extends ContentProvider {

    public static final int CODE_RECIPE = 100;
    public static final int CODE_RECIPE_SPECIFIC = 101;
    public static final int CODE_INGREDIENT = 200;
    public static final int CODE_INGREDIENT_SPECIFIC = 201;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private RecipeDbHelper mOpenHelper;


    public static UriMatcher buildUriMatcher() {


        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = RecipeContract.CONTENT_AUTHORITY;


        matcher.addURI(authority, RecipeContract.PATH_RECIPES, CODE_RECIPE);

        matcher.addURI(authority, RecipeContract.PATH_INGREDIENTS, CODE_INGREDIENT);

        matcher.addURI(authority, RecipeContract.PATH_RECIPES + "/#", CODE_RECIPE_SPECIFIC);

        matcher.addURI(authority,RecipeContract.PATH_INGREDIENTS + "/#", CODE_INGREDIENT_SPECIFIC);

        return matcher;
    }


    @Override
    public boolean onCreate() {

        mOpenHelper = new RecipeDbHelper(getContext());
        return true;
    }


    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {
throw new  RuntimeException("Boo!");
                //return super.bulkInsert(uri, values);

    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;


        switch (sUriMatcher.match(uri)) {

            case CODE_RECIPE_SPECIFIC: {
                String id = uri.getLastPathSegment();
                String[] selectionArguments = new String []{id};
                cursor = mOpenHelper.getReadableDatabase().query(
                        /* Table we are going to query */
                        RecipeContract.RecipeEntry.RECIPE_TABLE_NAME,

                        projection,

                        RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        sortOrder);

                break;
            }

            case CODE_RECIPE: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        RecipeContract.RecipeEntry.RECIPE_TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }


    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {

    int numRowsDeleted;


        if (null == selection) selection = "1";

        switch (sUriMatcher.match(uri)) {

            case CODE_RECIPE:
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        RecipeContract.RecipeEntry.RECIPE_TABLE_NAME,
                        selection,
                        selectionArgs);

                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not implemented.");
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {

        switch (sUriMatcher.match(uri)) {

            case CODE_RECIPE:

                int rowsInserted = 0;
                long id = mOpenHelper.getWritableDatabase().insert(RecipeContract.RecipeEntry.RECIPE_TABLE_NAME, null, values);
                if (id != -1) {
                    rowsInserted++;
                }

                if (rowsInserted > 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }

                return ContentUris.withAppendedId(uri,id);
        }
return null;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("Not implemented, needs to be");
    }
@Override
    @TargetApi(11)
    public void shutdown(){
        mOpenHelper.close();
        super.shutdown();
}

}

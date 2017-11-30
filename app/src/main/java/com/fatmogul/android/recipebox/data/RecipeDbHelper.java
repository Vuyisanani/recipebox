package com.fatmogul.android.recipebox.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by adam on 11/20/17.
 */

public class RecipeDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "kitchen.db";

    public static final int DATABASE_VERSION = 2;

    public RecipeDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_RECIPE_TABLE =
                "CREATE TABLE " + RecipeContract.RecipeEntry.RECIPE_TABLE_NAME + " (" +
                        RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecipeContract.RecipeEntry.COLUMN_NAME + " TEXT, " +
                        RecipeContract.RecipeEntry.COLUMN_IMAGE + " TEXT, " +
                        RecipeContract.RecipeEntry.COLUMN_CATEGORY + " TEXT, " +
                        RecipeContract.RecipeEntry.COLUMN_COOK_TIME + " TEXT, " +
                        RecipeContract.RecipeEntry.COLUMN_PREP_TIME + " TEXT, " +
                        RecipeContract.RecipeEntry.COLUMN_TOTAL_TIME + " TEXT, " +
                        RecipeContract.RecipeEntry.COLUMN_DIRECTIONS + " TEXT, " +
                        RecipeContract.RecipeEntry.COLUMN_SERVES + " INTEGER);";

        final String SQL_CREATE_INGREDIENT_TABLE =
                "CREATE TABLE " + RecipeContract.RecipeEntry.INGREDIENTS_TABLE_NAME + " (" +
                        RecipeContract.RecipeEntry.COLUMN_INGREDIENT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        RecipeContract.RecipeEntry.COLUMN_RECIPE_ID + " INTEGER, " +
                        RecipeContract.RecipeEntry.COLUMN_INGREDIENT + " TEXT, " +
                        RecipeContract.RecipeEntry.COLUMN_QUANTITY + " INTEGER, " +
                        RecipeContract.RecipeEntry.COLUMN_UNITS + " TEXT);";

        sqLiteDatabase.execSQL(SQL_CREATE_RECIPE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_INGREDIENT_TABLE);

    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.RECIPE_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + RecipeContract.RecipeEntry.INGREDIENTS_TABLE_NAME);

        onCreate(sqLiteDatabase);

    }
}

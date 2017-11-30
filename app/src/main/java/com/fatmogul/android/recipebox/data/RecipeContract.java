package com.fatmogul.android.recipebox.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by adam on 11/20/17.
 */

public class RecipeContract {
    public static final String CONTENT_AUTHORITY = "com.fatmogul.android.recipebox";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_RECIPES = "recipes";
    public static final String PATH_INGREDIENTS = "ingredients";
    /*inner class to define the table contents*/
    public static final class RecipeEntry implements BaseColumns {

        public static final Uri CONTENT_URI_RECIPES = BASE_CONTENT_URI.buildUpon().appendPath(PATH_RECIPES).build();
        public static final Uri CONTENT_URI_INGREDIENTS = BASE_CONTENT_URI.buildUpon().appendPath(PATH_INGREDIENTS).build();


        /*used internally to define the name table*/
        public static final String RECIPE_TABLE_NAME = "recipes";
        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PREP_TIME = "prep_time";
        public static final String COLUMN_COOK_TIME = "cook_time";
        public static final String COLUMN_TOTAL_TIME = "total_time";
        public static final String COLUMN_SERVES = "servings";
        public static final String COLUMN_DIRECTIONS = "directions";
        public static final String COLUMN_CATEGORY = "category";
        public static final String COLUMN_IMAGE = "image";



        /*used internally to define the ingredients table*/
        public static final String INGREDIENTS_TABLE_NAME = "ingredients";
        public static final String COLUMN_INGREDIENT_ID = "ingredient_id";
        public static final String COLUMN_INGREDIENT = "ingredient";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_UNITS = "units";

        public static Uri buildRecipeWithId(long id){
            return CONTENT_URI_RECIPES.buildUpon().appendPath(Long.toString(id)).build();
        }
        public static Uri buildIngredientsWithId(long id){
            return CONTENT_URI_INGREDIENTS.buildUpon().appendPath(Long.toString(id)).build();
        }
    }

}

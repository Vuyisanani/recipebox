package com.fatmogul.android.recipebox;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.fatmogul.android.recipebox.data.RecipeContract;

import java.util.ArrayList;

/**
 * Created by adam on 11/20/17.
 */

public class EditRecipeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    int ingredientCounter = 1;
    int unitsCounter = 1001;
    int quantityCounter = 2001;
    EditText mRecipeView;
    EditText mCategoryView;
    EditText mPrepView;
    EditText mCookView;
    EditText mTotalView;
    EditText mServesView;
    EditText mDirectionsView;
    long mRecipeId;


    public static final int INDEX_RECIPE_NAME = 0;
    public static final int INDEX_PREP_TIME = 1;
    public static final int INDEX_COOK_TIME = 2;
    public static final int INDEX_TOTAL_TIME = 3;
    public static final int INDEX_SERVES = 4;
    public static final int INDEX_DIRECTIONS = 5;
    public static final int INDEX_CATEGORY = 6;
    public static final int INDEX_RECIPE_ID = 7;
    public static final String[] RECIPE_DETAIL_PROJECTION = {
            RecipeContract.RecipeEntry.COLUMN_NAME,
            RecipeContract.RecipeEntry.COLUMN_PREP_TIME,
            RecipeContract.RecipeEntry.COLUMN_COOK_TIME,
            RecipeContract.RecipeEntry.COLUMN_TOTAL_TIME,
            RecipeContract.RecipeEntry.COLUMN_SERVES,
            RecipeContract.RecipeEntry.COLUMN_DIRECTIONS,
            RecipeContract.RecipeEntry.COLUMN_CATEGORY,
            RecipeContract.RecipeEntry.COLUMN_RECIPE_ID};
    private static final int ID_RECIPE_DETAIL_LOADER = 96;

    public static final String[] INGREDIENT_DETAIL_PROJECTION = {
            RecipeContract.RecipeEntry.COLUMN_INGREDIENT,
            RecipeContract.RecipeEntry.COLUMN_UNITS,
            RecipeContract.RecipeEntry.COLUMN_QUANTITY
    };
    public static final int INDEX_INGREDIENT_NAME = 0;
    public static final int INDEX_INGREDIENT_UNITS = 1;
    public static final int INDEX_INGREDIENT_QUANTITY = 2;
    public static final int ID_INGEDIENT_LOADER = 54;

    private Uri mIngredientUri;
    private Uri mUri;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe_view);
        EditText unusedIngredientView = (EditText) findViewById(R.id.add_recipe_ingredient_edit_view);
        EditText unusedUnitsView = (EditText) findViewById(R.id.add_recipe_units_edit_view);
        EditText unusedQuantityView = (EditText) findViewById(R.id.add_recipe_quantity_edit_view);
        unusedIngredientView.setVisibility(View.GONE);
        unusedUnitsView.setVisibility(View.GONE);
        unusedQuantityView.setVisibility(View.GONE);
        final LinearLayout root = (LinearLayout) findViewById(R.id.add_recipe_ingredient_edit_view_group);
        Button mButton = (Button) findViewById(R.id.add_recipe_add_ingredient_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientCounter++;
                quantityCounter++;
                unitsCounter++;
                LinearLayout linearLayout = new LinearLayout(EditRecipeActivity.this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                EditText quantity = new EditText(EditRecipeActivity.this);
                quantity.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                quantity.setHint("Quantity");
                quantity.setId(quantityCounter);
                EditText units = new EditText(EditRecipeActivity.this);
                units.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                units.setHint("Units");
                units.setId(unitsCounter);
                EditText ingredient = new EditText(EditRecipeActivity.this);
                ingredient.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ingredient.setHint("Ingredient");
                ingredient.setId(ingredientCounter);
                linearLayout.addView(quantity);
                linearLayout.addView(units);
                linearLayout.addView(ingredient);
                root.addView(linearLayout);

            }
        });
        mRecipeView = (EditText) findViewById(R.id.add_recipe_name_edit_view);
        mCategoryView = (EditText) findViewById(R.id.add_recipe_category_edit_view);
        mPrepView = (EditText) findViewById(R.id.add_recipe_prep_time_edit_view);
        mCookView = (EditText) findViewById(R.id.add_recipe_cook_time_edit_view);
        mTotalView = (EditText) findViewById(R.id.add_recipe_total_time_edit_view);
        mServesView = (EditText) findViewById(R.id.add_recipe_serves_edit_view);
        mDirectionsView = (EditText) findViewById(R.id.add_recipe_directions_edit_view);
        mUri = getIntent().getData();
        getSupportLoaderManager().initLoader(ID_RECIPE_DETAIL_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch (i) {
            case ID_RECIPE_DETAIL_LOADER:
                return new CursorLoader(this, mUri, RECIPE_DETAIL_PROJECTION, null, null, null);
            case ID_INGEDIENT_LOADER:
                return new CursorLoader(this, mIngredientUri, INGREDIENT_DETAIL_PROJECTION, null, null, null);
            default:
                throw new RuntimeException("Loader not implemented");
        }

    }


    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        boolean cursorHasValidData = false;

        if (cursor != null && cursor.moveToFirst()) {
            /* We have valid data, continue on to bind the data to the UI */
            cursorHasValidData = true;
        }

        if (!cursorHasValidData) {
            /* No data to display, simply return and do nothing */
            return;
        }

        if (cursor.getColumnCount() > 3) {
            mRecipeId = cursor.getLong(INDEX_RECIPE_ID);
            String recipeName = cursor.getString(INDEX_RECIPE_NAME);
            String prepTime = cursor.getString(INDEX_PREP_TIME);
            String cookTime = cursor.getString(INDEX_COOK_TIME);
            String totalTime = cursor.getString(INDEX_TOTAL_TIME);
            int servesInt = cursor.getInt(INDEX_SERVES);
            String serves = Integer.toString(servesInt);
            String directions = cursor.getString(INDEX_DIRECTIONS);
            String category = cursor.getString(INDEX_CATEGORY);

            mRecipeView.setText(recipeName);
            mPrepView.setText(prepTime);
            mCookView.setText(cookTime);
            mTotalView.setText(totalTime);
            mServesView.setText(serves);
            mDirectionsView.setText(directions);
            mCategoryView.setText(category);

            mIngredientUri = RecipeContract.RecipeEntry.buildIngredientsWithId(mRecipeId);
            getSupportLoaderManager().initLoader(ID_INGEDIENT_LOADER, null, this);

        } else {
            ingredientCounter ++;
            unitsCounter ++;
            quantityCounter ++;


            final LinearLayout root = (LinearLayout) findViewById(R.id.add_recipe_ingredient_edit_view_group);
            LinearLayout linearLayout = new LinearLayout(EditRecipeActivity.this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            EditText quantity = new EditText(EditRecipeActivity.this);
            quantity.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            String quantityString = cursor.getString(INDEX_INGREDIENT_QUANTITY);
            quantity.setText(quantityString);
            quantity.setId(quantityCounter);
            EditText units = new EditText(EditRecipeActivity.this);
            units.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            String unitsString = cursor.getString(INDEX_INGREDIENT_UNITS);
            units.setText(unitsString);
            units.setId(unitsCounter);
            EditText ingredient = new EditText(EditRecipeActivity.this);
            ingredient.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            String ingredientString = cursor.getString(INDEX_INGREDIENT_NAME);
            ingredient.setText(ingredientString);
            ingredient.setId(ingredientCounter);
            linearLayout.addView(quantity);
            linearLayout.addView(units);
            linearLayout.addView(ingredient);
            root.addView(linearLayout);


            while(cursor.moveToNext()){
                ingredientCounter ++;
                unitsCounter ++;
                quantityCounter ++;
                linearLayout = new LinearLayout(EditRecipeActivity.this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                quantity = new EditText(EditRecipeActivity.this);
                quantity.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                quantityString = cursor.getString(INDEX_INGREDIENT_QUANTITY);
                quantity.setText(quantityString);
                quantity.setId(quantityCounter);
                units = new EditText(EditRecipeActivity.this);
                units.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                unitsString = cursor.getString(INDEX_INGREDIENT_UNITS);
                units.setText(unitsString);
                units.setId(unitsCounter);
                ingredient = new EditText(EditRecipeActivity.this);
                ingredient.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ingredientString = cursor.getString(INDEX_INGREDIENT_NAME);
                ingredient.setText(ingredientString);
                ingredient.setId(ingredientCounter);
                linearLayout.addView(quantity);
                linearLayout.addView(units);
                linearLayout.addView(ingredient);
                root.addView(linearLayout);

            }




        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_save_action:

                String recipeName = mRecipeView.getText().toString();
                String category = mCategoryView.getText().toString();
                String prepTime = mPrepView.getText().toString();
                String cookTime = mCategoryView.getText().toString();
                String totalTime = mTotalView.getText().toString();
                String servesString = mServesView.getText().toString();
                int serves = 0;
                if (!servesString.matches("")) serves = Integer.parseInt(servesString);
                String directions = mDirectionsView.getText().toString();

                ArrayList<ContentValues> contentValues = new ArrayList<>();
                ContentValues cv = new ContentValues();
                cv.put(RecipeContract.RecipeEntry.COLUMN_NAME, recipeName);
                cv.put(RecipeContract.RecipeEntry.COLUMN_CATEGORY, category);
                cv.put(RecipeContract.RecipeEntry.COLUMN_PREP_TIME, prepTime);
                cv.put(RecipeContract.RecipeEntry.COLUMN_COOK_TIME, cookTime);
                cv.put(RecipeContract.RecipeEntry.COLUMN_TOTAL_TIME, totalTime);
                cv.put(RecipeContract.RecipeEntry.COLUMN_SERVES, serves);
                cv.put(RecipeContract.RecipeEntry.COLUMN_DIRECTIONS, directions);
                contentValues.add(cv);

                for (int i = 2; i <= ingredientCounter; i++) {
                    Log.d("Please work", "onOptionsItemSelected: ");
                    EditText subIngredientNameView = (EditText) findViewById(i);
                    EditText subUnitView = (EditText) findViewById(1000 + i);
                    EditText subQuantityView = (EditText) findViewById(2000 + i);

                    String subIngredientName = subIngredientNameView.getText().toString();
                    String subUnit = subUnitView.getText().toString();
                    String subQuantityString = subQuantityView.getText().toString();
                    int subQuantity = 0;
                    if (!subQuantityString.matches(""))
                        subQuantity = Integer.parseInt(subQuantityString);

                    ContentValues subCv = new ContentValues();
                    subCv.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENT, subIngredientName);
                    subCv.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENT, subIngredientName);
                    subCv.put(RecipeContract.RecipeEntry.COLUMN_UNITS, subUnit);
                    subCv.put(RecipeContract.RecipeEntry.COLUMN_QUANTITY, subQuantity);
                    contentValues.add(subCv);
                }

                new UpdateRecipe().execute(contentValues);
                finish();


                break;


            case R.id.edit_cancel_action:
                finish();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }
class UpdateRecipe extends AsyncTask<ArrayList<ContentValues>,Void,Void>{


    @Override
    protected Void doInBackground(ArrayList<ContentValues>[] arrayLists) {
        String delId = Long.toString(mRecipeId);
        getContentResolver().delete(RecipeContract.RecipeEntry.CONTENT_URI_INGREDIENTS,delId,null);
        int position = 0;
        long id = 0;
        for(ContentValues values : arrayLists[0]){
            if(position == 0) {
                Uri uri = RecipeContract.RecipeEntry.buildRecipeWithId(mRecipeId);
                getContentResolver().update(uri, values,null,null);
                position ++;
            }
            else{
                values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID, mRecipeId);
                getContentResolver().insert(RecipeContract.RecipeEntry.CONTENT_URI_INGREDIENTS,values);

            }}

        return null;}

}}


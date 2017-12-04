package com.fatmogul.android.recipebox;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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
    int addButtonCounter = 3001;
    int removeButtonCounter = 4001;
    EditText mRecipeView;
    EditText mCategoryView;
    EditText mPrepView;
    EditText mCookView;
    EditText mTotalView;
    EditText mServesView;
    EditText mDirectionsView;
    long mRecipeId;

    ArrayList<Integer> ids = new ArrayList<>();
    ArrayList<ContentValues> tempList = new ArrayList<>();


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
LinearLayout root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_recipe_view);
        root = (LinearLayout) findViewById(R.id.add_recipe_ingredient_edit_view_group);

        if (savedInstanceState != null) {
            tempList = new ArrayList<ContentValues>();
            tempList = savedInstanceState.getParcelableArrayList("templist");
            ids = new ArrayList<Integer>();
        } else {
        ingredientCounter++;
        quantityCounter++;
        unitsCounter++;
        addButtonCounter++;
        removeButtonCounter++;
        mRecipeView = (EditText) findViewById(R.id.add_recipe_name_edit_view);
        mCategoryView = (EditText) findViewById(R.id.add_recipe_category_edit_view);
        mPrepView = (EditText) findViewById(R.id.add_recipe_prep_time_edit_view);
        mCookView = (EditText) findViewById(R.id.add_recipe_cook_time_edit_view);
        mTotalView = (EditText) findViewById(R.id.add_recipe_total_time_edit_view);
        mServesView = (EditText) findViewById(R.id.add_recipe_serves_edit_view);
        mDirectionsView = (EditText) findViewById(R.id.add_recipe_directions_edit_view);
        mUri = getIntent().getData();
        getSupportLoaderManager().initLoader(ID_RECIPE_DETAIL_LOADER, null, this);
    }}

    public void addAddClickListener(Button button){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientCounter++;
                quantityCounter++;
                unitsCounter++;
                addButtonCounter++;
                removeButtonCounter++;
                ids.add(ingredientCounter);
                LinearLayout linearLayout = new LinearLayout(EditRecipeActivity.this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                EditText quantity = new EditText(EditRecipeActivity.this);
                quantity.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,.74f));
                quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                quantity.setId(quantityCounter);
                EditText units = new EditText(EditRecipeActivity.this);
                units.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,.74f));
                units.setId(unitsCounter);
                units.setNextFocusDownId(ingredientCounter);
                quantity.setNextFocusDownId(unitsCounter);
                EditText ingredient = new EditText(EditRecipeActivity.this);
                ingredient.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.65f));
                ingredient.setId(ingredientCounter);
                Button addButton = new Button(EditRecipeActivity.this);
                addButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,.6f));
                addButton.setText("+");
                addButton.setId(addButtonCounter);
                addAddClickListener(addButton);
                Button removeButton = new Button(EditRecipeActivity.this);
                removeButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,.6f));
                removeButton.setText("-");
                removeButton.setId(removeButtonCounter);
                addRemoveClickListener(removeButton);

                linearLayout.addView(removeButton);
                linearLayout.addView(quantity);
                linearLayout.addView(units);
                linearLayout.addView(ingredient);
                linearLayout.addView(addButton);
                root.addView(linearLayout);
            }

            ;
        });
    }
    @Override
    protected void onPause() {
        tempList = new ArrayList<ContentValues>();

        for (int number : ids) {
            EditText subIngredientNameView = (EditText) findViewById(number);
            EditText subUnitView = (EditText) findViewById(1000 + number);
            EditText subQuantityView = (EditText) findViewById(2000 + number);

            String subIngredientName = subIngredientNameView.getText().toString();
            String subUnit = subUnitView.getText().toString();
            String subQuantityString = subQuantityView.getText().toString();
            int subQuantity = 0;
            if (!subQuantityString.matches("")) subQuantity = Integer.parseInt(subQuantityString);

            ContentValues tempCv = new ContentValues();
            tempCv.put("ID", number);
            tempCv.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENT, subIngredientName);
            tempCv.put(RecipeContract.RecipeEntry.COLUMN_UNITS, subUnit);
            tempCv.put(RecipeContract.RecipeEntry.COLUMN_QUANTITY, subQuantity);
            tempList.add(tempCv);
        }
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("templist", tempList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {

        for (ContentValues cv : tempList) {
            int thisId = cv.getAsInteger("ID");
            ids.add(thisId);
            LinearLayout linearLayout = new LinearLayout(EditRecipeActivity.this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            EditText quantity = new EditText(EditRecipeActivity.this);
            quantity.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, .74f));
            quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
            String quantityString = cv.getAsString(RecipeContract.RecipeEntry.COLUMN_QUANTITY);
            quantity.setText(quantityString);
            quantity.setId(thisId + 2000);
            EditText units = new EditText(EditRecipeActivity.this);
            units.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, .74f));
            units.setId(thisId + 1000);
            units.setNextFocusDownId(ingredientCounter);
            quantity.setNextFocusDownId(unitsCounter);
            String unitsString = cv.getAsString(RecipeContract.RecipeEntry.COLUMN_UNITS);
            units.setText(unitsString);
            EditText ingredient = new EditText(EditRecipeActivity.this);
            ingredient.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.65f));
            ingredient.setId(thisId);
            String ingredientString = cv.getAsString(RecipeContract.RecipeEntry.COLUMN_INGREDIENT);
            ingredient.setText(ingredientString);
            Button addButton = new Button(EditRecipeActivity.this);
            addButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, .6f));
            addButton.setText("+");
            addButton.setId(thisId + 3000);
            addAddClickListener(addButton);
            Button removeButton = new Button(EditRecipeActivity.this);
            removeButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, .6f));
            removeButton.setText("-");
            removeButton.setId(thisId + 4000);
            addRemoveClickListener(removeButton);

            linearLayout.addView(removeButton);
            linearLayout.addView(quantity);
            linearLayout.addView(units);
            linearLayout.addView(ingredient);
            linearLayout.addView(addButton);
            root.addView(linearLayout);

        }
        ingredientCounter++;
        quantityCounter++;
        unitsCounter++;
        addButtonCounter++;
        removeButtonCounter++;
        super.onResume();
    }

    public void addRemoveClickListener(final Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ids.size() > 1) {
                    int counter = button.getId();
                    Button removeButton = findViewById(counter);
                    EditText quantity = (EditText) findViewById(counter - 2000);
                    EditText units = (EditText) findViewById(counter - 3000);
                    EditText ingredient = (EditText) findViewById(counter - 4000);
                    Button addButton = findViewById(counter - 1000);
                    quantity.setVisibility(View.GONE);
                    units.setVisibility(View.GONE);
                    ingredient.setVisibility(View.GONE);
                    removeButton.setVisibility(View.GONE);
                    addButton.setVisibility(View.GONE);
                    ids.remove(Integer.valueOf(counter - 4000));
                    ingredientCounter++;
                    quantityCounter++;
                    unitsCounter++;
                    addButtonCounter++;
                    removeButtonCounter++;
                } else {
                    int counter = button.getId();
                    Button removeButton = findViewById(counter);
                    EditText quantity = (EditText) findViewById(counter - 2000);
                    EditText units = (EditText) findViewById(counter - 3000);
                    EditText ingredient = (EditText) findViewById(counter - 4000);
                    Button addButton = findViewById(counter - 1000);
                    quantity.setText("");
                    units.setText("");
                    ingredient.setText("");

                }
            }
        });
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
            addButtonCounter ++;
            removeButtonCounter ++;
            ids.add(ingredientCounter);

            final LinearLayout root = (LinearLayout) findViewById(R.id.add_recipe_ingredient_edit_view_group);
            LinearLayout linearLayout = new LinearLayout(EditRecipeActivity.this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            EditText quantity = new EditText(EditRecipeActivity.this);
            quantity.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,.74f));
            String quantityString = cursor.getString(INDEX_INGREDIENT_QUANTITY);
            quantity.setText(quantityString);
            quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
            quantity.setId(quantityCounter);
            EditText units = new EditText(EditRecipeActivity.this);
            units.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,.74f));
            String unitsString = cursor.getString(INDEX_INGREDIENT_UNITS);
            units.setText(unitsString);
            units.setId(unitsCounter);
            units.setNextFocusDownId(ingredientCounter);
            quantity.setNextFocusDownId(unitsCounter);
            EditText ingredient = new EditText(EditRecipeActivity.this);
            ingredient.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.65f));
            String ingredientString = cursor.getString(INDEX_INGREDIENT_NAME);
            ingredient.setText(ingredientString);
            ingredient.setId(ingredientCounter);
            Button addButton = new Button(EditRecipeActivity.this);
            addButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,.6f));
            addButton.setText("+");
            addButton.setId(addButtonCounter);
            addAddClickListener(addButton);
            Button removeButton = new Button(EditRecipeActivity.this);
            removeButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,.6f));
            removeButton.setText("-");
            removeButton.setId(removeButtonCounter);
            addRemoveClickListener(removeButton);

            linearLayout.addView(removeButton);
            linearLayout.addView(quantity);
            linearLayout.addView(units);
            linearLayout.addView(ingredient);
            linearLayout.addView(addButton);
            root.addView(linearLayout);


            while(cursor.moveToNext()){
                ingredientCounter ++;
                unitsCounter ++;
                quantityCounter ++;
                addButtonCounter ++;
                removeButtonCounter ++;
                ids.add(ingredientCounter);

                linearLayout = new LinearLayout(EditRecipeActivity.this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                quantity = new EditText(EditRecipeActivity.this);
                quantity.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,.74f));
                quantityString = cursor.getString(INDEX_INGREDIENT_QUANTITY);
                quantity.setText(quantityString);
                quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                quantity.setId(quantityCounter);
                units = new EditText(EditRecipeActivity.this);
                units.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,.74f));
                unitsString = cursor.getString(INDEX_INGREDIENT_UNITS);
                units.setText(unitsString);
                units.setId(unitsCounter);
                units.setNextFocusDownId(ingredientCounter);
                quantity.setNextFocusDownId(unitsCounter);
                ingredient = new EditText(EditRecipeActivity.this);
                ingredient.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.65f));
                ingredientString = cursor.getString(INDEX_INGREDIENT_NAME);
                ingredient.setText(ingredientString);
                ingredient.setId(ingredientCounter);
                addButton = new Button(EditRecipeActivity.this);
                addButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,.6f));
                addButton.setText("+");
                addButton.setId(addButtonCounter);
                addAddClickListener(addButton);
                removeButton = new Button(EditRecipeActivity.this);
                removeButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,.6f));
                removeButton.setText("-");
                removeButton.setId(removeButtonCounter);
                addRemoveClickListener(removeButton);

                linearLayout.addView(removeButton);
                linearLayout.addView(quantity);
                linearLayout.addView(units);
                linearLayout.addView(ingredient);
                linearLayout.addView(addButton);
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
                String cookTime = mCookView.getText().toString();
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

                for (int i : ids) {
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
                break;

            case R.id.edit_delete_action:
                new AlertDialog.Builder(this)
                        .setTitle("Title")
                        .setMessage("Are you sure you want to delete?")
                        .setPositiveButton("Yes, I'm sure", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                new DeleteRecipe().execute();
                                Intent intent = new Intent(EditRecipeActivity.this, BoxviewActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                }})
                        .setNegativeButton("No, take me back to my recipe", null).show();


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle("Title")
                .setMessage("If you leave now, your recipe won't be saved")
                .setPositiveButton("Leave", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        finish();
                        }})
                .setNegativeButton("No, take me back to my recipe", null).setNeutralButton("Save Recipe", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String recipeName = mRecipeView.getText().toString();
                String category = mCategoryView.getText().toString();
                String prepTime = mPrepView.getText().toString();
                String cookTime = mCookView.getText().toString();
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

                for (int id : ids) {
                    EditText subIngredientNameView = (EditText) findViewById(id);
                    EditText subUnitView = (EditText) findViewById(1000 + id);
                    EditText subQuantityView = (EditText) findViewById(2000 + id);

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


            }
        }).show();



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

}
class DeleteRecipe extends AsyncTask<Void, Void,Void>{

    @Override
    protected Void doInBackground(Void... voids) {
        Uri uri = RecipeContract.RecipeEntry.buildRecipeWithId(mRecipeId);
        getContentResolver().delete(uri, null, null);
        return null;
    }
}

}


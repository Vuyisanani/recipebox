package com.fatmogul.android.recipebox;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
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

public class AddRecipeActivity extends AppCompatActivity {
    int ingredientCounter = 1;
    int unitsCounter = 1001;
    int quantityCounter = 2001;
    int addButtonCounter = 3001;
    int removeButtonCounter = 4001;
    ArrayList<Integer> ids = new ArrayList<>();
    ArrayList<ContentValues> tempList = new ArrayList<>();
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
            ids.add(ingredientCounter);
            Log.d(ids + "this", "onCreate: ");
            LinearLayout linearLayout = new LinearLayout(AddRecipeActivity.this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            EditText quantity = new EditText(AddRecipeActivity.this);
            quantity.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, .74f));
            quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
            quantity.setId(quantityCounter);
            EditText units = new EditText(AddRecipeActivity.this);
            units.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, .74f));
            units.setId(unitsCounter);
            units.setNextFocusDownId(ingredientCounter);
            quantity.setNextFocusDownId(unitsCounter);
            EditText ingredient = new EditText(AddRecipeActivity.this);
            ingredient.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.65f));
            ingredient.setId(ingredientCounter);
            Button addButton = new Button(AddRecipeActivity.this);
            addButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, .6f));
            addButton.setText("+");
            addButton.setId(addButtonCounter);
            addAddClickListener(addButton);
            Button removeButton = new Button(AddRecipeActivity.this);
            removeButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, .6f));
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
            LinearLayout linearLayout = new LinearLayout(AddRecipeActivity.this);
            linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            EditText quantity = new EditText(AddRecipeActivity.this);
            quantity.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, .74f));
            quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
            String quantityString = cv.getAsString(RecipeContract.RecipeEntry.COLUMN_QUANTITY);
            quantity.setText(quantityString);
            quantity.setId(thisId + 2000);
            EditText units = new EditText(AddRecipeActivity.this);
            units.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, .74f));
            units.setId(thisId + 1000);
            String unitsString = cv.getAsString(RecipeContract.RecipeEntry.COLUMN_UNITS);
            units.setText(unitsString);
            units.setNextFocusDownId(ingredientCounter);
            quantity.setNextFocusDownId(unitsCounter);
            EditText ingredient = new EditText(AddRecipeActivity.this);
            ingredient.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.65f));
            ingredient.setId(thisId);
            String ingredientString = cv.getAsString(RecipeContract.RecipeEntry.COLUMN_INGREDIENT);
            ingredient.setText(ingredientString);
            Button addButton = new Button(AddRecipeActivity.this);
            addButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, .6f));
            addButton.setText("+");
            addButton.setId(thisId + 3000);
            addAddClickListener(addButton);
            Button removeButton = new Button(AddRecipeActivity.this);
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

    public void addAddClickListener(Button button) {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientCounter++;
                quantityCounter++;
                unitsCounter++;
                addButtonCounter++;
                removeButtonCounter++;
                ids.add(ingredientCounter);
                Log.d(ids + "this", "onClick: ");
                LinearLayout linearLayout = new LinearLayout(AddRecipeActivity.this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                EditText quantity = new EditText(AddRecipeActivity.this);
                quantity.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, .74f));
                quantity.setInputType(InputType.TYPE_CLASS_NUMBER);
                quantity.setId(quantityCounter);
                EditText units = new EditText(AddRecipeActivity.this);
                units.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, .74f));
                units.setId(unitsCounter);
                units.setNextFocusDownId(ingredientCounter);
                quantity.setNextFocusDownId(unitsCounter);
                EditText ingredient = new EditText(AddRecipeActivity.this);
                ingredient.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1.65f));
                ingredient.setId(ingredientCounter);
                Button addButton = new Button(AddRecipeActivity.this);
                addButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, .6f));
                addButton.setText("+");
                addButton.setId(addButtonCounter);
                addAddClickListener(addButton);
                Button removeButton = new Button(AddRecipeActivity.this);
                removeButton.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, .6f));
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_recipe_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_save_action:
                EditText recipeNameView = (EditText) findViewById(R.id.add_recipe_name_edit_view);
                EditText categoryView = (EditText) findViewById(R.id.add_recipe_category_edit_view);
                EditText prepTimeView = (EditText) findViewById(R.id.add_recipe_prep_time_edit_view);
                EditText cookTimeView = (EditText) findViewById(R.id.add_recipe_cook_time_edit_view);
                EditText totalTimeView = (EditText) findViewById(R.id.add_recipe_total_time_edit_view);
                EditText servesView = (EditText) findViewById(R.id.add_recipe_serves_edit_view);
                EditText directionsView = (EditText) findViewById(R.id.add_recipe_directions_edit_view);

                String recipeName = recipeNameView.getText().toString();
                String category = categoryView.getText().toString();
                String prepTime = prepTimeView.getText().toString();
                String cookTime = cookTimeView.getText().toString();
                String totalTime = totalTimeView.getText().toString();
                String servesString = servesView.getText().toString();
                int serves = 0;
                if (!servesString.matches("")) serves = Integer.parseInt(servesString);
                String directions = directionsView.getText().toString();

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

                for (int number : ids) {
                    EditText subIngredientNameView = (EditText) findViewById(number);
                    EditText subUnitView = (EditText) findViewById(1000 + number);
                    EditText subQuantityView = (EditText) findViewById(2000 + number);

                    String subIngredientName = subIngredientNameView.getText().toString();
                    String subUnit = subUnitView.getText().toString();
                    String subQuantityString = subQuantityView.getText().toString();
                    int subQuantity = 0;
                    if (!subQuantityString.matches(""))
                        subQuantity = Integer.parseInt(subQuantityString);

                    ContentValues subCv = new ContentValues();
                    subCv.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENT, subIngredientName);
                    subCv.put(RecipeContract.RecipeEntry.COLUMN_UNITS, subUnit);
                    subCv.put(RecipeContract.RecipeEntry.COLUMN_QUANTITY, subQuantity);
                    contentValues.add(subCv);
                }

                new SaveRecipe().execute(contentValues);
                finish();


                break;


            case R.id.add_cancel_action:
                finish();
                return true;

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

                EditText recipeNameView = (EditText) findViewById(R.id.add_recipe_name_edit_view);
                EditText categoryView = (EditText) findViewById(R.id.add_recipe_category_edit_view);
                EditText prepTimeView = (EditText) findViewById(R.id.add_recipe_prep_time_edit_view);
                EditText cookTimeView = (EditText) findViewById(R.id.add_recipe_cook_time_edit_view);
                EditText totalTimeView = (EditText) findViewById(R.id.add_recipe_total_time_edit_view);
                EditText servesView = (EditText) findViewById(R.id.add_recipe_serves_edit_view);
                EditText directionsView = (EditText) findViewById(R.id.add_recipe_directions_edit_view);

                String recipeName = recipeNameView.getText().toString();
                String category = categoryView.getText().toString();
                String prepTime = prepTimeView.getText().toString();
                String cookTime = cookTimeView.getText().toString();
                String totalTime = totalTimeView.getText().toString();
                String servesString = servesView.getText().toString();
                int serves = 0;
                if (!servesString.matches("")) serves = Integer.parseInt(servesString);
                String directions = directionsView.getText().toString();

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

                for (int number : ids) {
                    EditText subIngredientNameView = (EditText) findViewById(number);
                    EditText subUnitView = (EditText) findViewById(1000 + number);
                    EditText subQuantityView = (EditText) findViewById(2000 + number);

                    String subIngredientName = subIngredientNameView.getText().toString();
                    String subUnit = subUnitView.getText().toString();
                    String subQuantityString = subQuantityView.getText().toString();
                    int subQuantity = 0;
                    if (!subQuantityString.matches(""))
                        subQuantity = Integer.parseInt(subQuantityString);

                    ContentValues subCv = new ContentValues();
                    subCv.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENT, subIngredientName);
                    subCv.put(RecipeContract.RecipeEntry.COLUMN_UNITS, subUnit);
                    subCv.put(RecipeContract.RecipeEntry.COLUMN_QUANTITY, subQuantity);
                    contentValues.add(subCv);
                }

                new SaveRecipe().execute(contentValues);
                finish();


            }
        }).show();


       }

    private class SaveRecipe extends AsyncTask<ArrayList<ContentValues>, Void, Void> {
        @Override
        protected Void doInBackground(ArrayList<ContentValues>... content) {

            int position = 0;
            long id = 0;
            for (ContentValues values : content[0]) {
                if (position == 0) {
                    Uri newUri = getContentResolver().insert(RecipeContract.RecipeEntry.CONTENT_URI_RECIPES, values);
                    id = Long.valueOf(newUri.getLastPathSegment());
                    position++;
                } else {
                    values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID, id);
                    getContentResolver().insert(RecipeContract.RecipeEntry.CONTENT_URI_INGREDIENTS, values);

                }
            }

            return null;
        }

    }
}
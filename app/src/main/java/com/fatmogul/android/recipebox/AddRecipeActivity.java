package com.fatmogul.android.recipebox;

import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.fatmogul.android.recipebox.data.RecipeContract;

import java.util.ArrayList;

/**
 * Created by adam on 11/20/17.
 */

public class AddRecipeActivity extends AppCompatActivity{
int ingredientCounter = 1;
int unitsCounter = 1001;
int quantityCounter = 2001;
int addButtonCounter = 3001;
int removeButtonCounter = 4001;
ArrayList<Integer> ids = new ArrayList<>();
LinearLayout root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe_view);

        root = (LinearLayout) findViewById(R.id.add_recipe_ingredient_edit_view_group);
        ingredientCounter++;
        quantityCounter++;
        unitsCounter++;
        addButtonCounter++;
        removeButtonCounter++;
        ids.add(ingredientCounter);
        LinearLayout linearLayout = new LinearLayout(AddRecipeActivity.this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        EditText quantity = new EditText(AddRecipeActivity.this);
        quantity.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.4f));
        quantity.setId(quantityCounter);
        EditText units = new EditText(AddRecipeActivity.this);
        units.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.4f));
        units.setId(unitsCounter);
        EditText ingredient = new EditText(AddRecipeActivity.this);
        ingredient.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
        ingredient.setId(ingredientCounter);
        Button addButton = new Button(AddRecipeActivity.this);
        addButton.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.5f));
        addButton.setText("+");
        addButton.setId(addButtonCounter);
        addAddClickListener(addButton);
        Button removeButton = new Button(AddRecipeActivity.this);
        removeButton.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.5f));
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
                LinearLayout linearLayout = new LinearLayout(AddRecipeActivity.this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                EditText quantity = new EditText(AddRecipeActivity.this);
                quantity.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.4f));
                quantity.setId(quantityCounter);
                EditText units = new EditText(AddRecipeActivity.this);
                units.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.4f));
                units.setId(unitsCounter);
                EditText ingredient = new EditText(AddRecipeActivity.this);
                ingredient.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1f));
                ingredient.setId(ingredientCounter);
                Button addButton = new Button(AddRecipeActivity.this);
                addButton.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.5f));
                addButton.setText("+");
                addButton.setId(addButtonCounter);
                addAddClickListener(addButton);
                Button removeButton = new Button(AddRecipeActivity.this);
                removeButton.setLayoutParams(new TableLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT,1.5f));
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
                        if (ingredientCounter > 2) {
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
                            ingredientCounter--;
                            quantityCounter--;
                            unitsCounter--;
                            addButtonCounter--;
                            removeButtonCounter--;
                        }
                    }
                });}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_recipe_menu,menu);
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
                cv.put(RecipeContract.RecipeEntry.COLUMN_NAME,recipeName);
                cv.put(RecipeContract.RecipeEntry.COLUMN_CATEGORY,category);
                cv.put(RecipeContract.RecipeEntry.COLUMN_PREP_TIME,prepTime);
                cv.put(RecipeContract.RecipeEntry.COLUMN_COOK_TIME,cookTime);
                cv.put(RecipeContract.RecipeEntry.COLUMN_TOTAL_TIME,totalTime);
                cv.put(RecipeContract.RecipeEntry.COLUMN_SERVES,serves);
                cv.put(RecipeContract.RecipeEntry.COLUMN_DIRECTIONS,directions);
                contentValues.add(cv);

                for(int number : ids){
                    EditText subIngredientNameView = (EditText) findViewById(number);
                    EditText subUnitView = (EditText) findViewById(1000 + number);
                    EditText subQuantityView = (EditText) findViewById(2000 + number);

                    String subIngredientName = subIngredientNameView.getText().toString();
                    String subUnit = subUnitView.getText().toString();
                    String subQuantityString = subQuantityView.getText().toString();
                    int subQuantity = 0;
                    if(!subQuantityString.matches(""))subQuantity = Integer.parseInt(subQuantityString);

                    ContentValues subCv = new ContentValues();
                    subCv.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENT,subIngredientName);
                    subCv.put(RecipeContract.RecipeEntry.COLUMN_UNITS,subUnit);
                    subCv.put(RecipeContract.RecipeEntry.COLUMN_QUANTITY,subQuantity);
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



private class SaveRecipe extends AsyncTask<ArrayList<ContentValues>,Void,Void> {
    @Override
    protected Void doInBackground(ArrayList<ContentValues>... content) {

        int position = 0;
        long id = 0;
        for(ContentValues values : content[0]){
            if(position == 0) {
                Uri newUri = getContentResolver().insert(RecipeContract.RecipeEntry.CONTENT_URI_RECIPES, values);
                id = Long.valueOf(newUri.getLastPathSegment());
                position ++;
            }
            else{
                values.put(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID, id);
                getContentResolver().insert(RecipeContract.RecipeEntry.CONTENT_URI_INGREDIENTS,values);

            }}

            return null;}

}}
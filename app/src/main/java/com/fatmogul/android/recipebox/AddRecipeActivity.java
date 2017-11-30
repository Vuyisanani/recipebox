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

import com.fatmogul.android.recipebox.data.RecipeContract;

import java.util.ArrayList;

/**
 * Created by adam on 11/20/17.
 */

public class AddRecipeActivity extends AppCompatActivity{
int ingredientCounter = 1;
int unitsCounter = 1001;
int quantityCounter = 2001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_recipe_view);

        final LinearLayout root = (LinearLayout) findViewById(R.id.ingredient_edit_view_group);
        Button mButton = (Button) findViewById(R.id.add_ingredient_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ingredientCounter ++;
                quantityCounter ++;
                unitsCounter ++;
                LinearLayout linearLayout = new LinearLayout(AddRecipeActivity.this);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                EditText quantity = new EditText(AddRecipeActivity.this);
                quantity.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                quantity.setHint("Quantity");
                quantity.setId(quantityCounter);
                EditText units = new EditText(AddRecipeActivity.this);
                units.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                units.setHint("Units");
                units.setId(unitsCounter);
                EditText ingredient = new EditText(AddRecipeActivity.this);
                ingredient.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                ingredient.setHint("Next Ingredient");
                ingredient.setId(ingredientCounter);
                linearLayout.addView(quantity);
                linearLayout.addView(units);
                linearLayout.addView(ingredient);
                root.addView(linearLayout);



            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_action:
                EditText recipeNameView = (EditText) findViewById(R.id.recipe_name_edit_view);
                EditText categoryView = (EditText) findViewById(R.id.category_edit_view);
                EditText prepTimeView = (EditText) findViewById(R.id.prep_time_edit_view);
                EditText cookTimeView = (EditText) findViewById(R.id.cook_time_edit_view);
                EditText totalTimeView = (EditText) findViewById(R.id.total_time_edit_view);
                EditText servesView = (EditText) findViewById(R.id.serves_edit_view);
                EditText directionsView = (EditText) findViewById(R.id.directions_edit_view);

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

                EditText ingredientNameView = (EditText) findViewById(R.id.ingedient_edit_view_1);
                EditText unitView = (EditText) findViewById(R.id.units_edit_view_1);
                EditText quantityView = (EditText) findViewById(R.id.quantity_edit_view_1);

                String ingredientName = ingredientNameView.getText().toString();
                String unit = unitView.getText().toString();
                String quantityString = quantityView.getText().toString();
                int quantity = 0;
                if(!quantityString.matches(""))quantity = Integer.parseInt(quantityString);

                ContentValues cvi = new ContentValues();
                cvi.put(RecipeContract.RecipeEntry.COLUMN_INGREDIENT,ingredientName);
                cvi.put(RecipeContract.RecipeEntry.COLUMN_UNITS,unit);
                cvi.put(RecipeContract.RecipeEntry.COLUMN_QUANTITY,quantity);
                contentValues.add(cvi);

                for(int i=2; i <= ingredientCounter; i++){
                    EditText subIngredientNameView = (EditText) findViewById(i);
                    EditText subUnitView = (EditText) findViewById(1000 + i);
                    EditText subQuantityView = (EditText) findViewById(2000 + i);

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


            case R.id.cancel_action:
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
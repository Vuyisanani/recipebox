package com.fatmogul.android.recipebox;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.fatmogul.android.recipebox.data.RecipeContract;

/**
 * Created by adam on 11/30/17.
 */

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private DetailAdapter mAdapter;

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
    private static final int ID_RECIPE_DETAIL_LOADER = 99;

    public static final String[] INGREDIENT_DETAIL_PROJECTION = {
        RecipeContract.RecipeEntry.COLUMN_INGREDIENT,
            RecipeContract.RecipeEntry.COLUMN_UNITS,
            RecipeContract.RecipeEntry.COLUMN_QUANTITY
    };
    public static final int INDEX_INGREDIENT_NAME = 0;
    public static final int INDEX_INGREDIENT_UNITS = 1;
    public static final int INDEX_INGREDIENT_QUANTITY = 2;
    public static final int ID_INGEDIENT_LOADER = 55;

    private Uri mIngredientUri;

    private Uri mUri;
    private TextView mRecipeView;
    private TextView mPrepView;
    private TextView mCookView;
    private TextView mTotalView;
    private TextView mServesView;
    private TextView mDirectionsView;
    private TextView mCategoryView;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;
    private Cursor mCursor;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mRecyclerView = (RecyclerView) this.findViewById(R.id.ingredient_list_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new DetailAdapter(this, mCursor);

        mRecyclerView.setAdapter(mAdapter);

        mRecipeView = (TextView) findViewById(R.id.recipe_name_detail_view);
        mPrepView = (TextView) findViewById(R.id.prep_time_detail_view);
        mCookView = (TextView) findViewById(R.id.cook_time_detail_view);
        mTotalView = (TextView) findViewById(R.id.total_time_detail_view);
        mServesView = (TextView) findViewById(R.id.serves_detail_view);
        mDirectionsView = (TextView) findViewById(R.id.directions_detail_view);
        mCategoryView = (TextView) findViewById(R.id.category_detail_view);

        mUri = getIntent().getData();
        getSupportLoaderManager().initLoader(ID_RECIPE_DETAIL_LOADER,null,this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch(i){
            case ID_RECIPE_DETAIL_LOADER:
                return new CursorLoader(this, mUri,RECIPE_DETAIL_PROJECTION,null,null,null);
            case ID_INGEDIENT_LOADER:
                return new CursorLoader(this, mIngredientUri,INGREDIENT_DETAIL_PROJECTION,null,null,null);
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
        String test = mRecipeView.getText().toString();
        //if (!("Recipe Name: " + cursor.getString(INDEX_RECIPE_NAME) == mRecipeView.getText().toString())) {
        if(cursor.getColumnCount() > 3){
            long recipeId = cursor.getLong(INDEX_RECIPE_ID);
            String recipeName = cursor.getString(INDEX_RECIPE_NAME);
            String prepTime = cursor.getString(INDEX_PREP_TIME);
            String cookTime = cursor.getString(INDEX_COOK_TIME);
            String totalTime = cursor.getString(INDEX_TOTAL_TIME);
            int servesInt = cursor.getInt(INDEX_SERVES);
            String serves = Integer.toString(servesInt);
            String directions = cursor.getString(INDEX_DIRECTIONS);
            String category = cursor.getString(INDEX_CATEGORY);

            mRecipeView.setText("Recipe Name: " + recipeName);
            mPrepView.setText("Preperation Time: " + prepTime);
            mCookView.setText("Cook Time: " + cookTime);
            mTotalView.setText("Total Time: " + totalTime);
            mServesView.setText("Serves: " + serves);
            mDirectionsView.setText("Directions: " + directions);
            mCategoryView.setText("Category: " + category);

            mIngredientUri = RecipeContract.RecipeEntry.buildIngredientsWithId(recipeId);
            getSupportLoaderManager().initLoader(ID_INGEDIENT_LOADER, null, this);

        }
        else{
            mAdapter.swapCursor(cursor);
            if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
            mRecyclerView.smoothScrollToPosition(mPosition);

        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

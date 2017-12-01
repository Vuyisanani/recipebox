package com.fatmogul.android.recipebox;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fatmogul.android.recipebox.data.RecipeContract;

public class BoxviewActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        BoxviewAdapter.RecipeAdapterOnClickHandler {

    private BoxviewAdapter mAdapter;
    private final static String LOG_TAG = BoxviewActivity.class.getSimpleName();
    private static final int ID_RECIPE_LOADER = 42;
    public static final int INDEX_RECIPE_NAME = 1;
    public static final int INDEX_RECIPE_TOTAL_TIME = 2;
    public static final int INDEX_RECIPE_ID = 0;
    private RecyclerView mRecyclerView;
    private int mPosition = RecyclerView.NO_POSITION;
    private static final String[] MAIN_RECIPE_PROJECTION = {RecipeContract.RecipeEntry.COLUMN_RECIPE_ID,RecipeContract.RecipeEntry.COLUMN_NAME, RecipeContract.RecipeEntry.COLUMN_TOTAL_TIME};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boxview);

        mRecyclerView = (RecyclerView) this.findViewById(R.id.recipe_list_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new BoxviewAdapter(this, this);

        mRecyclerView.setAdapter(mAdapter);

        getSupportLoaderManager().initLoader(ID_RECIPE_LOADER,null,this);

        }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.boxview_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add_recipe_button:
                addRecipe(mRecyclerView);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

public void addRecipe(View view){
        Intent intent = new Intent(BoxviewActivity.this, AddRecipeActivity.class);
        startActivity(intent);
}



    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        switch(i){

            case ID_RECIPE_LOADER:

                Uri recipeQueryUri = RecipeContract.RecipeEntry.CONTENT_URI_RECIPES;
                return new CursorLoader(this,recipeQueryUri,MAIN_RECIPE_PROJECTION,null,null,null );

        default:
            throw new RuntimeException("Loader Not Implemented");

    }}

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mAdapter.swapCursor(cursor);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        mRecyclerView.smoothScrollToPosition(mPosition);
        TextView emptyView = (TextView) findViewById(R.id.empty_text_view);
        ImageView emptyPlate = (ImageView) findViewById(R.id.empty_plate);

        if (cursor.getCount() == 0) {
            emptyView.setVisibility(View.VISIBLE);
            emptyPlate.setVisibility(View.VISIBLE);}
        else{
            emptyView.setVisibility(View.GONE);
            emptyPlate.setVisibility(View.GONE);

        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }

    @Override
    public void onClick(long id) {
        Intent intent = new Intent(BoxviewActivity.this, RecipeDetailActivity.class);
        Uri uri = RecipeContract.RecipeEntry.buildRecipeWithId(id);
        intent.putExtra("ID",id);
        intent.setData(uri);
        startActivity(intent);
        }

}

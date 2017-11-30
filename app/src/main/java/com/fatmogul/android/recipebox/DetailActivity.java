package com.fatmogul.android.recipebox;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.fatmogul.android.recipebox.data.RecipeContract;

/**
 * Created by adam on 11/30/17.
 */

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    public static final int INDEX_RECIPE_NAME = 0;
    public static final String[] RECIPE_DETAIL_PROJECTION = {
            RecipeContract.RecipeEntry.COLUMN_NAME};
    private static final int ID_RECIPE_DETAIL_LOADER = 99;


    private Uri mUri;
    private long mId;
    private TextView mRecipeView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mRecipeView = (TextView) findViewById(R.id.recipe_name_detail_view);

        mId = getIntent().getLongExtra("ID",0);
        mUri = getIntent().getData();
        getSupportLoaderManager().initLoader(ID_RECIPE_DETAIL_LOADER,null,this);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        switch(i){
            case ID_RECIPE_DETAIL_LOADER:
                return new CursorLoader(this, mUri,RECIPE_DETAIL_PROJECTION,null,null,null);

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

        String recipeName = cursor.getString(INDEX_RECIPE_NAME);
        mRecipeView.setText(recipeName);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}

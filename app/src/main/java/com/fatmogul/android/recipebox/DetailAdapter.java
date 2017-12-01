package com.fatmogul.android.recipebox;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fatmogul.android.recipebox.data.RecipeContract;

/**
 * Created by adam on 12/1/17.
 */

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.DetailViewHolder> {

private Context mContext;
    private Cursor mCursor;

    public DetailAdapter(@NonNull Context context, Cursor cursor){
        mCursor = cursor;
        mContext = context;
    }

    @Override
    public DetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.ingredient_list_view, parent, false);

        view.setFocusable(true);
        return new DetailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DetailViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String ingredient = mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_INGREDIENT));
        int quantityInt = mCursor.getInt(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_QUANTITY));
        String quantity = String.valueOf(quantityInt);
        String units = mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_UNITS));
        holder.ingredientNameTextView.setText(ingredient);
        holder.quantityTextView.setText(quantity);
        holder.unitsTextView.setText(units);
    }




    @Override
    public int getItemCount() {
        if(null == mCursor)return 0;
        return mCursor.getCount();
    }
    public void swapCursor(Cursor newCursor) {
        if(null != mCursor)mCursor.close();
        mCursor = newCursor;
        notifyDataSetChanged();
    }

public class DetailViewHolder extends RecyclerView.ViewHolder  {
    TextView ingredientNameTextView;
    TextView unitsTextView;
    TextView quantityTextView;

    public DetailViewHolder(View itemView) {
        super(itemView);
        ingredientNameTextView = (TextView) itemView.findViewById(R.id.ingedient_detail_view);
        unitsTextView = (TextView) itemView.findViewById(R.id.units_detail_view);
        quantityTextView = (TextView) itemView.findViewById(R.id.quantity_detail_view);
    }


    }
}


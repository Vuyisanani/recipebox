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
 * Created by adam on 11/20/17.
 */

public class BoxviewAdapter extends RecyclerView.Adapter<BoxviewAdapter.RecipeViewHolder> {
    final private RecipeAdapterOnClickHandler mClickHandler;
    private Context mContext;

public interface RecipeAdapterOnClickHandler{
    void onClick(long id);
}
    private Cursor mCursor;

    public BoxviewAdapter(@NonNull Context context, RecipeAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
        mContext = context;
    }

    @Override
    public RecipeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(mContext)
                .inflate(R.layout.recipe_list_view, parent, false);

        view.setFocusable(true);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecipeViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        String name = mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_NAME));
        int recipeIdInt = mCursor.getInt(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_RECIPE_ID));
        String recipeId = String.valueOf(recipeIdInt);
        String totalTime = mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_TOTAL_TIME));
        String servings = mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_SERVES));
        String category = mCursor.getString(mCursor.getColumnIndex(RecipeContract.RecipeEntry.COLUMN_CATEGORY));
        holder.recipeNameTextView.setText(name);
        holder.categoryTextView.setText("Category: " + category);
        holder.totalTimeTextView.setText("Time to Make: " + totalTime);
        holder.servingsTextView.setText("Serves: " + servings);
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

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView recipeNameTextView;
        TextView totalTimeTextView;
        TextView servingsTextView;
        TextView categoryTextView;

        public RecipeViewHolder(View itemView) {
            super(itemView);
            recipeNameTextView = (TextView) itemView.findViewById(R.id.recipe_name_text_view);
            totalTimeTextView = (TextView) itemView.findViewById(R.id.total_time_text_view);
            servingsTextView = (TextView) itemView.findViewById(R.id.servings_text_view);
            categoryTextView = (TextView) itemView.findViewById(R.id.category_text_view);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            long id = mCursor.getLong(BoxviewActivity.INDEX_RECIPE_ID);
            mClickHandler.onClick(id);
        }
    }
}

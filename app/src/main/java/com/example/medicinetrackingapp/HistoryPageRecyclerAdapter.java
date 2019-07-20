package com.example.medicinetrackingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HistoryPageRecyclerAdapter extends RecyclerView.Adapter<HistoryPageRecyclerAdapter.ViewHolder> {
    private IndividualMedicine[] mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    HistoryPageRecyclerAdapter(Context context, IndividualMedicine[] data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.history_recycler_view_element, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i("test", Boolean.toString(holder.myNameView == null));
        holder.myDateView.setText(String.format(Locale.getDefault(), "%d/%d", mData[position].takenDateTime.get(Calendar.MONTH),mData[position].takenDateTime.get(Calendar.DAY_OF_MONTH)));
        holder.myNameView.setText(mData[position].name);

        SimpleDateFormat s = new SimpleDateFormat("h:mm aa", Locale.getDefault());

        holder.myTimeView.setText(s.format(mData[position].takenDateTime.getTime()));
        holder.myQuantityView.setText(Long.toString(mData[position].quantity));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView myNameView;
        private TextView myTimeView;
        private TextView myQuantityView;
        private TextView myDateView;

        ViewHolder(View itemView) {
            super(itemView);
            Log.i("test", "running in viewholder");
            myNameView = itemView.findViewById(R.id.recycler_nameview);
            myTimeView = itemView.findViewById(R.id.recycler_timeview);
            myQuantityView = itemView.findViewById(R.id.recycler_quantityview);
            myDateView = itemView.findViewById(R.id.recycler_dateview);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    // convenience method for getting data at click position
    IndividualMedicine getItem(int id) {
        return mData[id];
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}

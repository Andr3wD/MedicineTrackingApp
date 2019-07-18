package com.example.medicinetrackingapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Calendar;

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
        holder.myDateView.setText(mData[position].takenDateTime.get(Calendar.MONTH) + "/" + mData[position].takenDateTime.get(Calendar.DAY_OF_MONTH));
        holder.myNameView.setText(mData[position].name); //TODO remove ""
        String ampm = "";
        if (mData[position].takenDateTime.get(Calendar.AM_PM) == 1) {
            ampm = "pm";
        } else {
            ampm = "am";
        }
        int hour = 0;
        if (mData[position].takenDateTime.get(Calendar.HOUR) == 0) {
            hour = 12;
        } else {
            hour = mData[position].takenDateTime.get(Calendar.HOUR);
        }
        holder.myTimeView.setText(hour + ":" + mData[position].takenDateTime.get(Calendar.MINUTE) + ampm); //TODO fix am/pm not showing
        holder.myQuantityView.setText(Long.toString(mData[position].quantity));
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView myNameView;
        public TextView myTimeView;
        public TextView myQuantityView;
        public TextView myDateView;

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

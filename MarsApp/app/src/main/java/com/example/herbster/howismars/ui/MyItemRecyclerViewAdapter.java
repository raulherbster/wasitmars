package com.example.herbster.howismars.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.herbster.howismars.R;
import com.example.herbster.howismars.json.MarsJSONParser;
import com.example.herbster.howismars.model.SingleMarsReport;
import com.example.herbster.howismars.ui.ItemFragment.OnListFragmentInteractionListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final List<SingleMarsReport> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyItemRecyclerViewAdapter(List<SingleMarsReport> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.archive_fragment_item, parent, false);
        return new ViewHolder(view);
    }

    private static String parseDateToString(Date dateOrig, String formatString) {
        SimpleDateFormat dateformat = new SimpleDateFormat(formatString);
        return dateformat.format(dateOrig);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(parseDateToString(mValues.get(position).getTerrestrialDate(), MarsJSONParser.DATE_FORMAT));
        holder.mContentView.setText(mValues.get(position).getSeason());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public SingleMarsReport mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (TextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

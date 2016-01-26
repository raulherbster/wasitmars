package com.example.herbster.howismars.ui;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.herbster.howismars.R;
import com.example.herbster.howismars.json.MarsJSONParser;
import com.example.herbster.howismars.model.SingleMarsWeatherReport;
import com.example.herbster.howismars.ui.MarsWeatherArchiveFragment.OnListFragmentInteractionListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 */
public class MarsItemRecyclerViewAdapter extends RecyclerView.Adapter<MarsItemRecyclerViewAdapter.ViewHolder> {

    private final List<SingleMarsWeatherReport> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MarsItemRecyclerViewAdapter(List<SingleMarsWeatherReport> items, OnListFragmentInteractionListener listener) {
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
        if (dateOrig == null)
            return "";
        SimpleDateFormat dateformat = new SimpleDateFormat(formatString);
        return dateformat.format(dateOrig);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        String terrestrialDateString = parseDateToString(mValues.get(position).getTerrestrialDate(), MarsJSONParser.DATE_FORMAT);
        holder.mTerrestrialDate.setText(terrestrialDateString);
        String tempString = "Max: " + mValues.get(position).getMinTemp() + " C Min: " + mValues.get(position).getMaxTemp() + " C";
        holder.mTemp.setText(tempString);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
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
        public final TextView mTerrestrialDate;
        public final TextView mTemp;
        public SingleMarsWeatherReport mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTerrestrialDate = (TextView) view.findViewById(R.id.itemTerrestrialDate);
            mTemp = (TextView) view.findViewById(R.id.itemTemp);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTerrestrialDate.getText() + "'" ;
        }
    }
}

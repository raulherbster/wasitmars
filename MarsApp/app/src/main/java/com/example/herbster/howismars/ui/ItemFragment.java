package com.example.herbster.howismars.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.herbster.howismars.R;
import com.example.herbster.howismars.communication.RequestResponse;
import com.example.herbster.howismars.communication.ServiceRequest;
import com.example.herbster.howismars.json.MarsJSONParser;
import com.example.herbster.howismars.model.MarsArchive;
import com.example.herbster.howismars.model.SingleMarsReport;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ItemFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters

    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    private ProgressDialog mProgressDialog;
    private boolean archiveLoaded = false;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ItemFragment newInstance() {
        ItemFragment fragment = new ItemFragment();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.archive_fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
        }
        return view;
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            if (!archiveLoaded) {
                FetchArchiveDataTask fetchDataTask = new FetchArchiveDataTask();
                String url = getActivity().getResources().getString(R.string.uri_archive_service);
                fetchDataTask.execute(url);
            }
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mProgressDialog = new ProgressDialog(getContext());
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(SingleMarsReport item);
    }


    class FetchArchiveDataTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... params) {
            if (params == null || params.length == 0)
                return "";

            ServiceRequest request = ServiceRequest.create(params[0]);
            if (request != null) {
                RequestResponse response = request.doGet();

                if (response != null && !response.isError()) {
                    return response.getContent();
                }  else
                    return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String string) {
            InputStream stream = new ByteArrayInputStream(string.getBytes());
            ParseRemoteDataTask parseRemoteData = new ParseRemoteDataTask();
            parseRemoteData.execute(stream);
        }
    }

    class ParseRemoteDataTask extends AsyncTask<InputStream,Void,MarsArchive> {

        @Override
        protected void onPreExecute() {
            mProgressDialog.setMessage("Fetching archive for Mars weather...");
            mProgressDialog.show();
        }

        @Override
        protected MarsArchive doInBackground(InputStream... params) {
            if (params == null || params.length == 0)
                return null;

            MarsArchive marsReport = MarsJSONParser.getInstance().parseArchiveResponse(params[0]);
            int inea = marsReport.getNumberReports();
            ServiceRequest request = ServiceRequest.create("oi");

            return marsReport;
        }

        @Override
        protected void onPostExecute(MarsArchive archive) {
            if (mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
            archiveLoaded = true;
            updateArchiveList(archive);
        }
    }

    private void updateArchiveList(MarsArchive archive) {
        recyclerView.setAdapter(new MyItemRecyclerViewAdapter(archive.getAsList(), mListener));
    }
}

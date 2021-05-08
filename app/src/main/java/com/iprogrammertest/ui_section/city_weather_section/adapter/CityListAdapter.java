package com.iprogrammertest.ui_section.city_weather_section.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.iprogrammertest.R;
import com.iprogrammertest.db_section.AppDatabase;
import com.iprogrammertest.ui_section.city_weather_section.model.Main;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CityListAdapter extends ArrayAdapter {
    private List<Main> mDataList;
    private int mItemLayout;
    private AppDatabase mAppDb;
    private CityListAdapter.ListFilter listFilter = new CityListAdapter.ListFilter();

    public CityListAdapter(Context context, int resource, List<Main> storeDataLst, AppDatabase appDb) {
        super(context, resource, storeDataLst);
        mDataList = storeDataLst;
        mItemLayout = resource;
        mAppDb=appDb;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Main getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public View getView(int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(mItemLayout, parent, false);
        }

        TextView strName = view.findViewById(R.id.tv_suggest_city_name);
        strName.setText(getItem(position).getCityName());

        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return listFilter;
    }

    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = new ArrayList<String>();
                    results.count = 0;
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                //Call to database to get matching records using room
                List<Main> matchValues = mAppDb.weatherDao().getCitiesForAutoPopulate(searchStrLowerCase+"%",new Date(System.currentTimeMillis() - 600 * 1000));

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                mDataList = (ArrayList<Main>)results.values;
            } else {
                mDataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}

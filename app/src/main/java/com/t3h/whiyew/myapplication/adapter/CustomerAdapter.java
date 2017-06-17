package com.t3h.whiyew.myapplication.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.t3h.whiyew.myapplication.R;
import com.t3h.whiyew.myapplication.model.Member;

import java.util.ArrayList;


public class CustomerAdapter extends ArrayAdapter<Member> {
    ArrayList<Member> customers, tempCustomer, suggestions;

    public CustomerAdapter(Context context, ArrayList<Member> objects) {
        super(context, R.layout.itemmember, objects);
        this.customers = objects;
        this.tempCustomer = new ArrayList<>();
        this.suggestions = new ArrayList<>();

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private View initView(int position, View convertView, ViewGroup parent) {
        Member customer = getItem(position);
        if (convertView == null) {
            if (parent == null)
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.itemmember, null);
            else
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.itemmember, parent, false);
        }
        TextView txtCustomer = (TextView) convertView.findViewById(R.id.name);
        if (txtCustomer != null)
            txtCustomer.setText(customer.getName() + " " + customer.getGmail());

        return convertView;
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }
    Filter myFilter =new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            Member customer =(Member)resultValue ;
            return customer.getName() + " " + customer.getGmail();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestions.clear();
                for (Member cust : tempCustomer) {
                    if (cust.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(cust);
                    }
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            ArrayList<Member> c =  (ArrayList<Member> )results.values ;
            if (results != null && results.count > 0) {
                clear();
                for (Member cust : c) {
                    add(cust);
                    notifyDataSetChanged();
                }
            }
            else{
                clear();
                notifyDataSetChanged();
            }
        }
    };
}

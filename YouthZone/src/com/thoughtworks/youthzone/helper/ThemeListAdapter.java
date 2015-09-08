package com.thoughtworks.youthzone.helper;

import java.util.List;

import com.thoughtworks.youthzone.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ThemeListAdapter extends ArrayAdapter<ThemeData> {

	public ThemeListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ThemeListAdapter(Context context, int resource, List<ThemeData> themeData) {
        super(context, resource, themeData);
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View view = convertView;

        if (view == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            view = vi.inflate(R.layout.onside_list_item, parent, false);
        }

        ThemeData themeData = getItem(position);

        if (themeData != null) {
        	TextView textView = (TextView) view.findViewById(R.id.label);
            textView.setText(themeData.getName());
            if (themeData.isComplete()) {
            	textView.setBackgroundColor(0x707bc143);
            }
        }

        return view;
	}
}

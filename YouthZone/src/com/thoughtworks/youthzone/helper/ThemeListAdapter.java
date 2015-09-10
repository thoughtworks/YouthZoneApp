package com.thoughtworks.youthzone.helper;

import java.util.List;

import com.thoughtworks.youthzone.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
        	textView.setCompoundDrawablePadding(30);
        	Drawable incomplete = parent.getResources().getDrawable(R.drawable.blank_mark);
        	Bitmap incompleteBmp = ((BitmapDrawable) incomplete).getBitmap();
        	// Scale it to 50 x 50
        	incomplete = new BitmapDrawable(parent.getResources(), Bitmap.createScaledBitmap(incompleteBmp, 50, 50, true));
        	textView.setCompoundDrawablesWithIntrinsicBounds(incomplete, null, null, null);
        	
            textView.setText(themeData.getName());
            
            if (themeData.isComplete()) {
            	textView.setBackgroundColor(0x707bc143);
            	Drawable complete = parent.getResources().getDrawable(R.drawable.check_mark);
            	Bitmap completeBmp = ((BitmapDrawable) complete).getBitmap();
            	// Scale it to 50 x 50
            	complete = new BitmapDrawable(parent.getResources(), Bitmap.createScaledBitmap(completeBmp, 50, 50, true));
            	textView.setCompoundDrawablesWithIntrinsicBounds(complete, null, null, null);
            }
        }

        return view;
	}
}

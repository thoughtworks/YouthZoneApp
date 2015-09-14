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

public class ThemeListCompleteAdapter extends ArrayAdapter<ThemeData> {
	
	private int resource;

	public ThemeListCompleteAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId);
	}

	public ThemeListCompleteAdapter(Context context, int resource, List<ThemeData> themeData) {
		super(context, resource, themeData);
		this.resource = resource;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;

		if (view == null) {
			LayoutInflater vi;
			vi = LayoutInflater.from(getContext());
			view = vi.inflate(resource, parent, false);
		}

		ThemeData themeData = getItem(position);
		float sum = 0.0f;
		float noOfQuestions = 0;

		for (QuestionData question : themeData.getQuestions()) {
			if (question.getRating() > 0.0f) {
				noOfQuestions++;
				sum += question.getRating();
			}
		}

		if (themeData != null) {
			TextView textViewTitle = (TextView) view.findViewById(R.id.label_title);
			textViewTitle.setCompoundDrawablePadding(30);

			textViewTitle.setCompoundDrawablesWithIntrinsicBounds(getListIcon(view, R.drawable.blank_mark), null, null,
					null);
			textViewTitle.setText(themeData.getName());

			if (themeData.isComplete()) {
				textViewTitle.setBackgroundColor(0x707bc143);
				textViewTitle.setCompoundDrawablesWithIntrinsicBounds(getListIcon(view, R.drawable.check_mark), null, null,
						null);
			}
			
			TextView textViewRating = (TextView) view.findViewById(R.id.label_rating);
			if(noOfQuestions == 0){
				textViewRating.setText(String.format("N/A"));
			} else {
				textViewRating.setText(String.format("%.1f", sum / noOfQuestions));
			}
		}

		return view;
	}

	private Drawable getListIcon(View view, int resourceId) {
		Drawable incomplete = view.getResources().getDrawable(resourceId);
		Bitmap incompleteBmp = ((BitmapDrawable) incomplete).getBitmap();
		// Scale it to 50 x 50
		return new BitmapDrawable(view.getResources(), Bitmap.createScaledBitmap(incompleteBmp, 50, 50, true));
	}
}

package com.goyalgadgets.hackathon;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class TripsAdapter extends ArrayAdapter<Trips> {

	Context context;
	int layoutResourceId;
	Trips data[] = null;

	public TripsAdapter(Context context, int layoutResourceId, Trips[] data) {
		super(context, layoutResourceId, data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		TripsHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new TripsHolder();
			holder.txtTitle = (TextView) row.findViewById(R.id.line_a);
			holder.txtDateTime = (TextView) row.findViewById (R.id.line_b);
			holder.txtEndAddress = (TextView) row.findViewById(R.id.line_c);

			row.setTag(holder);
		} else {
			holder = (TripsHolder) row.getTag();
		}

		Trips trip = data[position];
		holder.txtTitle.setText(trip.title);
		holder.txtDateTime.setText(trip.dateTime);
		holder.txtEndAddress.setText(trip.endAddress);

		return row;
	}

	static class TripsHolder {
		TextView txtTitle;
		TextView txtDateTime;
		TextView txtEndAddress;
	}
}
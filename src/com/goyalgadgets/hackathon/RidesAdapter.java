package com.goyalgadgets.hackathon;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RidesAdapter extends ArrayAdapter<Rides> {

	Context context;
	int layoutResourceId;
	Rides data[] = null;

	public RidesAdapter(Context context, int layoutResourceId, Rides[] rides_data) {
		super(context, layoutResourceId, rides_data);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.data = rides_data;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RidesHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new RidesHolder();
			holder.txtTitle = (TextView) row.findViewById(R.id.line_a);
			holder.txtDateTime = (TextView) row.findViewById (R.id.line_b);
			holder.txtEndAddress = (TextView) row.findViewById(R.id.line_c);

			row.setTag(holder);
		} else {
			holder = (RidesHolder) row.getTag();
		}

		Rides ride = data[position];
		holder.txtTitle.setText(ride.title);
		holder.txtDateTime.setText(ride.dateTime);
		holder.txtEndAddress.setText(ride.endAddress);

		return row;
	}

	static class RidesHolder {
		TextView txtTitle;
		TextView txtDateTime;
		TextView txtEndAddress;
	}
}
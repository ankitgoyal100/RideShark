package com.goyalgadgets.hackathon;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class DisplayTripsActivity extends Activity {
	private Context context = this;
	private ListView listView1;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.display_trips);

		handler = new Handler();

		Intent intent = getIntent();
		String json = intent.getStringExtra("json");

		ArrayList<Rides> rides_data = new ArrayList<Rides>();

		try {
			JSONObject json1 = new JSONObject(json);
			JSONArray data = json1.getJSONArray("data");
			System.out.println(data);
			for (int i = 0; i < data.length(); i++) {
				System.out.println(data.get(i));
				JSONObject temp = new JSONObject(data.getString(i).toString());
				JSONArray data2 = temp.getJSONArray("to");
				JSONObject jsonAddr = new JSONObject(data2.get(0).toString());
				if (temp.getString("driver") != null)
					rides_data.add(new Rides(temp.getString("driver"), jsonAddr.getString("addr"), temp.getString("date")));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		rides_data.add(new Rides(("Ankit Goyal"), "1193 Lees Meadow Court", "January 2nd, 2014"));

		// Parse the JSON

		Rides[] tempRides = new Rides[rides_data.size()];
		for(int i = 0; i < rides_data.size(); i++)
			tempRides[i] = rides_data.get(i);
		RidesAdapter adapter = new RidesAdapter(this, R.layout.rides_item_row, tempRides);

		listView1 = (ListView) findViewById(R.id.listView1);
		listView1.setAdapter(adapter);

		listView1.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
				builder.setTitle("Join Ankit Goyal's Carpool").setMessage("Are you sure you want to this trip by Ankit Goyal? You will be emailed if your request is accepted.")
						.setPositiveButton("Join", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// JOIN TRIP HERE
								dialog.cancel();
								handler.post(new Runnable() {
									@Override
									public void run() {
										Toast.makeText(context, "Your request has been sent to Ankit", Toast.LENGTH_LONG).show();
									}
								});
								Intent makeJoin = new Intent(DisplayTripsActivity.this, MakeJoinActivity.class);
								startActivity(makeJoin);
							}
						}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						}).show();
			}
		});

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.logout:
			new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					HttpClient httpclient = new DefaultHttpClient();
					try {
						HttpResponse response = httpclient.execute(new HttpPost("http://www.abigopal.com/mobile/logout"));
						HttpResponse confirmResponse = httpclient.execute(new HttpPost("http://www.abigopal.com/confirm"));
						HttpEntity entity = confirmResponse.getEntity();
						String responseString = EntityUtils.toString(entity, "UTF-8");
						System.out.println(responseString);
						try {
							JSONObject json = new JSONObject(responseString);
							JSONObject json2 = new JSONObject(json.getString("cookie"));
							if (json2.has("email") == false) {
								Intent logout = new Intent(DisplayTripsActivity.this, LoginActivity.class);
								startActivity(logout);
							}
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}).start();
			return true;
		case R.id.trip_info:
			Intent myTrips = new Intent(DisplayTripsActivity.this, MyTripsActivity.class);
			startActivity(myTrips);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}

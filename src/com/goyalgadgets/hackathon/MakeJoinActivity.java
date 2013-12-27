package com.goyalgadgets.hackathon;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MakeJoinActivity extends Activity {
	private Button btnMakeTrip;
	private Button btnJoinTrip;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.make_join);

		btnMakeTrip = (Button) findViewById(R.id.btnMakeTrip);
		btnMakeTrip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent makeTrip = new Intent(MakeJoinActivity.this, MakeTripActivity.class);
				startActivity(makeTrip);
			}

		});

		btnJoinTrip = (Button) findViewById(R.id.btnJoinTrip);
		btnJoinTrip.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent joinTrip = new Intent(MakeJoinActivity.this, JoinTripActivity.class);
				startActivity(joinTrip);
			}

		});
	}

	@Override
	public void onBackPressed() {
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
							Intent logout = new Intent(MakeJoinActivity.this, LoginActivity.class);
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
		super.onBackPressed();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
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
								Intent logout = new Intent(MakeJoinActivity.this, LoginActivity.class);
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
			Intent myTrips = new Intent(MakeJoinActivity.this, MyTripsActivity.class);
			startActivity(myTrips);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// @Override
	// public void onStop() {
	// Intent logout = new Intent(MakeJoinActivity.this, MyTripsActivity.class);
	// startActivity(logout);
	// super.onStop();
	// }
}

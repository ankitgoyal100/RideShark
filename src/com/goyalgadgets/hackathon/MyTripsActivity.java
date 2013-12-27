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

public class MyTripsActivity extends Activity {
	private Context context = this;
	private ListView listView1;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_trips);

		handler = new Handler();

		Intent intent = getIntent();
		String json = intent.getStringExtra("json");
		// Parse the JSON

		Trips trips_data[] = new Trips[] { new Trips(com.goyalgadgets.hackathon.LoginActivity.email, "11:42", "1193 Lees Meadow CT, Great Falls, VA 22066"),
				new Trips(com.goyalgadgets.hackathon.LoginActivity.email, "17:04", "1193 Lees Meadow CT, Great Falls, VA 22066"),
				new Trips(com.goyalgadgets.hackathon.LoginActivity.email, "21:36", "1000 Lombard ST, San Francisco, CA 94133") };

		TripsAdapter adapter = new TripsAdapter(this, R.layout.trips_item_row, trips_data);

		listView1 = (ListView) findViewById(R.id.listView1);
		listView1.setAdapter(adapter);

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
								Intent logout = new Intent(MyTripsActivity.this, LoginActivity.class);
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

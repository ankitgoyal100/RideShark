package com.goyalgadgets.hackathon;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MakeTripActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
	private Context context = this;
	private Button btn;
	private SharedPreferences prefs;
	private com.goyalgadgets.hackathon.DatePreference datePref;
	private com.goyalgadgets.hackathon.NumberPickerDialogPreference numberPickerPref;
	private EditTextPreference editTextPref;
	private Handler handler;
	private com.goyalgadgets.hackathon.HackathonListPreference collegePref;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.make_trip);
		setContentView(R.layout.main_make);
		handler = new Handler();
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		Editor editor = prefs.edit();
		editor.clear();
		editor.commit();
		System.out.println("here we are");
		collegePref = (com.goyalgadgets.hackathon.HackathonListPreference) findPreference("collegePicker");
		collegePref.setSummary("Pick a college");
		System.out.println(collegePref.getSummary());
		onContentChanged();

		datePref = (com.goyalgadgets.hackathon.DatePreference) findPreference("dob");
		editTextPref = (EditTextPreference) findPreference("prefEndAddress");
		editTextPref.setText("");
		numberPickerPref = (com.goyalgadgets.hackathon.NumberPickerDialogPreference) findPreference("numberPicker");

		btn = (Button) findViewById(R.id.doneButton);
		btn.setBackground(getResources().getDrawable(R.drawable.login_button_background));
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println(datePref.getSummary());
				System.out.println(numberPickerPref.getSummary());
				System.out.println(prefs.getString("prefsTimePicker", ""));
				System.out.println(prefs.getString("collegePicker", "-1"));
				System.out.println(prefs.getString("prefEndAddress", ""));

				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						// ////////
						// Create a new HttpClient and Post Header
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost("http://www.abigopal.com/mobile/maketrip");

						try {
							// Add your data
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
							nameValuePairs.add(new BasicNameValuePair("email", com.goyalgadgets.hackathon.LoginActivity.email));
							nameValuePairs.add(new BasicNameValuePair("capacity", numberPickerPref.getSummary() + ""));
							System.out.println(prefs.getString("collegePicker", "-1"));
							if (prefs.getString("collegePicker", "-1").equals("1")) {
								System.out.println("UVA");
								nameValuePairs.add(new BasicNameValuePair("college", "uva.edu"));
							}
							if (prefs.getString("collegePicker", "-1").equals("2")) {
								System.out.println("VT");
								nameValuePairs.add(new BasicNameValuePair("college", "vt.edu"));
							}
							if (prefs.getString("collegePicker", "-1").equals("3")) {
								System.out.println("VCU");
								nameValuePairs.add(new BasicNameValuePair("college", "vcu.edu"));
							}
							nameValuePairs.add(new BasicNameValuePair("time", datePref.getSummary() + " " + prefs.getString("prefsTimePicker", "")));

							httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

							// Execute HTTP Post Request
							HttpResponse response = httpclient.execute(httppost);
							HttpEntity entity = response.getEntity();
							String responseString = EntityUtils.toString(entity, "UTF-8");
							System.out.println(responseString);

						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
						} catch (IOException e) {
							// TODO Auto-generated catch block
						}

						// /////////
					}

				}).start();

				handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(context, "Thanks for your entry!", Toast.LENGTH_LONG).show();
					}
				});

				Intent makeJoin = new Intent(MakeTripActivity.this, MakeJoinActivity.class);
				startActivity(makeJoin);
			}

		});

	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		// if (findPreference(key) instanceof HackathonListPreference) {
		// Preference listP = findPreference("collegePicker");
		// HackathonListPreference etp = (HackathonListPreference)
		// findPreference("collegePicker");
		// System.out.println("Preferences channged");
		// }
		Preference pref = findPreference("prefEndAddress");
		if (pref instanceof EditTextPreference) {
			EditTextPreference etp = (EditTextPreference) pref;
			if (!etp.getText().equals(""))
				pref.setSummary(etp.getText());
		}
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
								Intent logout = new Intent(MakeTripActivity.this, LoginActivity.class);
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
			Intent myTrips = new Intent(MakeTripActivity.this, MyTripsActivity.class);
			startActivity(myTrips);
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onPause() {
		Editor editor = prefs.edit();
		getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
		editor.clear();
		editor.commit();
		super.onStop();
	}

	protected void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
		builder.setTitle("Confirm").setMessage("Are you sure you want to go back? All your current progress will be lost.").setPositiveButton("Leave", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				MakeTripActivity.super.onBackPressed();

			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.cancel();
			}
		}).show();
	}
}

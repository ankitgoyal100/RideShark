package com.goyalgadgets.hackathon;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.CookieManager;
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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

public class LoginActivity extends Activity {

	private ImageButton info;
	private Button login_button;
	private EditText username;
	private EditText password;
	private Handler handler;
	private Context context;
	public static String email;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		handler = new Handler();
		context = this;

		username = (EditText) findViewById(R.id.login_username);
		password = (EditText) findViewById(R.id.login_password);

		CookieManager cookieManager = new CookieManager();
		CookieHandler.setDefault(cookieManager);

		info = (ImageButton) findViewById(R.id.login_help_center_landscape);
		info.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Use the Builder class for convenient dialog construction
				AlertDialog.Builder builder = new AlertDialog.Builder(context, AlertDialog.THEME_HOLO_LIGHT);
				builder.setTitle("Information")
						.setMessage(
								"Welcome to RideShark! Coordinating carpools with other college students has never been easier! Now with this optimized user interface, it's easier than ever to pick the most convenient ride. \n\nCredit to this idea goes to Taylor Yohe. ")
						.setPositiveButton("Done", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}
						}).show();
				// Create the AlertDialog object and return it
			}

		});

		login_button = (Button) findViewById(R.id.login_login);
		login_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent makeJoin = new Intent(LoginActivity.this, MakeJoinActivity.class);
				startActivity(makeJoin);

				// Create a new HttpClient and Post Header
				new Thread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						HttpClient httpclient = new DefaultHttpClient();
						HttpPost httppost = new HttpPost("http://www.abigopal.com/mobile/login");

						try {
							// Add your data
							email = username.getText().toString();
							List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
							nameValuePairs.add(new BasicNameValuePair("email", username.getText().toString()));
							nameValuePairs.add(new BasicNameValuePair("password", password.getText().toString()));
							httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

							// Execute HTTP Post Request
							HttpResponse response = httpclient.execute(httppost);
							HttpEntity entity = response.getEntity();
							String responseString = EntityUtils.toString(entity, "UTF-8");
							System.out.println(responseString);

							try {
								JSONObject json = new JSONObject(responseString);
								String success = json.getString("success");
								System.out.println(success);
								if (success.equals("true")) {
									HttpResponse confirmResponse = httpclient.execute(new HttpPost("http://www.abigopal.com/confirm"));
									HttpEntity entityConfirm = confirmResponse.getEntity();
									String responseStringConfirm = EntityUtils.toString(entityConfirm, "UTF-8");
									System.out.println(responseStringConfirm);
									Intent makeJoin = new Intent(LoginActivity.this, MakeJoinActivity.class);
									startActivity(makeJoin);
								} else if (success.equals("false")) {
									handler.post(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(context, "Invalid username of password!", Toast.LENGTH_LONG).show();
										}
									});
								} else {
									handler.post(new Runnable() {
										@Override
										public void run() {
											Toast.makeText(context, "Internal server error", Toast.LENGTH_LONG).show();
										}
									});
								}
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} catch (ClientProtocolException e) {
							// TODO Auto-generated catch block
						} catch (IOException e) {
							// TODO Auto-generated catch block
						}
					}

				}).start();
			}

		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}

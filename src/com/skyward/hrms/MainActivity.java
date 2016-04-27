package com.skyward.hrms;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.skyward.utility.NetworkInformer;
import com.skyward.utility.Utility;

public class MainActivity extends Activity implements OnClickListener {
	Button btnSubmit;
	TextView tvChannel;
	EditText userNameTxt, passwordText;
	int count=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		init();
	}
@Override
public void onBackPressed() {
	// TODO Auto-generated method stub


	System.exit(0);

	super.onBackPressed();
}
	private void init() {
		userNameTxt = (EditText) findViewById(R.id.userNameTxt);
		passwordText = (EditText) findViewById(R.id.passwordTxt);
		btnSubmit = (Button) findViewById(R.id.loginsubmit);
		btnSubmit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				boolean isError = false;
				String errorMsg = "";
				if (userNameTxt.getText().toString().trim().length() == 0) {
					isError = true;
					errorMsg = "Please enter Username";
				} else if (passwordText.getText().toString().trim().length() == 0) {
					isError = true;
					errorMsg = "Please enter Password";

				}
				if (isError) {
					Toast.makeText(MainActivity.this, errorMsg,
							Toast.LENGTH_LONG).show();
				} else {
					login();

				}
			}
		});
	}

	protected void login() {
		if (NetworkInformer.isNetworkConnected(getBaseContext())) {
			new FetchDataTask().execute();
		} else {
			Toast.makeText(getBaseContext(), "Check your Internet Connection",
					Toast.LENGTH_LONG).show();

		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.loginsubmit:

			Intent i = new Intent(MainActivity.this, HomeActivity.class);
			startActivity(i);

			break;

		default:
			break;
		}
	}

	private class FetchDataTask extends AsyncTask<Void, Void, SoapObject> {
		private ProgressDialog progress;
		SoapObject result;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(MainActivity.this);
			progress.setMessage("Please wait...");
			progress.setCancelable(false);
			progress.show();
		}

		@Override
		protected SoapObject doInBackground(Void... arg0) {
			TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
			String machineCode = mngr.getDeviceId();
			SoapObject request = new SoapObject(Utility.NAMESPACE,
					Utility.METHOD_LOGIN);
			request.addProperty("Username", userNameTxt.getText().toString());
			request.addProperty("Password", passwordText.getText().toString());
			request.addProperty("MachineCode", Secure.getString(
					MainActivity.this.getContentResolver(), Secure.ANDROID_ID));
			SoapSerializationEnvelope mySoapEnvelop = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			mySoapEnvelop.dotNet = true;
			mySoapEnvelop.setOutputSoapObject(request);
			HttpTransportSE myAndroidHttpTransport = null;
			System.out.println(Utility.URL);
			try {
				try {
					myAndroidHttpTransport = new HttpTransportSE(Utility.URL);
					myAndroidHttpTransport.call(Utility.SOAP_ACTION
							+ Utility.METHOD_LOGIN, mySoapEnvelop);
				} catch (XmlPullParserException e) {
					// System.out.println(e.getClass());
					e.printStackTrace();
					// System.out.println("XmlPullParserException 0");
				} catch (SocketTimeoutException e) {
					// System.out.println(e.getClass());
					e.printStackTrace();
					// System.out.println("SocketTimeoutException 1");
				} catch (SocketException e) {
					// System.out.println(e.getClass());
					e.printStackTrace();
					// System.out.println("SocketException  2");
				} catch (IOException e) {
					// System.out.println(e.getClass());
					e.printStackTrace();
					System.out.println("IO Exception 3");
					// return objLoginBean;
				}

				result = (SoapObject) mySoapEnvelop.bodyIn;

				SoapObject soapObject = (SoapObject) result.getProperty(0);
				System.out.println("Result is : " + result.toString());
				boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
						"IsSucceed").toString());
				System.out.println(isLogin);
				Utility.authToken = soapObject.getProperty(
						"AuthenticationToken").toString();
			}

			catch (Exception e) {
				e.printStackTrace();
			}

			return result;
		}

		@Override
		protected void onPostExecute(SoapObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progress.dismiss();
			SoapObject soapObject = (SoapObject) result.getProperty(0);
			System.out.println("Result is : " + result.toString());
			boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
					"IsSucceed").toString());
			System.out.println(isLogin);
			if (isLogin) {
				Utility.authToken = soapObject.getProperty(
						"AuthenticationToken").toString();
				SharedPreferences settingsSave = getSharedPreferences("info",
						MODE_PRIVATE);
				SharedPreferences.Editor editor = settingsSave.edit();
				editor.putString("uname", userNameTxt.getText().toString());
				editor.putString("MachineCode",
						soapObject.getProperty("AuthenticationToken")
								.toString());

				System.out.println("token:" + Utility.authToken);
				// Commit the edits!
				editor.commit();
				Intent i = new Intent(MainActivity.this, HomeActivity.class);
				startActivity(i);
				finish();
			} else {
				Toast.makeText(getBaseContext(),
						soapObject.getProperty("ErrorMessage").toString(),
						Toast.LENGTH_LONG).show();
			}

		}
	}

}

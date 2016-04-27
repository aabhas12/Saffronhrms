package com.skyward.hrms;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.TextView;
import android.widget.Toast;

import com.skyward.utility.NetworkInformer;
import com.skyward.utility.Utility;

public class CustomerListActivity extends Activity {
	ExpandableCustomerListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<Customer>> listDataChild;
	TextView tvErrorMSg;

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (NetworkInformer.isNetworkConnected(getBaseContext())) {
			listDataHeader = new ArrayList<String>();
			listDataChild = new HashMap<String, List<Customer>>();

			getCustomerList();

		} else {
			Toast.makeText(getBaseContext(), "Check your Internet Connection",
					Toast.LENGTH_LONG).show();
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customerlist);
		expListView = (ExpandableListView) findViewById(R.id.lvExpForCustomer);
		tvErrorMSg = (TextView) findViewById(R.id.tvErrorMSg);
		// Listview Group click listener
		expListView.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
				// Toast.makeText(getApplicationContext(),
				// "Group Clicked " + listDataHeader.get(groupPosition),
				// Toast.LENGTH_SHORT).show();
				return false;
			}
		});

		// Listview Group expanded listener
		expListView.setOnGroupExpandListener(new OnGroupExpandListener() {

			@Override
			public void onGroupExpand(int groupPosition) {
			}
		});

		// Listview Group collasped listener
		expListView.setOnGroupCollapseListener(new OnGroupCollapseListener() {

			@Override
			public void onGroupCollapse(int groupPosition) {
			}
		});

		// Listview on child click listener
		expListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub

				return true;
			}
		});
		if (NetworkInformer.isNetworkConnected(getBaseContext())) {
			getCustomerList();

		} else {
			Toast.makeText(getBaseContext(), "Check your Internet Connection",
					Toast.LENGTH_LONG).show();
		}
	}

	private void getCustomerList() {
		new FetchCustomerList().execute();

	}

	private class FetchCustomerList extends AsyncTask<Void, Void, SoapObject> {
		SoapObject result;
		private ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(CustomerListActivity.this);
			progress.setMessage("Please wait...");
			progress.setCancelable(false);
			progress.show();
		}

		@Override
		protected SoapObject doInBackground(Void... arg0) {
			SharedPreferences settings = getSharedPreferences("info",
					MODE_PRIVATE);
			String machineCode = settings.getString("MachineCode", "");
			SoapObject request = new SoapObject(Utility.NAMESPACE,
					Utility.METHOD_GET_CUSTOMER_DETAILS);
			request.addProperty("Token", machineCode);
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
							+ Utility.METHOD_GET_CUSTOMER_DETAILS,
							mySoapEnvelop);
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
			boolean IsSucceed = Boolean.parseBoolean(soapObject.getProperty(
					"IsSucceed").toString());
			System.out.println(IsSucceed);
			if (IsSucceed) {
				SoapObject result1 = (SoapObject) soapObject.getProperty(1);
				System.out.println("Result1 is : " + result1.toString());
				SoapObject result3 = (SoapObject) result1.getProperty(1);
				System.out.println("Result3 is : " + result3.toString());
				SoapObject result4 = (SoapObject) result3.getProperty(0);
				System.out.println("Result4 is : " + result4.toString());
				int count = result4.getPropertyCount();
				System.out.println("Count is : " + count);
				listDataHeader = new ArrayList<String>();
				listDataChild = new HashMap<String, List<Customer>>();

				for (int i = 0; i < count; i++) {
					SoapObject soapResult = null;
					soapResult = (SoapObject) result4.getProperty(i);
					listDataHeader
							.add(soapResult.getProperty("CustomerName")
											.toString());
					//soapResult.getProperty("CustomerCode")
					//.toString()
					//+/ " - "
				//	+ 
					String dateStr = "04/05/2010";

					SimpleDateFormat curFormater = new SimpleDateFormat(
							"dd/MM/yyyy");
					Date dateObj = null;
					try {
						dateObj = curFormater.parse(dateStr);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Customer customer = new Customer(soapResult.getProperty(
							"CustomerName").toString(),soapResult.getProperty("CustomerCode").toString(),Integer.parseInt(soapResult.getProperty(
									"CustomerID").toString()),
							 soapResult
									.getProperty("PANNo").toString(),
							soapResult.getProperty("PassportNo").toString(),
							soapResult.getProperty("ClinicAddress").toString(),
							soapResult.getProperty("MobileNo").toString(),
							soapResult.getProperty("EmailID").toString(),
							soapResult.getProperty("Patients").toString(),
							soapResult.getProperty("AnniversaryDate")
									.toString(), soapResult.getProperty(
									"DateOfBirth").toString());
//Integer.parseInt(soapResult.getProperty(
				//"CustomerID").toString())
					ArrayList<Customer> attendenceList = new ArrayList<Customer>();
					attendenceList.add(customer);
					listDataChild.put(listDataHeader.get(i), attendenceList);
				}
				listAdapter = new ExpandableCustomerListAdapter(
						CustomerListActivity.this, listDataHeader,
						listDataChild);

				// setting list adapter
				expListView.setAdapter(listAdapter);
				tvErrorMSg.setVisibility(View.GONE);
			} else {
				Toast.makeText(getBaseContext(),
						soapObject.getProperty("ErrorMessage").toString(),
						Toast.LENGTH_LONG).show();
				tvErrorMSg.setVisibility(View.VISIBLE);
			}

		}
	}

}

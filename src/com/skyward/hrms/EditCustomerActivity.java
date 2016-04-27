package com.skyward.hrms;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.kobjects.util.Csv;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.skyward.hrms.DailyAttendanceActivity.PickDate;
import com.skyward.utility.NetworkInformer;
import com.skyward.utility.Utility;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditCustomerActivity extends Activity implements
		android.view.View.OnClickListener {
	EditText mobileNo, emailID, patients, anniverseryDate, dob, temp;
	TextView tvSpeciality, tvQualification,doctorname;
	boolean[] isSelectedArray = { true, false, true, true };
	CharSequence[] items = { "1", "2", "3", "4" };
	protected CharSequence[] colours = { "Red", "Green", "Blue", "Yellow",
			"Orange", "Purple" };
	String s = new String();
	String s1 = new String();
	Button btnSubmit;
	int count1 = 0;
	int count2 = 0;
	public final Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[a-zA-Z0-9+._%-+]{1,256}" + "@"
					+ "[a-zA-Z0-9][a-zA-Z0-9-]" + "(" + "."
					+ "[a-zA-Z0-9][a-zA-Z0-9-]" + ")+");
	private boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}
	ArrayList<String> store = new ArrayList<String>();
	ArrayList<String> store1 = new ArrayList<String>();
	ArrayList<String> specialityName = new ArrayList<String>();
	ArrayList<Integer> specialityID = new ArrayList<Integer>();
	private int customerID;
	ArrayList<Boolean> specialityCheck = new ArrayList<Boolean>();
	protected ArrayList<Integer> selectedspecialities = new ArrayList<Integer>();
	ArrayList<String> qualificationName = new ArrayList<String>();
	ArrayList<Integer> qualificationID = new ArrayList<Integer>();
	ArrayList<Boolean> qualificationCheck = new ArrayList<Boolean>();
	protected ArrayList<Integer> selectedqualification = new ArrayList<Integer>();
	SharedPreferences settings;
	String machineCode;
	private String initialDate;
	private String initialMonth;
	private String initialYear;
	Calendar c;
	private DatePickerDialog dialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_customer);
		settings = getSharedPreferences("info", MODE_PRIVATE);
		machineCode = settings.getString("MachineCode", "");

		Intent i = getIntent();
		String name = i.getStringExtra("name");
		customerID = i.getIntExtra("customerID", 0);
	
		doctorname= (TextView)findViewById(R.id.namedoc);
		
		mobileNo = (EditText) findViewById(R.id.etMobile);
		emailID = (EditText) findViewById(R.id.etEmail);
		patients = (EditText) findViewById(R.id.etPatients);
		anniverseryDate = (EditText) findViewById(R.id.etAnniverseryDate);
		dob = (EditText) findViewById(R.id.etDOb);
		if(i.getStringExtra("mobile").equals("anyType{}"))
		{
		mobileNo.setText(i.getStringExtra(""));
		}
		else{
			mobileNo.setText(i.getStringExtra("mobile"));
		}if(i.getStringExtra("email").equals("anyType{}"))
		{
			emailID.setText(i.getStringExtra(""));
		}else
		{
			emailID.setText(i.getStringExtra("email"));
		}
		if(i.getStringExtra("patients").equals("anyType{}"))
		{	
		patients.setText(i.getStringExtra(""));
		}
		else
		{
			patients.setText(i.getStringExtra("patients"));
		}	
			if(i.getStringExtra("anniversery").equals("anyType{}"))
		
		{
		anniverseryDate.setText("");
		}
		else
		{
			anniverseryDate.setText(i.getStringExtra("anniversery"));
		}
		if(i.getStringExtra("dob").equals("anyType{}"))
		{
		dob.setText("");
		}
		else
			{
			dob.setText(i.getStringExtra("dob"));
			}
		
			tvSpeciality = (TextView) findViewById(R.id.tvSpeciality);
			
		tvQualification = (TextView) findViewById(R.id.tvQualification);
		tvQualification.setOnClickListener(this);
		tvSpeciality.setOnClickListener(this);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		c = Calendar.getInstance();
		btnSubmit.setOnClickListener(this);
		dob.setOnClickListener(this);
		anniverseryDate.setOnClickListener(this);
		if (NetworkInformer.isNetworkConnected(getBaseContext())) {
			new FetchSpeciality().execute();

		} else {
			Toast.makeText(getBaseContext(), "Check your Internet Connection",
					Toast.LENGTH_LONG).show();
		}
		if (NetworkInformer.isNetworkConnected(getBaseContext())) {
			new FetchQualification().execute();
		} else {
			Toast.makeText(getBaseContext(), "Check your Internet Connection",
					Toast.LENGTH_LONG).show();
		}
	}

	private void openDialogforSpeciality() {
		String[] speciality = specialityName.toArray(new String[specialityName
				.size()]);
		int count = speciality.length;
		boolean[] checkedColours = new boolean[specialityCheck.size()];
		count1 = 0;

		for (int i = 0; i < specialityCheck.size(); i++) {
			System.out.println("speciality :" + specialityCheck.size());
			checkedColours[i] = specialityCheck.get(i);

			if (specialityCheck.get(i) == true) {
				count1++;
				store.add(specialityName.get(i));
				System.out.println("initial:" + specialityName.get(i));

			}
		}

		System.out.println("string value:" + s);
		tvSpeciality.setText(s);
		// System.out.println("count:" + count1);
		DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {

				if (isChecked && count1 < 4) {

					selectedspecialities.add(specialityID.get(which));
					ArrayList<String> store = new ArrayList<String>();
					specialityCheck.set(which, true);
					count1 = 0;
					for (int i = 0; i < specialityCheck.size(); i++) {
						if (specialityCheck.get(i) == true) {
							store.add(specialityName.get(i));
							count1++;

						}

					}
				}

				else {
					selectedspecialities.remove(specialityID.get(which));

					specialityCheck.set(which, false);

				}
				count1 = 0;
				// store = null;
				ArrayList<String> store = new ArrayList<String>();
				for (int i = 0; i < specialityCheck.size(); i++) {
					if (specialityCheck.get(i) == true) {
						store.add(specialityName.get(i));
						count1++;

					}

				}
				onChangeSelectedColours();
				s = "";
				for (int i = 0; i < store.size(); i++) {

					s += store.get(i) + ",";
				}
				tvSpeciality.setText(s);
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Select Specialities : ");

		builder.setMultiChoiceItems(speciality, checkedColours,
				coloursDialogListener);

		AlertDialog dialog = builder.create();
		builder.setPositiveButton("Done", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		dialog.show();
	}

	private void openDialogforqualification() {
		String[] qualification = qualificationName
				.toArray(new String[qualificationName.size()]);
		boolean[] checkedColours = new boolean[qualificationCheck.size()];
		
		for (int i = 0; i < qualificationCheck.size(); i++) {
			checkedColours[i] = qualificationCheck.get(i);
			if (qualificationCheck.get(i) == true) {
				count2++;
				store1.add(qualificationName.get(i));
			}
		}
		int count = qualification.length;

		DialogInterface.OnMultiChoiceClickListener coloursDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which,
					boolean isChecked) {

				if (isChecked && count2<4) {
					selectedqualification.add(qualificationID.get(which));
					ArrayList<String> store1 = new ArrayList<String>();
					qualificationCheck.set(which, true);
					count2 = 0;
					for (int i = 0; i < qualificationCheck.size(); i++) {
						if (qualificationCheck.get(i) == true) {
							store1.add(qualificationName.get(i));
							count2++;

						}

					}
					/*
					if (tvQualification.getText().toString().trim().length() == 0) {
						tvQualification.setText(qualificationName.get(which));

					} else {
						tvQualification.append(qualificationName.get(which)
								+ ",");
					}
*/
				}

				else {
					selectedqualification.remove(qualificationID.get(which));
					qualificationCheck.set(which, false);
				}
				count2 = 0;
				// store = null;
				ArrayList<String> store1 = new ArrayList<String>();
				for (int i = 0; i < qualificationCheck.size(); i++) {
					if (qualificationCheck.get(i) == true) {
						store1.add(qualificationName.get(i));
						count2++;

					}

				}
				onChangeSelectedColours();
				s1 = "";
				for (int i = 0; i < store1.size(); i++) {

					s1 += store1.get(i) + ",";
				}
				tvQualification.setText(s1);
			}
			

		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Select Qualification: ");

		builder.setMultiChoiceItems(qualification, checkedColours,
				coloursDialogListener);

		AlertDialog dialog = builder.create();
		builder.setPositiveButton("Done", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

			}
		});
		dialog.show();
	}

	protected void onChangeSelectedColours() {

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.tvSpeciality) {
			openDialogforSpeciality();
		} else if (v.getId() == R.id.tvQualification) {
			openDialogforqualification();
		} else if (v.getId() == R.id.btnSubmit) {
			if (NetworkInformer.isNetworkConnected(getBaseContext())) {
				if (checkEmail(emailID.getText().toString())||emailID.getText().toString().equals(" ")) {
					saveData();
				} else {
					Toast.makeText(getBaseContext(),
							"Please enter valid email address",
							Toast.LENGTH_LONG).show();

				}
			} else {
				Toast.makeText(getBaseContext(),
						"Please check your internet Connection",
						Toast.LENGTH_LONG).show();
			}

		} else if (v.getId() == R.id.etDOb) {
			setDate(v, dob);
		} else if (v.getId() == R.id.etAnniverseryDate) {
			setDate(v, anniverseryDate);
		}
	}

	private void saveData() {
		new SaveData().execute();
	}

	private class FetchSpeciality extends AsyncTask<Void, Void, SoapObject> {
		SoapObject result;
		private ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(EditCustomerActivity.this);
			progress.setMessage("Please wait...");
			progress.setCancelable(false);
			progress.show();
		}

		@Override
		protected SoapObject doInBackground(Void... arg0) {
			SoapObject request = new SoapObject(Utility.NAMESPACE,
					Utility.METHOD_GET_CUSTOMER_SPECIAlITY);
			request.addProperty("Token", machineCode);
			request.addProperty("CustomerID", customerID);

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
							+ Utility.METHOD_GET_CUSTOMER_SPECIAlITY,
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
				int count = 0;
				/*
				 * SoapObject nameResult = (SoapObject) result.getProperty(0);
				 * SoapObject result1 = (SoapObject) nameResult.getProperty(2);
				 * SoapObject result3 = (SoapObject) result1.getProperty(0);
				 * count = result3.getPropertyCount(); SoapObject soapResult =
				 * null; soapResult = (SoapObject) result3.getProperty(0);
				 * SoapObject soapObject = (SoapObject)
				 * soapResult.getProperty(0); System.out.println("Result is : "
				 * + soapObject.getProperty("IsSucceed"));
				 */
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
			System.out.println(soapObject.getProperty("IsSucceed"));
			if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
				SoapObject result1 = (SoapObject) soapObject.getProperty(1);
				System.out.println("Result1 is : " + result1.toString());

				SoapObject result3 = (SoapObject) result1.getProperty(1);
				System.out.println("Result3 is : " + result3.toString());
				SoapObject result4 = (SoapObject) result3.getProperty(0);
				System.out.println("Result4 is : " + result4.toString());

				int count = result4.getPropertyCount();
				System.out.println("Count is : " + count);
				for (int i = 0; i < count; i++) {
					SoapObject soapResult = null;
					soapResult = (SoapObject) result4.getProperty(i);
					specialityID.add(Integer.parseInt(soapResult.getProperty(
							"SpecialityID").toString()));
					specialityName.add(soapResult.getProperty("SpecialityName")
							.toString());
					if (Integer.parseInt(soapResult.getProperty("IsInserted")
							.toString()) == 1) {
						tvSpeciality.append(soapResult.getProperty(
								"SpecialityName").toString()
								+ ",");
						selectedspecialities.add(Integer.parseInt(soapResult
								.getProperty("SpecialityID").toString()));
						specialityCheck.add(true);
					} else {
						specialityCheck.add(false);
					}

				}
				for (int j = 0; j < specialityCheck.size(); j++) {
					System.out.println("Speciality : "
							+ specialityCheck.get(j).toString());
				}
			} else {
				Toast.makeText(getBaseContext(),
						soapObject.getProperty("ErrorMessage").toString(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private class FetchQualification extends AsyncTask<Void, Void, SoapObject> {
		SoapObject result;
		private ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(EditCustomerActivity.this);
			progress.setMessage("Please wait...");
			progress.setCancelable(false);
			progress.show();
		}

		@Override
		protected SoapObject doInBackground(Void... arg0) {
			SoapObject request = new SoapObject(Utility.NAMESPACE,
					Utility.mETHOD_GET_CUSTOMER_QUALIFICATION);
			request.addProperty("Token", machineCode);
			request.addProperty("CustomerID", customerID);

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
							+ Utility.mETHOD_GET_CUSTOMER_QUALIFICATION,
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
				int count = 0;
				/*
				 * SoapObject nameResult = (SoapObject) result.getProperty(0);
				 * SoapObject result1 = (SoapObject) nameResult.getProperty(2);
				 * SoapObject result3 = (SoapObject) result1.getProperty(0);
				 * count = result3.getPropertyCount(); SoapObject soapResult =
				 * null; soapResult = (SoapObject) result3.getProperty(0);
				 * SoapObject soapObject = (SoapObject)
				 * soapResult.getProperty(0); System.out.println("Result is : "
				 * + soapObject.getProperty("IsSucceed"));
				 */
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
			System.out.println(soapObject.getProperty("IsSucceed"));
			if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
				SoapObject result1 = (SoapObject) soapObject.getProperty(1);
				System.out.println("Result1 is : " + result1.toString());

				SoapObject result3 = (SoapObject) result1.getProperty(1);
				System.out.println("Result3 is : " + result3.toString());
				SoapObject result4 = (SoapObject) result3.getProperty(0);
				System.out.println("Result4 is : " + result4.toString());

				int count = result4.getPropertyCount();
				System.out.println("Count is : " + count);
				for (int i = 0; i < count; i++) {
					SoapObject soapResult = null;
					soapResult = (SoapObject) result4.getProperty(i);
					qualificationID.add(Integer.parseInt(soapResult
							.getProperty("QualificationID").toString()));
					qualificationName.add(soapResult.getProperty(
							"QualificationName").toString());
					if (Integer.parseInt(soapResult.getProperty("IsInserted")
							.toString()) == 1) {
						tvQualification.append(soapResult.getProperty(
								"QualificationName").toString()
								+ ",");
						selectedqualification.add(Integer.parseInt(soapResult
								.getProperty("QualificationID").toString()));
						qualificationCheck.add(true);
					} else {
						qualificationCheck.add(false);
					}
				}
				for (int i = 0; i < qualificationCheck.size(); i++) {
					System.out.println("Qualification : "
							+ qualificationCheck.get(i).toString());
				}
			} else {
				Toast.makeText(getBaseContext(),
						soapObject.getProperty("ErrorMessage").toString(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private class SaveData extends AsyncTask<Void, Void, SoapObject> {
		SoapObject result;
		private ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(EditCustomerActivity.this);
			progress.setMessage("Please wait...");
			progress.setCancelable(false);
			progress.show();
		}

		@SuppressLint("DefaultLocale")
		@Override
		protected SoapObject doInBackground(Void... arg0) {
			SoapObject request = new SoapObject(Utility.NAMESPACE,
					Utility.METHOD_UPDATE_CUSTOMER);
			
			
			request.addProperty("CustomerID", customerID);
			request.addProperty("Token", machineCode);
			request.addProperty("MobileNo", mobileNo.getText().toString());
			request.addProperty("EmailID", emailID.getText().toString());
			request.addProperty("Patients", patients.getText().toString());
			if (dob.getText().toString().trim().length() != 0) {
				String preExistingDate = (String) dob.getText().toString();
				StringTokenizer st = new StringTokenizer(preExistingDate, "/");
				String date = "";
				String month = "";
				String year = "";
				/*
			
				date = String.format("%02d", Integer.parseInt(st.nextToken()));
				month = String.format("%02d", Integer.parseInt(st.nextToken()));
				year = String.format("%02d", Integer.parseInt(st.nextToken()));
				*/String tempdate = date + ":" + month + ":" + year;
				request.addProperty("DOB", tempdate);

			} else {
				request.addProperty("DOB", dob.getText().toString());
			}
			if (anniverseryDate.getText().toString().trim().length() == 0) {
				String preExistingDate = (String) anniverseryDate.getText()
						.toString();
				StringTokenizer st = new StringTokenizer(preExistingDate, "/");
				String date = "";
				String month = "";
				String year = "";
			/*	date = String.format("%02d", Integer.parseInt(st.nextToken())).toString();
				month = String.format("%02d", Integer.parseInt(st.nextToken())).toString();
				year = String.format("%02d", Integer.parseInt(st.nextToken())).toString();
				*/String tempdate = date + ":" + month + ":" + year;

				request.addProperty("AnniversaryDate", tempdate);

			} else {
				request.addProperty("AnniversaryDate", anniverseryDate
						.getText().toString());

			}
			request.addProperty("AnniversaryDate", anniverseryDate.getText()
					.toString());
			if (selectedspecialities.size() != 0) {
				String selectedspeciality = null;

				for (int i = 0; i < selectedspecialities.size(); i++) {
					if (selectedspeciality == null) {
						selectedspeciality = selectedspecialities.get(i) + ",";
					} else {
						selectedspeciality = selectedspeciality
								+ selectedspecialities.get(i) + ",";
					}
				}
				selectedspeciality = cutString(selectedspeciality);
				request.addProperty("Speciality", selectedspeciality);

			}
			if (selectedqualification.size() != 0) {
				String selectedQualification = null;
				System.out.println(selectedqualification.size());
				for (int i = 0; i < selectedqualification.size(); i++) {

					if (selectedQualification == null) {
						selectedQualification = selectedqualification.get(i)
								+ ",";
					} else {

						selectedQualification = selectedQualification
								+ selectedqualification.get(i) + ",";
					}

				}
				selectedQualification = cutString(selectedQualification);
				request.addProperty("Qualification", selectedQualification);
			}
			System.out.println("Request : " + request.toString());
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
							+ Utility.METHOD_UPDATE_CUSTOMER, mySoapEnvelop);
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
				int count = 0;
				/*
				 * SoapObject nameResult = (SoapObject) result.getProperty(0);
				 * SoapObject result1 = (SoapObject) nameResult.getProperty(2);
				 * SoapObject result3 = (SoapObject) result1.getProperty(0);
				 * count = result3.getPropertyCount(); SoapObject soapResult =
				 * null; soapResult = (SoapObject) result3.getProperty(0);
				 * SoapObject soapObject = (SoapObject)
				 * soapResult.getProperty(0); System.out.println("Result is : "
				 * + soapObject.getProperty("IsSucceed"));
				 */
			}

			catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Result is" + result.toString());
			return result;
		}

		@Override
		protected void onPostExecute(SoapObject result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progress.dismiss();
			SoapObject soapObject = (SoapObject) result.getProperty(0);
			System.out.println(soapObject.getProperty("IsSucceed"));
			if (soapObject.getProperty("IsSucceed").toString().equals("true")) {
				Toast.makeText(getBaseContext(), "Update Sucessfully.",
						Toast.LENGTH_LONG).show();
				finish();
			} else {
				Toast.makeText(getBaseContext(),
						soapObject.getProperty("ErrorMessage").toString(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	public String cutString(String str) {
		if (str.length() > 0) {
			str = str.substring(0, str.length() - 1);
		}
		return str;
	}

	public final static boolean isValidEmail(CharSequence target) {
		return !TextUtils.isEmpty(target)
				&& android.util.Patterns.EMAIL_ADDRESS.matcher(target)
						.matches();
	}

	private void setDate(View v, EditText edittext) {
		temp = edittext;
		Calendar dtTxt = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"dd/M/yyyy hh:mm:ss");
		String preExistingDate = (String) edittext.getText().toString();
		if (preExistingDate.toString().trim().length() == 0
				|| preExistingDate.equals("")
				|| preExistingDate.toString() == null) {
			Calendar c = Calendar.getInstance();
			preExistingDate = String.format("%02d", Integer.parseInt(String
					.valueOf(c.get(Calendar.DAY_OF_MONTH))))
					+ "/"
					+ String.format("%02d", Integer.parseInt(String.valueOf(c
							.get(Calendar.MONTH) + 1)))
					+ "/"
					+ String.format("%02d", Integer.parseInt(String.valueOf(c
							.get(Calendar.YEAR))));

		}
		StringTokenizer st = new StringTokenizer(preExistingDate, "/");
		initialDate = String.format("%02d", Integer.parseInt(st.nextToken()));
		initialMonth = String.format("%02d", Integer.parseInt(st.nextToken()));
		initialYear = String.format("%02d", Integer.parseInt(st.nextToken()));

		dtTxt = Calendar.getInstance();
		try {
			Date date1 = simpleDateFormat.parse(initialDate + "/"
					+ initialMonth + "/" + initialYear + " 00:00:00");
			Date date2 = simpleDateFormat.parse(String.format("%02d", Integer
					.parseInt(String.valueOf(c.get(Calendar.DAY_OF_MONTH))))
					+ "/"
					+ String.format("%02d", Integer.parseInt(String.valueOf(c
							.get(Calendar.MONTH) + 1)))
					+ "/"
					+ String.format("%02d", Integer.parseInt(String.valueOf(c
							.get(Calendar.YEAR)))) + "00:00:00");

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (dialog == null)
			dialog = new DatePickerDialog(v.getContext(), new PickDate(),
					Integer.parseInt(initialYear),
					Integer.parseInt(initialMonth + 1),
					Integer.parseInt(initialDate));
		dialog.updateDate(Integer.parseInt(initialYear),
				(Integer.parseInt(initialMonth) - 1),
				Integer.parseInt(initialDate));

		dialog.show();

	}

	class PickDate implements DatePickerDialog.OnDateSetListener {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			view.updateDate(year, monthOfYear, dayOfMonth);
			monthOfYear = monthOfYear + 1;
			dialog.hide();
			temp.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
		}

	}

}

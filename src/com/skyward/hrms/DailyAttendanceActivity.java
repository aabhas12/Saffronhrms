package com.skyward.hrms;

import java.io.IOException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.skyward.utility.NetworkInformer;
import com.skyward.utility.Utility;

public class DailyAttendanceActivity extends Activity implements
		OnClickListener {

	ImageView ivadd, ivView;
	ExpandableListAdapter listAdapter;
	ExpandableListView expListView;
	List<String> listDataHeader;
	HashMap<String, List<Attendance>> listDataChild;
	LinearLayout layaddnew;
	private DatePickerDialog dialog = null;
	private Button btnSubmit;
	int abc;
	private LinearLayout laycustomerList;
	TextView etFromtime, tvDate, temp, tvVisitType, etClientName,
			tvDateforSelelction, tvErrorMSg, tvemployeename;
	private ImageView ivLogout;
	private String initialDate;
	private String initialMonth;
	private String initialYear;
	EditText etRemarks;
	private ArrayList<String> visitType = new ArrayList<String>();
	private ArrayList<Integer> visitTypeID = new ArrayList<Integer>();
	private ArrayList<String> employeename = new ArrayList<String>();
	private ArrayList<Integer> employeeid = new ArrayList<Integer>();
	private int tempvisitTypeID = 0;
	private static int tempemployeeid = 0;
	private ArrayList<String> customer = new ArrayList<String>();
	private ArrayList<Integer> customerID = new ArrayList<Integer>();
	private int tempCustomerID = 0;
	Calendar c;
	private int customerVisitID = 0;
	int a, b;

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		finish();
		super.onPause();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_attendance);
		// get the listview
		ivLogout = (ImageView) findViewById(R.id.ivLogout);
		tvErrorMSg = (TextView) findViewById(R.id.tvErrorMSg);
		tvDateforSelelction = (TextView) findViewById(R.id.tvDateforSelelction);
		laycustomerList = (LinearLayout) findViewById(R.id.laycustomerList);
		tvVisitType = (TextView) findViewById(R.id.tvVisitType1);
		etClientName = (TextView) findViewById(R.id.etClientname);
		etRemarks = (EditText) findViewById(R.id.etRemarks);
		expListView = (ExpandableListView) findViewById(R.id.lvExp);
		tvemployeename = (TextView) findViewById(R.id.tvemployeename);
		etFromtime = (TextView) findViewById(R.id.etFromTime);
		btnSubmit = (Button) findViewById(R.id.btnSubmit);
		btnSubmit.setOnClickListener(this);
		tvDate = (TextView) findViewById(R.id.tvDate);

		c = Calendar.getInstance();
		setCurrentTime(etFromtime);
		int Date = c.get(Calendar.DATE);
		tvDate.setText(String.format("%02d",
				Integer.parseInt(String.valueOf(c.get(Calendar.DAY_OF_MONTH))))
				+ "/"
				+ String.format("%02d", Integer.parseInt(String.valueOf(c
						.get(Calendar.MONTH) + 1)))
				+ "/"
				+ String.format("%02d",
						Integer.parseInt(String.valueOf(c.get(Calendar.YEAR)))));
		setCurrentdate(tvDate);
		etFromtime.setOnClickListener(this);
		tvVisitType.setOnClickListener(this);
		tvDate.setOnClickListener(this);
		tvemployeename.setOnClickListener(this);
		etClientName.setOnClickListener(this);
		tvDateforSelelction.setOnClickListener(this);
		layaddnew = (LinearLayout) findViewById(R.id.layaddnew);
		ivadd = (ImageView) findViewById(R.id.ivadd);
		ivView = (ImageView) findViewById(R.id.ivview);
		if (NetworkInformer.isNetworkConnected(getBaseContext())) {
			getVisitType();
			getemployeename();
		} else {
			Toast.makeText(getBaseContext(), "Check your Internet Connection",
					Toast.LENGTH_LONG).show();

		}
		if (NetworkInformer.isNetworkConnected(getBaseContext())) {

		} else {
			Toast.makeText(getBaseContext(), "Check your Internet Connection",
					Toast.LENGTH_LONG).show();

		}

		ivLogout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(DailyAttendanceActivity.this,
						MainActivity.class));
				DailyAttendanceActivity.this.finish();
			}
		});
		ivadd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ivadd.setImageResource(R.drawable.add);
				ivView.setImageResource(R.drawable.menu_view_add);
				layaddnew.setVisibility(View.VISIBLE);
				laycustomerList.setVisibility(View.GONE);
				setCurrentTime(etFromtime);
				setCurrentdate(tvDate);
				etClientName.setText("");
				etRemarks.setText("");
				tvVisitType.setText("");

			}
		});
		ivView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ivadd.setImageResource(R.drawable.add_normal);
				ivView.setImageResource(R.drawable.menu_view);
				layaddnew.setVisibility(View.GONE);
				laycustomerList.setVisibility(View.VISIBLE);
				if (tvDateforSelelction.getText().toString().length() == 0) {
					Calendar c = Calendar.getInstance();
					int Date = c.get(Calendar.DATE);
					a = Integer.parseInt(String.valueOf(c
							.get(Calendar.DAY_OF_MONTH)));
					tvDateforSelelction.setText(String.format("%02d", Integer
							.parseInt(String.valueOf(c
									.get(Calendar.DAY_OF_MONTH))))
							+ "/"
							+ String.format("%02d", Integer.parseInt(String
									.valueOf(c.get(Calendar.MONTH) + 1)))
							+ "/"
							+ String.format("%02d", Integer.parseInt(String
									.valueOf(c.get(Calendar.YEAR)))));

					String.format("%02d", Integer.parseInt(String.valueOf(c
							.get(Calendar.DAY_OF_MONTH))));

				}
				getCustomerVisit();
			}
		});

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
				List<Attendance> list = listDataChild.get(listDataHeader
						.get(groupPosition));
				System.out.println(list.get(0).getVisitTime().toString());
				etRemarks.setText(list.get(0).getRemarks().toString());
				tvDate.setText(list.get(0).getDate());
				etFromtime.setText(list.get(0).getVisitTime());
				tvVisitType.setText(list.get(0).getVisitType());
				customerVisitID = list.get(0).getCustomerVisitID();
				for (int i = 0; i < visitType.size(); i++) {
					if (list.get(0).getVisitType().equals(visitType.get(i))) {
						tempvisitTypeID = visitTypeID.get(i);
					}
				}
				etClientName.setText(listDataHeader.get(groupPosition)
						.toString());
				for (int i = 0; i < customer.size(); i++) {
					if (listDataHeader.get(groupPosition).toString()
							.equals(customer.get(i))) {
						tempCustomerID = customerID.get(i);
					}
				}
				ivadd.setImageResource(R.drawable.add);
				ivView.setImageResource(R.drawable.menu_view_add);
				layaddnew.setVisibility(View.VISIBLE);
				laycustomerList.setVisibility(View.GONE);

				return true;
			}
		});
	}

	private void setCurrentdate(TextView tvDate2) {
		Calendar c = Calendar.getInstance();
		tvDate2.setText(String.format("%02d",
				Integer.parseInt(String.valueOf(c.get(Calendar.DAY_OF_MONTH))))
				+ "/"
				+ String.format("%02d", Integer.parseInt(String.valueOf(c
						.get(Calendar.MONTH) + 1)))
				+ "/"
				+ String.format("%02d",
						Integer.parseInt(String.valueOf(c.get(Calendar.YEAR)))));

	}

	private void getCustomer() {
		System.out.println("called customer");
		new FetchCustomer().execute();
	}

	private void getVisitType() {
		new FetchVisitType().execute();
	}

	private void getemployeename() {
		new Fetchemployeename().execute();
	}

	/*
	 * Preparing the list data
	 */

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvDateforSelelction:
			setDate(v, tvDateforSelelction);
			break;
		case R.id.etFromTime:
			setTime(etFromtime);
			break;
		case R.id.tvDate:
			setDate(v, tvDate);
			break;
		case R.id.tvVisitType1:
			String[] countries = visitType
					.toArray(new String[visitType.size()]);
			alertForVisitType(countries, v, tvVisitType);
			break;
		case R.id.tvemployeename:
			if (tempvisitTypeID == 121) {
				String[] emp = employeename.toArray(new String[employeename
						.size()]);

				if (employeename.size() != 1) {
					alertForemployeename(emp, v, tvemployeename);
				}

			}
			break;

		case R.id.etClientname:
			if (tempvisitTypeID == 121) {
				String[] customers = customer.toArray(new String[customer
						.size()]);
				alertForCustomer(customers, v, etClientName);
			}
			break;

		case R.id.btnSubmit:

			if (etClientName.getText().toString().trim().length() == 0
					&& tempvisitTypeID == 121) {
				Toast.makeText(getBaseContext(), "Please enter client Name",
						Toast.LENGTH_LONG).show();
			} else if (tvDate.getText().toString().trim().length() == 0) {
				Toast.makeText(getBaseContext(), "Please enter Date",
						Toast.LENGTH_LONG).show();
			} else if (etFromtime.getText().toString().trim().length() == 0) {
				Toast.makeText(getBaseContext(), "Please enter From time",
						Toast.LENGTH_LONG).show();
			} else {
				if (NetworkInformer.isNetworkConnected(getBaseContext())) {

					addVisit();

				} else {
					Toast.makeText(getBaseContext(),
							"Check your Internet Connection", Toast.LENGTH_LONG)
							.show();
				}

			}
			break;
		default:
			break;
		}
	}

	private void getCustomerVisit() {
		new GetCustomerVisit().execute();
	}

	private void addVisit() {
		if (customerVisitID == 0) {
			new SetVisit().execute();
		} else {
			new UpdateVisit().execute();
		}

	}

	private void alertForemployeename(final String[] employeename, View v,
			final TextView tvemployeename2) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				DailyAttendanceActivity.this);
		// Source of the data in the DIalog

		// Set the dialog title
		builder.setTitle("Select Employee Name")
		// Specify the list array, the items to be selected by
		// default
		// (null for none),
		// and the listener through which to receive callbacks
		// when
		// items are selected

				.setSingleChoiceItems(employeename, 0,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								tvemployeename2.setText(employeename[which]);

								for (int i = 0; i < DailyAttendanceActivity.this.employeename
										.size(); i++) {
									if (DailyAttendanceActivity.this.employeename
											.get(i).toString()
											.equals(employeename[which])) {
										tempemployeeid = employeeid.get(i);

										abc = tempemployeeid;
										getCustomer();
									}
								}
								dialog.dismiss();
							}
						});

		// Set the action buttons
		builder.show();

	}

	private void alertForVisitType(final String[] visitType, View v,
			final TextView tvVisitType2) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				DailyAttendanceActivity.this);
		// Source of the data in the DIalog

		// Set the dialog title
		builder.setTitle("Select Visit Type")
		// Specify the list array, the items to be selected by
		// default
		// (null for none),
		// and the listener through which to receive callbacks
		// when
		// items are selected
				.setSingleChoiceItems(visitType, 0,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								tvVisitType2.setText(visitType[which]);

								for (int i = 0; i < DailyAttendanceActivity.this.visitType
										.size(); i++) {
									if (DailyAttendanceActivity.this.visitType
											.get(i).toString()
											.equals(visitType[which])) {
										tempvisitTypeID = visitTypeID.get(i);
										getemployeename();
									}
								}
								dialog.dismiss();
							}
						});

		// Set the action buttons
		builder.show();

	}

	private void alertForCustomer(final String[] visitType, View v,
			final TextView tvVisitType2) {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				DailyAttendanceActivity.this);
		// Source of the data in the DIalog

		// Set the dialog title
		builder.setTitle("Select Client")
		// Specify the list array, the items to be selected by
		// default
		// (null for none),
		// and the listener through which to receive callbacks
		// when
		// items are selected
				.setSingleChoiceItems(visitType, 0,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								for (int i = 0; i < DailyAttendanceActivity.this.customer
										.size(); i++) {
									if (DailyAttendanceActivity.this.customer
											.get(i).toString()
											.equals(visitType[which])) {
										tempCustomerID = customerID.get(i);
									}
								}
								tvVisitType2.setText(visitType[which]);
								dialog.dismiss();

							}
						});

		// Set the action buttons
		builder.show();

	}

	private void setDate(View v, TextView edittext) {
		temp = edittext;
		Calendar dtTxt = null;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"dd/M/yyyy hh:mm:ss");
		String preExistingDate = (String) edittext.getText().toString();
		if (preExistingDate != null && !preExistingDate.equals("")) {
			StringTokenizer st = new StringTokenizer(preExistingDate, "/");
			initialDate = String.format("%02d",
					Integer.parseInt(st.nextToken()));
			initialMonth = String.format("%02d",
					Integer.parseInt(st.nextToken()));
			initialYear = String.format("%02d",
					Integer.parseInt(st.nextToken()));
			dtTxt = Calendar.getInstance();
			try {
				Date date1 = simpleDateFormat.parse(initialDate + "/"
						+ initialMonth + "/" + initialYear + " 00:00:00");
				Date date2 = simpleDateFormat.parse(String.format("%02d",
						Integer.parseInt(String.valueOf(c
								.get(Calendar.DAY_OF_MONTH))))
						+ "/"
						+ String.format("%02d", Integer.parseInt(String
								.valueOf(c.get(Calendar.MONTH) + 1)))
						+ "/"
						+ String.format("%02d", Integer.parseInt(String
								.valueOf(c.get(Calendar.YEAR)))) + "00:00:00");

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

		} else {

			dtTxt = Calendar.getInstance();
			if (dialog == null)
				dialog = new DatePickerDialog(v.getContext(), new PickDate(),
						dtTxt.getTime().getYear(), dtTxt.getTime().getMonth(),
						dtTxt.getTime().getDay());
			dialog.updateDate(dtTxt.getTime().getYear(), dtTxt.getTime()
					.getMonth(), dtTxt.getTime().getDay());
			/*
			 * }
			 */
		}
		dialog.show();

	}

	private void setTime(final TextView etPickupTime2) {
		Calendar mcurrentTime = Calendar.getInstance();
		int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
		int minute = mcurrentTime.get(Calendar.MINUTE);
		TimePickerDialog mTimePicker;
		mTimePicker = new TimePickerDialog(DailyAttendanceActivity.this,
				new TimePickerDialog.OnTimeSetListener() {
					@Override
					public void onTimeSet(TimePicker timePicker,
							int selectedHour, int selectedMinute) {

						String hrs, min = null;

						if (selectedHour < 10) {
							hrs = "0" + String.valueOf(selectedHour);
						} else {
							hrs = String.valueOf(selectedHour);
						}
						if (selectedMinute < 10) {
							min = "0" + String.valueOf(selectedMinute);
						} else {
							min = String.valueOf(selectedMinute);
						}
						etPickupTime2.setText(hrs + ":" + min);
					}
				}, hour, minute, true);// Yes 24 hour time
		mTimePicker.setTitle("Select Time");
		mTimePicker.show();
	}

	class PickDate implements DatePickerDialog.OnDateSetListener {

		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			view.updateDate(year, monthOfYear, dayOfMonth);
			monthOfYear = monthOfYear + 1;
			dialog.hide();
			if (temp == tvDateforSelelction) {
				temp.setText(dayOfMonth + "/" + monthOfYear + "/" + year);
				getCustomerVisit();
			} else {
				SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
						"dd/MM/yyyy");
				try {
					Date date1 = simpleDateFormat.parse(dayOfMonth + "/"
							+ monthOfYear + "/" + year);
					Date date2 = simpleDateFormat.parse(String.format("%02d",
							Integer.parseInt(String.valueOf(c
									.get(Calendar.DAY_OF_MONTH))))
							+ "/"
							+ String.format("%02d", Integer.parseInt(String
									.valueOf(c.get(Calendar.MONTH) + 1)))
							+ "/"
							+ String.format("%02d", Integer.parseInt(String
									.valueOf(c.get(Calendar.YEAR)))));
					long diff = getDateDifference(date1, date2);
					if (diff < 3 && diff >= 0) {
						temp.setText(dayOfMonth + "/" + monthOfYear + "/"
								+ year);
					} else {
						Toast.makeText(getBaseContext(),
								"Please select valid date", Toast.LENGTH_LONG)
								.show();
					}
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}

	}

	private class FetchVisitType extends AsyncTask<Void, Void, SoapObject> {
		SoapObject result;
		private ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(DailyAttendanceActivity.this);
			progress.setMessage("Please wait...");
			progress.setCancelable(false);
			progress.show();
		}

		@Override
		protected SoapObject doInBackground(Void... arg0) {
			SoapObject request = new SoapObject(Utility.NAMESPACE,
					Utility.METHOD_VISITTYPPE);
			SharedPreferences settings = getSharedPreferences("info",
					MODE_PRIVATE);
			String machineCode = settings.getString("MachineCode", "");
			request.addProperty("Token", machineCode);
			// request.addProperty("Token",
			// "079D6642-81A2-4C0C-823C-03B82CEFCC20");
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
							+ Utility.METHOD_VISITTYPPE, mySoapEnvelop);
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
					visitType.add(soapResult.getProperty("Name").toString());
					visitTypeID.add(Integer.parseInt(soapResult.getProperty(
							"ID").toString()));
					abc();

				}
			} else {

				Toast.makeText(getBaseContext(),
						soapObject.getProperty("ErrorMessage").toString(),
						Toast.LENGTH_LONG).show();
			}
		}

		void abc() {
			tvVisitType.setText(visitType.get(0));
			tempvisitTypeID = visitTypeID.get(0);

		}
	}

	private class FetchCustomer extends AsyncTask<Void, Void, SoapObject> {
		SoapObject result;
		private ProgressDialog progress;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			etClientName.setText("");
			progress = new ProgressDialog(DailyAttendanceActivity.this);
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
					Utility.METHOD_CUSTOMER1);
			request.addProperty("Token", machineCode);
			System.out.println("tokencust:" + machineCode);
			System.out.println("enpl id:" + abc);
			request.addProperty("EmployeeID", abc);
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
							+ Utility.METHOD_CUSTOMER1, mySoapEnvelop);

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
				System.out.println("exec in try");
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
			SoapObject soapObject = (SoapObject) result.getProperty(0);
			progress.dismiss();
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
				customer.clear();
				customerID.clear();
				for (int i = 0; i < count; i++) {
					SoapObject soapResult = null;
					soapResult = (SoapObject) result4.getProperty(i);
					customer.add(soapResult.getProperty("CustomerName")
							.toString());
					customerID.add(Integer.parseInt(soapResult.getProperty(
							"CustomerID").toString()));
					System.out
							.println("Sss:"
									+ soapResult.getProperty("CustomerName")
											.toString());
				}
			} else {
				Toast.makeText(getBaseContext(),
						soapObject.getProperty("ErrorMessage").toString(),
						Toast.LENGTH_LONG).show();
			}

		}
	}

	private class Fetchemployeename extends AsyncTask<Void, Void, SoapObject> {
		SoapObject result;

		@Override
		protected void onPreExecute() {
			tvemployeename.setText("");
			employeeid.clear();
			employeename.clear();
			super.onPreExecute();
		}

		@Override
		protected SoapObject doInBackground(Void... arg0) {
			SoapObject request = new SoapObject(Utility.NAMESPACE,
					Utility.METHOD_EMP);
			SharedPreferences settings = getSharedPreferences("info",
					MODE_PRIVATE);
			String machineCode = settings.getString("MachineCode", "");
			request.addProperty("Token", machineCode);
			System.out.println("abcd:" + machineCode);
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
							+ Utility.METHOD_EMP, mySoapEnvelop);
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
					employeename.add(soapResult.getProperty("EmpName")
							.toString());
					System.out.println("aa:"
							+ soapResult.getProperty("EmpName").toString());
					employeeid.add(Integer.parseInt(soapResult
							.getProperty("ID").toString()));

				}
				if (count == 1) {
					if (employeename.size() == 1 && tempvisitTypeID == 121) {
						tvemployeename.setText(employeename.get(0));
						tempemployeeid = employeeid.get(0);
						abc = tempemployeeid;
						getCustomer();
					} else if (tempvisitTypeID != 121) {
						tvemployeename.setText(employeename.get(0));
						tempemployeeid = employeeid.get(0);
					}

				} else if (tempvisitTypeID != 121 && count > 1) {
					SharedPreferences settings = getSharedPreferences("info",
							MODE_PRIVATE);
					String machineCode1 = settings.getString("uname", "");
					for (int i = 0; i < employeename.size(); i++) {
						if (employeename.get(i).toLowerCase()
								.contains(machineCode1.toLowerCase())) {
							tvemployeename.setText(employeename.get(i));
							tempemployeeid = employeeid.get(i);
							break;
						}
					}
					
				}
			} else {

				Toast.makeText(getBaseContext(),
						soapObject.getProperty("ErrorMessage").toString(),
						Toast.LENGTH_LONG).show();
			}
		}
	}

	private class SetVisit extends AsyncTask<Void, Void, SoapObject> {
		private ProgressDialog progress;
		SoapObject result;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(DailyAttendanceActivity.this);
			progress.setMessage("Please wait...");
			progress.setCancelable(false);
			progress.show();
		}

		@Override
		protected SoapObject doInBackground(Void... arg0) {
			SharedPreferences settings = getSharedPreferences("info",
					MODE_PRIVATE);
			String machineCode = settings.getString("MachineCode", "");
			String preExistingDate = (String) tvDate.getText().toString();
			StringTokenizer st = new StringTokenizer(preExistingDate, "/");
			String date = "";
			String month = "";
			System.out.println("to enter:");
			String year = "";
			date = String.format("%02d", Integer.parseInt(st.nextToken()));
			month = String.format("%02d", Integer.parseInt(st.nextToken()));
			year = String.format("%02d", Integer.parseInt(st.nextToken()));
			String tempdate = date + ":" + month + ":" + year;
			System.out.println("Date: " + tempdate);
			System.out.println("tempCustomerID: " + tempCustomerID);
			System.out.println("tempvisitTypeID: " + tempvisitTypeID);
			System.out.println("From Time: " + etFromtime.getText().toString());
			System.out.println("Remarks: " + etRemarks.getText().toString());
			System.out.println("tokas:"+abc);
			System.out.println("Token :  " + machineCode);
			SoapObject request;
			request = new SoapObject(Utility.NAMESPACE,
					Utility.METHOD_INSERT_CUSTOMER_VISIT);

			request.addProperty("Token", machineCode);
			request.addProperty("VisitDate", tempdate);
			if (tempCustomerID == 0) {
				request.addProperty("CustomerID", -1);

			} else {
				request.addProperty("CustomerID", tempCustomerID);
			}
			request.addProperty("CustomerID", tempCustomerID);
			request.addProperty("CusotmerVisitTypeID", tempvisitTypeID);
			request.addProperty("VisitTime", etFromtime.getText().toString());
			request.addProperty("EmployeeID", abc);
			if (etRemarks.getText().toString().equals("")) {
				request.addProperty("Remarks", "NOT PROVIDED");
			} else {
				request.addProperty("Remarks", etRemarks.getText().toString());
			}
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
							+ Utility.METHOD_INSERT_CUSTOMER_VISIT,
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
			boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
					"IsSucceed").toString());
			System.out.println(isLogin);
			if (isLogin) {
				etClientName.setText("");
				setCurrentTime(etFromtime);
				etRemarks.setText("");
				setCurrentdate(tvDate);
				//tvVisitType.setText("");
				tvemployeename.setText("");
				Toast.makeText(getBaseContext(),
						" Customer visit added sucessfully", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(getBaseContext(),
						soapObject.getProperty("ErrorMessage").toString(),
						Toast.LENGTH_LONG).show();
			}

		}
	}

	private class UpdateVisit extends AsyncTask<Void, Void, SoapObject> {

		private ProgressDialog progress;
		SoapObject result;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(DailyAttendanceActivity.this);
			progress.setMessage("Please wait...");
			progress.setCancelable(false);
			progress.show();
		}

		@Override
		protected SoapObject doInBackground(Void... arg0) {
			SharedPreferences settings = getSharedPreferences("info",
					MODE_PRIVATE);
			String machineCode = settings.getString("MachineCode", "");
			String preExistingDate = (String) tvDate.getText().toString();
			StringTokenizer st = new StringTokenizer(preExistingDate, "/");
			String date = "";
			String month = "";
			String year = "";
			date = String.format("%02d", Integer.parseInt(st.nextToken()));
			month = String.format("%02d", Integer.parseInt(st.nextToken()));
			year = String.format("%02d", Integer.parseInt(st.nextToken()));
			String tempdate = date + ":" + month + ":" + year;
			System.out.println("Date: " + tempdate);
			System.out.println("tempCustomerID: " + tempCustomerID);
			System.out.println("tempvisitTypeID: " + tempvisitTypeID);
			System.out.println("From Time: " + etFromtime.getText().toString());
			System.out.println("Remarks: " + etRemarks.getText().toString());
			System.out.println("Token :  " + machineCode);
			System.out.println("tokas:"+abc);
			SoapObject request;
			request = new SoapObject(Utility.NAMESPACE,
					Utility.METHOD_UPDATE_CUSTOMER_VISIT);
			request.addProperty("Token", machineCode);
			request.addProperty("VisitDate", tempdate);
			request.addProperty("CustomerID", tempCustomerID);
			request.addProperty("CusotmerVisitTypeID", tempvisitTypeID);
			request.addProperty("VisitTime", etFromtime.getText().toString());
			request.addProperty("Remarks", etRemarks.getText().toString());
			request.addProperty("CustomerVisitDetailID", customerVisitID);
			System.out.println("tokas:"+abc);
			request.addProperty("EmployeeID", abc);
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
							+ Utility.METHOD_UPDATE_CUSTOMER_VISIT,
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
			boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
					"IsSucceed").toString());
			System.out.println(isLogin);
			if (isLogin) {
				etClientName.setText("");
				setCurrentTime(etFromtime);
				etRemarks.setText("");
				setCurrentdate(tvDate);
				tvVisitType.setText("");
				tvemployeename.setText("");
				Toast.makeText(getBaseContext(),
						" Customer visit updated sucessfully", Toast.LENGTH_LONG)
						.show();
			} else {
				Toast.makeText(getBaseContext(),
						soapObject.getProperty("ErrorMessage").toString(),
						Toast.LENGTH_LONG).show();
			}

		}

	}

	private class GetCustomerVisit extends AsyncTask<Void, Void, SoapObject> {
		private ProgressDialog progress;
		SoapObject result;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(DailyAttendanceActivity.this);
			progress.setMessage("Please wait...");
			progress.setCancelable(false);
			progress.show();
		}

		@Override
		protected SoapObject doInBackground(Void... arg0) {
			SharedPreferences settings = getSharedPreferences("info",
					MODE_PRIVATE);
			String machineCode = settings.getString("MachineCode", "");
			System.out.println("asdasd" + machineCode);
			String preExistingDate = (String) tvDateforSelelction.getText()
					.toString();
			StringTokenizer st = new StringTokenizer(preExistingDate, "/");
			String date = "";
			String month = "";
			String year = "";
			date = String.format("%02d", Integer.parseInt(st.nextToken()));
			month = String.format("%02d", Integer.parseInt(st.nextToken()));
			year = String.format("%02d", Integer.parseInt(st.nextToken()));
			String tempdate = date + ":" + month + ":" + year;
			System.out.println("Date: " + tempdate);
			// b = Integer.parseInt(st.nextToken());

			System.out.println("Token :  " + machineCode);
			SoapObject request = new SoapObject(Utility.NAMESPACE,
					Utility.METHOD_GETCUSTOMER_VISIT);
			request.addProperty("Token", machineCode);
			request.addProperty("VisitDate", tempdate);
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
							+ Utility.METHOD_GETCUSTOMER_VISIT, mySoapEnvelop);
				} catch (XmlPullParserException e) {
					// System.out.println(e.getClass());
					System.out.println("catch1");
					e.printStackTrace();
					// System.out.println("XmlPullParserException 0");
				} catch (SocketTimeoutException e) {
					// System.out.println(e.getClass());
					System.out.println("catch2");
					e.printStackTrace();
					// System.out.println("SocketTimeoutException 1");
				} catch (SocketException e) {
					// System.out.println(e.getClass());
					System.out.println("catch3");
					e.printStackTrace();
					// System.out.println("SocketException  2");
				} catch (IOException e) {
					// System.out.println(e.getClass());
					System.out.println("catch4");
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
			boolean isLogin = Boolean.parseBoolean(soapObject.getProperty(
					"IsSucceed").toString());
			System.out.println(isLogin);
			if (isLogin) {
				SoapObject result1 = (SoapObject) soapObject.getProperty(1);
				System.out.println("Result1 is : " + result1.toString());
				SoapObject result3 = (SoapObject) result1.getProperty(1);
				System.out.println("Result3 is : " + result3.toString());
				SoapObject result4 = (SoapObject) result3.getProperty(0);
				System.out.println("Result4 is : " + result4.toString());
				int count = result4.getPropertyCount();
				System.out.println("Count is : " + count);
				listDataHeader = new ArrayList<String>();
				listDataChild = new HashMap<String, List<Attendance>>();

				for (int i = 0; i < count; i++) {
					SoapObject soapResult = null;
					soapResult = (SoapObject) result4.getProperty(i);
					System.out.println(soapResult.getProperty("EmployeeName"));
					System.out.println("sdsdsd:"
							+ soapResult.getProperty("CustomerID").toString());
					if (Integer.parseInt(soapResult.getProperty("CustomerID")
							.toString()) != -1) {
						listDataHeader.add(soapResult.getProperty(
								"CustomerName").toString());

					} else {
						listDataHeader.add("");
					}
					Attendance attandance = new Attendance(soapResult
							.getProperty("Remarks").toString(), soapResult
							.getProperty("VisitDate").toString(), soapResult
							.getProperty("VisitTime").toString(), soapResult
							.getProperty("VisitType").toString(),
							Integer.parseInt(soapResult.getProperty(
									"CustomerVisitID1").toString()));
					ArrayList<Attendance> attendenceList = new ArrayList<Attendance>();
					attendenceList.add(attandance);
					listDataChild.put(listDataHeader.get(i), attendenceList);
				}
				listAdapter = new ExpandableListAdapter(
						DailyAttendanceActivity.this, listDataHeader,
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

	@Override
	public void onBackPressed() {
		startActivity(new Intent(DailyAttendanceActivity.this,
				HomeActivity.class));
	}

	public void setCurrentTime(TextView etFromtime2) {

		Calendar mcurrentTime = Calendar.getInstance();
		int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
		int minute = mcurrentTime.get(Calendar.MINUTE);
		etFromtime2.setText(hour + ":" + minute);

	}

	public long getDateDifference(Date startDate, Date endDate) {

		// milliseconds
		long different = endDate.getTime() - startDate.getTime();

		System.out.println("startDate : " + startDate);
		System.out.println("endDate : " + endDate);
		System.out.println("different : " + different);

		long secondsInMilli = 1000;
		long minutesInMilli = secondsInMilli * 60;
		long hoursInMilli = minutesInMilli * 60;
		long daysInMilli = hoursInMilli * 24;

		long elapsedDays = different / daysInMilli;
		different = different % daysInMilli;

		long elapsedHours = different / hoursInMilli;
		different = different % hoursInMilli;

		long elapsedMinutes = different / minutesInMilli;
		different = different % minutesInMilli;

		long elapsedSeconds = different / secondsInMilli;

		System.out.printf("%d days, %d hours, %d minutes, %d seconds%n",
				elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
		return elapsedDays;
	}

}

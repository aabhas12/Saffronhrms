package com.skyward.utility;


public class Utility {
	public static final String SOAP_ACTION = "http://tempuri.org/";

	public static final String NAMESPACE = "http://tempuri.org/";
	public static String METHOD_LOGIN = "UserAuthentication";
	public static String URL = "http://182.74.250.186:8020/WSSaffronHRMS.asmx";
//	public static String URL = "http://192.168.1.106:8055/WSSaffronHRMS.asmx";
	public static String authToken = "";
	public static String METHOD_LEADS = "GetLeadsToView";
	public static String METHOD_EMP="GetBusinessManagerByCustomerID";
	public static String METHOD_CUSTOMER1 = "GetCustomersByEmployeeID";
	public static String METHOD_VISITTYPPE = "GetCustomerVisitType";
	public static String METHOD_CUSTOMER = "GetCustomersByUser";
	public static String METHOD_INSERT_CUSTOMER_VISIT = "InsertCustomerVisit";
	public static String METHOD_GETCUSTOMER_VISIT = "GetCutomerVisitByVisitDate";
	public static String METHOD_UPDATE_CUSTOMER_VISIT = "UpdateCustomerVisit";
	public static String METHOD_GET_CUSTOMER_DETAILS = "GetCustomersDetailsByUser";
	public static String METHOD_GET_CUSTOMER_SPECIAlITY = "GetSpecialitiesByCustomerID";
	public static String mETHOD_GET_CUSTOMER_QUALIFICATION = "GetQualificationsByCustomerID";
	public static String METHOD_UPDATE_CUSTOMER = "UpdateCustomers";

	// public static String URL
	// ="http://skywardcrm.com/crminternal/service/crmwebservice.asmx";
	// public static String URL ;

	public static String formatIntoHHMMSS(long secsIn) {

		long hours = 0;
		long minutes = 0;
		long remainder = 0;
		long seconds = 0;
		String result = "";

		if (secsIn != 0) {
			hours = secsIn / 3600;
			remainder = secsIn % 3600;
			minutes = remainder / 60;
			seconds = remainder % 60;
			if (hours <= 0) {
				result = result + (minutes < 10 ? "0" : "") + minutes + ":"
						+ (seconds < 10 ? "0" : "") + seconds;
			} else if (hours > 0) {
				result = (hours < 10 ? "0" : "") + hours + ":"
						+ (minutes < 10 ? "0" : "") + minutes + ":"
						+ (seconds < 10 ? "0" : "") + seconds;
			}
		} else if (secsIn == 0) {
			result = "00:00";
		}

		return result;
	}

	public static String formatIntoHHMMSS1(long secsIn) {

		long hours = 0;
		long minutes = 0;
		long remainder = 0;
		long seconds = 0;
		String result = "";
		System.out.println("secsIn   :   " + secsIn);
		if (secsIn != 0) {
			hours = secsIn / 3600;
			remainder = secsIn % 3600;
			minutes = remainder / 60;
			seconds = remainder % 60;
			if (hours <= 0) {
				result = result + (minutes < 10 ? "0" : "") + minutes + "."
						+ (seconds < 10 ? "0" : "") + seconds;
			} else if (hours > 0) {
				result = (hours < 10 ? "0" : "") + hours + "."
						+ (minutes < 10 ? "0" : "") + minutes + "."
						+ (seconds < 10 ? "0" : "") + seconds;
			}
		} else if (secsIn == 0) {
			result = "00.00";
		}

		return result;
	}

	public static Date strToDate(String str) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = formatter.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;

	}

	public static String dateToString(Date d) {
		String strDate = "";
		SimpleDateFormat sdfDestination = new SimpleDateFormat("yyyy-MM-dd");
		strDate = sdfDestination.format(d);
		return strDate;
	}

	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		System.out.println(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

	public static boolean patternMatchDigit(String param) {
		boolean error = false;
		String a = "^[0-9]+$";
		Pattern p = Pattern.compile(a);
		Matcher m = p.matcher(param);
		error = m.find();
		return error;
	}
}

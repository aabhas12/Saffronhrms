package com.skyward.hrms;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ExpandableCustomerListAdapter extends BaseExpandableListAdapter {
	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, List<Customer>> _listDataChild;

	public ExpandableCustomerListAdapter(Context context,
			List<String> listDataHeader,
			HashMap<String, List<Customer>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {

		final Customer customer = (Customer) getChild(groupPosition,
				childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_customer_child,
					null);
		}
		ImageView ivEdit = (ImageView) convertView.findViewById(R.id.ivEdit);
		TextView tvPan = (TextView) convertView.findViewById(R.id.tvPan);
		TextView tvPassport = (TextView) convertView
				.findViewById(R.id.tvPassport);
		TextView tvclinicAddress = (TextView) convertView
				.findViewById(R.id.tvclinicAddress);
		ivEdit.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(_context, EditCustomerActivity.class);
				i.putExtra("mobile", customer.getMobileNo());
				i.putExtra("email", customer.getEmailId());
				i.putExtra("patients", customer.getPatients());
				i.putExtra("anniversery", customer.getAnniversaryDate());
				i.putExtra("dob", customer.getDateOfBirth());
				i.putExtra("name", customer.getCustomerName());
				i.putExtra("code", customer.getCustomerCode());
				i.putExtra("customerID", customer.getCustomerId());
				_context.startActivity(i);
			}
		});
		System.out.println(customer.getPanNo());
		if (customer.getPanNo() != null) {
			if (customer.getPanNo().trim().length() != 0) {
				tvPan.setText(customer.getPanNo());

			}
		}
		if (customer.getPassportNo().trim().length() != 0) {
			tvPassport.setText(customer.getPassportNo());
		}
		if (customer.getClinicAdress().trim().length() != 0) {
			tvclinicAddress.setText(customer.getClinicAdress());
		}
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.list_group, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.lblListHeader);
		lblListHeader.setTypeface(null, Typeface.BOLD);
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}

package org.harvard.edu.cscie57a.paymentcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.graphics.Typeface.BOLD;

public class PaymentCalendarAdapter extends BaseAdapter {
	private Date currentDate;
	private DecimalFormat decimalFormatter;
	private Context context;
	private Date[] dateGridPosition = new Date[42];
	private double[] paymentGridPosition = new double[42];
	private Drawable drawable = null;



	public PaymentCalendarAdapter(Context context, Date currentDate, Locale locale) {
		this.context = context;
		this.decimalFormatter = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
		this.currentDate = currentDate;
		init(currentDate);
	}

	@Override
	public int getCount() {
		return dateGridPosition.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.calendar_grid_item, parent, false);
		}
		TextView textView = convertView.findViewById(R.id.textviewelement);
		defaultColorTextView(position, textView);
		return convertView;
	}

	public void defaultColorTextView(int position, TextView textView) {
		Date date = dateGridPosition[position];
		double payment = paymentGridPosition[position];

		int day = date.getDate();

		String dayString = String.valueOf(day);
		String paymentString = String.valueOf(decimalFormatter.format(payment));

		SpannableStringBuilder sb = new SpannableStringBuilder();
		sb.append(dayString);

		// Show payment details in next line
		sb.append(System.lineSeparator());
		sb.append(paymentString);

		SpannableString sp = new SpannableString(sb.toString());

		// Set format for day of the month
		sp.setSpan(new StyleSpan(BOLD), 0, dayString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new RelativeSizeSpan(1.2f), 0, dayString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		// Set format for payment amount
		sp.setSpan(new RelativeSizeSpan(0.75f), dayString.length() + 1,
				(dayString + paymentString).length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		textView.setText(sp);


		int[] attrs = {android.R.attr.textColor};

		int todayDateTextColor = getColorStyle(attrs, R.style.todayDateText);
		int todayDateBackgroundColor = getColorStyle(attrs, R.style.todayDateBackground);
		int selectedDateTextColor = getColorStyle(attrs, R.style.selectedDateText);
		int selectedDateBackgroundColor = getColorStyle(attrs, R.style.selectedDateBackground);
		int unSelectedWeekDateTextColor = getColorStyle(attrs, R.style.unSelectedWeekDateText);
		int unSelectedWeekDateBackgroundColor = getColorStyle(attrs, R.style.unSelectedWeekDateBackground);
		int unSelectedWeekEndDateTextColor = getColorStyle(attrs, R.style.unSelectedWeekEndDateText);
		int unSelectedWeekEndDateBackgroundColor = getColorStyle(attrs, R.style.unSelectedWeekEndDateBackground);
		int outOfScopeDateTextColor = getColorStyle(attrs, R.style.outOfScopeDateText);
		int outOfScopeDateBackgroundColor = getColorStyle(attrs, R.style.outOfScopeDateBackground);

		colorCurrentItemViewSelected(textView, outOfScopeDateTextColor, outOfScopeDateBackgroundColor);

		if (dateGridPosition[position].getMonth() == currentDate.getMonth()
				&& dateGridPosition[position].getYear() == currentDate.getYear()) {
			colorCurrentItemViewSelected(textView, unSelectedWeekDateTextColor, unSelectedWeekDateBackgroundColor);

			// Color Saturdays and Sundays
			if (position % 7 == 0 || position % 7 == 6) {
				colorCurrentItemViewSelected(textView, unSelectedWeekEndDateTextColor, unSelectedWeekEndDateBackgroundColor);
			}
		}

		// Color today's dates
		if (dateGridPosition[position].equals(currentDate)) {
			colorCurrentItemViewSelected(textView, todayDateTextColor, todayDateBackgroundColor);
		}
	}

	public void selectedColorTextView(int position, TextView view) {
		int[] attrs = {android.R.attr.textColor};
		int selectedDateTextColor = getColorStyle(attrs, R.style.selectedDateText);
		int selectedDateBackgroundColor = getColorStyle(attrs, R.style.selectedDateBackground);
		colorCurrentItemViewSelected(view, selectedDateTextColor, selectedDateBackgroundColor);
	}


	public int getColorStyle(int[] attrs, int colorStyleName) {
		TypedArray typedArray = context.obtainStyledAttributes(colorStyleName, attrs);
		return typedArray.getColor(0, Color.BLACK);
	}

	private void colorCurrentItemViewSelected(View currentItemView, int textColor, int backgroundColor) {
		TextView textView = currentItemView.findViewById(R.id.textviewelement);
		textView.setBackgroundColor(backgroundColor);
		textView.setTextColor(textColor);
	}


	public void init(Date date) {
		computeGridValues(date);
	}

	private void computeGridValues(Date date) {

		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.set(Calendar.DATE, 1);

		int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);
		instance.add(Calendar.DATE, -dayOfWeek);

		for (int i = 0; i < dateGridPosition.length; i++) {
			instance.add(Calendar.DATE, 1);
			dateGridPosition[i] = instance.getTime();
			paymentGridPosition[i] = PaymentCalendarLocaleHelper.getInstance().randomDouble();
		}

	}

	public Date getDateByPosition(int position) {
		return dateGridPosition[position];
	}

	public double getPaymentByPosition(int position) {
		return paymentGridPosition[position];
	}

}

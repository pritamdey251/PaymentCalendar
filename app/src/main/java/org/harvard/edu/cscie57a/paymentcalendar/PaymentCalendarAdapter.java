package org.harvard.edu.cscie57a.paymentcalendar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
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

/**
 * PaymentCalendar Adapter class which creates and holds grid UI elements and values
 */
public class PaymentCalendarAdapter extends BaseAdapter {
	private Date currentDate;
	private DecimalFormat decimalFormatter;
	private Context context;

	/**
	 * Holds the 42 dates to be shown in a grid
	 */
	private Date[] dateGridPosition = new Date[42];

	/**
	 * Holds the 42 payment amounts to be shown in the grid
	 */
	private double[] paymentGridPosition = new double[42];


	/**
	 * Parameterized constructor to initialize grid
	 *
	 * @param context
	 * @param currentDate
	 * @param locale
	 */
	public PaymentCalendarAdapter(Context context, Date currentDate, Locale locale) {
		this.context = context;
		this.decimalFormatter = (DecimalFormat) NumberFormat.getCurrencyInstance(locale);
		this.currentDate = currentDate;
		init(currentDate);
	}

	public void init(Date date) {
		Calendar instance = Calendar.getInstance();
		instance.setTime(date);
		instance.set(Calendar.DATE, 1);

		int dayOfWeek = instance.get(Calendar.DAY_OF_WEEK);
		instance.add(Calendar.DATE, -dayOfWeek);

		for (int i = 0; i < dateGridPosition.length; i++) {
			instance.add(Calendar.DATE, 1);
			dateGridPosition[i] = instance.getTime();
			paymentGridPosition[i] = LocaleHelper.getInstance().randomDouble();
		}
	}

	/**
	 * Called to get each grid element.
	 * This method populates each element in the calendar grid
	 *
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.calendar_grid_item, parent, false);
		}

		// Get the calendar unit grid element
		TextView textView = convertView.findViewById(R.id.calendar_grid_element);

		// Populate text and format the element as to be shown at that position in the grid
		defaultColorTextView(position, textView);

		return convertView;
	}


	/**
	 * Method to populate the actual content and color format of a single calendar grid unit
	 *
	 * @param position
	 * @param calendarGridCellView
	 */
	public void defaultColorTextView(int position, TextView calendarGridCellView) {
		Date date = dateGridPosition[position];
		double payment = paymentGridPosition[position];

		int day = date.getDate();

		String dayString = String.valueOf(day);
		String paymentString = String.valueOf(decimalFormatter.format(payment));

		SpannableStringBuilder sb = new SpannableStringBuilder();
		sb.append(dayString);

		// Use system locale aware line separator.
		// This is used to show payment information in next line
		sb.append(System.lineSeparator());

		// Show payment details in next line
		sb.append(paymentString);

		SpannableString sp = new SpannableString(sb.toString());

		// Set format for day of the month
		sp.setSpan(new StyleSpan(BOLD), 0, dayString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		sp.setSpan(new RelativeSizeSpan(1.2f), 0, dayString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		// Set format for payment amount
		sp.setSpan(new RelativeSizeSpan(0.75f), dayString.length() + 1,
				dayString.length() + paymentString.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		calendarGridCellView.setText(sp);


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

		// Color everyone as grey
		colorCurrentItemViewSelected(calendarGridCellView, outOfScopeDateTextColor, outOfScopeDateBackgroundColor);

		// Condition to color this month darker
		if (dateGridPosition[position].getMonth() == currentDate.getMonth()
				&& dateGridPosition[position].getYear() == currentDate.getYear()) {

			// Color this month darker
			colorCurrentItemViewSelected(calendarGridCellView, unSelectedWeekDateTextColor, unSelectedWeekDateBackgroundColor);

			// Color Saturdays and Sundays with weekend color format
			if (position % 7 == 0 || position % 7 == 6) {
				colorCurrentItemViewSelected(calendarGridCellView, unSelectedWeekEndDateTextColor, unSelectedWeekEndDateBackgroundColor);
			}
		}

		// Color today's dates
		if (dateGridPosition[position].equals(currentDate)) {
			colorCurrentItemViewSelected(calendarGridCellView, todayDateTextColor, todayDateBackgroundColor);
		}
	}

	/**
	 * Populate selected calendar grid element with selected color scheme
	 *
	 * @param position
	 * @param view
	 */
	public void selectedColorTextView(int position, TextView view) {
		int[] attrs = {android.R.attr.textColor};
		int selectedDateTextColor = getColorStyle(attrs, R.style.selectedDateText);
		int selectedDateBackgroundColor = getColorStyle(attrs, R.style.selectedDateBackground);
		colorCurrentItemViewSelected(view, selectedDateTextColor, selectedDateBackgroundColor);
	}


	/**
	 * Read style attributes and return color value
	 *
	 * @param attrs
	 * @param colorStyleName
	 * @return
	 */
	public int getColorStyle(int[] attrs, int colorStyleName) {
		TypedArray typedArray = context.obtainStyledAttributes(colorStyleName, attrs);
		return typedArray.getColor(0, Color.BLACK);
	}

	/**
	 * Generic method to set text and background color of calendar grid element
	 *
	 * @param currentItemView
	 * @param textColor
	 * @param backgroundColor
	 */
	private void colorCurrentItemViewSelected(View currentItemView, int textColor, int backgroundColor) {
		TextView textView = currentItemView.findViewById(R.id.calendar_grid_element);
		textView.setBackgroundColor(backgroundColor);
		textView.setTextColor(textColor);
	}


	/**
	 * Helper method to retrieve date for a position in the 6X7 grid
	 *
	 * @param position
	 * @return
	 */
	public Date getDateByPosition(int position) {
		return dateGridPosition[position];
	}

	/**
	 * Helper method to retrieve payment amount for a position in the 6X7 grid
	 *
	 * @param position
	 * @return
	 */
	public double getPaymentByPosition(int position) {
		return paymentGridPosition[position];
	}


	@Override
	public int getCount() {
		return paymentGridPosition.length;
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

}

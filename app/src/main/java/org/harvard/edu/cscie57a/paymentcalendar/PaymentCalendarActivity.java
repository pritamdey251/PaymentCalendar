package org.harvard.edu.cscie57a.paymentcalendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Payment Calendar Activity class which lets user view calendar based on Locale
 * and also lets them record their payments.
 */
public class PaymentCalendarActivity extends Activity implements View.OnClickListener {

	private PaymentCalendarAdapter paymentCalendarAdapter;
	private Locale locale;


	/**
	 * Stores today's date
	 */
	private Date todayDate;

	/**
	 * Stores the date clicked OR the same day of of a different month
	 * when navigated to next or previous month
	 */
	private Date currentDate;
	private int prevPosition;
	private Calendar calendarInstance = Calendar.getInstance();


	/**
	 * Android UI elements used
	 */
	private ViewFlipper flipper;
	private GridView calendarGridView;
	private TextView currentYear;
	private TextView currentMonth;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private Button addPaymentButton;
	private TextView prevItemView;


	/**
	 * Default constructor
	 */
	public PaymentCalendarActivity() {
		// Initializes the app with today's date
		Date date = new Date();

		// Fetches the default system Locale
		locale = Locale.getDefault();
		this.todayDate = date;
		this.currentDate = date;
		this.calendarInstance.setTime(date);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_main);

		/**
		 * Receives the name of the user from previous screen
		 * And uses Locale aware API to uppercase the name and show
		 * at the top of the view. Uppercasing based on locale
		 * is an API whose results may change based on region or country
		 */
		Intent intent = getIntent();
		String username = intent.getStringExtra(LocaleHelper.USERNAME_KEY);
		TextView userNameTextView = findViewById(R.id.username_string);
		userNameTextView.setText(username.toUpperCase(locale));

		// Populate weekday names based on Locale aware API
		populateWeekDayNames();

		// UI elements to be populated for constructing the calendar
		prevMonth = findViewById(R.id.previousMonth);
		nextMonth = findViewById(R.id.nextMonth);
		currentMonth = findViewById(R.id.currentMonth);
		currentYear = findViewById(R.id.currentYear);

		// Sets the listener for navigating to next month and previous month
		setArrowButtonListener();

		// Main view which holds the grid and adds transition when arrow buttons are clicked
		flipper = findViewById(R.id.flipper);
		flipper.removeAllViews();

		paymentCalendarAdapter = new PaymentCalendarAdapter(this, todayDate, this.locale);

		// Populate calendar grid
		populateGridView();
		calendarGridView.setAdapter(paymentCalendarAdapter);
		flipper.addView(calendarGridView, 0);

		// Format calendar menu bar with month name and year name
		formatMenuBarText(currentDate);

		// Initializes Add Payment Button
		addPaymentButton = findViewById(R.id.payment_view);

		// AddPayment Button onClick listener
		addPaymentButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				LayoutInflater inflater = LayoutInflater.from(PaymentCalendarActivity.this);
				View addPaymentDialogView = inflater.inflate(R.layout.activity_add_payment_dialog,
						null);

				// Create a dialog box to enter payment information
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						PaymentCalendarActivity.this, AlertDialog.THEME_HOLO_LIGHT);
				alertDialogBuilder.setView(addPaymentDialogView);

				// Determine currency symbol position
				boolean currencySignPositionBefore = LocaleHelper.getInstance()
						.currencySymbolPosition(Locale.getDefault());

				// Populate currency symbol tag before/after the amount
				TextView enterAmountText = addPaymentDialogView.findViewById(R.id.postsymbolText);
				if (currencySignPositionBefore) {
					enterAmountText = addPaymentDialogView.findViewById(R.id.presymbolText);
				}
				enterAmountText.setText(LocaleHelper.getInstance()
						.getCurrencySymbol(locale));

				final EditText userInputAmount = addPaymentDialogView
						.findViewById(R.id.amountinputedittext);
				final EditText userInputDescription = addPaymentDialogView
						.findViewById(R.id.descriptioninputedittext);

				// Fetch values entered by user
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton(R.string.alert_add_text,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// get user input and log values
										// TODO: Add user input amount to existing payment amount
										Log.i("UserInputAmount", userInputAmount.getText()
												.toString());
										Log.i("UserInputDescription", userInputDescription
												.getText().toString());
									}
								});


				// create and show alert dialog
				alertDialogBuilder.create().show();
			}
		});
	}

	/**
	 * Populates week day names using Globalization API
	 * This is a locale aware method.
	 */
	private void populateWeekDayNames() {
		String[] shortWeekdays = LocaleHelper.getInstance().getShortNameWeekdays(locale);
		TextView day0 = findViewById(R.id.day0);
		day0.setText(shortWeekdays[1]);

		TextView day1 = findViewById(R.id.day1);
		day1.setText(shortWeekdays[2]);

		TextView day2 = findViewById(R.id.day2);
		day2.setText(shortWeekdays[3]);

		TextView day3 = findViewById(R.id.day3);
		day3.setText(shortWeekdays[4]);

		TextView day4 = findViewById(R.id.day4);
		day4.setText(shortWeekdays[5]);

		TextView day5 = findViewById(R.id.day5);
		day5.setText(shortWeekdays[6]);

		TextView day6 = findViewById(R.id.day6);
		day6.setText(shortWeekdays[7]);
	}

	/**
	 * Populates view when clicked on next month arrow
	 */
	private void enterNextMonth() {
		// Increment month by 1
		this.calendarInstance.add(Calendar.MONTH, 1);

		navigationArrowMonthClicked(true);
	}

	/**
	 * Populates view when clicked on previous month arrow
	 */
	private void enterPrevMonth() {
		// Decrement month by 1
		this.calendarInstance.add(Calendar.MONTH, -1);

		navigationArrowMonthClicked(false);
	}

	/**
	 * Method to repopulate calendar grid and show smooth transition to next view
	 */
	private void navigationArrowMonthClicked(boolean isNextMonth) {
		// Populate Calendar Grid UI elements
		populateGridView();
		paymentCalendarAdapter = new PaymentCalendarAdapter(this, this.calendarInstance.getTime(), this.locale);

		calendarGridView.setAdapter(paymentCalendarAdapter);
		formatMenuBarText(calendarInstance.getTime());

		// Smooth animation to go to next view
		if (isNextMonth) {
			flipper.setOutAnimation(AnimationUtils.loadAnimation(PaymentCalendarActivity.this,
					R.anim.push_left_out));
			flipper.setInAnimation(AnimationUtils.loadAnimation(PaymentCalendarActivity.this,
					R.anim.push_left_in));
			flipper.showNext();
		} else {
			flipper.setOutAnimation(AnimationUtils.loadAnimation(PaymentCalendarActivity.this,
					R.anim.push_right_out));
			flipper.setInAnimation(AnimationUtils.loadAnimation(PaymentCalendarActivity.this,
					R.anim.push_right_in));
			flipper.showPrevious();
		}
		flipper.addView(calendarGridView);
		flipper.removeViewAt(0);
	}

	/**
	 * Add Month and year name at Calendar top bar
	 * @param date
	 */
	public void formatMenuBarText(Date date) {
		String shortMonth = LocaleHelper.getInstance()
				.getShortNameMonths(locale)[date.getMonth()];
		calendarInstance.setTime(date);
		currentMonth.setText(shortMonth);
		currentYear.setText(String.valueOf(calendarInstance.get(Calendar.YEAR)));
	}

	/**
	 * Method to populate the main calendar grid view.
	 * This method encapsulates a lot of Globalization APIs. Mostly for:
	 * 1. CurrencyFormat
	 * 2. DateFormat
	 */
	private void populateGridView() {
		LayoutInflater li = LayoutInflater.from(PaymentCalendarActivity.this);
		View gridViewLayout = li.inflate(R.layout.calendar_gridview, null , false);
		calendarGridView = gridViewLayout.findViewById(R.id.gridview_layout);

		/**
		 * Show current date selected and payment amount on that date below calendar grid
		 */
		calendarGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> view, View currentItemView, int position, long arg3) {

				// Fetch locale aware date format
				SimpleDateFormat localeDateFormat = LocaleHelper.getInstance()
						.getLocaleDateFormat(locale);

				// Get date clicked by grid position
				Date dateClicked = paymentCalendarAdapter.getDateByPosition(position);

				TextView dateSelected = findViewById(R.id.date_selected);

				// Show date formatted according to locale date format
				dateSelected.setText(localeDateFormat.format(dateClicked));

				// Fetch payment value for a particular date/position
				double totalAmount = paymentCalendarAdapter.getPaymentByPosition(position);
				TextView totalAmountTextView = findViewById(R.id.total_amount);

				// Globalized currency Format for the locale selected.
				NumberFormat currencyInstanceFormatter = LocaleHelper.getInstance()
						.getCurrencyInstanceFormatter(locale);
				totalAmountTextView.setText(currencyInstanceFormatter.format(totalAmount));

				if (prevItemView != null) {
					paymentCalendarAdapter.defaultColorTextView(prevPosition, prevItemView);
				}

				TextView viewById = currentItemView.findViewById(R.id.calendar_grid_element);
				paymentCalendarAdapter.selectedColorTextView(position, viewById);

				// Store previous position to clear color formatting when next
				// position is selected
				prevPosition = position;
				prevItemView = viewById;

			}
		});
	}

	/**
	 * Add Next/Previous Arrow listeners
	 */
	private void setArrowButtonListener() {
		nextMonth.setOnClickListener(PaymentCalendarActivity.this);
		prevMonth.setOnClickListener(PaymentCalendarActivity.this);
	}

	/**
	 * onClick() Listener binded with this class.
	 * Which is being used by nextMonth and prevMonth ImageViews
	 * @param view
	 */
	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.nextMonth) {
			enterNextMonth();
		} else if (view.getId() == R.id.previousMonth){
			enterPrevMonth();
		}
	}

}
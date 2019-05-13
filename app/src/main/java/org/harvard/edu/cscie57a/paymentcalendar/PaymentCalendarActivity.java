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

public class PaymentCalendarActivity extends Activity implements View.OnClickListener {

	private PaymentCalendarAdapter paymentCalendarAdapter;
	private ViewFlipper flipper;
	private GridView gridView;

	private Date todayDate;
	private Date currentDate;
	private int prevPosition;

	private Calendar calendarInstance = Calendar.getInstance();

	private TextView currentYear;
	private TextView currentMonth;
	private ImageView prevMonth;
	private ImageView nextMonth;
	private Locale locale;

	private Button addPaymentButton;
	private TextView prevItemView;

	public PaymentCalendarActivity() {
		Date date = new Date();
		locale = Locale.getDefault();

		Calendar instance = Calendar.getInstance(locale);
		instance.setTime(date);

		this.todayDate = date;
		this.currentDate = date;
		this.calendarInstance.setTime(date);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendar_main);

		Intent intent = getIntent();
		String username = intent.getStringExtra("username");
		TextView userNameTextView = findViewById(R.id.username_string);
		userNameTextView.setText(username.toUpperCase(locale));

		populateWeekDayNames();

		prevMonth = findViewById(R.id.previousMonth);
		nextMonth = findViewById(R.id.nextMonth);
		currentMonth = findViewById(R.id.currentMonth);
		currentYear = findViewById(R.id.currentYear);

		setArrowButtonListener();

		flipper = findViewById(R.id.flipper);
		flipper.removeAllViews();

		paymentCalendarAdapter = new PaymentCalendarAdapter(this, todayDate, this.locale);
		populateGridView();

		gridView.setAdapter(paymentCalendarAdapter);
		flipper.addView(gridView, 0);
		formatMenuBarText(currentDate);

		addPaymentButton = findViewById(R.id.payment_view);

		// add addPaymentButton listener
		addPaymentButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				LayoutInflater inflater = LayoutInflater.from(PaymentCalendarActivity.this);
				View addPaymentDialogView = inflater.inflate(R.layout.activity_add_payment_dialog, null);

				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						PaymentCalendarActivity.this, AlertDialog.THEME_HOLO_LIGHT);

				alertDialogBuilder.setView(addPaymentDialogView);

				boolean currencySignPositionBefore = PaymentCalendarLocaleHelper.getInstance()
						.currencySignPosition(Locale.getDefault());

				TextView enterAmountText = addPaymentDialogView.findViewById(R.id.postsymbolText);
				if (currencySignPositionBefore) {
					enterAmountText = addPaymentDialogView.findViewById(R.id.presymbolText);
				}

				enterAmountText.setText(PaymentCalendarLocaleHelper.getInstance()
						.getCurrencySymbol(locale));

				final EditText userInputAmount = addPaymentDialogView
						.findViewById(R.id.amountinputedittext);

				final EditText userInputDescription = addPaymentDialogView
						.findViewById(R.id.descriptioninputedittext);

				// set dialog message
				alertDialogBuilder
						.setCancelable(false)
						.setPositiveButton(R.string.alert_add_text,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,int id) {
										// get user input and set it to amountEnteredInDialog
										// edit text
										Log.i("UserInputAmount", userInputAmount.getText().toString());
										Log.i("UserInputDescription", userInputDescription.getText().toString());
									}
								});


				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();

				// show it
				alertDialog.show();

			}
		});
	}

	private void populateWeekDayNames() {
		String[] shortWeekdays = PaymentCalendarLocaleHelper.getInstance().getShortNameWeekdays(locale);
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

	private void enterNextMonth() {
		populateGridView();

		this.calendarInstance.add(Calendar.MONTH, 1);
		paymentCalendarAdapter = new PaymentCalendarAdapter(this, this.calendarInstance.getTime(), this.locale);

		gridView.setAdapter(paymentCalendarAdapter);
		formatMenuBarText(calendarInstance.getTime());

		flipper.addView(gridView);
		flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
		flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
		flipper.showNext();
		flipper.removeViewAt(0);
	}

	private void enterPrevMonth() {
		populateGridView();
		this.calendarInstance.add(Calendar.MONTH, -1);
		paymentCalendarAdapter = new PaymentCalendarAdapter(this, this.calendarInstance.getTime(), this.locale);

		gridView.setAdapter(paymentCalendarAdapter);
		formatMenuBarText(calendarInstance.getTime());
		flipper.addView(gridView);

		flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
		flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));

		flipper.showPrevious();
		flipper.removeViewAt(0);
	}

	public void formatMenuBarText(Date date) {
		String shortMonth = PaymentCalendarLocaleHelper.getInstance()
				.getShortNameMonths(locale)[date.getMonth()];
		calendarInstance.setTime(date);
		currentMonth.setText(shortMonth);
		currentYear.setText(String.valueOf(calendarInstance.get(Calendar.YEAR)));
	}

	private void populateGridView() {
		LayoutInflater li = LayoutInflater.from(PaymentCalendarActivity.this);
		View gridViewLayout = li.inflate(R.layout.calendar_gridview, null , false);
		gridView = gridViewLayout.findViewById(R.id.gridview_layout);

		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> view, View currentItemView, int position, long arg3) {

				SimpleDateFormat localeDateFormat = PaymentCalendarLocaleHelper.getInstance()
						.getLocaleDateFormat(locale);
				Date dateClicked = paymentCalendarAdapter.getDateByPosition(position);

				TextView dateSelected = findViewById(R.id.date_selected);
				dateSelected.setText(localeDateFormat.format(dateClicked));

				double totalAmount = paymentCalendarAdapter.getPaymentByPosition(position);
				TextView totalAmountTextView = findViewById(R.id.total_amount);

				NumberFormat currencyInstanceFormatter = PaymentCalendarLocaleHelper.getInstance()
						.getCurrencyInstanceFormatter(locale);
				totalAmountTextView.setText(currencyInstanceFormatter.format(totalAmount));

				if (prevItemView != null) {
					paymentCalendarAdapter.defaultColorTextView(prevPosition, prevItemView);
				}

				TextView viewById = currentItemView.findViewById(R.id.textviewelement);
				paymentCalendarAdapter.selectedColorTextView(position, viewById);

				prevPosition = position;
				prevItemView = viewById;

			}
		});
	}

	private void setArrowButtonListener() {
		prevMonth.setOnClickListener(this);
		nextMonth.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.nextMonth) {
			enterNextMonth();
		} else if (view.getId() == R.id.previousMonth){
			enterPrevMonth();
		}
	}

}
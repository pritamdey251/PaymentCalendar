package org.harvard.edu.cscie57a.paymentcalendar;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.chrono.IsoChronology;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class PaymentCalendarLocaleHelper {

	private static PaymentCalendarLocaleHelper instance;
	private PaymentCalendarLocaleHelper() {

	}

	public static PaymentCalendarLocaleHelper getInstance() {
		if (instance == null) {
			instance = new PaymentCalendarLocaleHelper();
		}

		return instance;
	}

	public String getCurrencySymbol(Locale locale) {
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(locale);
		return decimalFormatSymbols.getCurrencySymbol();
	}

	public double randomDouble() {
		return ThreadLocalRandom.current().nextDouble(1, 100);
	}

	public boolean currencySignPosition(Locale locale) {
		NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(locale);
		String currencyFormatPattern = ((DecimalFormat) currencyInstance).toLocalizedPattern();

		// Index of currency in pattern tells us whether it is at the beginning or at the end of
		// of the format
		return currencyFormatPattern.indexOf('\u00A4') <= 0;  // currency sign
	}

	public SimpleDateFormat getLocaleDateFormat(Locale locale) {
		String localeDatePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
				FormatStyle.SHORT, null, IsoChronology.INSTANCE, locale);
		return new SimpleDateFormat(localeDatePattern);
	}

	public NumberFormat getCurrencyInstanceFormatter(Locale locale) {
		return NumberFormat.getCurrencyInstance(locale);
	}

	public String[] getShortNameMonths(Locale locale) {
		DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
		return dateFormatSymbols.getShortMonths();
	}

	public String[] getShortNameWeekdays(Locale locale) {
		DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
		return dateFormatSymbols.getShortWeekdays();
	}
}

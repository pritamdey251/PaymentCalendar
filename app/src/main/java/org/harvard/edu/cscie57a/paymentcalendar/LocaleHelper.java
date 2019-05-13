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

/**
 * Locale Helper class to use and create Locale aware objects and formatters
 */
public class LocaleHelper {

	public static final String USERNAME_KEY = "username";
	private static LocaleHelper instance;

	/**
	 * private constructor for Singleton instance
	 */
	private LocaleHelper() {}

	/**
	 * Singleton getInstance()
	 *
	 * @return
	 */
	public static LocaleHelper getInstance() {
		if (instance == null) {
			instance = new LocaleHelper();
		}

		return instance;
	}

	/**
	 * Globailized Locale aware method to fetch currency symbol
	 *
	 * @param locale
	 * @return
	 */
	public String getCurrencySymbol(Locale locale) {
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(locale);
		return decimalFormatSymbols.getCurrencySymbol();
	}

	/**
	 * Generate a random double number for default payment information
	 * @return
	 */
	public double randomDouble() {
		return ThreadLocalRandom.current().nextDouble(1, 10000);
	}

	/**
	 * Determines whether the currency symbol should appear before or after
	 * the currency amount.
	 * @see <a href="https://stackoverflow.com/questions/3809078/location-of-currency-symbol-for-a-particular-currency-in-java">stackoverflow-URL</a>
	 *
	 * @param locale
	 * @return
	 */
	public boolean currencySymbolPosition(Locale locale) {
		NumberFormat currencyInstance = NumberFormat.getCurrencyInstance(locale);
		String currencyFormatPattern = ((DecimalFormat) currencyInstance).toLocalizedPattern();

		// Index of currency in pattern tells us whether it is at the beginning or at the end of
		// of the format
		return currencyFormatPattern.indexOf('\u00A4') <= 0;  // currency sign
	}

	/**
	 * Globailized Locale aware method to get date format
	 *
	 * @param locale
	 * @return
	 */
	public SimpleDateFormat getLocaleDateFormat(Locale locale) {
		String localeDatePattern = DateTimeFormatterBuilder.getLocalizedDateTimePattern(
				FormatStyle.SHORT, null, IsoChronology.INSTANCE, locale);
		return new SimpleDateFormat(localeDatePattern);
	}

	/**
	 * Globailized Locale aware method to get currency number formatter
	 *
	 * @param locale
	 * @return
	 */
	public NumberFormat getCurrencyInstanceFormatter(Locale locale) {
		return NumberFormat.getCurrencyInstance(locale);
	}

	/**
	 * Globailized Locale aware method to get shortname for months
	 *
	 * @param locale
	 * @return
	 */
	public String[] getShortNameMonths(Locale locale) {
		DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
		return dateFormatSymbols.getShortMonths();
	}

	/**
	 * Globailized Locale aware method to get shortname for weekdays
	 *
	 * @param locale
	 * @return
	 */
	public String[] getShortNameWeekdays(Locale locale) {
		DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
		return dateFormatSymbols.getShortWeekdays();
	}
}

package org.harvard.edu.cscie57a.paymentcalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Activity class for Adding Payment
 * This is a TODO: class
 * Currently it takes the user back to home screen with a button activity
 */
public class AddPaymentActivity extends Activity {

    /**
     * Overriden onCreate method to start main activity.
     * This shows the "Under Construction" screen.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_todo);
    }

    /**
     * Method called as a part of onClick event from "Return to Home screen" button
     * @param view
     */
    public void showCalendarView(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

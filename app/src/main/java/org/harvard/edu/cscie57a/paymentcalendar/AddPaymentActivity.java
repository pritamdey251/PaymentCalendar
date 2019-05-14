package org.harvard.edu.cscie57a.paymentcalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddPaymentActivity extends Activity {
    /**
     * Overriden onCreate method to start main activity.
     * This shows the main welcome screen.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment_todo);
    }

    /**
     * Method called as a part of onClick event from "Next" button of home screen
     * @param view
     */
    public void showCalendarView(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}

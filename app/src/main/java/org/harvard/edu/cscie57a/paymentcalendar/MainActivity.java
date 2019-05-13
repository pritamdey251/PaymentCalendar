package org.harvard.edu.cscie57a.paymentcalendar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.harvard.edu.cscie57a.paymentcalendar.R;

/**
 * Main Activity class to initiate application
 */
public class MainActivity extends Activity {

    /**
     * Overriden onCreate method to start main activity.
     * This shows the main welcome screen.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    /**
     * Method called as a part of onClick event from "Next" button of home screen
     * @param view
     */
    public void showCalendarView(View view) {
        Intent intent = new Intent(this,PaymentCalendarActivity.class);

        // Fetches the entered name by the user
        EditText username = findViewById(R.id.name_main);

        // Passes on the name entered by the user to the next activity.
        // In this case it is the main calendar app.
        intent.putExtra(LocaleHelper.USERNAME_KEY, username.getText().toString());
        startActivity(intent);
    }
}

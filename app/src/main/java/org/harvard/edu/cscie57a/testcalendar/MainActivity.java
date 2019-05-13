package org.harvard.edu.cscie57a.testcalendar;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;

import org.harvard.edu.cscie57a.testcalendar.R;

import java.util.Locale;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
    }

    public void showCalendarView(View view) {
        Intent intent = new Intent(this,PaymentCalendarActivity.class);
        EditText username = findViewById(R.id.name_main);
        intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }
}

package aronharder.itemizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

import java.util.Calendar;

/**
 * Allows adding a new Item to the Itemizer
 * Created 2017-05-17
 * By Aron Harder
 */

public class AddItemActivity extends Activity implements View.OnClickListener {
    private NumberPicker month;
    private NumberPicker day;
    private NumberPicker year;
    private EditText desc;
    private EditText amount;
    private Intent call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        getActionBar().setDisplayHomeAsUpEnabled(true); //Create a back button in the action bar

        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(this);
        month = (NumberPicker) findViewById(R.id.month);
        month.setMinValue(1);
        month.setMaxValue(12);
        month.setDisplayedValues(new String[] {"January","February","March","April","May","June",
                "July","August","September","October","November","December"});
        day = (NumberPicker) findViewById(R.id.day);
        day.setMinValue(1);
        day.setMaxValue(31);
        year = (NumberPicker) findViewById(R.id.year);
        year.setMinValue(1900);
        year.setMaxValue(2100);
        desc = (EditText) findViewById(R.id.input_desc);
        amount = (EditText) findViewById(R.id.input_amount);

        call = getIntent();
        if (call.hasExtra("Article ID")){
            String[] today = call.getStringExtra("date").split("/");
            month.setValue(Integer.valueOf(today[0]));
            day.setValue(Integer.valueOf(today[1]));
            year.setValue(Integer.valueOf(today[2]));
            day.setMaxValue(getDaysInMonth(month.getValue(),year.getValue()));
            desc.setText(call.getStringExtra("desc"));
            amount.setText(String.valueOf(call.getDoubleExtra("amount",0.0)));
            add.setText(getResources().getString(R.string.update_item));
        } else {
            Calendar today = Calendar.getInstance();
            month.setValue(today.get(Calendar.MONTH)+1);
            day.setValue(today.get(Calendar.DAY_OF_MONTH));
            year.setValue(today.get(Calendar.YEAR));
            day.setMaxValue(getDaysInMonth(month.getValue(),year.getValue()));
        }

        NumberPicker.OnScrollListener scrollListener = new NumberPicker.OnScrollListener() {
            @Override
            public void onScrollStateChange(NumberPicker picker, int scrollState) {
                if (scrollState == SCROLL_STATE_IDLE) {
                    day.setMaxValue(getDaysInMonth(month.getValue(), year.getValue()));
                }
            }
        };

        month.setOnScrollListener(scrollListener);
        year.setOnScrollListener(scrollListener);
    }

    public void onClick(View v){
        switch(v.getId()) {
            case R.id.add:
                Intent intent = new Intent();
                String s_date = month.getValue() + "/" + day.getValue() + "/" + year.getValue();
                intent.putExtra("date", s_date);
                intent.putExtra("desc", String.valueOf(desc.getText()));
                double amnt;
                try {
                    amnt = Double.valueOf(String.valueOf(amount.getText())); //Get the amount
                } catch (NumberFormatException e){
                    amnt = 0.0; //If there is no amount, set it to 0
                }
                intent.putExtra("amount", amnt);
                if (call.hasExtra("Article ID")) {
                    intent.putExtra("Article ID", call.getIntExtra("Article ID", 0));
                }
                setResult(RESULT_OK, intent);
                finish();
        }
    }

    /**
     * Returns the number of days in that month
     * @param month - January = 1, December = 12
     * @param year - the year, used for determining leapday
     * @return number of days in the month
     */
    private int getDaysInMonth(int month, int year){
        if (month == 4 || month == 6 || month == 9 || month == 11){ //April, June, September, November
            return 30;
        } else if (month == 2){ //February
            if (year%400 == 0) {
                return 29;
            } else if (year%4 == 0 && year%100 != 0){
                return 29;
            } else {
                return 28;
            }
        } else { //31 in all other months, or if improper month number is given
            return 31;
        }
    }
}

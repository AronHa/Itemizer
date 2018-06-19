package aronharder.itemizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Allows adding new files
 * Created 2017-06-14
 * By Aron Harder
 */
public class AddFileActivity extends Activity implements View.OnClickListener {
    private FileManager fm;
    private EditText filename;
    private EditText units;
    private boolean newfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_file);
        getActionBar().setDisplayHomeAsUpEnabled(true); //Create a back button in the action bar

        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(this);
        filename = (EditText) findViewById(R.id.input_filename);
        units = (EditText) findViewById(R.id.input_units);

        Intent call = getIntent();
        String filepath = call.getStringExtra("filepath");
        if (call.hasExtra("filename")){
            filename.setText(call.getStringExtra("filename"));
            String settings = fm.loadFileSettings();
            units.setText(settings); //NOTE: settings is currently only the units
            fm = new FileManager(filepath,call.getStringExtra("filename"));
            newfile = false;
        } else {
            fm = new FileManager(filepath,"Untitled"); //No filename yet
            newfile = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.add:
                if (String.valueOf(filename.getText()).equals("")){
                    Toast.makeText(this,"Please input a filename",Toast.LENGTH_LONG).show();
                } else if (String.valueOf(units.getText()).equals("")){
                    Toast.makeText(this,"Please input a unit type",Toast.LENGTH_LONG).show();
                } else {
                    if (filename.getText().length() < 4 || ! filename.getText().toString().endsWith(".txt")) {
                        filename.setText(filename.getText()+".txt");
                    }
                    if (newfile) {
                        fm.setFilename(String.valueOf(filename.getText()));
                        fm.saveFileSettings(String.valueOf(units.getText())); //NOTE: Settings is currently only the units
                    } else {
                        fm.changeFile(String.valueOf(filename.getText()));
                        fm.saveFileSettings(String.valueOf(units.getText()));
                    }
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
                break;
        }
    }
}

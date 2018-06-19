package aronharder.itemizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Allows multiple files
 * Created 2017-06-12
 * By Aron Harder
 */
//TODO: Sort files by name
public class FileActivity extends Activity implements View.OnClickListener {
    public final String FILEPATH = "Itemizer/";
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file); //This what the user sees (UI)

        updateList();

        ListView file_list = (ListView) findViewById(R.id.file_list);
        file_list.setAdapter(adapter);

        file_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filename = adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Log.d("DEBUG",filename);
                intent.putExtra("filepath",FILEPATH);
                intent.putExtra("filename",filename);
                startActivity(intent);
            }
        });
        file_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener(){
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
                //TODO: On long click, dropdown menu of options to rename or delete file
                String filename = adapter.getItem(position);
                Intent intent = new Intent(getApplicationContext(), AddFileActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("filepath",FILEPATH);
                intent.putExtra("filename",filename);
                startActivityForResult(intent,0);
                return false;
            }
        });


        Button new_file = (Button) findViewById(R.id.new_file);
        new_file.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        updateList();
        ListView file_list = (ListView) findViewById(R.id.file_list);
        file_list.setAdapter(adapter);

    }

    private void updateList(){
        File d = new File(Environment.getExternalStorageDirectory(), FILEPATH);
        if (!d.exists()){
            if (!d.mkdir()){
                Log.e("ERROR","Problem creating file directory");
            }
        }

        String[] files = d.list(new FilenameFilter(){
            @Override
            public boolean accept(File dir, String name){
                return name.endsWith(".txt");
            }
        });
        //Map the list of files to our ListView. Ignore the file activity_list_view.xml, it does nothing.
        adapter = new ArrayAdapter<>(this,R.layout.activity_list_view, R.id.textView, files);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case (R.id.new_file): //Click of "New File" button
                Intent intent = new Intent(getApplicationContext(), AddFileActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("filepath",FILEPATH);
                startActivityForResult(intent,0);
                break;
        }
    }
}

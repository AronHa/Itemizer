package aronharder.itemizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Itemizer
 * Keeps track of items, and computes the sum of their amounts
 * Created 2017-05-17
 * By Aron Harder
 */

//TODO: Have the items displayed in a List View similar to the files
//TODO: Add an info bar at the top (remember that the first line in the file is the units)
//TODO: Be able to filter items by description, date, amount
//TODO: Be able to sort items by description, date, amount
//TODO: Add a "settings" button for filename, info, etc.?
//TODO: On clicking the back button from AddItemActivity, the list is empty. May be fixed by using a List View
public class MainActivity extends Activity implements View.OnClickListener {
    private FileManager fm;
    private LinearLayout items;
    private ArrayList<Article> list;
    private Intent call;
    private String filepath;
    private String filename;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getActionBar().setDisplayHomeAsUpEnabled(true); //Create a back button in the action bar

        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(this);
        items = (LinearLayout) findViewById(R.id.items);
        list = new ArrayList<>();

        call = getIntent();
        filepath = call.getStringExtra("filepath");
        filename = call.getStringExtra("filename");
        fm = new FileManager(filepath,filename);

        String content = fm.loadFileContents(); //Loads saved files; also preserves information if the device is turned sideways
        createArticlesFromFile(content);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (data.hasExtra("Article ID")){
                Article a = list.get(data.getIntExtra("Article ID",0));
                list.remove(a);
                a.setDate(data.getStringExtra("date"));
                a.setDesc(data.getStringExtra("desc"));
                a.setAmount(data.getDoubleExtra("amount",0.0));
                int insert = find_index(a);
                LinearLayout container = (LinearLayout) items.findViewWithTag(data.getIntExtra("Article ID",0));
                items.removeView(container);
                TextView date_text = (TextView) container.findViewWithTag("date");
                date_text.setText(a.getDate());
                TextView desc_text = (TextView) container.findViewWithTag("desc");
                desc_text.setText(a.getDesc());
                TextView amount_text = (TextView) container.findViewWithTag("amount");
                amount_text.setText(String.valueOf(a.getAmount()));
                int increment;
                if ((int) container.getTag() > insert){
                    increment = -1;
                } else {
                    increment = 1;
                }
                for (int i = (int) container.getTag()+increment; i != insert+increment; i+=increment){
                    items.findViewWithTag(i).setTag(i-increment);
                }
                container.setTag(insert);
                items.addView(container,insert);
                list.add(insert,a);
            } else if (data.getExtras().size() == 3) {
                String date = data.getStringExtra("date");
                String desc = data.getStringExtra("desc");
                double amount = data.getDoubleExtra("amount", 0);
                createArticleView(date, desc, amount);
            }
            String content = "";
            for (Article a : list){
                content+=(a.getDate()+" "+a.getDesc()+" "+a.getAmount()+"\n");
            }
            fm.saveFileContents(content);
        }
    }

    /**
     * Creates a new article into list, and puts a corresponding view into items
     * @param date - the date of the article
     * @param desc - the description of the article
     * @param amount - the amount of the article
     */
    private void createArticleView(String date, String desc, double amount){
        //Create a new article
        Article a = new Article(date, desc, amount);
        int insert = find_index(a);
        //create a container to display the article
        LinearLayout container = new LinearLayout(this);
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        container.setWeightSum(6);
        //Add the date to the container
        TextView date_text = new TextView(this);
        date_text.setTag("date");
        date_text.setText(a.getDate());
        date_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
        date_text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 2));
        date_text.setGravity(Gravity.LEFT);
        //Add the description to the container
        TextView desc_text = new TextView(this);
        desc_text.setTag("desc");
        desc_text.setText(a.getDesc());
        desc_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
        desc_text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 3));
        desc_text.setGravity(Gravity.CENTER_HORIZONTAL);
        //Add the amount to the container
        TextView amount_text = new TextView(this);
        amount_text.setText(String.valueOf(a.getAmount()));
        amount_text.setTag("amount");
        amount_text.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
        amount_text.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT, 1));
        amount_text.setGravity(Gravity.RIGHT);
        //Put all the pieces together
        container.addView(date_text);
        container.addView(desc_text);
        container.addView(amount_text);
        //TODO: On long click, dropdown menu of options to rename or delete item
        container.setOnClickListener(this);
        //Increment the tags on the other containers
        for (int i = insert; i < list.size(); i++) {
            items.findViewWithTag(i).setTag(i + 1);
        }
        container.setTag(insert); //Add a tag to this container
        items.addView(container,insert); //Add the container to the view in the correct placement
        list.add(insert,a); //Add the article to the list of articles
    }

    public void onClick(View v){
        if (v.getTag() != null){ //A container was clicked
            Intent intent = new Intent(getApplicationContext(), AddItemActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("Article ID",(int) v.getTag());
            intent.putExtra("date",list.get((int) v.getTag()).getDate());
            intent.putExtra("desc",list.get((int) v.getTag()).getDesc());
            intent.putExtra("amount",list.get((int) v.getTag()).getAmount());
            startActivityForResult(intent,0);
        } else { //A button was clicked
            switch (v.getId()) {
                case R.id.add: //Click on "Add New" Button
                    Intent intent = new Intent(getApplicationContext(), AddItemActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, 0);
                    break;
            }
        }
    }

    /**
     * Creates articles from the contents of a text file
     * @param content - the contents of the text file
     */
    private void createArticlesFromFile(String content){
        String[] articles = content.split("\n");
        for (String a : articles){
            if (a.length() == 0) {
                continue;
            }
            String[] data = a.split(" ");
            String date = data[0];
            Double amount = Double.valueOf(data[data.length-1]);
            String desc = "";
            for (int i = 1; i < data.length-1; i++){ //desc = join(" ",data[1,data.length-1])
                if (i != 1){
                    desc+=" ";
                }
                desc+=data[i];
            }
            createArticleView(date, desc, amount);
        }
    }

    /**
     * Uses a binary search to find where to insert the article in order to keep a sorted list
     * @param a - the article to insert
     * @return the index of where to insert the article
     */
    private int find_index(Article a){
        if (list.size() == 0)
            return 0;
        int low = 0;
        int high = list.size()-1;
        while (true){
            int index = low + (high-low)/2;
            if (a.date_equals_compare(list.get(index).getDate())){
                return index;
            } else if (high == index || low == index){
                if (a.date_greater_compare(list.get(high).getDate())){
                    return high+1;
                } else if (a.date_greater_compare(list.get(low).getDate())){
                    return low+1;
                } else {
                    return index;
                }
            } else if (a.date_greater_compare(list.get(index).getDate())){
                low = index;
            } else if (a.date_less_compare(list.get(index).getDate())){
                high = index;
            }
        }
    }
}

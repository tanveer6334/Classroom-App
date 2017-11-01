package com.diu.tanveer.classroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TeacherListActivity extends AppCompatActivity {

    private Toolbar mSignInToolbar;
    private ListView listView;
    private ArrayList<String> arrayList = new ArrayList<String >();
    private ArrayAdapter<String> arrayAdapter;
    private String[] name = {"Nafis Neehal", "Nayeema Sultana", "Md. Riazur Rahman", "Omar Shrif"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);

        mSignInToolbar = (Toolbar) findViewById(R.id.signInToolbar);
        mSignInToolbar.setTitle("Teacher's name");
        setSupportActionBar(mSignInToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.listview);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, name);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) listView.getItemAtPosition(position);

                // Show Alert
                /*Toast.makeText(getApplicationContext(),
                        "Position :"+itemPosition+"  ListItem : " +itemValue , Toast.LENGTH_LONG)
                        .show();*/

                Intent intent = new Intent(TeacherListActivity.this, TeacherContactActivity.class);
                intent.putExtra("Name", itemValue);
                startActivity(intent);
            }

        });
    }

}

package com.diu.tanveer.classroom;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class InfoActivity extends AppCompatActivity {

    private EditText room_name;
    private ListView listView;
    private ArrayAdapter<String> arrayAdapter;
    private ArrayList<String > list_of_rooms = new ArrayList<>();
    private String name;
    private FirebaseDatabase ref;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference root;
    private Toolbar mSignInToolbar;
    private ProgressDialog progressDialog;

    FirebaseUser user;

    //private DatabaseReference root;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        progressDialog = new ProgressDialog(this);
        mSignInToolbar = (Toolbar) findViewById(R.id.signInToolbar);
        mSignInToolbar.setTitle("Informations");
        setSupportActionBar(mSignInToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        root = FirebaseDatabase.getInstance().getReference("info");//.getRoot();

        room_name = (EditText) findViewById(R.id.info_text);
        listView = (ListView) findViewById(R.id.listView);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list_of_rooms);
        listView.setAdapter(arrayAdapter);

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Set<String> set = new HashSet<String>();
                Iterator i = dataSnapshot.getChildren().iterator();

                while (i.hasNext()){
                    set.add(((DataSnapshot)i.next()).getKey());
                }

                list_of_rooms.clear();
                list_of_rooms.addAll(set);

                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void add_room(View view){
        progressDialog.setMessage("Creating New Information");
        progressDialog.show();
        Map<String,Object> map = new HashMap<String, Object>();
        map.put(room_name.getText().toString(),"");
        root.updateChildren(map);
        progressDialog.dismiss();
        room_name.setText("");
    }
}

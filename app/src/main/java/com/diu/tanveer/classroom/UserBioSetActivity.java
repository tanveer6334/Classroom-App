package com.diu.tanveer.classroom;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class UserBioSetActivity extends AppCompatActivity {

    DatabaseReference myRef;
    EditText name;
    EditText phone1;
    EditText phone2;
    EditText email1;
    EditText email2;
    EditText fb_id;
    EditText pass1, pass2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_bio_set);

        name = (EditText) findViewById(R.id.name);

        phone1 = (EditText) findViewById(R.id.phone1);
        phone2 = (EditText) findViewById(R.id.phone2);
        email1 = (EditText) findViewById(R.id.email1);
        email2 = (EditText) findViewById(R.id.email1);
        fb_id = (EditText) findViewById(R.id.fb_id);
        pass1 = (EditText) findViewById(R.id.pass1);
        pass2 = (EditText) findViewById(R.id.pass2);


        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();     // initialize database
        myRef = database.getReference("user_info");
    }

    public void Insert(View view){
        // 1 - Create child in root object
        // 2 - Assign some value to the child object


        if(pass1.getText().toString().equals(pass2.getText().toString())) {

            String sfname = name.getText().toString();
            String sphone1 = phone1.getText().toString();
            String sphone2 = phone2.getText().toString();
            String semail1 = email1.getText().toString();
            String semail2 = email2.getText().toString();
            String sfb_id = fb_id.getText().toString();
            String spass = pass1.getText().toString();
/*

            myRef.child(sfname).child("Name").setValue(sfname);
            myRef.child(sfname).child("Phone").setValue(sphone1);
            myRef.child(sfname).child("Phone 2").setValue(sphone2);
            myRef.child(sfname).child("Email").setValue(semail1);
            myRef.child(sfname).child("Email 2").setValue(semail2);
            myRef.child(sfname).child("FB ID").setValue(sfb_id);
            myRef.child(sfname).child("Password").setValue(spass);
*/

            HashMap<String, String> dataMap = new HashMap<String, String>();
            dataMap.put("Name",sfname);
            dataMap.put("Email",semail1);
            myRef.push().setValue(dataMap).addOnCompleteListener(new OnCompleteListener<Void>() {    // PUsh with a random node;
                @Override                                                                            // Checks if upload is successful
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(UserBioSetActivity.this, "Stored...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(UserBioSetActivity.this, "Error Storing data", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            //myRef.setValue("Test 2!");
            Toast.makeText(UserBioSetActivity.this, "User Signed Up!", Toast.LENGTH_SHORT).show();

            //myRef.child("Email: ").setValue(data);    //Set child value          //setValue(data);

            //myRef.child("User: ").setValue(user);

             /*myRef.child("Phone no: ").setValue("01563");
            myRef.child("Address: ").setValue("Manual Address");*/
        }
        else{
            Toast.makeText(UserBioSetActivity.this, "Password does not match!", Toast.LENGTH_SHORT).show();
        }
    }

    public void Display(View view){
        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                //Log.d(TAG, "Value is: " + value);
                Toast.makeText(UserBioSetActivity.this, value+" ", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                //Log.w(TAG, "Failed to read value.", error.toException());
                Toast.makeText(UserBioSetActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

}

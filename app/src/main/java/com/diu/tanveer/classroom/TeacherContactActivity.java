package com.diu.tanveer.classroom;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Dictionary;
import java.util.HashMap;

public class TeacherContactActivity extends AppCompatActivity {

    private TextView phoneView;
    private TextView emailView;
    private TextView nameView;
    private String name, phone, email;
    private Toolbar mSignInToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_contact);
        mSignInToolbar = (Toolbar) findViewById(R.id.signInToolbar);
        mSignInToolbar.setTitle("Teacher's Details");
        setSupportActionBar(mSignInToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        nameView = (TextView) findViewById(R.id.name_view);
        phoneView = (TextView) findViewById(R.id.phone_view);
        emailView = (TextView) findViewById(R.id.email_view);

        name = getIntent().getStringExtra("Name");
        //Toast.makeText(this, getIntent().getStringExtra("Name"), Toast.LENGTH_SHORT).show();
        setDetail();
    }

    public void setDetail() {
        if (name.equals("Md. Riazur Rahman")) {
            phone = "01982076848";
            email = "riazur_rahman@daffodilvarsity.edu.bd";
        } else if (name.equals("Nafis Neehal")) {
            phone = "01521220782";
            email = "nafis.cse@diu.edu.bd";
        } else if (name.equals("Nayeema Sultana")) {
            phone = "01723209558";
            email = "nayeema.cse@diu.edu.bd";
        } else if (name.equals("Omar Sharif")) {
            phone = "01672664354";
            email = "omarsharif.ged@diu.edu.bd";
        }


        nameView.setText(name);
        phoneView.setText(phone);
        emailView.setText(email);
    }

    public void makeCall(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phone));
        /*if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED){
            Toast.makeText(this, "Not permitted", Toast.LENGTH_SHORT).show();
            return;
        }*/
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CALL_PHONE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

        }
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    public void sendMail(View view) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto: "+email));
        startActivity(Intent.createChooser(emailIntent, "Send feedback"));

    }
}
package com.diu.tanveer.classroom;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.Manifest.permission_group.CAMERA;
import static com.diu.tanveer.classroom.R.id.name;

/**
 * Created by Tanveer on 5/11/2017.
 */

public class SignUpActivity extends AppCompatActivity {
    private EditText txtEmailAddress, name;
    private EditText txtPassword, txtPassword2;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    private FirebaseUser user;


    private final static int ALL_PERMISSIONS_RESULT = 107;
    private Toolbar mSignInToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(!isNetworkAvailable()){
            startActivity(new Intent(SignUpActivity.this, MainActivity.class));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = (EditText) findViewById(R.id.name_field);
        txtEmailAddress = (EditText) findViewById(R.id.email_field);
        txtPassword = (EditText) findViewById(R.id.password_field);
        txtPassword2 = (EditText) findViewById(R.id.password_field2);
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        mSignInToolbar = (Toolbar) findViewById(R.id.signInToolbar);
        setSupportActionBar(mSignInToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    public void SignUp(View v) {
        if(!txtPassword.getText().toString().equals(txtPassword2.getText().toString())){
            Toast.makeText(this, "Passwod does not match...", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.setMessage("Registering user....");
        progressDialog.show();
        //final ProgressDialog progressDialog = ProgressDialog.show(SignUpActivity.this, "Please wait...", "Processing...", true);

        firebaseAuth.createUserWithEmailAndPassword(txtEmailAddress.getText().toString(), txtPassword.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //progressDialog.dismiss();

                        if (task.isSuccessful()) {
                            NameSet(name.getText().toString());
                            Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(i);
                        } else {
                            //Log.e("ERROR", task.getException().toString());
                            Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    public void NameSet(final String name){

        user = FirebaseAuth.getInstance().getCurrentUser();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(name).build();

        user.updateProfile(profileUpdates);
        Toast.makeText(this, "Welcome "+name+"!", Toast.LENGTH_SHORT).show();
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean decision = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        if (!decision){
            Toast.makeText(this, "Network Unavailable!", Toast.LENGTH_LONG).show();
        }
        return decision;
    }
}

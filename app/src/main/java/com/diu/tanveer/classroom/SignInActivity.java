package com.diu.tanveer.classroom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInActivity extends AppCompatActivity {
    private Toolbar mSignInToolbar;
    private EditText email;
    private EditText password;
    private ProgressDialog progressDialog;
    private String m_Text = "";

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(!isNetworkAvailable()){
            startActivity(new Intent(SignInActivity.this, MainActivity.class));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null){
            // Profile activity
        }

        email = (EditText) findViewById(R.id.email_field);
        password = (EditText) findViewById(R.id.pass_field);
        mSignInToolbar = (Toolbar) findViewById(R.id.signInToolbar);
        setSupportActionBar(mSignInToolbar);

        progressDialog = new ProgressDialog(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }


    public void SignIn(View view){
        String semail = email.getText().toString().trim();
        String spassword = password.getText().toString().trim();

        if(TextUtils.isEmpty(semail)){
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(spassword)){
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage("Signing in....");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(semail, spassword).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();

                if(task.isSuccessful()){
                    startActivity(new Intent(SignInActivity.this, MainActivity.class));
                }
                else {
                    Toast.makeText(SignInActivity.this, "Id or password doesn't match", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    
    public void ResetPassword(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Please enter your email....");

        // Set up the input
        final EditText input = new EditText(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                m_Text = input.getText().toString();
                reset(m_Text);
                Toast.makeText(SignInActivity.this, "Builder ok!", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    public void reset(String result){
        //Toast.makeText(this, "Test3", Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "Tapped reset!", Toast.LENGTH_SHORT).show();
        progressDialog.setMessage("Sending mail to reset password....");
        progressDialog.show();
        if(result.equals("")){
            Toast.makeText(this, "Please enter your mail first!", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            return;
        }
        String emailAddress = result.toString();

        firebaseAuth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            //Log.d(TAG, "Email sent.");
                            Toast.makeText(SignInActivity.this, "Email reset link sent! Check your email...", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        progressDialog.dismiss();
        //Toast.makeText(this, "Test4", Toast.LENGTH_SHORT).show();
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

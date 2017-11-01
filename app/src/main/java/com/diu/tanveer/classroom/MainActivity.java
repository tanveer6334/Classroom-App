/*
 Project Version 0.1
 Date: 17/5/2017
 Project Version 0.2
 Date: 17/5/2017 9:40am
 */
package com.diu.tanveer.classroom;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private String url;
    private WebView wb;
    private ProgressDialog progress;
    private WebSettings settings;
    boolean doubleBackToExitPressedOnce = false;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(getIntent().getBooleanExtra("EXIT", false)){
            finish();
        }

        // Set layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*// Floating action button, uninmplemented
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                //startActivity(new Intent(MainActivity.this, ChatActivity.class));
                *//*startActivity(new Intent(view.getContext(), ChatActivity.class));*//*
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null && isNetworkAvailable()) {
                    startActivity(new Intent(MainActivity.this, GetStartedAcitivity.class));
                    // User is signed in
                } else {
                    // User is signed out
                }
            }
        };

        wb = (WebView) findViewById(R.id.web_view);
        settings = wb.getSettings();
        settings.setJavaScriptEnabled(true);
        //url = "file:///android_asset/routine.html";

        wb.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);

        wb.getSettings().setBuiltInZoomControls(true);
        //wb.getSettings().setUseWideViewPort(true);
        wb.getSettings().setLoadWithOverviewMode(true);

        progress = new ProgressDialog(this);

        startWebView("file:///android_asset/routine.html","");
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startWebView(String url, String message) {

        progress.setMessage(message);
        progress.show();

        wb.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (progress.isShowing()) {
                    progress.dismiss();
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(MainActivity.this, "Error:" + description, Toast.LENGTH_SHORT).show();

            }
        });
        wb.loadUrl(url);
    }



    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            //super.onBackPressed();
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Do you really want to exit?");

            // Set up the input


            // Set up the buttons
            builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    AppExit();

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
            Toast.makeText(this, "Double pressed", Toast.LENGTH_SHORT).show();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if(drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                else if(wb.canGoBack()){
                    wb.goBack();
                    //address.setText(wb.getUrl().toString());
                }
            }
        }, 500);
    }

    public void AppExit()
    {

        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    /*int pid = android.os.Process.myPid();=====> use this if you want to kill your activity. But its not a good one to do.
    android.os.Process.killProcess(pid);*/

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "No settings provided yet!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*
            Routine
         */
        if (id == R.id.routine) {

            startWebView("file:///android_asset/routine.html", "Routine Loading...");
        }

        /*
            Contacts
         */
        else if (id == R.id.contacts) {

            //Toast.makeText(this, "Currently unavailable due to insufficient database", Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this, TeacherListActivity.class));

        }

        /*
            Result
         */
        else if (id == R.id.result) {
            if(isNetworkAvailable())
            startWebView("http://studentportal.diu.edu.bd/result", "Result is loading....");
        }

        /*
            Assignments
         */
        else if (id == R.id.assignment) {
            Toast.makeText(this,"No Assignment Asked yet!",Toast.LENGTH_LONG).show();
        }

        /*
            Exam
         */
        else if (id == R.id.exam) {
            Toast.makeText(this,"No Exam schedule or surprise test possibility available!",Toast.LENGTH_LONG).show();
        }
        else if(id == R.id.info){
            if(isNetworkAvailable())
            startActivity(new Intent(MainActivity.this, InfoActivity.class));
        }

        /*
            Online Code Editor
         */
        else if(id == R.id.programming_editor){
            if(isNetworkAvailable())
            startWebView("https://ideone.com/", "Starting online code editor....");
        }
        else if (id == R.id.solo_learn) {
            if(isNetworkAvailable())
            startWebView("https://www.sololearn.com/Courses/", "All courses are Loading...");
        }
        /*
        *   Python
        * */
        else if (id == R.id.python) {
            startWebView("file:///android_asset/python_tutorial/index.html", "Python tutorial Loading...");
        }

        /*
            Google classroom
         */
        else if(id == R.id.google_classroom){
            if(isNetworkAvailable()) {
                startWebView("https://classroom.google.com/h", "Google Classroom is loading....");
            }
        }

        /*
            Chat
         */
        else if (id == R.id.chat) {
            if(mAuth.getCurrentUser() == null){
                Toast.makeText(this, "Please Log in first!", Toast.LENGTH_SHORT).show();
            }else if(isNetworkAvailable()) {
                startActivity(new Intent(MainActivity.this, ChatActivity.class));
            }
            //startWebView("https://www.facebook.com/messages/t/434285090093010", "Group Chat loading....");
        }

        /*
            Facebook Group
         */
        else if (id == R.id.fb_group) {
            if(isNetworkAvailable())
            startWebView("https://www.facebook.com/groups/DIU.CSE.42th.secA/", "Facebook Group loading....");
        }

        else if (id == R.id.romjan_routine){
            startWebView("file:///android_asset/romjan_2017/index.html","Viewing Romjan Calendar.....");
        }

        /*
            User Activity
         */
        /*else if (id == R.id.user){
            Intent intent = new Intent(MainActivity.this, UserActionActivity.class); // this activity, to acltivity
            startActivity(intent);
        }
        */

        /*
            Log in
         */

        else if (id == R.id.log_in){
            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null && isNetworkAvailable()) {
                startActivity(new Intent(MainActivity.this, GetStartedAcitivity.class));
                // User is signed in
            } else {
                // User is signed out
            }
        }

        /*
            Log out
         */
        else if (id == R.id.log_out){
            /*Intent intent = new Intent(MainActivity.this, GetStartedAcitivity.class); // this activity, to acltivity
            startActivity(intent);*/


            // Firebase sign out
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, GetStartedAcitivity.class));
            Toast.makeText(this, "User signed out!", Toast.LENGTH_SHORT).show();

            // Google sign out

        }
        else if (id == R.id.exit) {
            AppExit();
        }

        /*else if(id == R.id.action_settings) {
            Toast.makeText(this, "No settings customized yet!", Toast.LENGTH_LONG).show();
        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
        Personal browser
     */
    private  class MyBrowser extends WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url){
            view.loadUrl(url);
            return true;
        }
    }

    public  void HtmlLoader(String link, String message){
        //wb.getSettings().setUseWideViewPort(false);   // Show narrow view
        progress.setMessage(message);
        progress.show();
        wb.loadUrl(link);;
        wb.setWebViewClient(new MyBrowser());
        progress.dismiss();
    }

    /*
        Lifetime variable
     */
    public void Write_sharedpref(){
        SharedPreferences sp = getSharedPreferences("SaveFile", Context.MODE_PRIVATE);    // File name

        SharedPreferences.Editor editor = sp.edit();    // Edit Permission
        editor.putString("Text", "Some text");       // (variable name, value)
        editor.apply(); // Apply editor
    }

    public void Read_sharedper(){
        SharedPreferences sp = getSharedPreferences("SaveFile", Context.MODE_PRIVATE);    // File name

        String s = sp.getString("Text", "");        // (variable name, null string)

        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
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

package com.example.godeye;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import java.util.List;

import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText username, password, name;
    Button create_account_button;

    SharedPreferences sharedPreferences;

    private static final int READ_PHONE_STATE_REQUEST_CODE = 10001;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("Auth", Context.MODE_PRIVATE);

        final String auth_key = sharedPreferences.getString("auth_key", null);

        if(auth_key != null){
            startActivity(new Intent(MainActivity.this, Dashboard.class));
            finish();
        }

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        name = findViewById(R.id.fullname);

        create_account_button = findViewById(R.id.create_account_button);
        create_account_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE)  != PackageManager.PERMISSION_GRANTED){
                    show_permission_alert("Allow the app to read the phone's information", "read_phone_state");
                }else {
                    if(username.getText().toString().length() < 5){
                        show_alert("Username must be more than 5 characters");
                        return;
                    }
                    if(password.getText().toString().length() < 5) {
                        show_alert("Password must be more than 5 characters");
                        return;
                    }
                    if(name.getText().toString().length() < 3) {
                        show_alert("Enter a valid name");
                        return;
                    }

                    progressDialog = new ProgressDialog(MainActivity.this);
                    progressDialog.setMessage("Creating account ...");
                    progressDialog.setCancelable(false);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    create_phone_account();

                }
            }
        });

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                collect_installed_app();
//            }
//        }).start();
    }

    private void show_alert(String message) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage(message);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void show_permission_alert(String message, final String permission) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setMessage("Allow the app to read the phone's information");
        dialog.setCancelable(false);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(permission.toLowerCase().equals("read_phone_state")){
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.READ_PHONE_STATE}, READ_PHONE_STATE_REQUEST_CODE);
                }
            }
        });
        dialog.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case READ_PHONE_STATE_REQUEST_CODE:{
                if(grantResults.length > 0 && grantResults[0] != PackageManager.PERMISSION_DENIED){
                    Toast.makeText(getApplicationContext(), "Without this permission, the desired action cannot be performed", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @SuppressLint("HardwareIds")
    private String getDeviceIMEI(){
        String deviceUniqueIdentifier = null;
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        if(null != tm){
            try {
                deviceUniqueIdentifier = tm.getDeviceId();
            }catch (SecurityException e) {
                return null;
            }
        }
        if (null == deviceUniqueIdentifier || 0 == deviceUniqueIdentifier.length()){
            deviceUniqueIdentifier = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceUniqueIdentifier;
    }

    //    private void collect_installed_app() {
//        final PackageManager pm = getPackageManager();
//        @SuppressLint("QueryPermissionsNeeded") List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);
//        for(ApplicationInfo packageinfo: packages){
//            if(pm.getLaunchIntentForPackage(packageinfo.packageName) != null){
//                String app_name = packageinfo.loadLabel(getPackageManager()).toString();
//                String app_package = packageinfo.processName;
//
//                Log.i("0x00sec", "App name: " + app_name + " Package Name: " + app_package);
//            }
//        }
//    }
}
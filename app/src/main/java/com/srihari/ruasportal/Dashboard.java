package com.srihari.ruasportal;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Notification;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class Dashboard extends AppCompatActivity {

    private ImageView attendance_act, Books_act,Assignments_act, Logout, Notifications_act;
    private TextView Name, Registration, Branch, Attendance_status, Submission_date;
    private DatabaseReference mdata;
    private FirebaseAuth mauth;
    private Boolean exit_flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        new AttendanceUpdateRequest().execute();

        attendance_act = findViewById(R.id.attendance_button);
        Books_act = findViewById(R.id.Books_Button);
        Name = findViewById(R.id.Name_view);
        Registration = findViewById(R.id.Registraion_view);
        Branch = findViewById(R.id.Branch_view);
        Logout = findViewById(R.id.Logout_Button);
        Attendance_status = findViewById(R.id.Attendance_Status);
        Assignments_act = findViewById(R.id.Assignments_Button);
        Notifications_act = findViewById(R.id.Notifications_Button);
        Submission_date = findViewById(R.id.Submission_Date);
        mauth = FirebaseAuth.getInstance();

        Status_update();
        Detail_Retrive();

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Dashboard.this)
                        .setMessage("Are you sure you want to Logout?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mauth.signOut();
                                startActivity(new Intent(Dashboard.this, MainActivity.class));
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

        attendance_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent( Dashboard.this, Attendance.class));
            }
        });

        Books_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Books.class));
            }
        });

        Assignments_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Assignments.class));
            }
        });

        Notifications_act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Dashboard.this, Notifications.class));
            }
        });

        date_update();
    }

    private void date_update(){
        mdata = FirebaseDatabase.getInstance().getReferenceFromUrl("https://ruasportal.firebaseio.com/");
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Submission_date.setText(dataSnapshot.child("Submission Date").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Status_update(){
        String User_UID = mauth.getUid().toString();

        mdata = FirebaseDatabase.getInstance().getReference("Users").child(User_UID);
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mdata = FirebaseDatabase.getInstance().getReference("Attendance")
                        .child(dataSnapshot.child("Registration").getValue().toString());
                mdata.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Double Average = Double.valueOf(dataSnapshot.child("Average").getValue().toString());
                        if(Average >= 80) {
                            Attendance_status.setText("Safe with " + Average + "%");
                            Attendance_status.setTextColor(Color.parseColor("#47C624"));
                        } else {
                            Attendance_status.setText("Danger with " + Average + "%");
                            Attendance_status.setTextColor(Color.parseColor("#C62424"));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Detail_Retrive(){
        String User_UID = mauth.getUid().toString();

        mdata = FirebaseDatabase.getInstance().getReference("Users").child(User_UID);
        mdata.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Name.setText(dataSnapshot.child("Name").getValue().toString());
                Registration.setText(dataSnapshot.child("Registration").getValue().toString());
                Branch.setText(dataSnapshot.child("Branch").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        if(exit_flag){
            exit_flag = false;
            Toast.makeText(this, "Press Back again to leave", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            exit_flag = true;
        }
    }

    public class AttendanceUpdateRequest extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url = new URL("https://script.google.com/macros/s/AKfycbzbPWTXJ04bA9HOxIR_uRdLHS_kdEaOnp-ALOzYRvJRG1n6alDN/exec");

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(15000);
                connection.setConnectTimeout(15000);
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.setDoOutput(true);

                int responseCode = connection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    return new String("Attendance Update Success");

                }else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String(e.toString());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(Dashboard.this, result, Toast.LENGTH_SHORT).show();
        }
    }

}

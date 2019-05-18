package com.srihari.ruasportal;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HttpsURLConnection;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class Attendance extends AppCompatActivity {

    private RelativeLayout layout;
    private RingProgressBar Subject1_bar, Subject2_bar, Subject3_bar, Subject4_bar;
    private TextView  Attendance_Status, Subject1_Attendance, Subject2_Attendance, Subject3_Attendance, Subject4_Attendance;
    private ImageView refresh;
    private DatabaseReference databaseReference, Registration_Data, qr_data;
    private Button Scanner, compilers, cg, dm_2,networks;
    private FirebaseAuth firebaseAuth;
    private String Registration_Number, Scanned_Data;
    private int Subject_Flag = 0;
    private boolean Visibility_flag = false, Attendance_Success_Flag = false;
    private String Subject_Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        layout = findViewById(R.id.Subject_Chooser);

        Update_Attendance();
        Attendance_Activator();

        Scanner = findViewById(R.id.Scan_Button);
        Subject1_bar = findViewById(R.id.Subject1_progress);
        Subject2_bar = findViewById(R.id.Subject2_progress);
        Subject3_bar = findViewById(R.id.Subject3_progress);
        Subject4_bar = findViewById(R.id.Subject4_progress);

        Subject1_Attendance = findViewById(R.id.Subject1_Attendance);
        Subject2_Attendance = findViewById(R.id.Subject2_Attendance);
        Subject3_Attendance = findViewById(R.id.Subject3_Attendance);
        Subject4_Attendance = findViewById(R.id.Subject4_Attendance);

        compilers = findViewById(R.id.Compilers_Button);
        cg = findViewById(R.id.CG_Button);
        dm_2 = findViewById(R.id.DM2_Button);
        networks = findViewById(R.id.Networks_Button);

        Attendance_Status = findViewById(R.id.attendance_Status);
        refresh = findViewById(R.id.refresh_button);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(Attendance.this, "Refreshing", Toast.LENGTH_SHORT).show();
                Update_Attendance();
            }
        });

        cg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subject_Flag = 3;
                Subject_Name = "Computer Graphics";
                QR_Scanning();
            }
        });

        compilers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subject_Flag = 4;
                Subject_Name = "Compilers";
                QR_Scanning();
            }
        });

        dm_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subject_Flag = 5;
                Subject_Name = "Discrete Mathematics-2";
                QR_Scanning();
            }
        });

        networks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Subject_Flag = 6;
                Subject_Name = "Networks";
                QR_Scanning();
            }
        });

        Scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Attendance_Activator();
                if(!Attendance_Success_Flag){
                    Visibility_flag = true;
                    Scanner.setVisibility(View.INVISIBLE);
                    layout.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(Attendance.this, "Attendance already given Successfully", Toast.LENGTH_SHORT).show();
                }
            }
        });

        String Mauth = FirebaseAuth.getInstance().getUid();
        Query query = FirebaseDatabase.getInstance().getReference("Users").child(Mauth);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Registration_Number = dataSnapshot.child("Registration").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Update_Attendance(){
        new AttendanceUpdateRequest().execute();

        String UID = FirebaseAuth.getInstance().getUid().toString();
        Registration_Data = FirebaseDatabase.getInstance().getReference("Users").child(UID);;
        Registration_Data.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://ruasportal.firebaseio.com/Attendance")
                        .child(dataSnapshot.child("Registration").getValue().toString());

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        int sub1_prog = Integer.parseInt(dataSnapshot.child("Compilers").getValue().toString());
                        int sub2_prog = Integer.parseInt(dataSnapshot.child("Computer Graphics").getValue().toString());
                        int sub3_prog = Integer.parseInt(dataSnapshot.child("Discrete Mathematics-2").getValue().toString());
                        int sub4_prog = Integer.parseInt(dataSnapshot.child("Networks").getValue().toString());


                        Subject1_bar.setProgress(sub1_prog);
                        Subject2_bar.setProgress(sub2_prog);
                        Subject3_bar.setProgress(sub3_prog);
                        Subject4_bar.setProgress(sub4_prog);

                        Subject1_Attendance.setText(String.valueOf(sub1_prog));
                        Subject2_Attendance.setText(String.valueOf(sub2_prog));
                        Subject3_Attendance.setText(String.valueOf(sub3_prog));
                        Subject4_Attendance.setText(String.valueOf(sub4_prog));

                        if(sub1_prog >= 80){
                            Subject1_bar.setRingColor(Color.parseColor("#47C624"));
                            Subject1_bar.setRingProgressColor(Color.parseColor("#47C624"));
                        }else{
                            Subject1_bar.setRingColor(Color.parseColor("#C62424"));
                            Subject1_bar.setRingProgressColor(Color.parseColor("#C62424"));
                        }

                        if(sub2_prog >= 80){
                            Subject2_bar.setRingColor(Color.parseColor("#47C624"));
                            Subject2_bar.setRingProgressColor(Color.parseColor("#47C624"));
                        }else{
                            Subject2_bar.setRingColor(Color.parseColor("#C62424"));
                            Subject2_bar.setRingProgressColor(Color.parseColor("#C62424"));
                        }

                        if(sub3_prog >= 80){
                            Subject3_bar.setRingColor(Color.parseColor("#47C624"));
                            Subject3_bar.setRingProgressColor(Color.parseColor("#47C624"));
                        }else{
                            Subject3_bar.setRingColor(Color.parseColor("#C62424"));
                            Subject3_bar.setRingProgressColor(Color.parseColor("#C62424"));
                        }

                        if(sub4_prog >= 80){
                            Subject4_bar.setRingColor(Color.parseColor("#47C624"));
                            Subject4_bar.setRingProgressColor(Color.parseColor("#47C624"));
                        }else{
                            Subject4_bar.setRingColor(Color.parseColor("#C62424"));
                            Subject4_bar.setRingProgressColor(Color.parseColor("#C62424"));
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

    public class AttendanceUpdateRequest extends AsyncTask<String, Void, String>{

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
            Toast.makeText(Attendance.this, result, Toast.LENGTH_SHORT).show();
        }
    }

    private void QR_Scanning(){
        IntentIntegrator integrator = new IntentIntegrator(Attendance.this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Scan for " + Subject_Name);
        integrator.setBeepEnabled(true);
        integrator.setCameraId(0);
        integrator.setBarcodeImageEnabled(false);
        integrator.initiateScan();
        layout.setVisibility(View.INVISIBLE);
        Scanner.setVisibility(View.VISIBLE);
        Visibility_flag = false;
        Attendance_Success_Flag = true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        if(result != null){
            Scanned_Data = result.getContents();
            Attendance_Success_Flag = true;
            if(Scanned_Data != null){

                qr_data = FirebaseDatabase
                        .getInstance()
                        .getReferenceFromUrl("https://ruasportal.firebaseio.com/QR Codes")
                        .child(Subject_Name);

                qr_data.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(Scanned_Data.matches(dataSnapshot.child("1").getValue().toString())){
                            new SendRequest().execute();
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public class SendRequest extends AsyncTask<String, Void, String> {


        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try{
                Attendance_Success_Flag = true;
                URL url = new URL("https://script.google.com/macros/s/AKfycbxeJyCkPZDi4-2Y4wmhOv_X5d0Rnp6EuPl6kDvgKFUYfB01utM/exec");

                final JSONObject postDataParams = new JSONObject();
                postDataParams.put("sdata",Registration_Number + "+" +Subject_Flag);
                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(20000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    Attendance_Success_Flag = true;
                    return sb.toString();

                }
                else {
                    Attendance_Success_Flag = false;
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                Attendance_Success_Flag = false;
                return new String("Registration Number Does not Exist or " + "Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Attendance_Status.setText(result);
            if(Attendance_Success_Flag){
                Toast.makeText(Attendance.this, result, Toast.LENGTH_SHORT).show();
            }else Toast.makeText(getApplicationContext(), result,Toast.LENGTH_LONG).show();
            new AttendanceUpdateRequest().execute();
        }
    }

    private void Attendance_Activator() {

        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("Users")
                .child(FirebaseAuth.getInstance().getUid().toString());

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DatabaseReference databaseReference = FirebaseDatabase
                        .getInstance()
                        .getReferenceFromUrl("https://ruasportal.firebaseio.com/Attendance")
                        .child(dataSnapshot.child("Registration").getValue().toString());

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String Last_mark_time = dataSnapshot.child("Last marked time").getValue().toString();
                        if(Last_mark_time.matches("")){
                            return ;
                        }
                        long time_border = Long.parseLong(Last_mark_time) + 20000L;
                        Date date = new Date();

                        if(time_border > new Date().getTime()){
                            Attendance_Status.setText("Attendance Given");
                            layout.setVisibility(View.INVISIBLE);
                            Scanner.setVisibility(View.VISIBLE);
                            Visibility_flag = false;
                            Attendance_Success_Flag = true;
                        }else{
                            Attendance_Success_Flag = false;
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

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


    @Override
    public void onBackPressed() {
        if(Visibility_flag){
            layout.setVisibility(View.INVISIBLE);
            Scanner.setVisibility(View.VISIBLE);
            Visibility_flag = false;
        }else super.onBackPressed();
    }
}

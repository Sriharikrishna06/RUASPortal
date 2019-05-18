package com.srihari.ruasportal;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Notifications extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView Notifications_View;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        Notifications_View = findViewById(R.id.Notifications_Recycler);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        Notifications_View.setLayoutManager(linearLayoutManager);
        Notifications_View.setHasFixedSize(true);
        Notifications_View.smoothScrollToPosition(RecyclerView.FOCUS_DOWN);
        fetch();
    }

    private void fetch() {
        Query query = FirebaseDatabase.getInstance().getReferenceFromUrl("https://ruasportal.firebaseio.com/Notifications");

        FirebaseRecyclerOptions<notifications_adapter> options =
                new FirebaseRecyclerOptions.Builder<notifications_adapter>()
                .setQuery(query, new SnapshotParser<notifications_adapter>() {
                    @NonNull
                    @Override
                    public notifications_adapter parseSnapshot(@NonNull DataSnapshot snapshot) {
                        String Message = snapshot.child("Message").getValue().toString();
                        String Title = snapshot.child("Title").getValue().toString();
                        String Time = snapshot.getRef().getKey().toString();
                        Log.d("Title", snapshot.child("Title").getValue().toString());
                        return new notifications_adapter(Title, Time, Message);
                    }
                }).build();

        adapter = new FirebaseRecyclerAdapter<notifications_adapter, viewholder>(options) {

            @Override
            public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.notification_design, viewGroup, false);
                return new viewholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull viewholder holder, int position,notifications_adapter model) {
                Log.d("Received noti",model.getBody());
                holder.setTitle(model.getTitle());
                holder.setTime(model.getTime());
                holder.setBody(model.getBody());

            }
        };

        Notifications_View.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public static class viewholder extends RecyclerView.ViewHolder{
        private TextView Title, Time, Body;
        private LinearLayout linearLayout;

        public viewholder(View itemView) {
            super(itemView);
            Title = itemView.findViewById(R.id.Title);
            Time = itemView.findViewById(R.id.Time);
            Body = itemView.findViewById(R.id.Body);
            linearLayout = itemView.findViewById(R.id.Notifications_Layout);
        }

        public void setTitle(String string){
            Title.setText(string);
        }

        public void setBody(String string){
            Body.setText(string);
        }

        public void setTime(String string) {
            Time.setText(string);
        }
    }
}

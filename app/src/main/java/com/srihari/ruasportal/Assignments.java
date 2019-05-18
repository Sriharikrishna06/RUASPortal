package com.srihari.ruasportal;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class Assignments extends AppCompatActivity {

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerAdapter adapter;
    private DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignments);

        recyclerView = findViewById(R.id.Assingments_Recycler);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();
    }

    private void fetch() {

        Query query = FirebaseDatabase.getInstance().getReference("Assignments");
        FirebaseRecyclerOptions<Assignment_Adapter> options =
                new FirebaseRecyclerOptions.Builder<Assignment_Adapter>()
                .setQuery(query, new SnapshotParser<Assignment_Adapter>() {
                    @NonNull
                    @Override
                    public Assignment_Adapter parseSnapshot(@NonNull DataSnapshot snapshot) {
                        return new Assignment_Adapter(snapshot.getKey().toString(),
                                snapshot.getValue().toString());
                    }
                }).build();

        adapter = new FirebaseRecyclerAdapter<Assignment_Adapter, viewholder>(options) {

            @NonNull
            @Override
            public viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.assingments_design, viewGroup, false);
                return new Assignments.viewholder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull viewholder holder, int position, @NonNull final Assignment_Adapter model) {
                holder.setDataKey(model.getKey());
                final String Value = model.getValue().toString();

                holder.Download.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(Value);
                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setTitle(model.getKey());
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        Long Reference = downloadManager.enqueue(request);
                        Toast.makeText(Assignments.this, "Download Started", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        };

        recyclerView.setAdapter(adapter);
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
        public TextView DataKey;
        public String DataValue;
        public ImageView Download;
        public LinearLayout linearLayout;

        public viewholder(View view){
            super(view);
            linearLayout = view.findViewById(R.id.Notes_Layout);
            DataKey = view.findViewById(R.id.Notes_Name);
            Download = view.findViewById(R.id.Download_Button);
        }

        public void setDataKey(String string)  {
            DataKey.setText(string);
        }

        public void setDataValue(String string){
            DataValue = string;
        }

    }
}

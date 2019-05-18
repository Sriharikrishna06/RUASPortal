package com.srihari.ruasportal;


import android.animation.Animator;
import android.app.DownloadManager;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Books extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView Books_View;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books);

        Books_View = (RecyclerView)findViewById(R.id.BookDetails_List);

        linearLayoutManager = new LinearLayoutManager(this);
        Books_View.setLayoutManager(linearLayoutManager);
        Books_View.setHasFixedSize(true);
        fetch();
    }

    private void fetch(){
        Query query = FirebaseDatabase.getInstance().getReference("Faculty");

        FirebaseRecyclerOptions<Faculty> options =
                new FirebaseRecyclerOptions.Builder<Faculty>()
                .setQuery(query, new SnapshotParser<Faculty>() {
                    @NonNull
                    @Override
                    public Faculty parseSnapshot(DataSnapshot snapshot) {
                        return new Faculty(snapshot.child("Course").getValue().toString(),
                                snapshot.child("Faculty_Name").getValue().toString());
                    }
                }).build();

        adapter = new FirebaseRecyclerAdapter<Faculty, viewholder>(options) {

            @Override
            public viewholder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.row, viewGroup, false);

                return new viewholder(view);
            }

            @Override
            protected void onBindViewHolder(viewholder holder, final int position, final Faculty faculty) {
                holder.setSubject(faculty.getCourse());
                holder.setFaculty(faculty.getFaculty_Name());

                holder.linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(Books.this, faculty.getCourse(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Books.this, Notes.class);
                        intent.putExtra("Coursing", faculty.getCourse());
                        startActivity(intent);
                    }
                });
            }
        };

        Books_View.setAdapter(adapter);
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
        public TextView Subject, Faculty;
        public LinearLayout linearLayout;

        public viewholder(View itemView) {
            super(itemView);
            Subject = itemView.findViewById(R.id.Subject_view);
            Faculty = itemView.findViewById(R.id.Faculty_view);
            linearLayout = itemView.findViewById(R.id.LinearLayout_row);
        }

        public void setSubject(String string){
            Subject.setText(string);
        }

        public void setFaculty(String string){
            Faculty.setText(string);
        }
    }
}



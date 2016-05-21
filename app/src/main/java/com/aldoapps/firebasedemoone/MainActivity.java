package com.aldoapps.firebasedemoone;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView mResult;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference("message");
    private DatabaseReference mDinoRef = mDatabase.getReference("dinosaur");
    private Dinosaur mCurrentDinosaur;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResult = (TextView) findViewById(R.id.result);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
            Toast.makeText(MainActivity.this, "asdf", Toast.LENGTH_SHORT).show();
//            setNewDinosaur();
            updateDinosaur();
        });


        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue().toString();
                mResult.setText(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void deleteDinosaur() {
        mDinoRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(MainActivity.this, "Dinosaur has been deleted", Toast.LENGTH_SHORT).show();
            }
        });

        mDinoRef.setValue(null, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(MainActivity.this, "Dinosaur has been nulled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setNewDinosaur(){
        Dinosaur stegosaurus = new Dinosaur("Stegosaurus", 12, 30.1, 1000, 4);
        mDinoRef.setValue(stegosaurus);
    }

    // Wrong way
    private void updateDinosaur(){
        Map<String, Object> dinoMap = new HashMap<>();
        dinoMap.put("name", "Pterodactyl");
        // all other field is set to null, so firebase will delete all of it
        // see deletedinosaur method
        mDinoRef.updateChildren(dinoMap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void actuallyUpdateDinosaur(){
        mDinoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCurrentDinosaur = dataSnapshot.getValue(Dinosaur.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mCurrentDinosaur.setName("Pterodactyl");
        Map<String, Object> dinoMap = mCurrentDinosaur.getMap();

        mDinoRef.updateChildren(dinoMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if(databaseError == null){
                    Toast.makeText(MainActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void pushNewDinosaur(){
        Dinosaur stegosaurus = new Dinosaur("Stegosaurus", 12, 30.1, 1000, 4);

        String dinoKey = mDinoRef.push().getKey();
        DatabaseReference newDinoRef = mDinoRef.child(dinoKey);
        newDinoRef.setValue(stegosaurus);
    }

}

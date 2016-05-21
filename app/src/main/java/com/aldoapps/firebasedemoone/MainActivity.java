package com.aldoapps.firebasedemoone;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView mResult;
    private TextView mDinoInfo;
    private TextView mQueryInfo;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference("message");
    private DatabaseReference mDinoRef = mDatabase.getReference("dinosaur");
    private Dinosaur mCurrentDinosaur;
    private ValueEventListener mHelloListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResult = (TextView) findViewById(R.id.result);
        mDinoInfo = (TextView) findViewById(R.id.dino_info);
        mQueryInfo = (TextView) findViewById(R.id.query_info);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
            Toast.makeText(MainActivity.this, "Clicked", Toast.LENGTH_SHORT).show();
//            setNewDinosaur();
//            updateDinosaur();
//            updateCurrentDinosaur();
//            deleteDinosaur();
//            pushSomeDinosaurs();
            queryDinosaur();
        });

        mDinoRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                mDinoInfo.setText("Dino Added");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                mDinoInfo.setText("Dino Updated");
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                mDinoInfo.setText("Dino Removed");
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                mDinoInfo.setText("Dino Moved");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                mDinoInfo.setText("Dino Cancelled");
            }
        });

        mHelloListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String result = dataSnapshot.getValue().toString();
                mResult.setText(result);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mRef.addValueEventListener(mHelloListener);
    }

    private void deleteDinosaur() {
        mDinoRef.removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                Toast.makeText(MainActivity.this, "Dinosaur has been deleted", Toast.LENGTH_SHORT).show();
            }
        });

        // or you can use this method
//        mDinoRef.setValue(null, new DatabaseReference.CompletionListener() {
//            @Override
//            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//                Toast.makeText(MainActivity.this, "Dinosaur has been nulled", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    private void setNewDinosaur(){
        Dinosaur stegosaurus = new Dinosaur("Stegosaurus", 12, 30.1, 1000, 4);
        mDinoRef.setValue(stegosaurus);
    }

    // Kinda wrong way
    // you need to add annotation in Dinosaur
    private void updateDinosaur(){
        Map<String, Object> dinoMap = new HashMap<>();
        dinoMap.put("name", "Pterodactyl");
        // if you didn't specify Dinosaur POJO
        // all other field is set to null, so firebase will delete all of it
        // see deletedinosaur method
        mDinoRef.updateChildren(dinoMap);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRef.removeEventListener(mHelloListener);
    }

    private void updateCurrentDinosaur(){
        mDinoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mCurrentDinosaur = dataSnapshot.getValue(Dinosaur.class);

                mCurrentDinosaur.setName("Dingodile");
                Map<String, Object> dinoMap = mCurrentDinosaur.getMap();

                mDinoRef.updateChildren(dinoMap, (databaseError, databaseReference) -> {
                    if(databaseError == null){
                        Toast.makeText(MainActivity.this, "Updated!", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void queryDinosaur(){
        StringBuilder stringBuilder = new StringBuilder();

        Query queryRef = mDinoRef.orderByChild("leg");
//        Query queryRef = mDinoRef.orderByChild("name").equalTo("Dingodile");
        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Dinosaur dinosaur = dataSnapshot.getValue(Dinosaur.class);
                stringBuilder.append(dinosaur.getName());

                mQueryInfo.setText("  ");
                mQueryInfo.setText(stringBuilder.toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void pushNewDinosaur(){
        Dinosaur stegosaurus = new Dinosaur("Stegosaurus", 12, 30.1, 1000, 4);

        String dinoKey = mDinoRef.push().getKey();
        DatabaseReference newDinoRef = mDinoRef.child(dinoKey);
        newDinoRef.setValue(stegosaurus);
    }

    private void pushSomeDinosaurs(){
        Dinosaur stegosaurus = new Dinosaur("Stegosaurus", 12, 30.1, 1000, 4);
        Dinosaur pterodatyl = new Dinosaur("Pterodactyl", 7, 48, 500, 2);
        Dinosaur dingodile = new Dinosaur("Dingodile", 9.7, 19.5, 100, 2);

        mDinoRef.push().setValue(stegosaurus);
        mDinoRef.push().setValue(pterodatyl);
        mDinoRef.push().setValue(dingodile);
    }
}

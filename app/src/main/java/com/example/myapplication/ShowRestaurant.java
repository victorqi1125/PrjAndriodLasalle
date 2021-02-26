package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class ShowRestaurant extends AppCompatActivity  {

     int min = 1;
     int max = 8;
     int random = new Random().nextInt((max - min) + 1) + min;
     String r = Integer.toString(random);


     TextView tvPhone , tvname , tvaddress;
     DatabaseReference databaseReference , imgDb;
     ImageView HotelImage;
     Button spinAgain;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_restaurant);

        tvname = findViewById(R.id.textViewNameofPlace);
       tvaddress = findViewById(R.id.textView8Addresss);
        tvPhone = findViewById(R.id.textView7PhoneNumber);
        HotelImage = findViewById(R.id.imageView3);
        spinAgain = findViewById(R.id.btnSpinAgain);

        spinAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBacktoWheel();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Restaurant").child("Restaurant" + r);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = snapshot.child("Name").getValue().toString();
                String address = snapshot.child("Address").getValue().toString();
                String phone = snapshot.child("Phone").getValue().toString();

                tvname.setText(name);
                tvaddress.setText(address);
                tvPhone.setText(phone);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        imgDb = FirebaseDatabase.getInstance().getReference().child("Restaurant").child("Restaurant" + r).child("image");
        imgDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String link = snapshot.getValue(String.class);
                Picasso.get().load(link).into(HotelImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void goBacktoWheel() {
        Intent intent = new Intent(this,SpinTheWheel.class);
        startActivity(intent);
    }


}
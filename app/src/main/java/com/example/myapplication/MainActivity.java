package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    TextView fullName,email,Phone , emailVerification;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore; // for the data retrieval
    String userId;
    Button btnResendCode , SpinWheel , ChangeProfileImage;
    FirebaseUser user;
    ImageView profileImage;
    StorageReference storageReference;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //declaring variables
        fullName = findViewById(R.id.textViewProfileName);
        email = findViewById(R.id.textViewProfileEmail);
        Phone = findViewById(R.id.textViewProfilePhone);

        btnResendCode =  findViewById(R.id.btnResendVerification);
        emailVerification = findViewById(R.id.textViewEmailVerification);
        profileImage = findViewById(R.id.ProfileImage);
        ChangeProfileImage = findViewById(R.id.btnProfilePic);

        SpinWheel = findViewById(R.id.btnSpintheWheel);
        SpinWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWheel();
            }
        });

        //instantiate firebaseAuth and Firebase Firestore

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        userId =  fAuth.getCurrentUser().getUid();
         user = fAuth.getCurrentUser();

        if(!user.isEmailVerified()){
            emailVerification.setVisibility(View.VISIBLE);
            btnResendCode.setVisibility(View.VISIBLE);

            btnResendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View view) {

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(view.getContext(),"Verfication Email has been sent",Toast.LENGTH_SHORT);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "OnFailure: email not sent" + e.getMessage() );
                        }
                    });

                }
            });
        }
        else {

            //getting the data to display

            DocumentReference documentReference = fStore.collection("users").document(userId);
            //document refrence contains the refrence to the database

            documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {

                    if(documentSnapshot.exists()) {
                        Phone.setText(documentSnapshot.getString("phone"));
                        fullName.setText(documentSnapshot.getString("fName"));
                        email.setText(documentSnapshot.getString("email"));
                    }
                    else {
                        Log.d("tag" , "onEven: Document do not exist");
                    }

                }
            });

            ChangeProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //open gallery
                    Intent i = new Intent(view.getContext(),EditProfile.class);
                    i.putExtra("fullName",fullName.getText().toString());
                    i.putExtra("email",email.getText().toString());
                    i.putExtra("phone",Phone.getText().toString());
                    startActivity(i);

                   // Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                  //  startActivityForResult(openGalleryIntent, 1000);
                }
            });



        }


    }






    public void openWheel() {
        Intent intent = new Intent(this, SpinTheWheel.class);
        startActivity(intent);
    }


    public void logout(View view) {
        try {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getApplicationContext(),Login.class));
            finish();
        }catch (Exception e){
            Log.d("Logout","Error"+ e.getMessage());
        }

    }





}
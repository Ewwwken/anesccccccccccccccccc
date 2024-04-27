package com.example.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class AjouterProduitActivity extends AppCompatActivity {
    private TextInputEditText labelInput;
    private TextInputEditText descInput;
    private Button addPhotobtn;
    private ImageView imgview;
    private Button ajouterProduitbtn;
    private Button annulerProduitbtn;
    private Bitmap photo;
    private Produit newProduit;
    private Produit receivedEditProduit;
    private int position;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajouter_produit);
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://fir-exople-default-rtdb.firebaseio.com/");
        databaseReference = database.getReference("Produits");


        labelInput = findViewById(R.id.labelleInputProduit);
        descInput = findViewById(R.id.descInputProduit);
        addPhotobtn = findViewById(R.id.addphoto);
        imgview = findViewById(R.id.produitimage);
        ajouterProduitbtn = findViewById(R.id.ajouterProduit);
        annulerProduitbtn = findViewById(R.id.annulerProduit);

        addPhotobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

        annulerProduitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ajouterProduitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = labelInput.getText().toString();
                String desc = descInput.getText().toString();
                if (label.isEmpty() || desc.isEmpty()) {
                    Toast.makeText(AjouterProduitActivity.this, "Please fill the form", Toast.LENGTH_SHORT).show();
                    return; // Exit early if form fields are empty
                }

                if (photo == null) {
                    Toast.makeText(AjouterProduitActivity.this, "No photo taken", Toast.LENGTH_SHORT).show();
                    return; // Exit early if no photo was taken
                }

                String imageString = Produit.BitmapToString(photo); // Convert photo to string
                String id = generateUniqueId(); // Generate unique ID for new product
                Produit newProduit = new Produit(id, label, desc, imageString);

                // Set result and finish if editing an existing product
                Intent intent = new Intent();
                if (receivedEditProduit != null) {
                    intent.putExtra("EditedProduit", newProduit);
                    intent.putExtra("position", position);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    // If not editing, proceed to add new product to Firebase
                    databaseReference.child(id).setValue(newProduit)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(AjouterProduitActivity.this, "Product added successfully!", Toast.LENGTH_SHORT).show();
                                intent.putExtra("newProduit", newProduit);
                                setResult(Activity.RESULT_OK, intent);
                                finish();  // Close the activity only on successful save
                            })
                            .addOnFailureListener(e -> Toast.makeText(AjouterProduitActivity.this, "Failed to add product: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }
        });

    }

    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1888);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1888 && resultCode == Activity.RESULT_OK) {
            if (data != null && data.getExtras() != null) {
                photo = (Bitmap) data.getExtras().get("data");
                imgview.setImageBitmap(photo);
            } else {
                Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String generateUniqueId() {
        // Generate a unique id using UUID
        return UUID.randomUUID().toString();
    }
}

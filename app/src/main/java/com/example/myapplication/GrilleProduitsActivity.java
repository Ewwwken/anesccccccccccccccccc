package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class GrilleProduitsActivity extends AppCompatActivity {
    // Declare REQUEST_CODE_EDIT_PRODUIT
    private static final int REQUEST_CODE_PRODUIT = 210;
    private static final int REQUEST_CODE_EDIT_PRODUIT = 211;

    private ArrayList<Produit> RecievedListProduit = new ArrayList<>();
    private GridAdapter adapter;
    RelativeLayout relativeLayout = null;
    TextView NoProduittextView = null;
    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grille_produits);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gridView = findViewById(R.id.gridview);
        adapter = new GridAdapter(this, RecievedListProduit);
        gridView.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_grille_produits, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.addNewCategorie) {
            Intent intent = new Intent(GrilleProduitsActivity.this, AjouterProduitActivity.class);
            startActivityForResult(intent, REQUEST_CODE_PRODUIT );

            return true;
        }
        return super.onOptionsItemSelected(item);
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PRODUIT && resultCode == Activity.RESULT_OK) {
            if (data != null && data.hasExtra("newProduit")) {
                Produit newProduit = (Produit) data.getSerializableExtra("newProduit");
                if (newProduit != null) {
                    RecievedListProduit.add(newProduit);
                    if (relativeLayout != null && NoProduittextView != null) {
                        relativeLayout.removeView(NoProduittextView);
                        setContentView(gridView);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        }
        if (requestCode == REQUEST_CODE_EDIT_PRODUIT && resultCode == Activity.RESULT_OK) {
            if (data != null && data.hasExtra("EditedProduit")) {
                Produit editedProduit = (Produit) data.getSerializableExtra("EditedProduit");
                int position = data.getIntExtra("position", -1);
                if (editedProduit != null && position != -1 && position < RecievedListProduit.size()) {
                    RecievedListProduit.set(position, editedProduit);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }
}
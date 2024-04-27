package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;

public class GridAdapter extends ArrayAdapter<Produit> {
    private Context gridContext;
    private ArrayList<Produit> listProduits;

    public GridAdapter(Context context, ArrayList<Produit> items) {
        super(context, 0, items);
        gridContext = context;
        this.listProduits = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) gridContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.ma_cellule, parent, false);
        }

        Produit produit = getItem(position);
        TextView textView = view.findViewById(R.id.produittextview);
        if (produit != null) {
            textView.setText(produit.getLabel());
        }
        TextView textView2 = view.findViewById(R.id.dds);
        if (produit != null) {
            textView.setText(produit.getDescription());
        }
        ImageView imageview = view.findViewById(R.id.produitimage);
        if (produit.getPhoto() != null) {
            Bitmap bitmapImage = null;
            try {
                bitmapImage = Produit.StringToBitmap(produit.getPhoto());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            imageview.setImageBitmap(bitmapImage);
        }

        Button editProduitBtn = view.findViewById(R.id.editProduitBtn);
        Button deleteProduitBtn = view.findViewById(R.id.deleteProduitBtn);

        deleteProduitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listProduits.remove(position);
                notifyDataSetChanged();
            }
        });




        return view;
    }
}
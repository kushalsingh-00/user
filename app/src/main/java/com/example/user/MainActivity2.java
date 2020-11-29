package com.example.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.user.adapter.ProductAdapter;
import com.example.user.databinding.ActivityMain2Binding;
import com.example.user.model.Cart;
import com.example.user.model.Inventory;
import com.example.user.model.Product;
import com.example.user.model.Variant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private ActivityMain2Binding b;
    private Cart cart = new Cart();
    private List<Product> list;
    private ProductAdapter adapter;
    private MyApp myApp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        myApp= (MyApp) getApplicationContext();
        
        load();

        setupCheckout();
    }

    private void load() {
        if(myApp.isOffline())
        {
            myApp.showToast(this,"It Seems Your App Is Ofline");
            return;
        }

        myApp.showLoadingDialog(this);

        myApp.db.collection("inventory").document("products")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists())
                        {
                            Inventory inventory=documentSnapshot.toObject(Inventory.class);
                            list=inventory.products;
                        }
                        else
                            list=new ArrayList<>();

                        settingUpListOfProduct();
                        myApp.hideLoadingDialog();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        myApp.hideLoadingDialog();
                        myApp.showToast(MainActivity2.this, e.getMessage());
                    }
                });
    }

    private void setupCheckout() {
        b.checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCartSummary();
            }
        });
    }

    private void openCartSummary() {
        Intent intent=new Intent(MainActivity2.this,CartSummaryActivity.class);
        intent.putExtra("Cart Items", cart);
        startActivity(intent);
    }

    private void settingUpListOfProduct() {

        adapter=new ProductAdapter(this,list,cart);
        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration itemDecor = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        b.recyclerView.addItemDecoration(itemDecor);

        b.recyclerView.setAdapter(adapter);
    }


    public void updateCartSummary(){
        if(cart.noOfItems==0){
            b.checkout.setVisibility(View.GONE);
        } else {
            b.checkout.setVisibility(View.VISIBLE);
            b.summary.setText("Total : Rs. " + cart.subTotal + "\n" + cart.noOfItems + " items");
        }
    }

    public void cartSummary()
    {
        if(cart.noOfItems==0)
        {
            b.checkout.setVisibility(View.GONE);
        }
        else {
            b.checkout.setVisibility(View.VISIBLE);
            b.summary.setText("Total Rs. "+cart.subTotal+"\n"+cart.noOfItems+" items");
        }
    }
}
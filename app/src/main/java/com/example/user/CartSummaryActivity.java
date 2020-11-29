package com.example.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.user.databinding.ActivityCartSummaryBinding;
import com.example.user.databinding.ItemsViewBinding;
import com.example.user.model.Cart;
import com.example.user.model.CartItem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CartSummaryActivity extends AppCompatActivity {
    private ActivityCartSummaryBinding b;
    private ItemsViewBinding b1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityCartSummaryBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        Intent i=getIntent();
        Cart c= (Cart) i.getExtras().getSerializable("Cart Items");

        displayingValuesInView(c);
    }

    private void displayingValuesInView(Cart c) {
        b1=ItemsViewBinding.inflate(getLayoutInflater());
        Map<String, CartItem> items=c.items;

        Iterator<String> i=items.keySet().iterator();

        while(i.hasNext())
        {
            String k=i.next();
            b1.productName.setText(items.get(k).name);
            b1.productQtyAndPrice.setText(items.get(k).qty+" kg * Rs. "+items.get(k).price+"/kg");
            b1.productTotal.setText(c.subTotal+"");
            b.itemsCart.addView(b1.getRoot());
        }
    }
}
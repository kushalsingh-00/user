package com.example.user.controller;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import com.example.user.MainActivity2;
import com.example.user.databinding.SingleVbProductLayoutBinding;
import com.example.user.model.Cart;
import com.example.user.model.Product;

public class SingleVBBinder {

    private SingleVbProductLayoutBinding b;

    public void bind(SingleVbProductLayoutBinding b, final Product product, final Cart cart)
    {
        this.b = b;
        b.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.addToCart(product,product.varients.get(0));
                updateViews(1);

            }
        });

        b.incrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=cart.addToCart(product,product.varients.get(0));
                updateViews(i);
            }
        });

        b.decrementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i=cart.removeFromCart(product,product.varients.get(0));
                updateViews(i);
            }
        });

    }

    private void updateViews(int i) {
        if(i==0)
        {
            b.addBtn.setVisibility(View.VISIBLE);
            b.qtyGroup.setVisibility(View.GONE);
        }

        else if (i==1)
        {
            b.addBtn.setVisibility(View.GONE);
            b.qtyGroup.setVisibility(View.VISIBLE);
        }

        b.quantity.setText(i+"");

        updateSUmmary();
    }

    private void updateSUmmary() {
        Context context=b.getRoot().getContext();
        if(context instanceof MainActivity2)
        {
            ((MainActivity2) context).cartSummary();
        }
        else
            Toast.makeText(context, "something went wrong", Toast.LENGTH_SHORT).show();
    }
}

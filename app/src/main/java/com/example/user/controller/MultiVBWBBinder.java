package com.example.user.controller;

import android.content.Context;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.user.MainActivity2;
import com.example.user.databinding.SingleVbProductLayoutBinding;
import com.example.user.databinding.WbMultivarientProductLayoutBinding;
import com.example.user.dialogs.VarientPickerDialog;
import com.example.user.dialogs.WeightPickerDialog;
import com.example.user.model.Cart;
import com.example.user.model.Product;

import java.util.concurrent.CopyOnWriteArrayList;

public class MultiVBWBBinder {

    private WbMultivarientProductLayoutBinding b;
    private Product product;
    private Cart cart;

    public void bind(WbMultivarientProductLayoutBinding b, Product product, Cart cart)
    {

        this.b = b;
        this.product = product;
        this.cart = cart;

        b.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });

        b.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });
    }

    private void dialog() {
        if(product.type==Product.WEIGHT_BASED)
            showWeightPicker();
        else
            showVarientPicker();
    }

    private void showVarientPicker() {
        Context context=b.getRoot().getContext();

        new VarientPickerDialog()
                .show(context, cart, product, new VarientPickerDialog.OnVariantPickedListener() {
                    @Override
                    public void onQtyUpdated(int qty) {
                        updateQty(qty + "");
                    }

                    @Override
                    public void onRemoved() {
                        hideQty();
                    }
                });


    }

    private void showWeightPicker() {
        Context context=b.getRoot().getContext();

        new WeightPickerDialog()
                .show(context,cart,product,new WeightPickerDialog.OnWeightPickedListener() {
                    @Override
                    public void onWeightPicked(int kg,int g) {
                        updateQty(kg+"kg "+g+"g");
                    }

                    @Override
                    public void onRemoved() {
                        hideQty();
                    }
                });

    }

    private void updateQty(String qtyString) {
        b.qtyGroup.setVisibility(View.VISIBLE);
        b.addBtn.setVisibility(View.GONE);

        b.quantity.setText(qtyString);
        updateCheckoutSummary();
    }

    private void updateCheckoutSummary() {
        Context context = b.getRoot().getContext();
        if(context instanceof MainActivity2){
            ((MainActivity2) context).updateCartSummary();
        } else {
            Toast.makeText(context, "Something went wrong!", Toast.LENGTH_SHORT).show();
        }
    }

    private void hideQty() {
        b.qtyGroup.setVisibility(View.GONE);
        b.addBtn.setVisibility(View.VISIBLE);

        updateCheckoutSummary();
    }

}

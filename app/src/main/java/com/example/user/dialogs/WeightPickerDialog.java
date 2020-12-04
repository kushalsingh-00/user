package com.example.user.dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;

import com.example.user.databinding.WeightPickerDialogBinding;
import com.example.user.model.Cart;
import com.example.user.model.Product;

public class WeightPickerDialog {
    private WeightPickerDialogBinding b;
    private Cart cart;
    private Product product;

    public void show(Context context, final Cart cart, final Product product, final OnWeightPickedListener listener){
        b = WeightPickerDialogBinding.inflate(
                LayoutInflater.from(context)
        );
        this.cart = cart;
        this.product = product;

        new AlertDialog.Builder(context)
                .setTitle(product.name)
                .setView(b.getRoot())
                .setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        int kg = b.numberPickerKg.getValue();
                        int g = b.numberPickerG.getValue() * 50;

                        //GuardCode to prevent user from selecting 0kg 0g
                        if(kg == 0 && g == 0)
                            return;

                        changeInCart(kg + (g/1000f));
                        listener.onWeightPicked(kg, g);
                    }
                })
                .setNegativeButton("REMOVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        cart.removeFromCartWB(product);
                        listener.onRemoved();
                    }
                })
                .show();

        setupNumberPickers();
        showPreviouslySelectedQty();
    }

    private void showPreviouslySelectedQty() {
        if(cart.items.containsKey(product.name)){
            float qty = cart.items.get(product.name).qty;

            b.numberPickerKg.setValue((int) qty);
            b.numberPickerG.setValue((int) (qty - (int) qty) * 1000 / 50);
        }
    }

    private void changeInCart(float qty) {
        cart.updateWBQuantity(product, qty);
    }

    private void setupNumberPickers() {
        // Define this method to setup kg & g NumberPickers as per the given ranges
        //kg Range - 0kg to 10kg
        //g Range - 0g to 950g
        float quantity = product.qty;
        b.numberPickerKg.setMinValue((int) (quantity));
        b.numberPickerKg.setMaxValue(10);
        b.numberPickerG.setMinValue(((int) (quantity % 1000)) / 50);
        b.numberPickerG.setMaxValue(19);

        b.numberPickerKg.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return value + " " + "kg";
            }
        });

        b.numberPickerG.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {
                return (value * 50) + " " + "g";
            }
        });

        updateFirstItemViewInNumberPicker(b.numberPickerKg);
        updateFirstItemViewInNumberPicker(b.numberPickerG);

    }

    private void updateFirstItemViewInNumberPicker(NumberPicker p) {
        View firstItem = p.getChildAt(0);
        if (firstItem != null) {
            firstItem.setVisibility(View.INVISIBLE);
        }
    }

    public interface OnWeightPickedListener{
        void onWeightPicked(int kg, int g);
        void onRemoved();
    }
}

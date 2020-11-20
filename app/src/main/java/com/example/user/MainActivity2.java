package com.example.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;

import com.example.user.adapter.ProductAdapter;
import com.example.user.databinding.ActivityMain2Binding;
import com.example.user.model.Cart;
import com.example.user.model.Product;
import com.example.user.model.Variant;

import java.util.Arrays;
import java.util.List;

public class MainActivity2 extends AppCompatActivity {

    private ActivityMain2Binding b;
    private Cart cart = new Cart();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(b.getRoot());
        settingUpListOfProduct();
    }

    private void settingUpListOfProduct() {
        List<Product> list=getProducts();

        ProductAdapter adapter=new ProductAdapter(this,list,cart);
        b.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        b.recyclerView.setAdapter(adapter);
    }

    private List<Product> getProducts() {
        return Arrays.asList(
                new Product("Bread", Arrays.asList(
                        new Variant("big", 10)
                        , new Variant("small", 20)
                        , new Variant("medium", 30)
                ))
                , new Product("Apple", 30, 1)
                , new Product("Kiwi", Arrays.asList(
                        new Variant("1kg", 100)
                ))
        );
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
package com.example.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.user.adapter.ProductAdapter;
import com.example.user.databinding.ActivityMain2Binding;
import com.example.user.databinding.UserDetailsBinding;
import com.example.user.fcmsender.FCMSender;
import com.example.user.fcmsender.MessageFormatter;
import com.example.user.model.Cart;
import com.example.user.model.CartItem;
import com.example.user.model.Inventory;
import com.example.user.model.Order;
import com.example.user.model.Product;
import com.example.user.model.Variant;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import kotlin.jvm.internal.Ref;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MainActivity2 extends AppCompatActivity {

    private ActivityMain2Binding b;
    private Cart cart = new Cart();
    private List<Product> list;
    private ProductAdapter adapter;
    private MyApp myApp;
    private Order order;
    public String uName,uAddress,uMobile;
    private UserDetailsBinding userDetailsBinding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b=ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        userDetailsBinding=UserDetailsBinding.inflate(getLayoutInflater());

        myApp= (MyApp) getApplicationContext();

        load();
        setupCheckout();

        FirebaseMessaging.getInstance().subscribeToTopic("user");

    }

    private void sendNotification(String id) {

        Log.e(String.valueOf(MainActivity2.this),"called");
        String message = MessageFormatter
                .getSampleMessage("admin", "New Order", "Order Id:"+id);

        new FCMSender()
                .send(message, new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(MainActivity2.this)
                                                .setTitle("Failure")
                                                .setMessage(e.toString())
                                                .show();
                            }
                        });

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull final Response response) throws IOException {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                new AlertDialog.Builder(MainActivity2.this)
                                                .setTitle("Success")
                                                .setMessage(response.toString())
                                                .show();
                            }
                        });

                    }
                });
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
//                sendNotification();
                userDetailsDialog();

            }
        });
    }

    private void userDetailsDialog() {
        new AlertDialog.Builder(this)
                .setTitle("User Details")
                .setCancelable(false)
                .setView(userDetailsBinding.getRoot())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        uName=userDetailsBinding.nameU.getText().toString();
                        uAddress=userDetailsBinding.addressU.getText().toString();
                        uMobile=userDetailsBinding.mobileU.getText().toString();
                        openCartSummary();
                        uploadOrderDetails();
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       finish();
                    }
                })
                .show();
    }

    private void uploadOrderDetails() {
        myApp.db.collection("Order")
                .add(new Order(uName,uAddress,uMobile, (HashMap<String, CartItem>) cart.items,cart.subTotal,1))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(MainActivity2.this, "uploaded", Toast.LENGTH_SHORT).show();
                        sendNotification(documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity2.this, "Failed", Toast.LENGTH_SHORT).show();
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
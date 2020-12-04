package com.example.user.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.user.controller.MultiVBWBBinder;
import com.example.user.controller.SingleVBBinder;
import com.example.user.databinding.SingleVbProductLayoutBinding;
import com.example.user.databinding.WbMultivarientProductLayoutBinding;
import com.example.user.model.Cart;
import com.example.user.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_SINGLE_VB = 0, TYPE_WB_OR_MULTIPLE_VB = 1;
    private Context context;
    private List<Product> products;
    private Cart cart;

    public ProductAdapter(Context context, List<Product> products, Cart cart) {
        this.context = context;
        this.products = products;
        this.cart = cart;
    }

    @Override
    public int getItemViewType(int position) {
        //getting product for specific position
        Product product=products.get(position);
        // checking for type and size
        if(Product.WEIGHT_BASED==product.type||product.varients.size()>1)
            return TYPE_WB_OR_MULTIPLE_VB;

        return TYPE_SINGLE_VB;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==TYPE_SINGLE_VB) {
            SingleVbProductLayoutBinding b = SingleVbProductLayoutBinding.inflate(
                    LayoutInflater.from(context)
                    , parent
                    , false
            );
            return new SingleVBViewBinder(b);
        }

        else {
            WbMultivarientProductLayoutBinding b =WbMultivarientProductLayoutBinding.inflate(
                    LayoutInflater.from(context)
                    ,parent
                    ,false
            );

            return new WBMBBinder(b);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        Product product=products.get(position);

        if(getItemViewType(position)==TYPE_SINGLE_VB)
        {
            SingleVBViewBinder singleVBViewBinder=(SingleVBViewBinder) holder;
            singleVBViewBinder.b.name.setText(product.name+" "+product.varients.get(0).name);
            singleVBViewBinder.b.price.setText(product.priceString());

            new SingleVBBinder().bind(singleVBViewBinder.b,product,cart);
        }
        else {
            WBMBBinder wbmbBinder=(WBMBBinder) holder;
            wbmbBinder.b.name.setText(product.name);
            wbmbBinder.b.price.setText(product.priceString());

            new MultiVBWBBinder().bind(wbmbBinder.b,product,cart);
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class SingleVBViewBinder extends RecyclerView.ViewHolder{

        private SingleVbProductLayoutBinding b;
        public SingleVBViewBinder(SingleVbProductLayoutBinding b) {
            super(b.getRoot());
            this.b=b;
        }
    }

    public class WBMBBinder extends RecyclerView.ViewHolder{

        private WbMultivarientProductLayoutBinding b;
        public WBMBBinder(WbMultivarientProductLayoutBinding b) {
            super(b.getRoot());
            this.b=b;
        }
    }
}

package com.example.sugarcosmetics;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sugarcosmetics.db.Product;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private List<Product> productList;
    private Context context;
    private Product product;

    public CustomAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // infalte the item Layout
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.items, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        // set the data in items
        Product product = productList.get(position);
        if (product != null) {
            holder.name.setText(product.getProductName());
            holder.itemView.setTag(product.getProduct_id());
            try {
                Picasso.with(context).load(product.getImageUrl())
                        .placeholder(R.drawable.sugar_cap)
                        .into(holder.image);
            } catch (Exception ex) {
                holder.image.setImageResource(R.drawable.sugar_cap);
            }
            // holder.image.setImageResource(product.getImageUrl());
            // implement setOnClickListener event on item view.
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // open another activity on item click
                    Intent intent = new Intent(context, ProductDetailsActivity.class);
                    intent.putExtra("productId", (Long) holder.itemView.getTag()); // put image data in Intent
                    context.startActivity(intent); // start Intent
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return product == null ? productList.size() : 1;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView name;
        ImageView image;

        MyViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            name = (TextView) itemView.findViewById(R.id.tvproductName);
            image = (ImageView) itemView.findViewById(R.id.item_image);
        }
    }
}

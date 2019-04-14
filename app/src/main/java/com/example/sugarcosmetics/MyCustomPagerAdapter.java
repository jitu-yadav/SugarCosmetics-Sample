package com.example.sugarcosmetics;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MyCustomPagerAdapter extends PagerAdapter {

    private Context context;
    private List<String> images;
    private LayoutInflater layoutInflater;


    public MyCustomPagerAdapter(Context context, List<String> images) {
        this.context = context;
        this.images = images;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return images.size() > 5 ? 5 : images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View itemView = layoutInflater.inflate(R.layout.itemimage, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        try {
            Picasso.with(context).load(images.get(position))
                    .placeholder(R.drawable.sugar_cap)
                    .into(imageView);
        } catch (Exception ex) {
            imageView.setImageResource(R.drawable.sugar_cap);
        }
        container.addView(itemView);

        //listening to image click
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "you clicked image " + (position + 1), Toast.LENGTH_LONG).show();
            }
        });

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}

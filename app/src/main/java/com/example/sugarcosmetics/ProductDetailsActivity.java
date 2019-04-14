package com.example.sugarcosmetics;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sugarcosmetics.db.AppDataBase;
import com.example.sugarcosmetics.db.Product;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private List<String> images;
    private MyCustomPagerAdapter myCustomPagerAdapter;
    private TextView tvProductName;
    private TextView tvProductPrice;
    private TextView tvProductDetails;

    private AppDataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        viewPager = (ViewPager) findViewById(R.id.pager);
        tvProductName = (TextView) findViewById(R.id.productName);
        tvProductPrice = (TextView) findViewById(R.id.productPrice);
        tvProductDetails = (TextView) findViewById(R.id.productDesc);
        Intent intent = getIntent();
        dataBase = AppDataBase.getAppDatabase(ProductDetailsActivity.this);
        if (intent != null && intent.hasExtra("productId")) {
            Product product = dataBase.productDao().getProduct(intent.getLongExtra("productId", -1));
            tvProductName.setText(product.getProductName());
        tvProductPrice.setText(Html.fromHtml(getString(R.string.price) + product.getPrice()));
        tvProductDetails.setText(Html.fromHtml(getString(R.string.description) + product.getProductDescription()));
        myCustomPagerAdapter = new MyCustomPagerAdapter(ProductDetailsActivity.this, getImageUrls(product.getImageUrlList()));
        viewPager.setAdapter(myCustomPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);
        }

    }

    private List<String> getImageUrls(String imageUrlList) {
        List<String> imageUrls = new ArrayList<>();
        imageUrlList = imageUrlList.replace("[", "");
        imageUrlList = imageUrlList.replace("]", "");
        String images[] = imageUrlList.split(",");
        imageUrls.addAll(Arrays.asList(images));
        return imageUrls;
    }
}

/*RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://sugarcosmetics.s3.amazonaws.com/feeds/eyes/3.json";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i("ProductResponse : ", response);

                            JSONObject jsonResponse = new JSONObject(response);
                            if (jsonResponse.has("products")) {
                                JSONArray jsonArrayProduct = jsonResponse.getJSONArray("products");
                                JSONObject jsonObject = (JSONObject) jsonArrayProduct.get(0);
                                if (jsonObject.has("title")) {
                                    tvProductName.setText(jsonObject.getString("title"));
                                }
                                JSONArray jsonArray = jsonObject.getJSONArray("variants");
                                tvProductPrice.setText(Html.fromHtml(getString(R.string.price) + ((JSONObject) jsonArray.get(0)).getString("price")));
                                tvProductDetails.setText(Html.fromHtml(getString(R.string.description) + jsonObject.getString("body_html")));
                                myCustomPagerAdapter = new MyCustomPagerAdapter(ProductDetailsActivity.this, images);
                                viewPager.setAdapter(myCustomPagerAdapter);
                                TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
                                tabLayout.setupWithViewPager(viewPager, true);
                            }

                            // Display the first 500 characters of the response string.
                        } catch (Exception ex) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);*/

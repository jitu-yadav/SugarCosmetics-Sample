package com.example.sugarcosmetics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
import java.util.List;


public class MainActivity extends AppCompatActivity {

    public final static String PRODUCT_CATEGORY_URL = "https://sugarcosmetics.s3.amazonaws.com/feeds/category.json";

    private List<String> lipsUrlList;
    private List<String> faceUrlList;
    private List<String> eyesUrlList;

    private List<Product> lipsList;
    private List<Product> faceList;
    private List<Product> eyesList;

    private RecyclerView rvLips, rvFace, rvEyes;
    private CustomAdapter adapter;
    private AppDataBase dataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvLips = (RecyclerView) findViewById(R.id.rvlips);
        rvFace = (RecyclerView) findViewById(R.id.rvface);
        rvEyes = (RecyclerView) findViewById(R.id.rveyes);

        rvLips.setLayoutManager(getGridLayoutManager()); // set LayoutManager to RecyclerView
        rvFace.setLayoutManager(getGridLayoutManager()); // set LayoutManager to RecyclerView
        rvEyes.setLayoutManager(getGridLayoutManager()); // set LayoutManager to RecyclerView

        dataBase = AppDataBase.getAppDatabase(getApplicationContext());

        List<Product> productList = dataBase.productDao().getAll();

        if (productList != null && !productList.isEmpty()) {
            updateView(productList);
        } else {
            // Instantiate the RequestQueue.
            RequestQueue queue = Volley.newRequestQueue(this);
            // Request a string response from the provided URL.
            StringRequest stringRequest = new StringRequest(Request.Method.GET, PRODUCT_CATEGORY_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.i("ProductResponse : ", response);

                                JSONObject jsonResponse = new JSONObject(response);
                                if (jsonResponse.has("category")) {
                                    JSONArray jsonArrCategory = jsonResponse.getJSONArray("category");
                                    lipsUrlList = new ArrayList<>();
                                    faceUrlList = new ArrayList<>();
                                    eyesUrlList = new ArrayList<>();
                                    for (int i = 0; i < jsonArrCategory.length(); i++) {
                                        JSONObject jsonCategory = (JSONObject) jsonArrCategory.get(i);
                                        if (jsonCategory.has("lips")) {
                                            JSONArray jsonArray = jsonCategory.getJSONArray("lips");
                                            for (int j = 0; j < jsonArray.length(); j++) {
                                                String position = (String) jsonArray.get(j);
                                                String lipsProductUrl = String.format(getString(R.string.producturl), "lips", position);
                                                lipsUrlList.add(lipsProductUrl);
                                            }
                                            if (lipsUrlList != null && !lipsUrlList.isEmpty()) {
                                                getProductLipsDetails(lipsUrlList);
                                            }
                                        }

                                        if (jsonCategory.has("face")) {
                                            JSONArray jsonArray = jsonCategory.getJSONArray("face");
                                            for (int j = 0; j < jsonArray.length(); j++) {
                                                String position = (String) jsonArray.get(j);
                                                String faceProductUrl = String.format(getString(R.string.producturl), "face", position);
                                                faceUrlList.add(faceProductUrl);
                                            }

                                            if (faceUrlList != null && !faceUrlList.isEmpty()) {
                                                getProductFaceDetails(faceUrlList);
                                            }
                                        }

                                        if (jsonCategory.has("eyes")) {
                                            JSONArray jsonArray = jsonCategory.getJSONArray("eyes");
                                            for (int j = 0; j < jsonArray.length(); j++) {
                                                String position = (String) jsonArray.get(j);
                                                String eyesProductUrl = String.format(getString(R.string.producturl), "eyes", position);
                                                eyesUrlList.add(eyesProductUrl);
                                            }

                                            if (eyesUrlList != null && !eyesUrlList.isEmpty()) {
                                                getProductEyesDetails(eyesUrlList);
                                            }
                                        }

                                    }
                                }

                            } catch (Exception ex) {
                                Log.e("Exception", ex.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            // Add the request to the RequestQueue.
            queue.add(stringRequest);

        }
    }

    private void getProductLipsDetails(final List<String> lipsUrlList) {
        lipsList = new ArrayList<>();
        for (int i = 0; i < lipsUrlList.size(); i++) {
            // Request a string response from the provided URL.
            final StringRequest request = new StringRequest(Request.Method.GET, lipsUrlList.get(i),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.i("ProductResponse : ", response);
                                JSONObject jsonResponse = new JSONObject(response);
                                Product product = new Product();
                                if (jsonResponse.has("products")) {
                                    JSONArray jsonArrayProduct = jsonResponse.getJSONArray("products");
                                    JSONObject jsonObject = (JSONObject) jsonArrayProduct.get(0);
                                    if (jsonObject.has("title") && jsonObject.has("id")) {
                                        product.setProductName(jsonObject.getString("title"));
                                        product.setProduct_id(jsonObject.getLong("id"));
                                    }
                                    JSONArray jsonArray = jsonObject.getJSONArray("variants");
                                    product.setPrice(((JSONObject) jsonArray.get(0)).getString("price"));
                                    product.setProductDescription(jsonObject.getString("body_html"));

                                    JSONArray jsonArrayImage = jsonObject.getJSONArray("images");
                                    List<String> imageUrls = new ArrayList<>();
                                    for (int k = 0; k < jsonArrayImage.length(); k++) {
                                        imageUrls.add((jsonArrayImage.getJSONObject(k)).getString("src"));
                                        if (k == 0) {
                                            product.setImageUrl((jsonArrayImage.getJSONObject(0)).getString("src"));
                                        }
                                    }
                                    if (!imageUrls.isEmpty()) {
                                        product.setImageUrlList(imageUrls.toString());
                                    }
                                    product.setProductType("lips");
                                    lipsList.add(product);
                                    dataBase.productDao().insertAll(product);
                                    if (lipsList.size() == lipsUrlList.size()) {
                                        adapter = new CustomAdapter(MainActivity.this, lipsList);
                                        rvLips.setAdapter(adapter);
                                    }
                                    Log.i("ProductResponse : ", product.toString());
                                }
                            } catch (Exception ex) {
                                Log.e("Exception", ex.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            Volley.newRequestQueue(getApplicationContext()).add(request);
        }

    }

    private void getProductFaceDetails(final List<String> faceurlList) {
        faceList = new ArrayList<>();
        for (int i = 0; i < faceurlList.size(); i++) {
            // Request a string response from the provided URL.
            final StringRequest request = new StringRequest(Request.Method.GET, faceurlList.get(i),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.i("ProductResponse : ", response);
                                JSONObject jsonResponse = new JSONObject(response);
                                Product product = new Product();
                                if (jsonResponse.has("products")) {
                                    JSONArray jsonArrayProduct = jsonResponse.getJSONArray("products");
                                    JSONObject jsonObject = (JSONObject) jsonArrayProduct.get(0);
                                    if (jsonObject.has("title") && jsonObject.has("id")) {
                                        product.setProductName(jsonObject.getString("title"));
                                        product.setProduct_id(jsonObject.getLong("id"));
                                    }
                                    JSONArray jsonArray = jsonObject.getJSONArray("variants");
                                    product.setPrice(((JSONObject) jsonArray.get(0)).getString("price"));
                                    product.setProductDescription(jsonObject.getString("body_html"));
                                    JSONArray jsonArrayImage = jsonObject.getJSONArray("images");
                                    List<String> imageUrls = new ArrayList<>();
                                    for (int k = 0; k < jsonArrayImage.length(); k++) {
                                        imageUrls.add((jsonArrayImage.getJSONObject(k)).getString("src"));
                                        if (k == 0) {
                                            product.setImageUrl((jsonArrayImage.getJSONObject(0)).getString("src"));
                                        }
                                    }
                                    if (!imageUrls.isEmpty()) {
                                        product.setImageUrlList(imageUrls.toString());
                                    }
                                    product.setProductType("face");
                                    faceList.add(product);
                                    dataBase.productDao().insertAll(product);
                                    if (faceList.size() == faceurlList.size()) {
                                        adapter = new CustomAdapter(MainActivity.this, faceList);
                                        rvFace.setAdapter(adapter);
                                    }
                                    Log.i("ProductResponse : ", product.toString());
                                }
                            } catch (Exception ex) {
                                Log.e("Exception", ex.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            Volley.newRequestQueue(getApplicationContext()).add(request);
        }
    }

    private void getProductEyesDetails(final List<String> eyesUrlList) {
        eyesList = new ArrayList<>();
        for (int i = 0; i < eyesUrlList.size(); i++) {
            // Request a string response from the provided URL.
            final StringRequest request = new StringRequest(Request.Method.GET, eyesUrlList.get(i),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                Log.i("ProductResponse : ", response);
                                JSONObject jsonResponse = new JSONObject(response);
                                Product product = new Product();
                                if (jsonResponse.has("products")) {
                                    JSONArray jsonArrayProduct = jsonResponse.getJSONArray("products");
                                    JSONObject jsonObject = (JSONObject) jsonArrayProduct.get(0);
                                    if (jsonObject.has("title") && jsonObject.has("id")) {
                                        product.setProductName(jsonObject.getString("title"));
                                        product.setProduct_id(jsonObject.getLong("id"));
                                    }
                                    JSONArray jsonArray = jsonObject.getJSONArray("variants");
                                    product.setPrice(((JSONObject) jsonArray.get(0)).getString("price"));
                                    product.setProductDescription(jsonObject.getString("body_html"));
                                    JSONArray jsonArrayImage = jsonObject.getJSONArray("images");
                                    List<String> imageUrls = new ArrayList<>();
                                    for (int k = 0; k < jsonArrayImage.length(); k++) {
                                        imageUrls.add((jsonArrayImage.getJSONObject(k)).getString("src"));
                                        if (k == 0) {
                                            product.setImageUrl((jsonArrayImage.getJSONObject(0)).getString("src"));
                                        }
                                    }
                                    if (!imageUrls.isEmpty()) {
                                        product.setImageUrlList(imageUrls.toString());
                                    }
                                    product.setProductType("eyes");
                                    eyesList.add(product);
                                    dataBase.productDao().insertAll(product);

                                    if (eyesUrlList.size() == eyesList.size()) {
                                        adapter = new CustomAdapter(MainActivity.this, eyesList);
                                        rvEyes.setAdapter(adapter);
                                    }
                                    // adapter.notifyData(product);
                                    Log.i("ProductResponse : ", product.toString());
                                }
                            } catch (Exception ex) {
                                Log.e("Exception", ex.getMessage());
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                }
            });
            Volley.newRequestQueue(getApplicationContext()).add(request);
        }

    }


    private void updateView(List<Product> productList) {
        lipsList = new ArrayList<>();
        faceList = new ArrayList<>();
        eyesList = new ArrayList<>();
        for (Product product : productList) {
            if (product.getProductType() != null) {
                if (product.getProductType().contains("lips")) {
                    lipsList.add(product);
                } else if (product.getProductType().contains("face")) {
                    faceList.add(product);
                } else if (product.getProductType().contains("eyes")) {
                    eyesList.add(product);
                }
            }
        }

        if (!lipsList.isEmpty()) {
            adapter = new CustomAdapter(MainActivity.this, lipsList);
            rvLips.setAdapter(adapter);
        }
        if (!faceList.isEmpty()) {
            adapter = new CustomAdapter(MainActivity.this, faceList);
            rvFace.setAdapter(adapter);
        }
        if (!eyesList.isEmpty()) {
            adapter = new CustomAdapter(MainActivity.this, eyesList);
            rvEyes.setAdapter(adapter);
        }
    }


    private GridLayoutManager getGridLayoutManager() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        return gridLayoutManager;
    }
}

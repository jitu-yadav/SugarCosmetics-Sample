package com.example.sugarcosmetics.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface ProductDao {

    @Query("SELECT * FROM product")
    List<Product> getAll();

    @Query("SELECT * FROM product WHERE product_id = :product_id ")
    Product getProduct(long product_id);

    @Insert
    void insertAll(Product... product);

    @Delete
    void delete(Product user);

}

package com.example.shoppingapp.mvvm;

import androidx.annotation.Nullable;

import com.example.shoppingapp.model.Product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProductRepo {
    FirebaseFirestore  firestore=FirebaseFirestore.getInstance();
    List<Product> productList=new ArrayList<>();

    OnProductInterface onProductInterface;

    public ProductRepo(OnProductInterface onProductInterface) {
        this.onProductInterface = onProductInterface;
    }

    public void getAllProducts(){
        firestore.collection("Products").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                productList.clear();
                for (DocumentSnapshot ds :value.getDocuments()
                     ) {
                    Product product=ds.toObject(Product.class);
                    productList.add(product);
                    onProductInterface.products(productList);
                }
            }
        });
    }
    public interface OnProductInterface{
        void products(List<Product> productList);
    }

}

package com.example.shoppingapp.mvvm;

import androidx.annotation.Nullable;

import com.example.shoppingapp.model.Cart;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CartRepo {
    FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    String userId;
    FirebaseAuth auth=FirebaseAuth.getInstance();
    List<Cart> cartList=new ArrayList<>();
    CartInterFace cartInterFace;

    public CartRepo(CartInterFace cartInterFace) {
        this.cartInterFace = cartInterFace;
    }

    public void getCartShit(){
        assert auth.getCurrentUser() != null;
        userId=auth.getCurrentUser().getUid();

        firestore.collection("Cart"+userId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                cartList.clear();
                for(DocumentSnapshot ds:value.getDocuments()){
                    Cart cart=ds.toObject(Cart.class);
                    cartList.add(cart);
                    cartInterFace.cartList(cartList);
                }
            }
        });
    }

    public interface CartInterFace{
        void cartList(List<Cart> cartList);
    }
}

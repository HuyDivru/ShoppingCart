package com.example.shoppingapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.databinding.FragmentProductDetailBinding;
import com.example.shoppingapp.model.Product;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;


public class ProductDetailFragment extends Fragment {
    private FragmentProductDetailBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String title,imageUrl,desc,productId,userId;
    int position=0;
    int price=0;
    int finalPrice=0;
    int quantity=0;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentProductDetailBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        navController= Navigation.findNavController(view);
        userId=auth.getCurrentUser().getUid();

        imageUrl=ProductDetailFragmentArgs.fromBundle(getArguments()).getImageUrl();
        title=ProductDetailFragmentArgs.fromBundle(getArguments()).getTitle();
        desc=ProductDetailFragmentArgs.fromBundle(getArguments()).getDescription();
        position=ProductDetailFragmentArgs.fromBundle(getArguments()).getPosition();
        productId=ProductDetailFragmentArgs.fromBundle(getArguments()).getProductId();
        price=ProductDetailFragmentArgs.fromBundle(getArguments()).getPrice();

        binding.detailProTitle.setText(title);
        binding.detailProDes.setText(desc);

        Glide.with(getActivity()).load(imageUrl).centerCrop().into(binding.imageView);
        binding.pricetotal.setText("Price 1 for item is"+String.valueOf(price));


        firestore.collection("Products").document(productId).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                Product product=value.toObject(Product.class);

                String lastestQuantity=String.valueOf(product.getQuantity());

                binding.quantityDisplay.setText(lastestQuantity);
            }
        });
        binding.btnAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity=Integer.parseInt(binding.quantityDisplay.getText().toString());

                quantity++;

                finalPrice=quantity*price;
                binding.pricetotal.setText("Total is"+String.valueOf(quantity)+ " x "+String.valueOf(finalPrice));
            }
        });
        binding.btnSubQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantity=Integer.parseInt(binding.quantityDisplay.getText().toString());
                if(quantity<=0){
                    quantity=0;
                    finalPrice=0;
                }
                else{
                    quantity--;
                    finalPrice=quantity*price;
                    binding.pricetotal.setText("Total is "+String.valueOf(quantity)+" x "+String.valueOf(finalPrice));
                    firestore.collection("Products").document(productId).update("quantity",quantity).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                }
            }
        });

        binding.btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantity==0){
                    navController.navigate(R.id.action_cartFragment_to_productFragment);
                    Toast.makeText(getContext(), "Nothing Added To Cart", Toast.LENGTH_SHORT).show();
                }
                else if(quantity!=0){
                    addInCart();
                    ProductDetailFragmentDirections.ActionProductDetailFragmentToProductFragment actions=ProductDetailFragmentDirections.actionProductDetailFragmentToProductFragment();
                    actions.setQuantity(quantity);
                    navController.navigate(actions);
                    Toast.makeText(getContext(), "Added In Cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void addInCart() {
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("quantity",quantity);
        hashMap.put("price",price);
        hashMap.put("title", title);
        hashMap.put("imageUrl",imageUrl);
        hashMap.put("productid",productId);

        firestore.collection("Cart"+userId).document(title).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        });
    }
}
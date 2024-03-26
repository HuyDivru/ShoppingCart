package com.example.shoppingapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.shoppingapp.R;
import com.example.shoppingapp.adapter.CartAdapter;
import com.example.shoppingapp.databinding.FragmentCartBinding;
import com.example.shoppingapp.model.Cart;
import com.example.shoppingapp.mvvm.CartViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {
    private FragmentCartBinding binding;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private NavController navController;
    private CartAdapter adapter;
    private CartViewModel viewModel;
    private String userId;
    private int totalPrice=0;
    private List<Integer> saveTotalPrice=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentCartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        navController= Navigation.findNavController(view);
        userId=auth.getCurrentUser().getUid();
        binding.recViewCart.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new CartAdapter();

        viewModel=new ViewModelProvider(getActivity()).get(CartViewModel.class);

        viewModel.cartDataList().observe(getViewLifecycleOwner(), new Observer<List<Cart>>() {
            @Override
            public void onChanged(List<Cart> carts) {
                adapter.setListCart(carts);
                binding.recViewCart.setAdapter(adapter);
            }
        });

        firestore.collection("Cart"+userId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot ds:value.getDocuments()){
                    Cart cart=ds.toObject(Cart.class);
                    int total=cart.getPrice();

                    saveTotalPrice.add(total);
                }
                for (int i = 0; i < saveTotalPrice.size(); i++) {
                    totalPrice+=Integer.parseInt(String.valueOf(saveTotalPrice.get(i)));

                    binding.totalPriceOrderCart.setText(String.valueOf(totalPrice));
                }
                saveTotalPrice.clear();
                totalPrice=0;
            }
        });

        binding.btnCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firestore.collection("Products").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            QuerySnapshot shit=task.getResult();

                            for(DocumentSnapshot sn:shit.getDocuments()){
                                sn.getReference().update("quantity",0);
                            }
                        }
                    }
                });
                firestore.collection("Cart"+userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(DocumentSnapshot ds:task.getResult()){
                                ds.getReference().delete();
                            }
                        }
                    }
                });
                navController.navigate(R.id.action_cartFragment_to_productFragment);
                Toast.makeText(getContext(), "Order placed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
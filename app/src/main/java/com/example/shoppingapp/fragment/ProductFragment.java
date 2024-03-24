package com.example.shoppingapp.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoppingapp.R;
import com.example.shoppingapp.adapter.ProductAdapter;
import com.example.shoppingapp.databinding.FragmentProductBinding;
import com.example.shoppingapp.model.Cart;
import com.example.shoppingapp.model.Product;
import com.example.shoppingapp.mvvm.ProductViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ProductFragment extends Fragment implements ProductAdapter.OnClickedProduct {


    private FragmentProductBinding binding;
    private FirebaseAuth  mAuth;
    private FirebaseFirestore firestore;
    private ProductAdapter adapter;
    private ProductViewModel viewModel;
    String userId;
    int sum=0;

    private NavController navController;
    List<Integer> saveQuantity=new ArrayList<>();


    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentProductBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController= Navigation.findNavController(view);
        mAuth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        userId=mAuth.getCurrentUser().getUid();

        binding.recProduct.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new ProductAdapter(this);

        binding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_productFragment_to_productDetailFragment);
            }
        });

        firestore.collection("Cart"+userId).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentSnapshot ds:value.getDocuments()){
                    Cart cart=ds.toObject(Cart.class);
                    int quantityCounter=cart.getQuantity();

                    saveQuantity.add(quantityCounter);

                }
                for(int i=0;i<saveQuantity.size();i++){
                    sum+=Integer.parseInt(String.valueOf(saveQuantity.get(i)));
                }

                binding.cartquantity.setText(String.valueOf(sum));
                sum=0;
                saveQuantity.clear();
            }
        });
        viewModel=new ViewModelProvider(getActivity()).get(ProductViewModel.class);
        viewModel.getAllProducts().observe(getViewLifecycleOwner(), products -> {
            adapter.setListProduct(products);
            binding.recProduct.setAdapter(adapter);
        });
    }

    @Override
    public void onProductClicked(List<Product> productList, int position) {
        ProductFragmentDirections.ActionProductFragmentToProductDetailFragment action = ProductFragmentDirections.actionProductFragmentToProductDetailFragment();
        action.setTitle(productList.get(position).getTitle());
        action.setDescription(productList.get(position).getDescription());
        action.setImageUrl(productList.get(position).getIamgeUrl());
        action.setProductId(productList.get(position).getProductId());
        action.setPosition(position);

        navController.navigate(action);

    }

    @Override
    public void onResume() {
        super.onResume();
        sum=0;
    }
}
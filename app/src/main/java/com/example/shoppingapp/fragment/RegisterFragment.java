package com.example.shoppingapp.fragment;

import android.content.Intent;
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

import com.example.shoppingapp.R;
import com.example.shoppingapp.databinding.FragmentRegisterBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class RegisterFragment extends Fragment {
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private FragmentRegisterBinding binding;
    private NavController navController;
    private String name,email,password;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        auth=FirebaseAuth.getInstance();
        firestore=FirebaseFirestore.getInstance();
        navController= Navigation.findNavController(view);

        binding.btnLogSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_registerFragment_to_loginFragment);
            }
        });
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name=binding.edRegName.getText().toString();
                email=binding.edRegEmail.getText().toString();
                password=binding.edRegPass.getText().toString();

                if(name.isEmpty()){
                    binding.edRegName.setError("Enter Name");
                }
                else if(email.isEmpty()){
                    binding.edRegEmail.setError("Enter Email");
                }
                else if(password.isEmpty()||binding.edRegPass.length()<6){
                    binding.edRegPass.setError("Password must be length greather than 6 character");
                }
                else{
                    creatUser(name,email,password);
                }
            }
        });
    }

    private void creatUser(String name, String email, String password) {
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    FirebaseUser user=auth.getCurrentUser();

                    String userId=user.getUid();

                    HashMap<String ,Object> hashMap=new HashMap<>();

                    hashMap.put("username",name);
                    hashMap.put("email",email);
                    hashMap.put("userid",userId);

                    firestore.collection("Users").document(userId).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    Toast.makeText(getContext(), "Registered", Toast.LENGTH_SHORT).show();
                    navController.navigate(R.id.action_registerFragment_to_productFragment);
                }
            }
        });
    }
}
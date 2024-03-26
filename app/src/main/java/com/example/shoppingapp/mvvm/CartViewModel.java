package com.example.shoppingapp.mvvm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.shoppingapp.model.Cart;

import java.util.List;

public class CartViewModel extends ViewModel implements CartRepo.CartInterFace{
    CartRepo cartRepo=new CartRepo(this);
    MutableLiveData<List<Cart>> cartList=new MutableLiveData<>();
    public CartViewModel() {
        cartRepo=new CartRepo(this);
        cartRepo.getCartShit();
    }

    public LiveData<List<Cart>> cartDataList(){
        return cartList;
    }

    @Override
    public void cartList(List<Cart> cartList) {
        this.cartList.setValue(cartList);
    }

}

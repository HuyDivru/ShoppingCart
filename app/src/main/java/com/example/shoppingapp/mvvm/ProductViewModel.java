package com.example.shoppingapp.mvvm;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.example.shoppingapp.model.Product;
import androidx.lifecycle.ViewModel;
import java.util.List;

public class ProductViewModel extends ViewModel implements ProductRepo.OnProductInterface {

    MutableLiveData<List<Product>> mutableLiveData=new MutableLiveData<>();
    ProductRepo productRepo=new ProductRepo(this);


    public ProductViewModel() {
        productRepo.getAllProducts();
    }

    public LiveData<List<Product>> getAllProducts(){
        return mutableLiveData;
    }

    @Override
    public void products(List<Product> productList) {
//        ProductFragmetDirections.ActionProductFragmentToProductDetailFragment action=ProductFragmetDirections.actionProductFragmentToProductDetailFragment();

    }


}

package com.example.shoppingapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.databinding.ProductitemBinding;
import com.example.shoppingapp.model.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductHolder> {
    private List<Product> listProduct;
    OnClickedProduct interfaceClickProduct;
    @NonNull
    @Override
    public ProductAdapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProductHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.productitem,parent,false));
    }

    public ProductAdapter(OnClickedProduct interfaceClickProduct) {
        this.interfaceClickProduct = interfaceClickProduct;
    }

    public void setListProduct(List<Product> listProduct) {
        this.listProduct = listProduct;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ProductHolder holder, int position) {
        holder.binding.productName.setText(listProduct.get(position).getTitle());
        holder.binding.productContent.setText(listProduct.get(position).getDescription());
        Glide.with(holder.itemView.getContext()).load(listProduct.get(position).getIamgeUrl()).centerCrop().into(holder.binding.circleImageView);
    }

    @Override
    public int getItemCount() {
        if(listProduct==null){
            return 0;
        }
        else {
            return listProduct.size();
        }
    }

    public class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ProductitemBinding binding;
        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            binding.productName.setOnClickListener(this);
            binding.productContent.setOnClickListener(this);
            binding.circleImageView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            interfaceClickProduct.onProductClicked(listProduct,getAdapterPosition());
        }
    }
    public interface OnClickedProduct{
        void onProductClicked(List<Product> productList,int position);
    }
}

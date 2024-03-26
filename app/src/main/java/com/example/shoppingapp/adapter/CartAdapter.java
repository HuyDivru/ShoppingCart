package com.example.shoppingapp.adapter;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoppingapp.R;
import com.example.shoppingapp.databinding.CartitemsBinding;
import com.example.shoppingapp.model.Cart;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private List<Cart> listCart;
    private FirebaseFirestore firestore=FirebaseFirestore.getInstance();
    private FirebaseAuth auth=FirebaseAuth.getInstance();
    private String userid;
    private String documentid;

    public void setListCart(List<Cart> listCart) {
        this.listCart = listCart;
    }

    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.cartitems,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        holder.binding.cartproducttitle.setText(listCart.get(position).getTitle());
        holder.binding.cartproductprice.setText("Total Price is: "+ listCart.get(position).getPrice()+ "For the items: "+listCart.get(position).getQuantity());

        Glide.with(holder.itemView.getContext()).load(listCart.get(position).getImageUrl()).centerCrop().into(holder.binding.circleImageView2);
    }

    @Override
    public int getItemCount() {
        if(listCart==null){
            return 0;
        }
        return listCart.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CartitemsBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            binding.btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String  title=listCart.get(getAdapterPosition()).getTitle();
                    userid=auth.getCurrentUser().getUid();
                    documentid=listCart.get(getAdapterPosition()).getProductId();
                    firestore.collection("Products").document(documentid).update("quantity",0).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    firestore.collection("Cart"+userid).document(title).delete();
                    listCart.remove(listCart.get(getAdapterPosition()));
                    notifyItemRemoved(getAdapterPosition());
                }
            });
        }
    }
}

package shoppingapp.com.shoppingapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import shoppingapp.com.shoppingapp.Interface.ItemClickListner;
import shoppingapp.com.shoppingapp.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView cartProductName_tv,cartProductPrice_tv,cartProductQuantity_tv;
    private ItemClickListner itemClickListner;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        cartProductName_tv = itemView.findViewById(R.id.cart_product_name);
        cartProductPrice_tv = itemView.findViewById(R.id.cart_product_price);
        cartProductQuantity_tv = itemView.findViewById(R.id.cart_product_qty);
    }

    @Override
    public void onClick(View v) {
        itemClickListner.onClick(v, getAdapterPosition(), false);
    }
}

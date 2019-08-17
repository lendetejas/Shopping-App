package shoppingapp.com.shoppingapp.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import shoppingapp.com.shoppingapp.Interface.ItemClickListner;
import shoppingapp.com.shoppingapp.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

   public TextView product_name_tv,product_price_tv,product_desc_tv;
   public ImageView product_item_image;
    public ItemClickListner listner;
    public ProductViewHolder(@NonNull View itemView) {
        super(itemView);

        product_name_tv = itemView.findViewById(R.id.product_item_name);
        product_price_tv = itemView.findViewById(R.id.product_item_price);
        product_desc_tv = itemView.findViewById(R.id.product_item_desc);
        product_item_image = itemView.findViewById(R.id.product_item_image);
    }
    public void setItemClickListner(ItemClickListner listner){
        this.listner = listner;
    }

    @Override
    public void onClick(View view) {

        listner.onClick(view, getAdapterPosition(),false);
    }
}

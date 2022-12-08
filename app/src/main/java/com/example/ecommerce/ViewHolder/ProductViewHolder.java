package com.example.ecommerce.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.ecommerce.Interface.ItemClickListner;
import com.example.ecommerce.R;
//vid number 16
public class ProductViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener
{
    //acces to ower field in the xml
    public TextView txtProductName, txtProductDescription, txtProductPrice;
    public ImageView imageView;
    //acces to ower interface
    public ItemClickListner listner;


    public ProductViewHolder(View itemView)
    {
        super(itemView);


        imageView = (ImageView) itemView.findViewById(R.id.product_image);
        txtProductName = (TextView) itemView.findViewById(R.id.product_name);
        txtProductDescription = (TextView) itemView.findViewById(R.id.product_description);
        txtProductPrice = (TextView) itemView.findViewById(R.id.product_price);
    }
//when the user click on post (product) so we move the user to detail activity
    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner = listner;
    }

    @Override
    public void onClick(View view)
    {
        listner.onClick(view, getAdapterPosition(), false);
    }
}

package com.example.ecommerce.Interface;

import android.view.View;
//vid number 16
public interface ItemClickListner {
    //wich product the user clicked on
    void onClick(View view, int position, boolean isLongClick);
}

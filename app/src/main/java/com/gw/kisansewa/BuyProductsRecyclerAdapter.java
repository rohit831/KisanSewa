package com.gw.kisansewa;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class BuyProductsRecyclerAdapter extends RecyclerView.Adapter<BuyProductsRecyclerAdapter.BuyProductsRecyclerViewHolder>
{
    ArrayList<CropDetails> cropDetails;
    ArrayList<String> sellerNames;
    String userMobileNo;
    Context context;

    public BuyProductsRecyclerAdapter(ArrayList<CropDetails> cropDetails,ArrayList<String> sellerNames, Context context,String userMobileNo)
    {
        this.context=context;
        this.cropDetails=cropDetails;
        this.sellerNames=sellerNames;
        this.userMobileNo=userMobileNo;
    }

    @Override
    public BuyProductsRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.buy_product_layout,parent,false);

        BuyProductsRecyclerViewHolder holder=new BuyProductsRecyclerViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(BuyProductsRecyclerViewHolder holder, int position) {
        holder.vh_sellerName.setText(sellerNames.get(position));
        holder.vh_buyProductQuantity.setText(cropDetails.get(position).getCropQuantity());
        holder.vh_buyProductPrice.setText(cropDetails.get(position).getCropPrice());
        holder.vh_buyProductName.setText(cropDetails.get(position).getCropName());
        holder.vh_sellerMobileNo.setText(cropDetails.get(position).getMobileNo());
    }

    @Override
    public int getItemCount() {
        return cropDetails.size();
    }

    public class BuyProductsRecyclerViewHolder extends RecyclerView.ViewHolder
    {
        TextView vh_buyProductName,vh_buyProductQuantity,vh_sellerName,vh_buyProductPrice,vh_sellerMobileNo;
        public BuyProductsRecyclerViewHolder(final View view)
        {
            super(view);
            vh_sellerMobileNo=(TextView)view.findViewById(R.id.sellerMobileNo);
            vh_buyProductName=(TextView)view.findViewById(R.id.buyProductName);
            vh_buyProductPrice=(TextView)view.findViewById(R.id.buyProductPrice);
            vh_buyProductQuantity=(TextView)view.findViewById(R.id.buyProductQuantity);
            vh_sellerName=(TextView)view.findViewById(R.id.sellerName);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(context,ConfirmProductBuy.class);
                    intent.putExtra("sellerMobileNo",vh_sellerMobileNo.getText().toString());
                    intent.putExtra("userMobileNo",userMobileNo);
                    intent.putExtra("productName",vh_buyProductName.getText().toString());
                    view.getContext().startActivity(intent);

                }
            });
        }
    }
}

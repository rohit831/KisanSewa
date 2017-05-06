package com.gw.kisansewa;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

public class BuyProducts extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DBHandler dbHandler;
    private RecyclerView.Adapter adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    final Context mContext=this;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<CropDetails> cropDetails;
    String userMobileNo=new String();
    final Context context=this;
    ArrayList<String> sellerNames;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buy_products);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.buyProductsRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        cropDetails=new ArrayList<CropDetails>();
        sellerNames=new ArrayList<String>();
        userMobileNo=getIntent().getStringExtra("mobileNo");

        dbHandler=new DBHandler(context,null,null,1);
        recyclerView=(RecyclerView)findViewById(R.id.buyProductsView);
        cropDetails=dbHandler.getCropsAvailable();
        sellerNames=dbHandler.getSellerNames(cropDetails);
        adapter=new BuyProductsRecyclerAdapter(cropDetails,sellerNames,context,userMobileNo);
        layoutManager=new LinearLayoutManager(context);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dbHandler.removeUsedCrops();
                        cropDetails=dbHandler.getCropsAvailable();
                        sellerNames=dbHandler.getSellerNames(cropDetails);
                        adapter=new BuyProductsRecyclerAdapter(cropDetails,sellerNames,context,userMobileNo);
                        recyclerView.setAdapter(adapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },500);

            }
        });
    }
}

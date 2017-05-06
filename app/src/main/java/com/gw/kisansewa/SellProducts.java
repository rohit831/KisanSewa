package com.gw.kisansewa;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class SellProducts extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DBHandler dbHandler;
    SwipeRefreshLayout swipeRefreshLayout;
    final Context mContext=this;
    private RecyclerView.Adapter recyclerAdapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<CropDetails> cropDetails;
    private Button addProduct;
    String mobileNo=new String();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sell_products);
        cropDetails=new ArrayList<CropDetails>();

        dbHandler=new DBHandler(this,null,null,1);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.sellProductsRefresh);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mobileNo=getIntent().getStringExtra("mobileNo");
        recyclerView=(RecyclerView)findViewById(R.id.sellProductsView);
        cropDetails=dbHandler.retrieveSellerCrops(mobileNo);
        recyclerAdapter=new SellProductsRecyclerAdapter(cropDetails,mContext,mobileNo);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(recyclerAdapter);
        addProduct=(Button)findViewById(R.id.addProduct);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                LayoutInflater li=LayoutInflater.from(mContext);
                final View dialogView=li.inflate(R.layout.add_product,null);
                final AlertDialog.Builder customDialog= new AlertDialog.Builder(mContext);

                customDialog.setView(dialogView);
                final TextView title;
                final EditText productName,productPrice,productQuantity;
                productName=(EditText)dialogView.findViewById(R.id.productNameDialog);
                productPrice=(EditText)dialogView.findViewById(R.id.productPriceDialog);
                productQuantity=(EditText)dialogView.findViewById(R.id.productQuantityDialog);
                title=(TextView)dialogView.findViewById(R.id.textView);

                customDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                customDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(productName.getText().toString().equals("")
                                || productPrice.getText().toString().equals("")
                                || productQuantity.getText().toString().equals(""))
                        {
                            Toast.makeText(getApplicationContext(),"Enter All Details!!",Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            CropDetails crop = new CropDetails(productName.getText().toString(),
                                    productQuantity.getText().toString(),
                                    productPrice.getText().toString(),mobileNo);
                            if(dbHandler.checkSameCrop(mobileNo,productName.getText().toString()))
                                Toast.makeText(mContext,"Same product already exists",Toast.LENGTH_SHORT).show();
                            else if(Long.parseLong(productQuantity.getText().toString())==0)
                                Toast.makeText(mContext,"Quantity cannot be zero",Toast.LENGTH_SHORT).show();
                            else {
                                //Adding to database
                                if (dbHandler.addNewProduct(crop) != -1)
                                    cropDetails.add(crop);

                                else
                                    Toast.makeText(getApplicationContext(), "Product unable to add", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });
                customDialog.create();
                customDialog.show();
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cropDetails=dbHandler.retrieveSellerCrops(mobileNo);
                        recyclerAdapter=new SellProductsRecyclerAdapter(cropDetails,mContext,mobileNo);
                        recyclerView.setAdapter(recyclerAdapter);
                        swipeRefreshLayout.setRefreshing(false);
                    }
                },500);
            }
        });

    }

}


package com.gw.kisansewa;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FarmerLogin extends AppCompatActivity {

    private DBHandler dbHandler;
    private EditText mobileNo;
    private EditText password;
    private Button loginButton;
    private TextView registerHere;

    public static final String FarmerPreferences= "FarmerPrefs";
    public static final String FMobileNo="FMobileNo";

    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        sharedPreferences=getSharedPreferences(FarmerPreferences,MODE_PRIVATE);
        if(sharedPreferences.getLong(FMobileNo,0)!=0)
        {
            Intent intent=new Intent(this,HomeScreen.class);
            intent.putExtra("mobileNo",String.valueOf(sharedPreferences.getLong(FMobileNo,0)));
            startActivity(intent);
            finish();
        }

        farmerInitialization();
    }

    public void farmerInitialization()
    {
        dbHandler= new DBHandler(this,null,null,1);

        mobileNo=(EditText)findViewById(R.id.loginMobileNo);
        password=(EditText)findViewById(R.id.loginPassword);
        loginButton=(Button)findViewById(R.id.loginButton);
        registerHere=(TextView)findViewById(R.id.registerHere);

    }

    public void LoginOnClick(View view)
    {
        Cursor cursor=null;

        if(mobileNo.getText().toString().equals("") || password.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(),"Enter Mobile No/Password First!!",Toast.LENGTH_SHORT).show();
        else {

            cursor = dbHandler.searchFarmerLogin(mobileNo.getText().toString(), password.getText().toString());

            if (cursor.moveToFirst()) {

                SharedPreferences.Editor editor=sharedPreferences.edit();
                editor.putLong(FMobileNo,Long.parseLong(mobileNo.getText().toString()));
                editor.commit();

                Intent intent = new Intent(this, HomeScreen.class);
                intent.putExtra("mobileNo",mobileNo.getText().toString());
                startActivity(intent);
                finish();
            } else
                Toast.makeText(getApplicationContext(), "Invalid User Credentials!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void RegisterHereOnClick(View view)
    {
        Intent intent=new Intent(this,FarmerRegister.class);
        startActivity(intent);
    }
}

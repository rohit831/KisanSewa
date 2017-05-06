package com.gw.kisansewa;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class FarmerRegister extends AppCompatActivity {

    private DBHandler dbHandler;
    private FarmerDetails farmerDetails;

    private EditText name;
    private EditText mobileNo;
    private EditText area;
    private EditText city;
    private EditText state;
    private EditText password;
    private EditText confirmPassword;
    private Button register;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        farmerInitialization();
    }

    public void farmerInitialization()
    {
        dbHandler = new DBHandler(this,null,null,1);

        name=(EditText)findViewById(R.id.name);
        mobileNo=(EditText)findViewById(R.id.mobileNo);
        area=(EditText)findViewById(R.id.area);
        city=(EditText)findViewById(R.id.city);
        state=(EditText)findViewById(R.id.state);
        password=(EditText)findViewById(R.id.password);
        confirmPassword=(EditText)findViewById(R.id.confirmPassword);
        register=(Button) findViewById(R.id.registerBtn);

    }

    public void RegisterOnClick(View view)
    {
        if(name.getText().toString().equals("") || mobileNo.getText().toString().equals("") ||
                area.getText().toString().equals("") || city.getText().toString().equals("") ||
                state.getText().toString().equals("") || password.getText().toString().equals("") ||
                confirmPassword.getText().toString().equals(""))
            Toast.makeText(getApplicationContext(),"Enter All Details!!",Toast.LENGTH_SHORT).show();
        else {
            if (mobileNo.getText().toString().length() != 10)
                Toast.makeText(getApplicationContext(), "Enter a valid Mobile No!!", Toast.LENGTH_SHORT).show();
            else {

                if (!password.getText().toString().equals(confirmPassword.getText().toString()))
                    Toast.makeText(getApplicationContext(), "Passwords Don't Match!!", Toast.LENGTH_SHORT).show();

                else {

                    if (dbHandler.checkSameNoFarmer(mobileNo.getText().toString()))
                        Toast.makeText(getApplicationContext(), "Mobile No already registered!", Toast.LENGTH_SHORT).show();
                    else {
                        farmerDetails = new FarmerDetails(name.getText().toString(), mobileNo.getText().toString(),
                                area.getText().toString(), city.getText().toString(),
                                state.getText().toString(), password.getText().toString());

                        if (dbHandler.addNewFarmerRecord(farmerDetails) != -1) {
                            dbHandler.createPurchaseTable(mobileNo.getText().toString());
                            dbHandler.createSoldTable(mobileNo.getText().toString());
                            Toast.makeText(getApplicationContext(), "Registered Successfully!", Toast.LENGTH_SHORT).show();

                            SharedPreferences sharedPreferences = getSharedPreferences(FarmerLogin.FarmerPreferences, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putLong(FarmerLogin.FMobileNo, Long.parseLong(mobileNo.getText().toString()));
                            editor.commit();

                            Intent intent = new Intent(this, HomeScreen.class);
                            intent.putExtra("mobileNo", mobileNo.getText().toString());
                            startActivity(intent);
                            finish();
                        } else
                            Toast.makeText(getApplicationContext(), "Not Registered!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }
}

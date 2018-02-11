package com.idsc.projectconference20;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class RoleActivity extends AppCompatActivity {
    Button mBroadcast;
    Button mSubscribe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.role_layout);
        mBroadcast=findViewById(R.id.btn_broadcast);
        mSubscribe=findViewById(R.id.btn_subscribe);

        mBroadcast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent broadcast=new Intent(RoleActivity.this,BroadcastActivity.class);
                startActivity(broadcast);
                finish();
            }
        });
        mSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent suscribe = new Intent(RoleActivity.this,SuscriberActivity.class);
                startActivity(suscribe);
                finish();
            }
        });
    }

}

package com.example.ttproject1;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;


import android.os.Bundle;




public class MainActivity extends AppCompatActivity {
    TextView registerUser;
    EditText username, password;
    Button loginButton;
    String user, pass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        registerUser = (TextView)findViewById(R.id.register);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        loginButton = (Button)findViewById(R.id.loginButton);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
        });


      //  https://console.firebase.google.com/u/1/project/ttproject1-33fb5/database/ttproject1-33fb5/data

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if(user.equals("")){
                    username.setError("can't be blank");
                }
                else if(pass.equals("")){
                    password.setError("can't be blank");
                }
                else{
                    String url = "https://ttproject1-33fb5.firebaseio.com/users.json";
                    final ProgressDialog pd = new ProgressDialog(MainActivity.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            if(s.equals("null")){
                                Toast.makeText(MainActivity.this, "user not found", Toast.LENGTH_LONG).show();
                            }
                            else{
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if(!obj.has(user)){
                                        Toast.makeText(MainActivity.this, "user not found", Toast.LENGTH_LONG).show();
                                    }
                                    else if(obj.getJSONObject(user).getString("password").equals(pass)){//if password matches username
                                        UserDetails.username = user;
                                        UserDetails.password = pass;

                                        Intent intent22 = new Intent(MainActivity.this, Users.class);
                                        intent22.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//dosent go back to login screen
                                        intent22.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent22);
                                        finish();//dosent go back to login screen
                                    }
                                    else {
                                        Toast.makeText(MainActivity.this, "incorrect password", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            pd.dismiss();
                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(MainActivity.this);
                    rQueue.add(request);
                }

            }
        });
    }
}

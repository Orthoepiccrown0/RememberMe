package com.armor.rememberme;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setElevation(0);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1==1)Toast.makeText(Login.this, "Check username or password",Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void tryLogin(View view) {
        final EditText username  =findViewById(R.id.usernamebox);
        final EditText password  =findViewById(R.id.passwordbox);
        if(!username.getText().toString().trim().equals("")&&!username.getText().toString().trim().equals(""))
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    URL url = new URL("http://roccos.altervista.org/rest/login.php?username="+username.getText().toString()+"&password="+password.getText().toString()+"");
                    // Read all the text returned by the server
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String str;
                    String final_object = "0 results";
                    JSONObject myJson=null;
                    boolean passed = false;
                    while ((str = in.readLine()) != null)
                        final_object = str;
                    if(!(final_object.equals("0 results"))){
                        myJson = new JSONObject(final_object);
                        User.iduser = myJson.getString("id");
                        User.Name = myJson.getString("name");
                        User.username = myJson.getString("username");
                        passed = true;
                    }else{
                        Message msg = Message.obtain();
                        msg.arg1=1;
                        handler.sendMessage(msg);
                    }

                    in.close();
                    if(passed){
                        Intent intent = new Intent(Login.this,MainActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception  e) {
                    e.printStackTrace();
                    e.toString();
                }
            }
        }).start();
        else{
            Toast.makeText(this, "Compile all fields",Toast.LENGTH_SHORT).show();
        }
    }

    public void testLogin(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    URL url = new URL("http://roccos.altervista.org/rest/login.php?username=beer&password=123");
                    // Read all the text returned by the server
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String str;
                    String final_object = "0 results";
                    JSONObject myJson=null;
                    boolean passed = false;
                    while ((str = in.readLine()) != null)
                        final_object = str;
                    if(!(final_object=="0 results")){
                        myJson = new JSONObject(final_object);
                        User.iduser = myJson.getString("id");
                        User.Name = myJson.getString("name");
                        User.username = myJson.getString("username");
                        passed = true;
                    }

                    in.close();
                    if(passed){
                        Intent intent = new Intent(Login.this,MainActivity.class);
                        startActivity(intent);
                    }
                } catch (Exception  e) {
                    e.printStackTrace();
                    e.toString();
                }
            }
        }).start();
    }

    public void tryRegister(View view){
        Intent intent = new Intent(Login.this,Register.class);
        startActivity(intent);
    }
}

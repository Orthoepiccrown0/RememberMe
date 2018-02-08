package com.armor.rememberme;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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
        autoLogin();
    }

    private void autoLogin() {
        DataHelper helper = new DataHelper(this);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.query("Login",new String[]{"Username", "Password"},null,null,null,null,null);

        if(cursor.moveToFirst()){


            String Username = cursor.getString(0);
            String Password = cursor.getString(1);
            execLogin(Username,Password);
        }
    }

    public void tryLogin(View view) {
        final EditText usernameE  =findViewById(R.id.usernamebox);
        final EditText passwordE  =findViewById(R.id.passwordbox);
        String username = usernameE.getText().toString().trim();
        String password = passwordE.getText().toString().trim();

        if(!username.equals("")&&!password.equals(""))
            execLogin(username,password);
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
                        DataHelper helper = new DataHelper(Login.this);
                        SQLiteDatabase db = helper.getReadableDatabase();
                        DataHelper.insertUser(db,User.username,myJson.getString("password"));
                        passed = true;
                    }

                    in.close();
                    if(passed){
                        Intent intent = new Intent(Login.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
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

    private void execLogin(final String user,final String pass){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    URL url = new URL("http://roccos.altervista.org/rest/login.php?username="+user+"&password="+pass+"");
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
                        DataHelper helper = new DataHelper(Login.this);
                        SQLiteDatabase db = helper.getReadableDatabase();
                        DataHelper.insertUser(db,User.username,myJson.getString("password"));
                        passed = true;
                    }else{
                        Message msg = Message.obtain();
                        msg.arg1=1;
                        handler.sendMessage(msg);
                    }

                    in.close();
                    if(passed){
                        Intent intent = new Intent(Login.this,MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        finish();
                        startActivity(intent);
                    }
                } catch (Exception  e) {
                    e.printStackTrace();
                    e.toString();
                }
            }
        }).start();
    }
}

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
import java.io.InputStreamReader;
import java.net.URL;

public class Register extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Registration");
        getSupportActionBar().setElevation(0);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1==1) Toast.makeText(Register.this,"Username is already used",Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void onRegister(View view) {
        final EditText name = findViewById(R.id.reg_name);
        final EditText username = findViewById(R.id.reg_username);
        final EditText password = findViewById(R.id.reg_pass);
        final EditText password_confirm = findViewById(R.id.reg_pass2);

        final String pass = password.getText().toString().trim();
        final String pass_conf = password_confirm.getText().toString().trim();
        final String nameS = name.getText().toString().trim();
        final String usernameS = username.getText().toString().trim();
        TextView allfields = findViewById(R.id.allfields_error);
        if(pass.length()>3) {
            if (!pass.equals("") && !pass_conf.equals("") && pass.equals(pass_conf)) {
                if (!nameS.equals("") && !usernameS.equals(""))
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                URL url = new URL("https://roccos.altervista.org/rest/register.php?name=" + nameS + "&password=" + pass + "&username=" + usernameS + "");
                                // Read all the text returned by the server
                                BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                                String str;
                                String final_object = "User exist";
                                JSONObject myJson = null;
                                boolean passed = false;
                                while ((str = in.readLine()) != null)
                                    final_object = str;
                                if (final_object.equals("New records created successfully")) {
                                    url = new URL("http://roccos.altervista.org/rest/login.php?username=" + usernameS + "&password=" + pass + "");
                                    // Read all the text returned by the server
                                    in = new BufferedReader(new InputStreamReader(url.openStream()));
                                    final_object = "0 results";
                                    while ((str = in.readLine()) != null)
                                        final_object = str;
                                    if (!(final_object.equals("0 results"))) {
                                        myJson = new JSONObject(final_object);
                                        User.iduser = myJson.getString("id");
                                        User.Name = myJson.getString("name");
                                        User.username = myJson.getString("username");
                                        passed = true;
                                    }

                                } else if (final_object.equals("User exist")) {
                                    Message msg = Message.obtain();
                                    msg.arg1 = 1;
                                    handler.sendMessage(msg);
                                }

                                in.close();
                                if (passed) {
                                    Intent intent = new Intent(Register.this, MainActivity.class);
                                    startActivity(intent);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                                e.toString();
                            }
                        }
                    }).start();
                else allfields.setVisibility(View.VISIBLE);

            } else {
                Toast.makeText(this, "Verify your password", Toast.LENGTH_SHORT).show();
            }
        }else Toast.makeText(this, "Password length must be 4+ characters", Toast.LENGTH_SHORT).show();
    }
}

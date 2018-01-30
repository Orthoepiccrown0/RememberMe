package com.armor.rememberme;

import android.annotation.SuppressLint;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNote extends AppCompatActivity {


    public String header_string;
    public String content_string;
    public String Data;
    public String ccolor;
    public String hcolor;
    public String bcolor;

    public Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        setTitle("Add note");
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1 == 1)
                    Toast.makeText(AddNote.this, "Can't add your note", Toast.LENGTH_SHORT).show();
            }
        };
    }

    public void trySave(View view) {

        final EditText header = findViewById(R.id.add_header_box);
        final EditText content = findViewById(R.id.add_content_box);

        final String content_color = "Black";
        final String header_color = "Black";
        final String back_color = "White";
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        final String data = dateFormat.format(date);
        if (header.getText().toString().trim().equals(""))
            Toast.makeText(this, "Make sure to fill every box", Toast.LENGTH_SHORT).show();
        if (content.getText().toString().trim().equals(""))
            Toast.makeText(this, "Make sure to fill every box", Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String urlp = "http://roccos.altervista.org/rest/addnote.php?header=" + header.getText().toString() + "&content=" + content.getText().toString() + "&" +
                            "hcolor=" + header_color + "&ccolor=" + content_color + "&bcolor=" + back_color + "&id=" + User.iduser + "&date=" + data + "&tipo=" + User.type;
                    URL url = new URL(urlp);
                    // Read all the text returned by the server
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String str;
                    String final_object;

                    final_object = in.readLine();
                    if (!final_object.equals("New records created successfully")) {
                        Message msg = Message.obtain();
                        msg.arg1 = 1;
                        handler.sendMessage(msg);

                    }

                    in.close();
                    AddNote.this.finish();

                } catch (Exception e) {
                    e.printStackTrace();
                    //e.toString();
                }
            }
        }).start();

    }

    public void chooseColor(View view) {
        Toast.makeText(this, "Not supported yet", Toast.LENGTH_SHORT).show();
    }
}

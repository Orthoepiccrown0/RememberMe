package com.armor.rememberme;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditNote extends AppCompatActivity {

    public String header_string;
    public String content_string;
    public String Data;
    public String ccolor= "Black";
    public String hcolor= "Black";
    public String bcolor= "White";

    public Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);
        setTitle("Edit");
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.arg1==1)  Toast.makeText(EditNote.this,"Can't update your note",Toast.LENGTH_SHORT).show();
            }
        };
        if(User.note2edit!=null){
             EditText header = findViewById(R.id.edit_header_box);
             EditText content = findViewById(R.id.edit_content_box);

             header.setText(User.note2edit.getHeader());
             content.setText(User.note2edit.getContent());

             ccolor = User.note2edit.getContent_color();
             hcolor = User.note2edit.getHeader_color();
             bcolor = User.note2edit.getBack_color();


        }else{
            finish();
        }
    }
    public void trySave(View view) {

        final EditText header = findViewById(R.id.edit_header_box);
        final EditText content = findViewById(R.id.edit_content_box);


        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        final String data = dateFormat.format(date);
        if(header.getText().toString().trim().equals("")||content.getText().toString().trim().equals(""))
            Toast.makeText(this,"Make sure to fill every box",Toast.LENGTH_SHORT).show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    String urlp = "http://roccos.altervista.org/rest/updatenote.php?header="+header.getText().toString()+"&content="+content.getText().toString()+"&"+
                            "header_color="+hcolor+"&content_color="+ccolor+"&bcolor="+bcolor+"&id="+User.note2edit.getnId()+"&date="+data;
                    URL url = new URL(urlp);
                    // Read all the text returned by the server
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String str;
                    String final_object = "0 results";


                    final_object = in.readLine();
                    if(!final_object.equals("Record is updated")){
                        Message msg = Message.obtain();
                        msg.arg1 =1;
                        handler.sendMessage(msg);

                    }

                    in.close();
                    EditNote.this.finish();

                } catch (Exception  e) {
                    e.printStackTrace();
                    e.toString();
                }
            }
        }).start();

    }

    public void chooseColor(View view) {
        Toast.makeText(this,"Not supported yet",Toast.LENGTH_SHORT).show();
    }
}

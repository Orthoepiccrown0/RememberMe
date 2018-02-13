package com.armor.rememberme;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AddNote extends AppCompatActivity {


    public String content_color = "000000";
    public String header_color = "000000";
    public String back_color = "ffffff";
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

    public void updateColors() {
        EditText header = findViewById(R.id.add_header_box);
        EditText content = findViewById(R.id.add_content_box);
        header.setTextColor(Color.parseColor("#" + header_color));
        content.setTextColor(Color.parseColor("#" + content_color));

        //getWindow().setNavigationBarColor(Color.parseColor("#"+back_color));
        if (!back_color.toLowerCase().equals("ffffff")) {
            int statusColor = manipulateColor(Color.parseColor("#" + back_color), 0.8f);
            getWindow().setStatusBarColor(statusColor);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#" + back_color)));
        } else {
            getWindow().setStatusBarColor(Color.parseColor("#355088"));
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3b5998")));
        }
    }

    public int manipulateColor(int color, float factor) {
        int a = Color.alpha(color);
        int r = Math.round(Color.red(color) * factor);
        int g = Math.round(Color.green(color) * factor);
        int b = Math.round(Color.blue(color) * factor);
        return Color.argb(a,
                Math.min(r, 255),
                Math.min(g, 255),
                Math.min(b, 255));
    }

    public void chooseCColor(View view) {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose content color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));

                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        content_color = String.format("%06X", (0xFFFFFF & selectedColor));
                        updateColors();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .build()
                .show();
    }

    public void chooseBColor(View view) {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose background color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));

                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        back_color = String.format("%06X", (0xFFFFFF & selectedColor));
                        updateColors();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .build()
                .show();
    }

    public void chooseHColor(View view) {
        ColorPickerDialogBuilder
                .with(this)
                .setTitle("Choose header color")
                .initialColor(Color.WHITE)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setOnColorSelectedListener(new OnColorSelectedListener() {
                    @Override
                    public void onColorSelected(int selectedColor) {
                        //toast("onColorSelected: 0x" + Integer.toHexString(selectedColor));

                    }
                })
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        header_color = String.format("%06X", (0xFFFFFF & selectedColor));
                        updateColors();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }
}

package com.armor.rememberme;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class BusinessNotesCopy extends Fragment {

    RecyclerView recyclerview;
    RANotes adapter;
    List<Note> items = new ArrayList<>();
    List<JSONObject> jsonObjects = new ArrayList<>();
    boolean parse_passed= false;
    public BusinessNotesCopy() {
        // Required empty public constructor
    }


    public static BusinessNotesCopy newInstance(String param1, String param2) {
        BusinessNotesCopy fragment = new BusinessNotesCopy();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadCards();

    }

    private void loadCards() {
        //recyclerview = (RecyclerView) getView().findViewById(R.id.recycler_view);
        loadItems();
        if (!items.isEmpty()) {
            adapter = new RANotes(getActivity(), items);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerview.setLayoutManager(mLayoutManager);
            recyclerview.setAdapter(adapter);
        } else {
            Toast toast = Toast.makeText(getActivity(), "Nessuna connessione", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private void loadItems(){


        new Thread(new Runnable() {
            @Override
            public void run() {
                try
                {
                    parse_passed = false;
                    List<String> objects = new ArrayList<>();
                    URL url = new URL("https://roccos.altervista.org/rest/loadnotes.php?idcliente="+User.iduser+"&tipo=1");
                    // Read all the text returned by the server
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String str;
                    String final_object = "";

                    //boolean passed = false;
                    while ((str = in.readLine()) != null)
                        final_object += str;

                    if (final_object != "") {
                        boolean opened = true;
                        int startpos = 0;
                        int endpos = 0;
                        char[] charArray = final_object.toCharArray();
                        for (int i = 1; i < final_object.length() - 1; i++)
                        {

                            if (charArray[i] == '{') { opened = true; startpos = i; }
                            if (charArray[i] == '}')
                            {
                                opened = false; endpos = i;
                            }

                            if (opened == false)
                            {
                                String tmp = "";
                                for (int j = startpos; j <= endpos; j++)
                                {
                                    tmp += charArray[j];
                                }
                                objects.add(tmp);
                                opened = true;
                            }
                        }

                    }
                    parseJsonObjects(objects);
                    parseNotes();
                    in.close();
                    parse_passed = true;
                    /*if(passed){
                        Intent intent = new Intent(Login.this,MainActivity.class);
                        startActivity(intent);
                    }*/
                } catch (Exception  e) {
                    e.printStackTrace();
                   //e.toString();
                }
            }
            private void parseJsonObjects(List<String> objects){
                jsonObjects.clear();
                try {
                    for (String str : objects) {
                        jsonObjects.add(new JSONObject(str));
                    }
                }catch (Exception ex){ex.printStackTrace();}
            }

            private void parseNotes(){
                items.clear();
                try {
                    for (JSONObject obj: jsonObjects) {
                         String id = obj.getString("idNota");
                         String content = obj.getString("content");
                         String header = obj.getString("header");
                         String content_color = obj.getString("content_color");
                         String header_color = obj.getString("header_color");
                         String back_color = obj.getString("back_color");
                        items.add(new Note(id,content,header,content_color,header_color,back_color));
                    }
                }catch (Exception ex){ex.printStackTrace();}
            }
        }).start();
        int i = 0;
        while(!parse_passed)i++;
        //System.out.println(i+" Cycles");
        //i = 0;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_notes, container, false);
        View RootView = inflater.inflate(R.layout.fragment_notes, container, false);

        recyclerview = RootView.findViewById(R.id.recycler_view);
        //tv.setText("HI");
        loadCards();
        return RootView;
    }

    // TODO: Rename method, update argument and hook method into UI event


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


}

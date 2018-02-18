package com.armor.rememberme;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PersonalNotes extends Fragment {

    RecyclerView recyclerview;
    FloatingActionButton mFloatingActionButton;
    RANotes adapter;
    List<Note> items = new ArrayList<>();
    List<JSONObject> jsonObjects = new ArrayList<>();
    Handler handler;
    boolean parse_passed = false;

    public PersonalNotes() {
        // Required empty public constructor
    }


    public static BusinessNotes newInstance(String param1, String param2) {
        BusinessNotes fragment = new BusinessNotes();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //loadCards();

    }

    private void loadCards() {
        //recyclerview = (RecyclerView) getView().findViewById(R.id.recycler_view);

        int i = 0;
        if (!items.isEmpty()) {
            TextView emptynotes = getActivity().findViewById(R.id.emptynotes);
            emptynotes.setVisibility(View.INVISIBLE);
            adapter = new RANotes(getActivity(), items);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerview.setLayoutManager(mLayoutManager);
            recyclerview.setAdapter(adapter);
        } else {
            TextView emptynotes = getActivity().findViewById(R.id.emptynotes);
            emptynotes.setVisibility(View.VISIBLE);
        }
    }

    public void loadItems() {

        try {
            parse_passed = false;
            List<String> objects = new ArrayList<>();
            URL url = new URL("https://roccos.altervista.org/rest/loadnotes.php?idcliente=" + User.iduser + "&tipo=2");
            // Read all the text returned by the server
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String str;
            String final_object = "";

            //boolean passed = false;
            while ((str = in.readLine()) != null)
                final_object += str;

            if (!final_object.equals("0 results")) {
                JSONArray jarray = new JSONArray(final_object);
                jsonObjects.clear();
                for (int i = 0; i < jarray.length(); i++) {
                    jsonObjects.add(jarray.getJSONObject(i));
                }

                parseNotes();
                in.close();
                parse_passed = true;
            }
            Message msg = Message.obtain();
            msg.arg1 = 0;
            handler.sendMessage(msg);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @SuppressLint("HandlerLeak")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_notes, container, false);
        View RootView = inflater.inflate(R.layout.fragment_notes, container, false);

        recyclerview = RootView.findViewById(R.id.recycler_view);
        mFloatingActionButton = RootView.findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                // .setAction("Action", null).show();
                Intent intent = new Intent(getContext(), AddNote.class);
                startActivity(intent);
            }
        });
        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && mFloatingActionButton.getVisibility() == View.VISIBLE) {
                    mFloatingActionButton.hide();
                } else if (dy < 0 && mFloatingActionButton.getVisibility() != View.VISIBLE) {
                    mFloatingActionButton.show();
                }
            }
        });

        final SwipeRefreshLayout refreshLayout = RootView.findViewById(R.id.swipeRefreshLayout);
        refreshLayout.setColorSchemeColors(Color.GRAY);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.setRefreshing(true);
                        loadItems();
                        refreshLayout.setRefreshing(false);
                    }
                }).start();
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                loadItems();
            }
        }).start();

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.arg1 == 0)
                    loadCards();
            }

        };

        User.type = "2";
        return RootView;
    }

    // TODO: Rename method, update argument and hook method into UI event


    private void parseNotes() {
        items.clear();
        try {
            for (JSONObject obj : jsonObjects) {
                String id = obj.getString("idNota");
                String content = obj.getString("content");
                String header = obj.getString("header");
                String content_color = obj.getString("content_color");
                String header_color = obj.getString("header_color");
                String back_color = obj.getString("back_color");
                items.add(new Note(id, content, header, content_color, header_color, back_color));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                loadItems();
            }
        }).start();
    }


}
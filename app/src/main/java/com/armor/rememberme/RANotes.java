package com.armor.rememberme;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;


public class RANotes extends RecyclerView.Adapter<RANotes.MyViewHolder>{
    private Context mContext;
    private List<Note> noteList;
    private String selectedType;
    public Note item;
    private CheckBox CHBX;


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        item = noteList.get(position);
        holder.header.setText(item.getHeader());
        holder.content.setText(item.getContent());
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callEditor(holder.overflow);
            }
        });
        holder.overflow.setContentDescription((CharSequence) item.getnId());
        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.overflow);
            }
        });

    }

    private void callEditor(ImageView overflow) {
        boolean passed = false;
        for(Note n : noteList){
            if(n.getnId()==overflow.getContentDescription()){
                User.note2edit = n;
                passed = true;
            }
        }
        if(passed){
            Intent intent = new Intent(mContext, EditNote.class);
            mContext.startActivity(intent);
        }
    }

    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_card, popup.getMenu());
        String id = (String) view.getContentDescription();
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(id));
        popup.show();
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    public RANotes(Context mContext, List<Note> noteList) {
        this.mContext = mContext;
        this.noteList = noteList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView header;
        TextView content;
        CardView card;
        ImageView overflow;
        String idNote;
        public MyViewHolder(View itemView) {
            super(itemView);
            header = (TextView) itemView.findViewById(R.id.header);
            content = (TextView) itemView.findViewById(R.id.content);
            card = itemView.findViewById(R.id.item_card);
            overflow = itemView.findViewById(R.id.overflow);

        }
    }

    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private String id;
        public MyMenuItemClickListener(String id) {
            this.id = id;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.delete_card:
                    deleteNote();
                    return true;

            }
            return false;
        }

        private void deleteNote(){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        String urlp = "https://roccos.altervista.org/rest/deletenote.php?id="+id;
                        URL url = new URL(urlp);
                        // Read all the text returned by the server
                        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                        String str;
                        String final_object = "0 results";


                        final_object = in.readLine();
                        if(final_object.equals("Record successfully deleted")){
                            //Toast.makeText(mContext, "Can't delete the note", Toast.LENGTH_SHORT).show();
                            if(User.type=="2"){
                                MainActivity notes = (MainActivity) mContext;
                                final PersonalNotes myFragment = (PersonalNotes) notes.getSupportFragmentManager().findFragmentByTag("notes");
                                if (myFragment != null && myFragment.isVisible()) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            myFragment.loadItems();
                                        }
                                    }).start();

                                }
                            }else if(User.type=="1"){
                                MainActivity notes = (MainActivity) mContext;
                                final BusinessNotes myFragment = (BusinessNotes) notes.getSupportFragmentManager().findFragmentByTag("notes");
                                if (myFragment != null && myFragment.isVisible()) {
                                    new Thread(new Runnable() {
                                        @Override
                                        public void run() {
                                            myFragment.loadItems();
                                        }
                                    }).start();

                                }
                            }
                        }else{
                            //Toast.makeText(mContext, "The note deleted successfully!", Toast.LENGTH_SHORT).show();
                        }

                        in.close();


                    } catch (Exception  e) {
                        e.printStackTrace();
                        e.toString();
                    }
                }
            }).start();
        }
    }
}

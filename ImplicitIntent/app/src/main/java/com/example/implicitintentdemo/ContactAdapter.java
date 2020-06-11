package com.example.implicitintentdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.implicitintentdemo.Contact;
import com.example.implicitintentdemo.R;

public class ContactAdapter extends RecyclerView.Adapter {
    private Context ctx;
    private ArrayList<Contact> ContactList;

    public ContactAdapter(Context ctx,ArrayList<Contact> ContactList){
        this.ctx = ctx;
        this.ContactList = ContactList;
    }

    //this method is called only once when setAdapter method is called from container class
    //it is used to provide design that should repeated to display each and every contact object from ContactList ArrayList

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View ContactRow = inflater.inflate(R.layout.contact_row,null);
        MyWidgetContainer container = new MyWidgetContainer(ContactRow);
        return container;
    }

    //this method is called automatically after oncreateviewholder method is called
    //it will called as many times as many object are there in contactlist arraylist
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MyWidgetContainer container = (MyWidgetContainer) holder;
        Contact c = ContactList.get(position); //get method return object from given position
        container.lblname.setText(c.getName());
        container.lblcontact.setText(c.getNumber());
        container.relcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number = c.getNumber(); //get the contact on which user clicked
                Intent ReturnIntent = new Intent();
                ReturnIntent.putExtra("contact",number);
                Activity activity = (Activity) ctx;
                activity.setResult(99,ReturnIntent);
                activity.finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return ContactList.size();
    }

    class MyWidgetContainer extends RecyclerView.ViewHolder
    {
        public TextView lblname,lblcontact;
        public RelativeLayout relcontact;
        public MyWidgetContainer(@NonNull View itemView) {
            super(itemView);
            lblname = itemView.findViewById(R.id.txtcontact);
            lblcontact = itemView.findViewById(R.id.txtnumber);
            relcontact = itemView.findViewById(R.id.relcontact);
        }
    }
}


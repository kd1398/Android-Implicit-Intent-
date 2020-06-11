package com.example.implicitintentdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import com.example.implicitintentdemo.Contact;
import com.example.implicitintentdemo.R;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
//used to get data from data source( Content Provider (android system))
public class ContactContainer extends AppCompatActivity {
    Context ctx = this;
    ArrayList<Contact> ContactList= new ArrayList<>(); //arrayList
    Set<Contact> ContactTreeSet; //it will store only n number of unique Contact type object

    @BindView(R.id.reccontact) RecyclerView reccontact;

    private final String CONTACT_NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;
    private final String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

    Contact CurrentContact = null;
    String name,number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_container);
        ButterKnife.bind(this); //it's compulsory to call this method for butterknief lib to work
        ContactTreeSet = new TreeSet<Contact>(new Contact());
        GetDataFromSystem();
    }
    public void GetDataFromSystem(){
        //get data from system(contact name and numbers)
        ContentResolver cr = this.getContentResolver();
        String[] FieldList = {CONTACT_NAME,NUMBER};
        Cursor c = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                FieldList,null,null,CONTACT_NAME);
        if(c!=null && c.getCount()!=0)
        {
            while(c.moveToNext())
            {
                name = c.getString(c.getColumnIndex(CONTACT_NAME));
                number = c.getString(c.getColumnIndex(NUMBER));
                CurrentContact = new Contact(name,number);
                //ContactList.add(CurrentContact);
                ContactTreeSet.add(CurrentContact);
            }
            c.close();
            for(Contact con:ContactTreeSet){
                ContactList.add(new Contact(con.getName(),con.getNumber()));
            }
            ContactAdapter adapter = new ContactAdapter(ctx,ContactList);
            reccontact.setItemAnimator(new DefaultItemAnimator());
            reccontact.setLayoutManager(new GridLayoutManager(ctx,1));
            reccontact.setAdapter(adapter);
        }
    }
}

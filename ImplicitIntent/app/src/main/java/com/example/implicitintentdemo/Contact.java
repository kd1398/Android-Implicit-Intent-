package com.example.implicitintentdemo;

import java.util.Comparator;

public class Contact implements Comparator {
    private String name,contact;

    public Contact(){

    }
    public Contact(String name,String contact){
        this.name = name;
        this.contact = contact;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return contact;
    }

    public void setNumber(String contact) {
        this.contact = contact;
    }

    @Override
    public int compare(Object old, Object current) {
        Contact c1 =(Contact) old;
        Contact c2 =(Contact) current;
        String num1 = c1.getNumber().replaceAll("[^0-9]", ""); //9662512857 96625212857
        String num2 = c2.getNumber().replaceAll("[^0-9]", "");
        if(num1.equals(num2))
            return 0;
        else
            return 1;
    }
}

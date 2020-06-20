package com.example.rpstock.Objects;

public class Item {


    private String name;
    private int amount;


    public Item() {
    }

    public Item(String _name, int _amount) {
        this.name = _name;
        this.amount = _amount;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}

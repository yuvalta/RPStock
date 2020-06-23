package com.example.rpstock.Objects;

public class Item {


    private String name;
    private int amount;
    private String diameter;
    private String kind;


    public Item() {
    }

    public Item(String _name, int _amount, String _diameter, String _kind) {
        this.name = _name;
        this.amount = _amount;
        this.kind = _kind;
        this.diameter = _diameter;

    }

    public String getDiameter() {
        return diameter;
    }

    public void setDiameter(String diameter) {
        this.diameter = diameter;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
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

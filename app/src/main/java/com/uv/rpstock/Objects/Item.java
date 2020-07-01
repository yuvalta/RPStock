package com.uv.rpstock.Objects;

public class Item {


    private String name;
    private int amount;
    private int seq;
    private String diameter;
    private String kind;
    private String ID;
    private String nippleLength;

    public Item() {
    }

    public Item(String _name, int _amount, String _diameter, String _kind, String _ID, int _seq, String _nippleLength) {
        this.name = _name;
        this.amount = _amount;
        this.kind = _kind;
        this.diameter = _diameter;
        this.ID = _ID;
        this.seq = _seq;
        this.nippleLength = _nippleLength;
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

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getNippleLength() {
        return nippleLength;
    }

    public void setNippleLength(String nippleLength) {
        this.nippleLength = nippleLength;
    }

    public static Double convertDiameter(String strDiameter) {
        switch (strDiameter) {
            case "3/4\"":
                return 0.75;
            case "1\"":
                return 1.0;
            case "1.25\"":
                return 1.25;
            case "1.5\"":
                return 1.5;
            case "2\"":
                return 2.0;
            default:
                return 0.0;
        }
    }

    public static Integer convertLength(String nippleLength) {
        if (!nippleLength.equals("ללא")) {
            return Integer.valueOf(nippleLength);
        }
        return 0;
    }
}

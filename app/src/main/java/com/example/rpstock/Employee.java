package com.example.rpstock;

public class Employee {

    private String name;
    private String email;
    private String password;
    private String phone;
    private boolean isAdmin;

    public Employee() {
    }

    public Employee(String _name, String _email, String _password, String _phone, boolean _is_admin) {
        this.name = _name;
        this.email = _email;
        this.password = _password;
        this.phone = _phone;
        this.isAdmin = _is_admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}

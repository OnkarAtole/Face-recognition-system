package com.example.try16;

public class HelperClass {
    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
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

    public String getConfirm() {
        return confirm;
    }

    public void setConfirm(String confirm) {
        this.confirm = confirm;
    }

    public String getTid() {
        return Tid;
    }

    public void setTid(String tid) {
        Tid = tid;
    }



    public HelperClass() {
    }

    String first, last, email, password, confirm, Tid;

    public HelperClass(String first, String last, String email, String password, String confirm, String tid) {
        this.first = first;
        this.last = last;
        this.email = email;
        this.password = password;
        this.confirm = confirm;
        this.Tid = tid;

    }
}

package com.example.demo.pojo;

public class User {

    int id;
    String username;
    String password;
    String confirmPassword;
    String booktitle;
    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getUsername(){
        return username;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public String getBooktitle(){
        return booktitle;
    }
    public void setBooktitle(String booktitle){
        this.booktitle = booktitle;
    }


    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getConfirmPassword(){
        return confirmPassword;
    }


}


package com.musicapp.urmusic;
 
public class Category {
     
    private int id;
    private String message;
     
    public Category(){}
     
    public Category(int id, String message){
        this.id = id;
        this.message = message;
    }
     
    public void setId(int id){
        this.id = id;
    }
     
    public void setMessage(String message){
        this.message = message;
    }
     
    public int getId(){
        return this.id;
    }
     
    public String getMessage(){
        return this.message;
    }
 
}
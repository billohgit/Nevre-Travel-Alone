package com.example.splitfare;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")

/// Helper class which defines the basic structure of a post object
/// This defines all methods and variables of posts
public class Post implements Serializable{

    private String name;
    private String source;
    private String destination;
    private String MoT;
    private int persons;
    private String Mobile;
    private String Date;
    private String Pid;
    private String Uid;
    private String Time;
    private String useremail;
    private ArrayList<String> comments = new ArrayList<String>();

    public ArrayList<String> getList(){
        return comments;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public void setlist(ArrayList<String> mylist){
        this.comments = mylist;
    }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getPid() {
        return Pid;
    }

    public void setPid(String pid) {
        Pid = pid;
    }

    public Post() {


    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
    public String getID() {
        return Pid;
    }

    public void setID(String ID) {
        this.Pid = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getMoT() {
        return MoT;
    }

    public void setMoT(String moT) {
        MoT = moT;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

   public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }
}

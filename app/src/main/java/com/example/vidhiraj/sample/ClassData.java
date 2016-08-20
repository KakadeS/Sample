package com.example.vidhiraj.sample;

/**
 * Created by vidhiraj on 10-08-2016.
 */
public class ClassData {
   public String name;
    public String subject;
   public  int image;
    public int id;



    public ClassData()
    {


    }
    public ClassData(String name, String subject,int image,int id) {
        this.name = name;
        this.subject = subject;
        this.image=image;
        this.id=id;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }
    public String getSubject() {
        return subject;
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setImage(int image) {
        this.image = image;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}

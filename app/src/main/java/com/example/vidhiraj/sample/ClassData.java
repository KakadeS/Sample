package com.example.vidhiraj.sample;

/**
 * Created by vidhiraj on 10-08-2016.
 */
public class ClassData {
    String name;
    String version;
    int image;

    public ClassData(String name, String version,int image) {
        this.name = name;
        this.version = version;
        this.image=image;
    }


    public String getName() {
        return name;
    }


    public String getVersion() {
        return version;
    }

    public int getImage() {
        return image;
    }


}

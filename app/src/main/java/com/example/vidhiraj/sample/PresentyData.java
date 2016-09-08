package com.example.vidhiraj.sample;

import java.io.Serializable;

/**
 * Created by lenovo on 31/08/2016.
 */
public class PresentyData implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private int pointId;

    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    private boolean isSelected;

    public PresentyData() {

    }



    public PresentyData(String name, int pointId) {

        this.name = name;
        this.pointId= pointId;

    }

    public PresentyData(String name,boolean isSelected,int pointId) {

        this.name = name;
        this.isSelected = isSelected;
        this.pointId= pointId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

}


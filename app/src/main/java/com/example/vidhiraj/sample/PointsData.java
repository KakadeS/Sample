package com.example.vidhiraj.sample;

import java.io.Serializable;

/**
 * Created by lenovo on 21/08/2016.
 */
public class PointsData implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String name;
    private int pointId;
    private boolean isSelected;

    public PointsData() {

    }

    public int getPointId() {
        return pointId;
    }

    public void setPointId(int pointId) {
        this.pointId = pointId;
    }

    public PointsData(String name, int pointId) {

        this.name = name;
        this.pointId= pointId;

    }

    public PointsData(String name,boolean isSelected,int pointId) {

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


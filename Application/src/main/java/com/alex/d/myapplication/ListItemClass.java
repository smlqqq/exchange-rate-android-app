package com.alex.d.myapplication;

public class ListItemClass {

    String data1;
    String data2;
    String data3;
    String imageUrl;



    public ListItemClass(String data1, String data2, String data3, String image) {
        this.data1 = data1;
        this.data2 = data2;
        this.data3 = data3;
        this.imageUrl = image;
    }

    public ListItemClass() {

    }
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getData1() {
        return data1;
    }

    public void setData1(String data1) {
        this.data1 = data1;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public String getData3() {
        return data3;
    }

    public void setData3(String data3) {
        this.data3 = data3;
    }
}

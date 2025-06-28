package com.example.try16;

public class Helper {

    private String presentCount;
    private String rollNo;
    private String name;
    private String imageUrl;

    public Helper() {

        this.presentCount= String.valueOf(0);
    }

    public Helper(String rollNo, String name, String imageUrl,String presentCount) {
        this.rollNo = rollNo;
        this.name = name;
        this.imageUrl = imageUrl;
        this.presentCount=presentCount;
    }

    // Getters and setters
    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPresentCount() {
        return presentCount;
    }

    public void setPresentCount(String rollNo) {
        this.presentCount = presentCount;
    }
}

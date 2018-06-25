package org.redwings.cameratest2;

/**
 * Created by 이준희 on 2018-06-24.
 */

public class FoodListItem {
    public int number;
    public String name;
    public String imageURL;

    public int getNumber() {return number;}
    public String getName() {return name;}
    public String getImageURL() {return imageURL;}

    public FoodListItem(int number, String name, String imageURL) {
        this.number = number;
        this.name = name;
        this.imageURL = imageURL;
    }
}
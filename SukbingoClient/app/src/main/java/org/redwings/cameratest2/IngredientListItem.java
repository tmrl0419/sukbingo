package org.redwings.cameratest2;

/**
 * Created by 이준희 on 2018-06-23.
 */

public class IngredientListItem {
    public int number;
    public String name;

    public int getNumber() {return number;}
    public String getName() {return name;}

    public IngredientListItem(int number, String name) {
        this.number = number;
        this.name = name;
    }
}
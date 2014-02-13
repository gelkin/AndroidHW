package com.gmail.mazinva.WashTimetable;

public class Order {
    String brand;
    String color;
    String telephone;
    int time;

    public Order(String brand, String color, String telephone, int time) {
        this.brand = brand;
        this.color = color;
        this.telephone = telephone;
        this.time = time;
    }

    public static String timeToString(int time) {
        String timeS = new Integer(time).toString();
        timeS = timeS.substring(0, (timeS.length())/2 - 1)
                + ":"
                + timeS.substring((timeS.length())/2, (timeS.length()) - 1);
        return timeS;
    }
}

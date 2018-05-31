package com.refactoring.chapter1.ex3;//  Refactoring, a First Example, step3, (~p21)

import java.util.*;

public class Movie {
    public static final int CHILDRENS = 2;
    public static final int REGULAR = 0;
    public static final int NEW_RELEASE = 1;

    private String _title;        // 名稱
    private int _priceCode;       // 價格

    public Movie(String title, int priceCode){
        _title = title;
        _priceCode = priceCode;
    }

    public int getPriceCode(){
        return _priceCode;
    }

    public void setPriceCode(int arg){
        _priceCode = arg;
    }

    public String getTitle(){
        return _title;
    }

    // jjhou add
    public static void main(String[] args) {
        System.out.println("Refactoring, a First Example, step3");
    }

}

class Rental {
    private Movie _movie;         // 影片
    private int _daysRented;      // 租期

    public Rental(Movie movie, int daysRented) {
        _movie = movie;
        _daysRented = daysRented;
    }

    public int getDaysRented() {
        return _daysRented;
    }

    public Movie getMovie() {
        return _movie;
    }

    double getCharge() {
        double result = 0;
        switch (getMovie().getPriceCode()) {
            case Movie.REGULAR:
                result += 2;
                if (getDaysRented() > 2)
                    result += (getDaysRented() - 2) * 1.5;
                break;
            case Movie.NEW_RELEASE:
                result += getDaysRented() * 3;
                break;
            case Movie.CHILDRENS:
                result += 1.5;
                if (getDaysRented() > 3)
                    result += (getDaysRented() - 3) * 1.5;
                break;
        }
        return result;
    }
}

class Customer {
    private String _name;                                 // 姓名
    private Vector _rentals = new Vector();               // 租借記錄

    public Customer(String name) {
        _name = name;
    }

    public void addRental(Rental arg) {
        _rentals.addElement(arg);
    }

    public String getName() {
        return _name;
    }

    public String statement() {
        double totalAmount = 0;             // 總消費金額
        int frequentRenterPoints = 0;       // 常客積點
        Enumeration rentals = _rentals.elements();
        String result = "Rental Record for " + getName() + "\n";

        while(rentals.hasMoreElements()) {
            double thisAmount = 0;
            Rental each = (Rental) rentals.nextElement(); // 取得一筆租借記錄

            // add frequent renter points（累加 常客積點）
            frequentRenterPoints ++;
            // add bonus for a two day new release rental
            if ((each.getMovie().getPriceCode() == Movie.NEW_RELEASE) &&
                    each.getDaysRented() > 1)
                frequentRenterPoints ++;

            // show figures for this rental（顯示此筆租借資料）
            result += "\t" + each.getMovie().getTitle() + "\t" +
                    String.valueOf(each.getCharge()) + "\n";
            totalAmount += each.getCharge();
        }

        // add footer lines（結尾列印）
        result += "Amount owed is " + String.valueOf(totalAmount) + "\n";
        result += "You earned " + String.valueOf(frequentRenterPoints) +
                " frequent renter points";
        return result;
    }
}

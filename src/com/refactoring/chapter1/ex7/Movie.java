package com.refactoring.chapter1.ex7;//  Refactoring, a First Example, step7, (~p52)

import java.util.*;

public class Movie {
    public static final int CHILDRENS = 2;
    public static final int REGULAR = 0;
    public static final int NEW_RELEASE = 1;

    private String _title;        // 名稱
    private Price _price;         // 價格   // <-- changed

    public Movie(String title, int priceCode){
        _title = title;
        setPriceCode(priceCode);
    }

    public int getPriceCode(){
        return _price.getPriceCode();   // <-- changed
    }

    public void setPriceCode(int arg) {   // <-- changed
        switch (arg) {
            case REGULAR:               // 普通片
                _price = new RegularPrice();
                break;
            case CHILDRENS:             // 兒童片
                _price = new ChildrensPrice();
                break;
            case NEW_RELEASE:           // 新片
                _price = new NewReleasePrice();
                break;
            default:
                throw new IllegalArgumentException("Incorrect Price Code");
        }
    }

    public String getTitle(){
        return _title;
    }

    // jjhou add
    public static void main(String[] args) {
        System.out.println("Refactoring, a First Example, step7");

        Movie m1 = new Movie("Seven", Movie.NEW_RELEASE);
        Movie m2 = new Movie("Terminator", Movie.REGULAR);
        Movie m3 = new Movie("Star Trek", Movie.CHILDRENS);

        Rental r1 = new Rental(m1, 4);
        Rental r2 = new Rental(m1, 2);
        Rental r3 = new Rental(m3, 7);
        Rental r4 = new Rental(m2, 5);
        Rental r5 = new Rental(m3, 3);

        Customer c1 = new Customer("jjhou");
        c1.addRental(r1);
        c1.addRental(r4);

        Customer c2 = new Customer("gigix");
        c2.addRental(r1);
        c2.addRental(r3);
        c2.addRental(r2);

        Customer c3 = new Customer("jiangtao");
        c3.addRental(r3);
        c3.addRental(r5);

        Customer c4 = new Customer("yeka");
        c4.addRental(r2);
        c4.addRental(r3);
        c4.addRental(r5);

        System.out.println(c1.statement());
        System.out.println(c2.statement());
        System.out.println(c3.statement());
        System.out.println(c4.statement());
    }

    double getCharge(int daysRented) {
        return _price.getCharge(daysRented);
    }

    int getFrequentRenterPoints(int daysRented) {
        return _price.getFrequentRenterPoints(daysRented);
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
        return _movie.getCharge(_daysRented);
    }

    int getFrequentRenterPoints() {
        return _movie.getFrequentRenterPoints(_daysRented);
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
        Enumeration rentals = _rentals.elements();
        String result = "Rental Record for " + getName() + "\n";

        while (rentals.hasMoreElements()) {
            Rental each = (Rental) rentals.nextElement();

            //show figures for this rental
            result += "\t" + each.getMovie().getTitle()+ "\t" +
                    String.valueOf(each.getCharge()) + "\n";
        }

        //add footer lines（結尾列印）
        result += "Amount owed is " +
                String.valueOf(getTotalCharge()) + "\n";
        result += "You earned " +
                String.valueOf(getTotalFrequentRenterPoints()) +
                " frequent renter points";
        return result;
    }

    public String htmlStatement() {
        Enumeration rentals = _rentals.elements();
        String result = "<H1>Rentals for <EM>" + getName() +
                "</EM></H1><P>\n";
        while (rentals.hasMoreElements()) {
            Rental each = (Rental) rentals.nextElement();
            //show figures for each rental
            result += each.getMovie().getTitle()+ ": " +
                    String.valueOf(each.getCharge()) + "<BR>\n";
        }
        //add footer lines
        result +=  "<P>You owe <EM>" +
                String.valueOf(getTotalCharge()) +
                "</EM><P>\n";
        result += "On this rental you earned <EM>" +
                String.valueOf(getTotalFrequentRenterPoints()) +
                "</EM> frequent renter points<P>";
        return result;
    }

    // 譯註：此即所謂query method
    private int getTotalFrequentRenterPoints(){
        int result = 0;
        Enumeration rentals = _rentals.elements();
        while (rentals.hasMoreElements()) {
            Rental each = (Rental) rentals.nextElement();
            result += each.getFrequentRenterPoints();
        }
        return result;
    }

    // 譯註：此即所謂query method
    private double getTotalCharge() {
        double result = 0;
        Enumeration rentals = _rentals.elements();
        while (rentals.hasMoreElements()) {
            Rental each = (Rental) rentals.nextElement();
            result += each.getCharge();
        }
        return result;
    }
}

abstract class Price {
    abstract int getPriceCode();            // 取得價格代號
    abstract double getCharge(int daysRented);

    //默认行为
    int getFrequentRenterPoints(int daysRented) {
        return 1;
    }

}

class ChildrensPrice extends Price {
    int getPriceCode() {
        return Movie.CHILDRENS;
    }

    double getCharge(int daysRented) {
        double result = 1.5;
        if (daysRented > 3)
            result += (daysRented - 3) * 1.5;
        return result;
    }
}

class NewReleasePrice extends Price {
    int getPriceCode() {
        return Movie.NEW_RELEASE;
    }

    double getCharge(int daysRented) {
        return daysRented * 3;
    }

    int getFrequentRenterPoints(int daysRented) {
        return (daysRented > 1) ? 2: 1;
    }
}

class RegularPrice extends Price {
    int getPriceCode() {
        return Movie.REGULAR;
    }

    double getCharge(int daysRented) {
        double result = 2;
        if (daysRented > 2)
            result += (daysRented - 2) * 1.5;
        return result;
    }
}
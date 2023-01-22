package model;

import java.util.ArrayList;

public class Person {

    public int id;
    public String name;
    public String email;
    public Address address;
    public ArrayList<Children> children;

    public static class Address {
        public String street;
        public String house;
        public String appartment;
        public String city;
        public String zipcode;
    }

    public static class Children {
        public String name;
        public String age;
    }
}

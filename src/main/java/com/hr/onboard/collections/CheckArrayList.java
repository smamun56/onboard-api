package com.hr.onboard.collections;

import java.util.ArrayList;
import java.util.Collections;

public class CheckArrayList {

    public static void main(String[] args){
        ArrayList<String> people = new ArrayList<>();

        people.add("Jack");
        people.add("Adam");
        people.add("Lesti");

//        people.remove("Lesti");

        Collections.sort(people);

        System.out.println(people);

        if(people.contains("Lesti")){
            System.out.println("Lesti exists");
        }
        if (people.isEmpty()){
            System.out.println("Empty People");
        }

        people.remove("Adam");

        people.clear();


//        System.out.println("hello check");
    }

}

package com.hr.onboard.collections;


import java.util.Collections;
import java.util.LinkedList;

public class CheckLinkList {
    public static void main(String[] args){
        LinkedList<String> people = new LinkedList<>();

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

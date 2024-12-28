package com.hr.onboard.collections;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class CheckArrayList {

    public static void main(String[] args) throws InterruptedException {
        Instant start = Instant.now();
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

        Thread.sleep(2000); // Simulate a delay
        Instant end = Instant.now();

        Duration duration = Duration.between(start, end);
        Date testDate = new Date();
        System.out.println(start + " - - " + end);
        System.out.println(testDate);
        System.out.println("Elapsed Time: " + duration.toMillis() + " ms");

    }

}

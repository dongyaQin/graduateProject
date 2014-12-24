package test;

import java.util.*;

public class GarbageCollector {

    public static void main(String... args) {

        System.out.printf("Testing...%n");
        List<Double> list = new ArrayList<Double>();
        for (int outer = 0; outer < 10000; outer++) {

             list = new ArrayList<Double>(10000); // BAD
//             list = new ArrayList<Double>(); // WORSE
//            list.clear(); // BETTER

            for (int inner = 0; inner < 10000; inner++) {
                list.add(Math.random());
            }

            if (outer % 1000 == 0) {
                System.out.printf("Outer loop at %d%n", outer);
            }

        }
        System.out.printf("Done.%n");
    }
}
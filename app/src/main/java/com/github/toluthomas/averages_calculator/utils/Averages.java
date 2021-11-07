package com.github.toluthomas.averages_calculator.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

// For measures of central tendency (mean, median and mode)
public class Averages {

    public static Double getMean(int sum, int count){
        return (sum * 1.0)/count; // To cast to double (if we get a decimal during division)
    }

    public static Double getMedian(ArrayList<Integer> numbers){
        Collections.sort(numbers); // Sort array of numbers in ascending order
        int n = numbers.size(); // where n is length of array
        if (n % 2 == 0) // If array has an even length, median is average of middle two numbers
            return ((numbers.get(n / 2 - 1) + numbers.get(n / 2)) * 1.0)/2;
        else // If array has an odd length, median is middle number
            return (double) numbers.get(n / 2);
    }

    public static int getMode(ArrayList<Integer> numbers){
        HashMap<Integer,Integer> modes = new HashMap<>();
        int max  = 1;
        int mode = 0;

        for(int i = 0; i < numbers.size(); i++) { // Loop through every number
            int number = numbers.get(i); // Get the current number in the loop
            if (modes.get(number) != null) { // If the number has been added to the hashmap
                int frequency = modes.getOrDefault(number, 0); // Get the number's frequency
                frequency++; // Increment it's frequency
                modes.put(number, frequency); // Update the hashmap to track the number and its frequency

                if(frequency > max) { // If the number's frequency is greater than the current max,
                    max  = frequency; // update our max frequency with its frequency
                    mode = number; // that's our new mode
                }
            }
            else
                modes.put(number,1); // If the number has not been added to the hashmap, add it with a frequency of 1
        }
        return mode; // Return mode
    }
}

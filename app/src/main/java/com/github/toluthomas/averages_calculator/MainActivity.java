package com.github.toluthomas.averages_calculator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.toluthomas.averages_calculator.components.CalculatorButton;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FlexboxLayout operandsContainer, operatorsContainer; // Containers for calculator buttons
    String[] operands, operators; // Names of calculator buttons
    Drawable operandBackground, operatorBackground; // Backgrounds of calculator buttons
    ViewGroup.LayoutParams buttonSize; // Dimensions for calculator buttons
    TextView numbersTextView, resultTextView; // Text view where result and numbers will show

    String lastButtonPressed; // Last button that the user pressed

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // This activity will use activity_main xml as its view
        this.operandsContainer = findViewById(R.id.operands_container); // Get the container of the operands
        this.operatorsContainer = findViewById(R.id.operators_container); // Get the container of the operators
        this.numbersTextView = findViewById(R.id.numbers); // Get the text view where inputs will go
        this.resultTextView = findViewById(R.id.result); // Get the text view where the mean, median or mode will show
        this.operators = new String[]{"C", "x̄", "x̃", "Mo", "->"}; // Prepare the operators
        this.operands = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", /*".",*/ "AC"}; // Prepare the operands
        this.buttonSize = new FlexboxLayout.LayoutParams(200, 200); // Set size of buttons
        this.operandBackground = AppCompatResources.getDrawable(this, R.drawable.button_white); // Background for each operand
        this.operatorBackground = AppCompatResources.getDrawable(this, R.drawable.button_green); // Background for each operator
        createButtons(operators, operatorsContainer, buttonSize, this, operatorBackground, 13); // Create buttons for operators
        createButtons(operands ,operandsContainer, buttonSize, this, operandBackground, 1); // Create buttons for operators
    }

    protected void createButtons(String[] buttonNames, FlexboxLayout layout, ViewGroup.LayoutParams layoutParams, Context context, Drawable background, int index){
        for (String buttonName: buttonNames) { // Iterate over array of button names to create buttons with those names
            // Initialise a new button that will be added to the calculator
            Button button = new CalculatorButton(context, buttonName, background, index, layoutParams).getButton();
            button.setOnClickListener(this);
            layout.addView(button); // Insert each button into the flexbox container
            index++; // Increment the index so that the ID changes
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        String buttonPressed = this.getButtonPressed(v.getId()); // Get the string value of a pressed button
        String currentText = this.numbersTextView.getText().toString(); // Get the text in the result text field
        // Check if button is a number
        if(isButtonANumber(buttonPressed)){
            this.numbersTextView.append(buttonPressed);
        }
        // Check if button is a period (for decimal numbers)
//        else if(buttonPressed.equals(".") && !currentText.contains(".")){
//            this.numbersTextView.append("."); // Append . to the number shown in number textview
//        }
        else if (buttonPressed.equals("x̄")){
            Double mean = this.getMean(this.getSum(this.getIntNumbers()), this.getIntNumbers().size()); // Get mean
            String result = String.valueOf(mean); // Get string value of mean
            this.resultTextView.setText(result); // Show result
        }
        else if (buttonPressed.equals("x̃")){
            Double median = this.getMedian(this.getIntNumbers()); // Get median
            String result = String.valueOf(median); // Get string value of median
            this.resultTextView.setText(result); // Show result
        }
        else if (buttonPressed.equals("Mo")){
            int mode =  this.getMode(this.getIntNumbers()); // Get mode
            String result = String.valueOf(mode); // Get string value of mode
            this.resultTextView.setText(result); // Show result
        }
        // Append number to the list of numbers in numbers text view
        else if (buttonPressed.equals("->")){
            // Only append a comma if there's already a number in the text view
            if (!numbersTextView.getText().toString().isEmpty()){
                this.numbersTextView.append(",");
            }
            // Prevent repeated commas
            else if(!this.lastButtonPressed.equals(",")){
                this.numbersTextView.append(",");
            }
        }
        // If user presses the C button, backspace
        else if (buttonPressed.equals("C")){
            this.backspace(); // Backspace
        }
        // If user presses the AC button, clear all
        else if (buttonPressed.equals("AC")){
            this.allClear(); // Clear everything
        }
        this.lastButtonPressed = buttonPressed; // Keep track of the last button that user pressed
    }

    private void backspace(){
        String currentText = numbersTextView.getText().toString(); // Get the text that's currently in the number text view
        // If user has not typed anything, no need to backspace
        // If user has, create a new string that ends before the last character
        String newText = currentText.length() > 0 ? currentText.substring(0, currentText.length()) : currentText;
        numbersTextView.setText(newText);
    }

    private void allClear(){
        this.numbersTextView.setText(""); // Reset numbers display
        this.resultTextView.setText(""); // Reset result display
        this.lastButtonPressed = ""; // Reset the last button pressed
    }

    private ArrayList<Integer> getIntNumbers(){
        String numbers = this.numbersTextView.getText().toString(); // Get the text in the numbers text view
        ArrayList<Integer> intNumbers = new ArrayList<>(); // A place to numbers to be operated on
        String[] stringNumbers = numbers.split(","); // Get comma delimited numbers
        for (String stringNumber: stringNumbers) // Loop through the numbers (strings)
            intNumbers.add(Integer.valueOf(stringNumber)); // Save integer value of each string in array
        return intNumbers; // Return the array list of integers
    }

    private Double getMean(int sum, int count){
        return (sum * 1.0)/count; // To cast to double (if we get a decimal during division)
    }

    private Double getMedian(ArrayList<Integer> numbers){
        Collections.sort(numbers); // Sort array of numbers in ascending order
        int n = numbers.size(); // where n is length of array
        if (n % 2 == 0) // If array has an even length, median is average of middle two numbers
            return ((numbers.get(n / 2 - 1) + numbers.get(n / 2)) * 1.0)/2;
        else // If array has an odd length, median is middle number
            return (double) numbers.get(n / 2);
    }

    private int getMode(ArrayList<Integer> numbers){
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

    // Numbers in an array list
    private int getSum(ArrayList<Integer> numbers){
        int sum = 0;
        for (int number: numbers)
            sum+=number;
        return sum;
    }

    // Check if a button is a number
    private boolean isButtonANumber(String button){
        return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0").contains(button);
    }

    // Use the ID of the button to retrieve the associated button name
    private String getButtonPressed(int id){
        switch (id) {
            case 11: // AC
                return "AC";
            case 13: // C
                return "C̄";
            case 14: // x̄
                return "x̄";
            case 15: // x̃
                return "x̃";
            case 16: // Mo
                return "Mo";
            case 17: // ->
                return "->";
            default: // 1, 2, 3, 4, 5, 6, 7, 8, 9, 0
                // If the ID is 10, the button is 0. Otherwise, return the ID of the button
                return ("" + id).equals("10") ? "0" : "" + id;
        }
    }
}
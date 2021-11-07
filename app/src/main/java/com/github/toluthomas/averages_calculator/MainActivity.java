package com.github.toluthomas.averages_calculator;

import static com.github.toluthomas.averages_calculator.utils.Averages.getMean;
import static com.github.toluthomas.averages_calculator.utils.Averages.getMedian;
import static com.github.toluthomas.averages_calculator.utils.Averages.getMode;

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
            Double mean = getMean(this.getSum(this.getIntNumbers()), this.getIntNumbers().size()); // Get mean
            String result = String.valueOf(mean); // Get string value of mean
            this.resultTextView.setText(result); // Show result
        }
        else if (buttonPressed.equals("x̃")){
            Double median = getMedian(this.getIntNumbers()); // Get median
            String result = String.valueOf(median); // Get string value of median
            this.resultTextView.setText(result); // Show result
        }
        else if (buttonPressed.equals("Mo")){
            int mode =  getMode(this.getIntNumbers()); // Get mode
            String result = String.valueOf(mode); // Get string value of mode
            this.resultTextView.setText(result); // Show result
        }
        // Append number to the list of numbers in numbers text view
        else if (buttonPressed.equals("->")){
            // Only append a comma if there's already a number in the text view
            if (!numbersTextView.getText().toString().isEmpty())
                // Prevent repeated commas
                if(!currentText.substring(currentText.length() - 1).equals(","))
                        this.numbersTextView.append(","); // If last char in string does not have , you can append it
        }
        // If user presses the C button, backspace
        else if (buttonPressed.equals("C̄")){
            this.backspace(); // Backspace
        }
        // If user presses the AC button, clear all
        else if (buttonPressed.equals("AC")){
            this.allClear(); // Clear everything
        }
    }

    private void backspace(){
        String currentText = numbersTextView.getText().toString(); // Get the text that's currently in the number text view
        // If user has not typed anything, no need to backspace
        // If user has, create a new string that ends before the last character
        String newText = currentText.length() > 0 ? currentText.substring(0, currentText.length() - 1) : currentText;
        this.numbersTextView.setText(newText);
    }

    private void allClear(){
        this.numbersTextView.setText(""); // Reset numbers display
        this.resultTextView.setText("0"); // Reset result display
    }

    private ArrayList<Integer> getIntNumbers(){
        String numbers = this.numbersTextView.getText().toString(); // Get the text in the numbers text view
        ArrayList<Integer> intNumbers = new ArrayList<>(); // A place to numbers to be operated on
        String[] stringNumbers = numbers.split(","); // Get comma delimited numbers
        for (String stringNumber: stringNumbers) // Loop through the numbers (strings)
            if(!stringNumber.isEmpty()) // This can happen if user enters "1, 2," where there's a trailing comma
                intNumbers.add(Integer.valueOf(stringNumber)); // Save integer value of each string in array
        return intNumbers; // Return the array list of integers
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
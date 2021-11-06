package com.github.toluthomas.calculator;

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

import com.github.toluthomas.calculator.components.CalculatorButton;
import com.google.android.flexbox.FlexboxLayout;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FlexboxLayout operandsContainer, operatorsContainer; // Containers for calculator buttons
    String[] operands, operators; // Names of calculator buttons
    Drawable operandBackground, operatorBackground; // Backgrounds of calculator buttons
    ViewGroup.LayoutParams buttonSize; // Dimensions for calculator buttons
    TextView textView; // Text view where result of calculations will show

    String lastButtonPressed; // Last button that the user pressed

    Double accumulator = 0.0; // Result of calculation
    Double operand = 0.0; // Current operand to perform operation with
    String operator = ""; // Current operator to perform operation with

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // This activity will use activity_main xml as its view
        this.operandsContainer = findViewById(R.id.operands_container); // Get the container of the operands
        this.operatorsContainer = findViewById(R.id.operators_container); // Get the container of the operators
        this.textView = findViewById(R.id.result); // Get the text view where inputs will go
        this.operators = new String[]{"+", "-", "x", "/", "="}; // Prepare the operators
        this.operands = new String[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "0", ".", "AC"}; // Prepare the operands
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
        String currentText = this.textView.getText().toString(); // Get the text in the result text field
        // Check if button is a number
        if(isButtonANumber(buttonPressed)){
            if(currentText.equals("0")){
                this.textView.setText(buttonPressed);
                this.accumulator = Double.valueOf(this.textView.getText().toString()); // Start accumulator as first number typed
            }
            else{
                if(this.operator.isEmpty()){
                    this.textView.append(buttonPressed);
                    this.accumulator = Double.valueOf(this.textView.getText().toString());
                }
                else {
                    // if last button was an operator, update operand
                    this.operand = this.isLastButtonAnOperator() ? Double.valueOf(buttonPressed) : Double.valueOf(this.textView.getText().toString() + buttonPressed);
                    if (this.isLastButtonAnOperator()){ // If previous button was an operator, replace the text in the result
                        this.textView.setText(buttonPressed);
                    }
                    else{ // If previous button was NOT an operator, append the new text to the old text
                        this.textView.append(buttonPressed);
                    }
                }
            }
        }
        // Check if button is a period (for decimal numbers)
        else if(buttonPressed.equals(".") && !currentText.contains(".")){
            this.textView.append("."); // Append . to the number shown in result
            this.accumulator = Double.valueOf(this.textView.getText().toString() + "0"); // Append 0 to the string since string ends in .
        }
        // Check if button pressed can be used to calculate
        else if (Arrays.asList("+", "-", "x", "/").contains(buttonPressed)){
            this.operator = buttonPressed; // Record the operator that the user has selected
        }
        // Check if button pressed is =
        else if (buttonPressed.equals("=")){
            if(!this.operator.isEmpty()){ // Check if current operator is NOT empty
                Double zero = 0.0;
                if (zero.equals(this.operand) && this.operator.equals("/")){ // If user is trying to divide by 0
                    this.allClear(); // Reset operand, operator, and accumulator
                    this.textView.setText(R.string.Undefined); // Show undefined
                }
                else{
                    this.accumulate(this.operator); // Calculate new result
                    this.textView.setText(String.valueOf(this.accumulator)); // Show result
                }
            }
        }
        // If user presses the AC button, clear result
        else if (buttonPressed.equals("AC")){
            this.allClear(); // Clear result
        }
        this.lastButtonPressed = buttonPressed; // Keep track of the last button that user pressed
    }

    private void accumulate(String operator){
        // Determine the operation to perform on operand and accumulator by the operator supplied
        switch(operator){
            case "+":
                this.accumulator+=this.operand; // Increment accumulator by operand
                break;
            case "-":
                this.accumulator-=this.operand; // Decrement accumulator by operand
                break;
            case "x":
                this.accumulator*=this.operand; // Multiply accumulator by operand
                break;
            case "/":
                this.accumulator/=this.operand; // Divide accumulator by operand
                break;
            default: // If operator does not match any of the above, leave the Switch
                break;
        }
    }
    private boolean isDouble(String number){
        return number.matches("[-+]?[0-9]*\\.?[0-9]+"); // Check if a number is a Double type
    }

    private boolean areDouble(String a, String b){
        return this.isDouble(a) || this.isDouble(b); // Check if one of two numbers is a Double type
    }

    private double getDouble(String number){
        return Double.parseDouble(number); // Attempt to get a Double from a string i.e if the string looks like a Double
    }

    private int getInt(String number){
        return Integer.parseInt(number); // Attempt to get an integer from a string i.e if the string looks like an integer
    }

    private void add(int a, int b){
        this.textView.setText(String.valueOf(a + b)); // Add two integers together
    }

    private void add(double a, double b){
        this.textView.setText(String.valueOf(a + b)); // Add two Doubles together
    }

    private void subtract(int a, int b){
        this.textView.setText(String.valueOf(a - b)); // Subtract an integer from another
    }

    private void subtract(double a, double b){
        this.textView.setText(String.valueOf(a - b)); // Subtract a Double from another
    }

    // Divide one integer by another
    private void divide(int a, int b){
        if(b == 0){
            this.textView.setText(R.string.Undefined); // Be careful if the divisor is 0. Show undefined in the result field
        }
        else{
            this.textView.setText(String.valueOf(a/b)); // If divisor is not 0, continue with division
        }
    }

    // Divide one Double by another
    private void divide(double a, double b){
        if(b == 0.0){
            this.textView.setText(R.string.Undefined); // Be careful if the divisor is 0. Show undefined in the result field
        }
        else{
            this.textView.setText(String.valueOf(a/b)); // If divisor is not 0, continue with division
        }
    }

    // Multiply one integer by another
    private void multiply(int a, int b){
        this.textView.setText(String.valueOf(a * b)); // Update the result field with the result of the multiplication
    }

    // Multiply one integer by another
    private void multiply(double a, double b){
        this.textView.setText(String.valueOf(a * b)); // Update the result field with the result of the multiplication
    }

    // Perform operation on two integers depending on the operator passed
    private void returnResult(int a, int b, String operator){
        switch(operator){
            case "+":
                add(a, b); // Add two integers
                break;
            case "-":
                subtract(a, b); // Subtract from integer from another
                break;
            case "x":
                multiply(a, b); // Multiply one integer by another
                break;
            case "/":
                divide(a, b); // Divide one integer by another
                break;
            default: // For instance, if equals, do nothing
                break;
        }
    }

    // Perform operation on two Doubles depending on the operator passed
    private void returnResult(double a, double b, String operator){
        switch(operator){
            case "+":
                add(a, b); // Add two Doubles
                break;
            case "-":
                subtract(a, b); // Subtract from Double from another
                break;
            case "x":
                multiply(a, b); // Multiply one Double by another
                break;
            case "/":
                divide(a, b); // Divide one Double by another
                break;
            default: // For instance, if equals, do nothing
                break;
        }
    }

    // Calculate using the previous and current operands, as well as the operator required for the operationn
    private void doArithmetic(String a, String b, String operator){
        if (this.areDouble(a, b)){ // Are numbers double?
            this.returnResult(getDouble(a), getDouble(b), operator); // Do calculations as Doubles
        }
        else {
            this.returnResult(getInt(a), getInt(b), operator); // Do calculations as integers
        }
    }

    // Update the last operator
    private void updateLastOperator(String operator){
        this.operator = operator;
    }

    // Check if a button is a number
    private boolean isButtonANumber(String button){
        return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0").contains(button);
    }

    // Check if the last button that the user tapped is a number
    private boolean isLastButtonANumber(){
        return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0").contains(this.lastButtonPressed);
    }

    // Update the last button that user tapped
    private void updateLastButtonPressed(String buttonName) {
        this.lastButtonPressed = buttonName;
    }

    // Check if the last button that the user tapped is an operator
    private boolean isLastButtonAnOperator() {
        return Arrays.asList("+", "-", "x", "/", "=", "AC").contains(this.lastButtonPressed);
    }

    // Check if a button is an operator
    private boolean isCurrentButtonAnOperator(String currentButton) {
        return Arrays.asList("+", "-", "x", "/", "=", "AC").contains(currentButton);
    }

    // Use the ID of the button to retrieve the associated button name
    private String getButtonPressed(int id){
        switch (id) {
            case 11: // .
                return ".";
            case 12: // AC
                return "AC";
            case 13: // +
                return "+";
            case 14: // -
                return "-";
            case 15: // x
                return "x";
            case 16: // /
                return "/";
            case 17: // =
                return "=";
            default: // 1, 2, 3, 4, 5, 6, 7, 8, 9, 0
                return ("" + id).equals("10") ? "0" : "" + id; // If the ID is 10, the button is 0. Otherwise, return the ID of the button
        }
    }

    private void allClear(){ // Clear all values used to make calculations
        this.operand = 0.0;
        this.operator = "";
        this.accumulator = 0.0;
        this.textView.setText("0"); // Reset the result to 0
    }
}
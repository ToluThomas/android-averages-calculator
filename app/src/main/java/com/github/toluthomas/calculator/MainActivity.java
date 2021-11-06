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
    FlexboxLayout operandsContainer, operatorsContainer;
    String[] operands, operators;
    Drawable operandBackground, operatorBackground;
    ViewGroup.LayoutParams buttonSize;
    TextView textView;

    String lastButtonPressed;

    Double accumulator = 0.0;
    Double operand;
    String operator = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
            System.out.println(buttonName);
            System.out.println(index);
            // Instantiate a new button that will be added to the calculator
            Button button = new CalculatorButton(context, buttonName, background, index, layoutParams).getButton();
            button.setOnClickListener(this);
            layout.addView(button); // Insert each button into the flexbox container
            index++; // Increment the index so that the ID changes
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        String buttonPressed = this.getButtonPressed(v.getId());
        String currentText = this.textView.getText().toString();
        if(isButtonANumber(buttonPressed)){
            if(currentText.equals("0")){
                this.textView.setText(buttonPressed);
                this.operand = Double.valueOf(this.textView.getText().toString());
            }
            else{
                // if last button was an operator, update operand
                this.operand = this.isLastButtonAnOperator() ? Double.valueOf(buttonPressed) : Double.valueOf(this.textView.getText().toString() + buttonPressed);
                if (this.isLastButtonAnOperator()){
                    this.textView.setText(buttonPressed);
                }
                else{
                    this.textView.append(buttonPressed);
                }
            }
        }
        else if(buttonPressed.equals(".") && !currentText.contains(".")){
            this.textView.append(".");
            this.accumulator = Double.valueOf(this.textView.getText().toString() + "0"); // Append 0 to the string since string ends in .
        }
        else if (Arrays.asList("+", "-", "x", "/").contains(buttonPressed)){
            this.accumulate(buttonPressed); // Calculate new result
            this.textView.setText(String.valueOf(this.accumulator)); // Show result
            this.operator = buttonPressed;
        }
        else if (buttonPressed.equals("=")){
            if(!this.operator.isEmpty()){
                this.accumulate(this.operator); // Calculate new result
                this.textView.setText(String.valueOf(this.accumulator)); // Show result
            }
        }
        else if (buttonPressed.equals("AC")){
            // All clear
            this.allClear();
        }
        this.lastButtonPressed = buttonPressed;
    }

    private void accumulate(String operator){
        System.out.println(this.operator);
        System.out.println(this.operand);
        System.out.println(this.accumulator);

        switch(operator){
            case "+":
                this.accumulator+=this.operand;
                break;
            case "-":
                this.accumulator-=this.operand;
                break;
            case "x":
                this.accumulator*=this.operand;
                break;
            case "/":
                this.accumulator/=this.operand;
                break;
            default:
                break;
        }
    }
    private boolean isDouble(String number){
        return number.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    private boolean areDouble(String a, String b){
        return this.isDouble(a) || this.isDouble(b);
    }

    private double getDouble(String number){
        return Double.parseDouble(number);
    }

    private int getInt(String number){
        return Integer.parseInt(number);
    }

    private void add(int a, int b){
        this.textView.setText(String.valueOf(a + b));
    }

    private void add(double a, double b){
        this.textView.setText(String.valueOf(a + b));
    }

    private void subtract(int a, int b){
        this.textView.setText(String.valueOf(a - b));
    }

    private void subtract(double a, double b){
        this.textView.setText(String.valueOf(a - b));
    }

    private void divide(int a, int b){
        if(b == 0){
            this.textView.setText(R.string.Undefined);
        }
        else{
            this.textView.setText(String.valueOf(a/b));
        }
    }

    private void divide(double a, double b){
        if(b == 0.0){
            this.textView.setText(R.string.Undefined);
        }
        else{
            this.textView.setText(String.valueOf(a/b));
        }
    }

    private void multiply(int a, int b){
        this.textView.setText(String.valueOf(a * b));
    }

    private void multiply(double a, double b){
        this.textView.setText(String.valueOf(a * b));
    }

    private void returnResult(int a, int b, String operator){
        switch(operator){
            case "+":
                add(a, b);
                break;
            case "-":
                subtract(a, b);
                break;
            case "x":
                multiply(a, b);
                break;
            case "/":
                divide(a, b);
                break;
            default: // For instance, if equals, do nothing
                break;
        }
    }

    private void returnResult(double a, double b, String operator){
        switch(operator){
            case "+":
                add(a, b);
                break;
            case "-":
                subtract(a, b);
                break;
            case "x":
                multiply(a, b);
                break;
            case "/":
                divide(a, b);
                break;
            default: // For instance, if equals, do nothing
                break;
        }
    }

    private void doArithmetic(String a, String b, String operator){
        if (this.areDouble(a, b)){ // Are numbers double?
            this.returnResult(getDouble(a), getDouble(b), operator);
        }
        else {
            this.returnResult(getInt(a), getInt(b), operator);
        }
    }

    private void updateLastOperator(String operator){
        this.operator = operator;
    }

    private boolean isButtonANumber(String button){
        return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0").contains(button);
    }

    private boolean isLastButtonANumber(){
        return Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9", "0").contains(this.lastButtonPressed);
    }

    private void updateLastButtonPressed(String buttonName) {
        this.lastButtonPressed = buttonName;
    }

    private boolean isLastButtonAnOperator() {
        return Arrays.asList("+", "-", "x", "/", "=", "AC").contains(this.lastButtonPressed);
    }

    private boolean isCurrentButtonAnOperator(String currentButton) {
        return Arrays.asList("+", "-", "x", "/", "=", "AC").contains(currentButton);
    }

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
                return ("" + id).equals("10") ? "0" : "" + id;
        }
    }

    private void allClear(){
        this.operand = 0.0;
        this.operator = "";
        this.accumulator = 0.0;
        this.textView.setText("0");
    }
}
package com.github.toluthomas.calculator.components;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;

@SuppressLint("ViewConstructor")
public class CalculatorButton extends androidx.appcompat.widget.AppCompatButton {
    private Button button;
    final String defaultButtonName = "";
    @SuppressLint("UseCompatLoadingForDrawables")
    final Drawable defaultButtonBackground = getResources().getDrawable(android.R.drawable.btn_default);
    final int defaultIndex = 0;
    final int defaultWidth = 100;
    final int defaultHeight = 100;
    final ViewGroup.LayoutParams defaultLayoutParams = new ViewGroup.LayoutParams(defaultWidth, defaultHeight);

    public CalculatorButton(@NonNull Context context, String buttonName, Drawable background, int index, ViewGroup.LayoutParams layoutParams) {
        super(context);
        this.button = new Button(context); // Create new instance of button
        this.button.setText(buttonName == null ? defaultButtonName : buttonName); // Set button text
        this.button.setBackground(background == null ? defaultButtonBackground : background); // Give button some background
        this.button.setId(index); // Set the ID for each button
        this.button.setLayoutParams(layoutParams == null ? defaultLayoutParams : layoutParams); // Set width and height of button
    }

    // If index is not supplied
    public CalculatorButton(@NonNull Context context, String buttonName, Drawable background, ViewGroup.LayoutParams layoutParams) {
        super(context);
        this.button.setText(buttonName == null ? defaultButtonName : buttonName); // Set button text
        this.button.setBackground(background == null ? defaultButtonBackground : background); // Give button some background
        this.button.setId(defaultIndex); // Use default index as ID
        this.button.setLayoutParams(layoutParams == null ? defaultLayoutParams : layoutParams); // Set width and height of button
    }

    // If no index or no layout params are specified
    public CalculatorButton(@NonNull Context context, String buttonName, Drawable background) {
        super(context);
        this.button.setText(buttonName == null ? defaultButtonName : buttonName); // Set button text
        this.button.setBackground(background == null ? defaultButtonBackground : background); // Give button some background
        this.button.setId(defaultIndex); // Use default index as ID
        this.button.setLayoutParams(defaultLayoutParams); // Use default width and height
    }

    // For a bare button
    public CalculatorButton(@NonNull Context context) {
        super(context);
        this.button.setText(defaultButtonName); // Use default button text
        this.button.setBackground(defaultButtonBackground); // Use default background
        this.button.setId(defaultIndex); // Use default index as ID
        this.button.setLayoutParams(defaultLayoutParams); // Use default width and height
    }

    public Button getButton(){
        return this.button;
    }
}

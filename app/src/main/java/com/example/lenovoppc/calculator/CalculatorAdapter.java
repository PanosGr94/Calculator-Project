package com.example.lenovoppc.calculator;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovoppc.calculator.Model.NumberButton;
import com.example.lenovoppc.calculator.Model.SharedViewModel;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
public class CalculatorAdapter extends RecyclerView.Adapter<CalculatorAdapter.NumberViewHolder> {

    private ArrayList<NumberButton> layoutContents;
    private Resources resources;
    private CalculatorFragment calculatorInstance;
    //for managing operations
    private static StringBuilder stringBuilder;
    private static String operationHeld;   //operation that will be done when 2 values have been written
    private int posLastOperation;  //spot where last operand was
    private int currentPosition;  //latest mHistory position
    private double resultScreen; //value that should be on mResult
    private static double savedResultScreen = 0; //value on mResult after config change
    private double currTotal;   //value being calculated continuously

    public CalculatorAdapter(ArrayList<NumberButton> layoutContents, Resources resources,
                             CalculatorFragment calculatorInstance) {
        this.layoutContents = layoutContents;
        this.resources = resources;
        this.calculatorInstance = calculatorInstance;
        stringBuilder = getStringBuilder();
        currentPosition = stringBuilder.length();
        if (currentPosition == 0) {
            operationHeld = "first"; //value given for init purposes
        } else {
            //if the currposition is not 0, then the operations are being made by onsaveinstance
            //variables. the last operatioin and its position must be re-set
            operationHeld = getOperationHeld();
            posLastOperation = stringBuilder.lastIndexOf(operationHeld) + 2; //+2 for the spaces
        }
    }

    public static String getOperationHeld() {
        return operationHeld;
    }

    public static void setOperationHeld(String operationHeld) {
        CalculatorAdapter.operationHeld = operationHeld;
    }

    public static double getSavedResultScreen() {
        return savedResultScreen;
    }

    public static void setSavedResultScreen(double savedResultScreen) {
        CalculatorAdapter.savedResultScreen = savedResultScreen;
    }

    public static StringBuilder getStringBuilder() {
        if (stringBuilder == null) return new StringBuilder();
        return stringBuilder;
    }

    public static void setHistorySB(StringBuilder stringBuilder) {
        CalculatorAdapter.stringBuilder = stringBuilder;
    }

    @Override
    public int getItemCount() {
        return layoutContents.size();
    }

    @NonNull
    @Override
    public CalculatorAdapter.NumberViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_button,viewGroup,false);

        NumberViewHolder numberViewHolder = new NumberViewHolder(view);
        return numberViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CalculatorAdapter.NumberViewHolder numberViewHolder, int i) {

        final String value = layoutContents.get(i).getValue();
        final String type = layoutContents.get(i).getType();
        numberViewHolder.mNumber.setText(value);


        //change keyboard background colors depending on type
        if (type.equals(CalculatorFragment.OPERATION)) {
            numberViewHolder.mButton.setCardBackgroundColor(resources.getColor(R.color.colorPrimaryDark));
        }else if(type.equals(CalculatorFragment.NUMBER)||type.equals(CalculatorFragment.DOT)){
            numberViewHolder.mButton.setCardBackgroundColor(resources.getColor(R.color.colorPrimary));
        } else if (type.equals(CalculatorFragment.FUNCTION)) {
            numberViewHolder.mButton.setCardBackgroundColor(resources.getColor(R.color.colorAccent));
        }

        numberViewHolder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAndAdd(value, type);
            }
        });

    }

    /**
     * NUMBER, DOT AND FUNCTION mostly handle the ui side and affect the length of the string
     * OPERATION will actually add to or remove from history
     *
     * @param value the value of the key pressed
     * @param type the type of the key pressed from {@link CalculatorFragment}
     */
    private void checkAndAdd(String value, String type) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        switch (type) {
            case CalculatorFragment.NUMBER:
                stringBuilder = stringBuilder.append(value);
                currentPosition = stringBuilder.length();
                //restart the current calculation everytime a new number is added
                currTotal = 0;
                if (getSavedResultScreen() != 0) {
                    resultScreen = getSavedResultScreen();
                    setSavedResultScreen(0);
                }
                //if no other operations have been made
                if (operationHeld.equals("first")) {
                    resultScreen = Double.parseDouble(stringBuilder.toString());
                    currTotal = resultScreen;
                    calculatorInstance.setResult(df.format(resultScreen));
                } else { //if some operation have been made
                    //calculate the latest result and add it to mResult
                    currTotal = calculate(operationHeld);
                    calculatorInstance.setResult(df.format(currTotal));
                }

                calculatorInstance.setHistory(stringBuilder.toString());
                break;
            case CalculatorFragment.DOT:
                stringBuilder = stringBuilder.append(value);
                currentPosition = stringBuilder.length();

                calculatorInstance.setHistory(stringBuilder.toString());
                break;
            case CalculatorFragment.FUNCTION:
                if (value.equals("AC")) {
                    clearComputer();
                }
                break;
            case CalculatorFragment.OPERATION:
                //if a new operation is starting, update mResult value now so it doesn't disappear.
                if (getSavedResultScreen() != 0) {
                    currTotal = getSavedResultScreen();
                    setSavedResultScreen(0);
                }
                resultScreen = currTotal;
                //add spaces formatting for beauty
                stringBuilder = stringBuilder.append(" ").append(value).append(" ");
                //add the 2 spaces to current position count
                currentPosition = stringBuilder.length() + 2;
                //update the mHistory string
                calculatorInstance.setHistory(stringBuilder.toString());
                //save the latest operand for when a new number arrives!
                operationHeld = value;
                //keep the position of the latest operand without the added space
                posLastOperation = stringBuilder.length() - 1;
                break;

        }

    }

    /**
     * Method that calculates the operation and returns the result.
     * New user value is received by distance from last operand
     *
     * @param operand latest operand to use on values
     * @return the result of the operation
     */
    private double calculate(String operand) {

        String newUserValue = stringBuilder.substring(posLastOperation, currentPosition);
        newUserValue = newUserValue.trim();
        //turn string into a double to operate
        double newDoubValue = Double.parseDouble(newUserValue);
        switch (operand) {
            case "+":
                return resultScreen + newDoubValue;
            case "-":
                return resultScreen - newDoubValue;
            case "x":
                return resultScreen * newDoubValue;
            case "÷":
                if (newDoubValue == 0) {
                    Toast.makeText(calculatorInstance.getContext(), "Division by zero not allowed", Toast.LENGTH_SHORT).show();
                    break;
                }
                return resultScreen / newDoubValue;
        }

        return newDoubValue;
    }

    public class NumberViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_button_number)
        TextView mNumber;
        @BindView(R.id.cl_item_button)
        CardView mButton;

        public NumberViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }


    /**
     * Simple helper method to clear all the variables when "AC" is used
     */
    private void clearComputer() {
        operationHeld = "first";
        stringBuilder = new StringBuilder("");
        calculatorInstance.setResult("0");
        calculatorInstance.setHistory("0");
    }

}

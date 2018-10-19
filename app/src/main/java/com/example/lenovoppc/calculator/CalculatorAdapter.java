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
    ArrayList<Double> toCalculate;
    private CalculatorFragment calculatorInstance;
    private SharedViewModel viewModel;
    private StringBuilder stringBuilder;
    private int[] parenthesis;
    private int open;
    private String operationHeld;
    private int posLastOperation;
    private int hasBeenAdded, currentPosition; //for managing operations
    private double resultScreen;
    private String newOperandFlag;
    private double currTotal;

    public CalculatorAdapter(ArrayList<NumberButton> layoutContents, Resources resources, CalculatorFragment calculatorInstance, SharedViewModel viewModel) {
        this.layoutContents = layoutContents;
        this.resources = resources;
        this.calculatorInstance = calculatorInstance;
        this.viewModel = viewModel;
        stringBuilder = new StringBuilder();
        operationHeld = "first"; //value given for init purposes
        currentPosition = hasBeenAdded = 0;
        toCalculate = new ArrayList<Double>();
        open = 0;
        parenthesis = new int[10];
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
     * @param value
     * @param type
     */
    private void checkAndAdd(String value, String type) {
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);

        switch (type) {
            case CalculatorFragment.NUMBER:
                stringBuilder = stringBuilder.append(value);
                currentPosition = stringBuilder.length();
                currTotal = 0;

                if (operationHeld.equals("first")) { //no other operations have been pressed
                    resultScreen = Double.parseDouble(stringBuilder.toString());
                    currTotal = resultScreen;
                    calculatorInstance.setResult(df.format(resultScreen));
                } else { //some operation has been pressed on the numbers
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
                //if a new operation is starting, update the total now so it doesn't disappear.
                resultScreen = currTotal;
                stringBuilder = stringBuilder.append(value);
                currentPosition = stringBuilder.length();

                calculatorInstance.setHistory(stringBuilder.toString());
                operationHeld = value;
                posLastOperation = stringBuilder.length();
                Log.d("strbld", "position of latest operation is "
                        + posLastOperation + ", operationHeld is " + operationHeld);


//                calculate(value); //add the value and the operand to array of operations

//                newOperandFlag = value; //the change of operand creates a new array addition
                break;

        }

//        if(newOperandFlag !=null)


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


    /**
     * Simple helper method to clear all the variables when "AC" is used
     */
    private void clearComputer() {
        hasBeenAdded = 0;
        operationHeld = "first";
        stringBuilder = new StringBuilder("");
        calculatorInstance.setResult("0");
        calculatorInstance.setHistory("0");
        toCalculate.clear();
    }

    /*  Log.d("strbld", "hasbeenadded " + hasBeenAdded + " crps " + currentPosition);

     */
/*
        toCalculate.add(new NumberButton(trimmedSubstring,CalculatorFragment.NUMBER));
//            resultScreen = (double)toCalculate.get(0);
        int indexOfLast = toCalculate.size()-1;
        Log.d("strbld", "last saved value is: " + toCalculate.get(indexOfLast).getValue()+"while size is "+toCalculate.size());
        operationHeld = operand;

        if(operand.equals("=")){
            fetchResult();
            return;
        }else {
            toCalculate.add(new NumberButton(operand, CalculatorFragment.OPERATION));
            hasBeenAdded = currentPosition;//adds the spaces and the symbol which have just been added
        }
         /*



        *//* this arraylist has the numbers to be taken into account.
     *  if the numbers have been added or have not been created the arrayList will be empty
     */
        /*if (toCalculate.isEmpty()) {
            Log.d("strbld", "al is empty");
            //Double.parseDouble(substringToArray)
            toCalculate.add(valueToArray);
            resultScreen = toCalculate.get(0);
            int indexOfLast = toCalculate.size() - 1;
            Log.d("strbld", "last saved value is: " + toCalculate.get(indexOfLast).toString() + "while size is " + toCalculate.size());
            hasBeenAdded = currentPosition;
            operationHeld = operand;
        } else {
            toCalculate.add(valueToArray);
            int indexOfLast = toCalculate.size() - 1;
            Log.d("strbld", "last saved value is: " + toCalculate.get(indexOfLast).toString() + "while size is " + toCalculate.size());
            hasBeenAdded = currentPosition;
            switch (operationHeld) {
                case "+":
                    Log.d("strbld", "adding " + resultScreen + " + " + toCalculate.get(indexOfLast));
                    resultScreen = resultScreen + toCalculate.get(indexOfLast);
                    calculatorInstance.setResult(String.valueOf(resultScreen));
                    Log.d("strbld", "new currTotal is " + resultScreen);
                    break;
                case "-":
                    Log.d("strbld", "adding " + resultScreen + " - " + toCalculate.get(indexOfLast));
                    resultScreen = resultScreen - toCalculate.get(indexOfLast);
                    calculatorInstance.setResult(String.valueOf(resultScreen));
                    Log.d("strbld", "new currTotal is " + resultScreen);
                    break;
                case "X":
                    Log.d("strbld", "adding " + resultScreen + " * " + toCalculate.get(indexOfLast));
                    resultScreen = resultScreen * toCalculate.get(indexOfLast);
                    calculatorInstance.setResult(String.valueOf(resultScreen));
                    Log.d("strbld", "new currTotal is " + resultScreen);
                    break;
                case "÷":
                    Log.d("strbld", "adding " + resultScreen + " / " + toCalculate.get(indexOfLast));
                    resultScreen = resultScreen / toCalculate.get(indexOfLast);
                    calculatorInstance.setResult(String.valueOf(resultScreen));
                    Log.d("strbld", "new currTotal is " + resultScreen);
                    break;
            }
            operationHeld = operand;
        }
*/
/*
        if(operand.equals("=")){
            stringBuilder = new StringBuilder("");
            toCalculate.clear();
            operationHeld = null;
            newOperandFlag = null;
            return;
        }

        newOperandFlag = null;*/



    private void fetchResult() {
     /*   for(NumberButton item : toCalculate){
            operationHeld = null;
            Log.d("strbld item",item.getValue()+", type is :"+item.getType());
            if(item.getType().equals(CalculatorFragment.OPERATION)) {
                operationHeld = item.getValue();
                int indexofItem = toCalculate.indexOf(item);
                switch (operationHeld){
                    case "+":
//                    Log.d("strbld", "adding "+resultScreen +" + "+ toCalculate.get(indexOfLast));
                        resultScreen = resultScreen + Double.parseDouble(toCalculate.get(indexofItem-1).getValue());
                        break;
                    case "-":
                        Log.d("strbld", "adding "+resultScreen +" - "+ toCalculate.get(indexOfLast));
                        resultScreen = resultScreen - Double.parseDouble(item.getValue());
                        break;
                    case "X":
                        Log.d("strbld", "adding "+resultScreen +" * "+ toCalculate.get(indexOfLast));
                        resultScreen = resultScreen * Double.parseDouble(item.getValue());
                        break;
                    case "÷":
                        Log.d("strbld", "adding "+resultScreen +" / "+ toCalculate.get(indexOfLast));
                        resultScreen = resultScreen / Double.parseDouble(item.getValue());
                        break;
                    default:
                        break;
                }
            }
        }*/
        /*calculatorInstance.setResult(String.valueOf(resultScreen));
        Log.d("strbld", "new currTotal is "+resultScreen);
        */

    }


    private boolean isOperator(String lc) {
        return lc.equals("(") || lc.equals(")") || lc.equals("÷") ||
                lc.equals("-") || lc.equals("+") || lc.equals("x");

    }

    @Override
    public int getItemCount() {
        return layoutContents.size();
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
}

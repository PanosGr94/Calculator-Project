package com.example.lenovoppc.calculator;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lenovoppc.calculator.Model.NumberButton;
import com.example.lenovoppc.calculator.Model.SharedViewModel;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Uses {@link com.example.lenovoppc.calculator.Model.SharedViewModel} to share data.
 */
public class CalculatorFragment extends Fragment {

    //keyboard types
    public static final String FUNCTION = "function";
    public static final String OPERATION = "operation";
    public static final String NUMBER = "number";
    public static final String DOT = "dot";
    //number of rows in keyboard
    public static final int SPAN_COUNT = 4;
    //parameters for onsaveInstance
    public static final String BUNDLE_PARAM_RESULT = "param_result";
    public static final String BUNDLE_PARAM_HISTORY = "param_history";
    public static final String BUNDLE_PARAM_OPERATION = "param_operation";

    private SharedViewModel viewModel;
    @BindView(R.id.tv_result)
    TextView mResult;
    @BindView(R.id.tv_history)
    TextView mHistory;
    @BindView(R.id.rv_buttons)
    RecyclerView mButtonsRV;

    //update value on screen. called by CalculatorAdapter
    public void setResult(String resultString) {
        mResult.setText(resultString);
        viewModel.updateValue(resultString);
    }

    //update value on mHistory. called by CalculatorAdapter
    public void setHistory(String updatedHistory) {
        mHistory.setText(updatedHistory);
    }



    public CalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);
        ButterKnife.bind(this, view);
        //use the sharedviewmodel to update data on ConverterFragment Edittext
        viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        //turn off scrolling for recycleview
        GridLayoutManager gridLayoutManager = new GridLayoutManager(container.getContext(),
                SPAN_COUNT) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mButtonsRV.setLayoutManager(gridLayoutManager);
        //create a new adapter instance
        CalculatorAdapter calculatorAdapter = new CalculatorAdapter(setUpCalculator(),
                getResources(), this);
        mButtonsRV.setAdapter(calculatorAdapter);

        return view;
    }

    //configuration changes saved here
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(BUNDLE_PARAM_RESULT, mResult.getText().toString());
        outState.putString(BUNDLE_PARAM_HISTORY, mHistory.getText().toString());
        outState.putString(BUNDLE_PARAM_OPERATION, CalculatorAdapter.getOperationHeld());
    }

    //configuration changes retrieved here
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(BUNDLE_PARAM_RESULT)) {
                String result = savedInstanceState.getString(BUNDLE_PARAM_RESULT);
                mResult.setText(result);
                CalculatorAdapter.setSavedResultScreen(Double.parseDouble(result));
            }
            if (savedInstanceState.containsKey(BUNDLE_PARAM_HISTORY)) {
                String history = savedInstanceState.getString(BUNDLE_PARAM_HISTORY);
                mHistory.setText(history);
                CalculatorAdapter.setHistorySB(new StringBuilder(history));
            }
            if (savedInstanceState.containsKey(BUNDLE_PARAM_OPERATION)) {
                CalculatorAdapter.setOperationHeld(savedInstanceState.getString(BUNDLE_PARAM_OPERATION));
            }
        }
    }

    /**
     * Simple helper method to build the {@link NumberButton} objects in the {@link CalculatorAdapter}
     * @return objects to pass to adapter
     */
    private ArrayList<NumberButton> setUpCalculator(){
        String[] array = getResources().getStringArray(R.array.calculator_contents);
        ArrayList<NumberButton> numberButtons = new ArrayList<>();

        numberButtons.add(new NumberButton(array[0], NUMBER));
        numberButtons.add(new NumberButton(array[1], NUMBER));
        numberButtons.add(new NumberButton(array[2], NUMBER));
        numberButtons.add(new NumberButton(array[3],OPERATION));
        numberButtons.add(new NumberButton(array[4], NUMBER));
        numberButtons.add(new NumberButton(array[5],NUMBER));
        numberButtons.add(new NumberButton(array[6],NUMBER));
        numberButtons.add(new NumberButton(array[7],OPERATION));
        numberButtons.add(new NumberButton(array[8],NUMBER));
        numberButtons.add(new NumberButton(array[9],NUMBER));
        numberButtons.add(new NumberButton(array[10],NUMBER));
        numberButtons.add(new NumberButton(array[11],OPERATION));
        numberButtons.add(new NumberButton(array[12], DOT));
        numberButtons.add(new NumberButton(array[13],NUMBER));
        numberButtons.add(new NumberButton(array[14], FUNCTION));
        numberButtons.add(new NumberButton(array[15], OPERATION));

        return numberButtons;
    }
}

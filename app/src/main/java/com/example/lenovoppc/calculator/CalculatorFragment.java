package com.example.lenovoppc.calculator;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lenovoppc.calculator.Model.NumberButton;
import com.example.lenovoppc.calculator.Model.SharedViewModel;
import com.example.lenovoppc.calculator.Network.NetworkStatusReceiver;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Uses {@link com.example.lenovoppc.calculator.Model.SharedViewModel} to share data.
 */
public class CalculatorFragment extends Fragment {

    public static final String FUNCTION = "function";
    public static final String OPERATION = "operation";
    public static final String NUMBER = "number";
    public static final String DOT = "dot";
    public static final int SPAN_COUNT = 4;

    private SharedViewModel viewModel;
    @BindView(R.id.ib_exchange)
    ImageButton mExchange;
    @BindView(R.id.tv_result)
    TextView mResult;
    @BindView(R.id.tv_history)
    TextView mHistory;
    @BindView(R.id.rv_buttons)
    RecyclerView mButtonsRV;

    public void setResult(String resultString) {
        mResult.setText(resultString);
        viewModel.updateValue(resultString);
    }

    public void setHistory(String updatedHistory) {
        mHistory.setText(updatedHistory);
    }



    public CalculatorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        GridLayoutManager gridLayoutManager = new GridLayoutManager(container.getContext(), SPAN_COUNT){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mButtonsRV.setLayoutManager(gridLayoutManager);
        //create a new adapter instance
        CalculatorAdapter calculatorAdapter = new CalculatorAdapter(setUpCalculator(), getResources(), this, viewModel);
        mButtonsRV.setAdapter(calculatorAdapter);

        mExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentValue = mResult.getText().toString();
                viewModel.updateValue(currentValue);

                /*notify {@link MainActivity} that we are replacing Fragments to show
                   offline message if needed */
                if(!NetworkStatusReceiver.isAppOnline()){
                    MainActivity.notifyOffline(MainActivity.TAG_CONVERTER_FRAGMENT,
                            (TextView) getActivity().findViewById(R.id.tv_error_message),
                            getResources().getString(R.string.offline_error_message));
                }

                //when clicked, go to the other fragment
                FragmentTransaction fragmentTransaction = getActivity().
                        getSupportFragmentManager().beginTransaction();

                ConverterFragment converterFragment = new ConverterFragment();
                fragmentTransaction.replace(R.id.fl_fragment_container, converterFragment);
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

                //Update bottom navigation tab to highlight the correct tab
                BottomNavigationView mBtmView = (BottomNavigationView) getActivity().findViewById(R.id.navigation);
                mBtmView.getMenu().findItem(R.id.navigation_convertor).setChecked(true);

            }
        });

        return view;
    }


    /**
     * Simple helper method to build the {@link NumberButton} objects in the RV
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

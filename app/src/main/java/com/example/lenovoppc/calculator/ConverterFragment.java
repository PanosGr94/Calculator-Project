package com.example.lenovoppc.calculator;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.lenovoppc.calculator.Model.Exchange;
import com.example.lenovoppc.calculator.Model.ExchangeViewModel;
import com.example.lenovoppc.calculator.Model.SharedViewModel;

import java.math.RoundingMode;
import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Uses {@link com.example.lenovoppc.calculator.Model.SharedViewModel} to share data.
 */
public class ConverterFragment extends Fragment {

    @BindView(R.id.et_sell_value)
    EditText mSellValue;
    @BindView(R.id.et_buy_value)
    EditText mBuyValue;
    @BindView(R.id.sp_currency)
    Spinner mCurrencies;
    @BindView(R.id.tv_sell_currency)
    TextView mSellSign;

    private Exchange mExchange;
    private String currencySign;
    private int curAtPos;

    public ConverterFragment() {
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
        View view = inflater.inflate(R.layout.fragment_converter, container, false);
        ButterKnife.bind(this, view);

        //setupSpinner
        // Create an ArrayAdapter using the string array and a default spinner layout
        mSellSign.setText("$");
        curAtPos = 0;
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(view.getContext(),
                R.array.currencies, R.layout.spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        mCurrencies.setAdapter(adapter);

        mCurrencies.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSign(parent.getItemAtPosition(position).toString());
                curAtPos = position;
                updateSellValue(mBuyValue.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //User input on change listener
        mBuyValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0) updateSellValue(s.toString());
                else if(s.length()==0) {
                    updateSellValue("0.0");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //Online results on change listener
        ExchangeViewModel exchangeViewModel = ViewModelProviders.of(getActivity()).get(ExchangeViewModel.class);
        exchangeViewModel.getNewExchangeRates().observe(this, new Observer<Exchange>() {
            @Override
            public void onChanged(@Nullable Exchange exchange) {
                mExchange = exchange;
            }
        });

        //Calculator input on change listener
        SharedViewModel viewModel = ViewModelProviders.of(getActivity()).get(SharedViewModel.class);
        viewModel.getValueToExchange().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                mBuyValue.setText(s);
                updateSellValue(s);
            }
        });

        //make editText not editable
        mSellValue.setKeyListener(null);



        return view;
    }

    private void updateSign(String selectedCurrency) {
        char[] currencySignArray = selectedCurrency.toCharArray();
        currencySign = String.valueOf(currencySignArray[currencySignArray.length-2]);
        mSellSign.setText(currencySign);
    }

    private void updateSellValue(String s) {
        //calculate and round result to 2 decimals
        double userValue = Double.parseDouble(s);
        double result;

        switch (curAtPos){
            case 0:
                result =  userValue*mExchange.getEx_usd();
                break;
            case 1:
                result =  userValue*mExchange.getEx_mex();
                break;
            case 2:
                result =  userValue*mExchange.getEx_jap();
                break;
            case 3:
                result =  userValue*mExchange.getEx_gbp();
                break;
            case 4:
                result =  userValue*mExchange.getEx_aus();
                break;
            default:
                result =  userValue*mExchange.getEx_usd();
                break;

        }

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        //get the currency sign from the spinner option selected by user
        StringBuilder sb = new StringBuilder("  ");
        sb = sb.append(df.format(result));
        mSellValue.setText(sb);

    }


}

package com.example.lenovoppc.calculator;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovoppc.calculator.Model.NumberButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CalculatorFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CalculatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalculatorFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static final String FUNCTION = "function";
    public static final String OPERATION = "operation";
    public static final String NUMBER = "number";
    public static final String DOT = "dot";
    public static final String RESULT = "result";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    @BindView(R.id.rv_buttons) RecyclerView mButtonsRV;

    private OnFragmentInteractionListener mListener;

    public CalculatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalculatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalculatorFragment newInstance(String param1, String param2) {
        CalculatorFragment fragment = new CalculatorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    //TODO: when convertion button is pressed send value to converter
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);
        ButterKnife.bind(this, view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(container.getContext(),4){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };
        mButtonsRV.setLayoutManager(gridLayoutManager);

        CalculatorAdapter calculatorAdapter = new CalculatorAdapter(setUpCalculator(),getResources());
        mButtonsRV.setAdapter(calculatorAdapter);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    /**
     * Simple helper method to build the {@link NumberButton} objects
     * @return objects to pass to adapter
     */
    private ArrayList<NumberButton> setUpCalculator(){
        String[] array = getResources().getStringArray(R.array.calculator_contents);
        ArrayList<NumberButton> numberButtons = new ArrayList<>();
        numberButtons.add(new NumberButton(array[0], FUNCTION));
        numberButtons.add(new NumberButton(array[1], OPERATION));
        numberButtons.add(new NumberButton(array[2],OPERATION));
        numberButtons.add(new NumberButton(array[3],OPERATION));
        numberButtons.add(new NumberButton(array[4], NUMBER));
        numberButtons.add(new NumberButton(array[5],NUMBER));
        numberButtons.add(new NumberButton(array[6],NUMBER));
        numberButtons.add(new NumberButton(array[7],OPERATION));
        numberButtons.add(new NumberButton(array[8],NUMBER));
        numberButtons.add(new NumberButton(array[9],NUMBER));
        numberButtons.add(new NumberButton(array[10],NUMBER));
        numberButtons.add(new NumberButton(array[11],OPERATION));
        numberButtons.add(new NumberButton(array[12],NUMBER));
        numberButtons.add(new NumberButton(array[13],NUMBER));
        numberButtons.add(new NumberButton(array[14],NUMBER));
        numberButtons.add(new NumberButton(array[15],OPERATION));
        numberButtons.add(new NumberButton(array[16], DOT));
        numberButtons.add(new NumberButton(array[17],NUMBER));
        numberButtons.add(new NumberButton(array[18],FUNCTION));
        numberButtons.add(new NumberButton(array[19], RESULT));

        return numberButtons;
    }
}

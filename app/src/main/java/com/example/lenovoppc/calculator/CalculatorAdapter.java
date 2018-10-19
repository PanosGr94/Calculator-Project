package com.example.lenovoppc.calculator;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovoppc.calculator.Model.NumberButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
//TODO: MAYBE IMPLEMENT WITH GRIDVIEW/LAYOUT?
public class CalculatorAdapter extends RecyclerView.Adapter<CalculatorAdapter.NumberViewHolder> {

    private ArrayList<NumberButton> layoutContents;
    private Resources resources;

    public CalculatorAdapter(ArrayList<NumberButton> layoutContents, Resources resources) {
        this.layoutContents = layoutContents;
        this.resources = resources;
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

        String value = layoutContents.get(i).getValue();
        String type = layoutContents.get(i).getType();
        numberViewHolder.mNumber.setText(value);

        if(type.equals(CalculatorFragment.FUNCTION)||type.equals(CalculatorFragment.OPERATION)){
            numberViewHolder.mButton.setCardBackgroundColor(resources.getColor(R.color.colorPrimaryDark));
        }else if(type.equals(CalculatorFragment.NUMBER)||type.equals(CalculatorFragment.DOT)){
            numberViewHolder.mButton.setCardBackgroundColor(resources.getColor(R.color.colorPrimary));
        }else if(type.equals(CalculatorFragment.RESULT)){
            numberViewHolder.mButton.setCardBackgroundColor(resources.getColor(R.color.colorAccent));
        }

        final int position = i;
        numberViewHolder.mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), position+" has been clicked.", Toast.LENGTH_SHORT).show();
            }
        });

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

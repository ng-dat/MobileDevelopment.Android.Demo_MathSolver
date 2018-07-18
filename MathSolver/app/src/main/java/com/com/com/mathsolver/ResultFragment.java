package com.com.com.mathsolver;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.com.com.mathsolver.Common.Store;
import com.com.com.mathsolver.WolframModels.Pod;
import com.com.com.mathsolver.WolframModels.Subpod;

import java.util.List;

/**
 * Created by DatGatto on 7/16/2018.
 */

public class ResultFragment extends Fragment {
    View thisView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.result_layout,container,false);

        Button backButton = (Button)thisView.findViewById(R.id.button_back_to_solver);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame,new SolverFragment()).commit();
            }
        });

        ShowPredictedFormula();
        ShowPredictedSolution();
        return  thisView;
    }
    private void ShowPredictedFormula(){
        if (Store.predictedFormula !=null)
            ((TextView)thisView.findViewById(R.id.text_predictedFormula)).setText(Store.predictedFormula);
    }
    private void ShowPredictedSolution(){
        if (Store.predictedSolutionTitle != null )
            ((TextView)thisView.findViewById(R.id.text_predictedSolutionTitle)).setText(Store.predictedSolutionTitle);
        if (Store.predictedSolution != null )
            ((TextView)thisView.findViewById(R.id.text_predictedSolution)).setText(Store.predictedSolution);
    }
}
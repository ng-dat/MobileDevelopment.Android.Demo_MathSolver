package com.com.com.mathsolver;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.com.com.mathsolver.Camera.CameraManager;
import com.com.com.mathsolver.Camera.CameraView;
import com.com.com.mathsolver.Common.Store;
import com.com.com.mathsolver.Mathpix.ProcessSingleImageTask;
import com.com.com.mathsolver.Wolfram.WolframConnector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by DatGatto on 7/16/2018.
 */

public class SolverFragment extends Fragment {
    View thisView;
    CameraManager cameraManager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.solve_math_layout, container, false);

        // Create an instance of Camera
        cameraManager = new CameraManager();
        cameraManager.mCamera = cameraManager.getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        cameraManager.mCameraView = new CameraView(thisView.getContext(), cameraManager.mCamera);
        FrameLayout preview = (FrameLayout) thisView.findViewById(R.id.camera_view);
        preview.addView(cameraManager.mCameraView);

        // Add a listener to the Capture button
        ImageButton captureButton = (ImageButton) thisView.findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get an image from the camera
                        cameraManager.mCamera.takePicture(null, null, cameraManager.mPicture);
                    }
                }
        );
        // Add a listener to the Close Button
        ImageButton closeButton = (ImageButton) thisView.findViewById(R.id.button_close);
        closeButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.exit(0);
                    }
                }
        );

        // Add a listener to the Restart Button
        ImageButton restartButton = (ImageButton) thisView.findViewById(R.id.button_restart);
        restartButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.exit(0);
                    }
                }
        );

        // Add a listener to the GetFormula button
        Button getFormulaButton = (Button) thisView.findViewById(R.id.button_getFormula);
        getFormulaButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFormula();
                    }
                }
        );

        // Add a listener to the GetSolution button
        Button getSolutionBtn = (Button) thisView.findViewById(R.id.button_getSolution);
        getSolutionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetSolution();
            }
        });

        return thisView;
    }
    private void getFormula(){
        try {
            String result = new ProcessSingleImageTask().execute(cameraManager.getOutputMediaFile(1)).get();
            if (result != null){
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(result);
                    String formula = jsonObject.getString("latex");
                    Double confidenceRate = 0.0;
                    confidenceRate = jsonObject.getDouble("latex_confidence_rate");

                    if (formula != null)
                        Store.predictedFormula = formula;
                    if (confidenceRate != null && confidenceRate > 0.25)
                        Store.predictedFormulaTitle = "With Confidence Rate: "+(int)(confidenceRate*100)+"%.";
                    else{
                        Store.predictedFormulaTitle = "We cannot find out optiomal prediction for this.";
                    }
                    ShowPredictedFormula();
                } catch (JSONException e) {
                    Log.e("Error", "Retrived data is in wrong format" );
                }
            } else {
                Log.e("Error", "Couldn't get json from server. Check LogCat for possible errors!");
            }

        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void ShowPredictedFormula() {
        if (Store.predictedFormulaTitle !=null)
            ((TextView)thisView.findViewById(R.id.text_predictedFormula_title)).setText(Store.predictedFormulaTitle);
        if (Store.predictedFormula !=null)
            ((TextView)thisView.findViewById(R.id.text_predictedFormula)).setText(Store.predictedFormula);
    }

    private  void GetSolution(){
        if (Store.predictedFormula == null) {
            Toast.makeText(getActivity(), "There has been no validated formula.", Toast.LENGTH_LONG).show();
            return;
        }
        WolframConnector wolframConector = new WolframConnector();
        wolframConector.GetSolution("http://api.wolframalpha.com/v2/query?appid=36L6WX-K9E69URUJK&input=solve+"+Store.predictedFormula.trim().replace(" ","")+"&podstate=Result__Step-by-step+solution",getFragmentManager());
    }

}

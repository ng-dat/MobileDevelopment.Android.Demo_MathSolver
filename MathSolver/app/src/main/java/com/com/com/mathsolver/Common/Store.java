package com.com.com.mathsolver.Common;

import com.com.com.mathsolver.WolframModels.Pod;
import com.com.com.mathsolver.WolframModels.Subpod;

import java.util.List;

/**
 * Created by DatGatto on 7/17/2018.
 */

public class Store {
    public static String predictedFormulaTitle;
    public static String predictedFormula;
    public static String predictedSolution;
    public static String predictedSolutionTitle;
    public static List<Pod> pods;
    public void PodsToSolution(){
        String solutionTitle = "We cannot find out a suitable solution for this.";
        String solution = "";

        for (Pod pod : pods) {
            if (pod == null)
                continue;
            if (!pod.title.equals("Results"))
                continue;
            if (pod.subpods ==null)
                break;
            for (Subpod subpod : pod.subpods) {
                if (subpod == null)
                    continue;
                if (subpod.title.equals("") || subpod.title.equals("***")){
                    solutionTitle = "We cannot find out a optimal solution for this. But these could be the results:";
                    solution = solution.concat("\n\t"+subpod.plaintext);
                    continue;
                }
                if (subpod.title.equals("Possible intermediate steps")){
                    solutionTitle = subpod.title;
                    solution = subpod.plaintext;
                    break;
                }
            }
            break;
        }

        predictedSolutionTitle = solutionTitle;
        predictedSolution = solution;
    }
//    List<Pod> pods = Store.pods;
//    String htmlString = "";
//        for (Pod pod : pods) {
//        if (pod == null)
//            continue;
//        htmlString = htmlString.concat("- "+ pod.title +" " + pod.id + " " +pod.position + " " + pod.error + " " + pod.numsubpods + "\n");
//        if (pod.subpods ==null)
//            continue;
//        for (Subpod subpod : pod.subpods) {
//            if (subpod == null)
//                continue;
//            htmlString = htmlString.concat(htmlString + "\t-- "+ subpod.title + " " + subpod.plaintext + "\n");
//        }
//    }
}

package com.com.com.mathsolver.WolframModels;

import java.util.List;

/**
 * Created by DatGatto on 7/17/2018.
 */

public class Pod {
    public  String title;
    public  String scanner;
    public  String id;
    public  String position;
    public  String error;
    public  String numsubpods;
    public List<Subpod> subpods;

    public Pod(String title, String sccaner, String id, String position, String error, String numsubpods, List<Subpod> subpods) {
        this.title = title;
        this.scanner = sccaner;
        this.id = id;
        this.position = position;
        this.error = error;
        this.numsubpods = numsubpods;
        this.subpods = subpods;
    }
}

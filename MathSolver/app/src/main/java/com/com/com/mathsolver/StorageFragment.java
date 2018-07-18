package com.com.com.mathsolver;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by DatGatto on 7/16/2018.
 */

public class StorageFragment extends Fragment {
    View thisView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        thisView = inflater.inflate(R.layout.storage_layout,container,false);
        return  thisView;
    }
}
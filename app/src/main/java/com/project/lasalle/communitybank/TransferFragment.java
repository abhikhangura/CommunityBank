package com.project.lasalle.communitybank;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class TransferFragment extends Fragment {

    Button btnSelfTransfer,btnTransferToOther;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);
        init(view);
        return view;
    }

    public void init(View view){
        btnSelfTransfer = view.findViewById(R.id.btnSelfTransfer);
        btnTransferToOther = view.findViewById(R.id.btnOtherTransfer);

        btnTransferToOther.setOnClickListener(view1 -> {
            Intent intent = new Intent(this.getContext(),TransferToOtherActivity.class);
            startActivity(intent);
        });
    }
}
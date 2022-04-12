package com.project.lasalle.communitybank;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class TransferFragment extends Fragment implements View.OnClickListener {


    CardView cardViewSelfTransfer,cardViewTransferToOther;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transfer, container, false);
        init(view);
        return view;
    }

    public void init(View view){
        cardViewSelfTransfer = view.findViewById(R.id.cdSelfTransfer);
        cardViewTransferToOther = view.findViewById(R.id.cdTransferToOther);
        cardViewSelfTransfer.setOnClickListener(this);
        cardViewTransferToOther.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cdSelfTransfer:
                Intent intent1 = new Intent(this.getContext(),TransferToSelfActivity.class);
                startActivity(intent1);
                break;
            case R.id.cdTransferToOther:
                Intent intent = new Intent(this.getContext(),TransferToOtherActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
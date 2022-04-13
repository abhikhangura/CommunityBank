package com.project.lasalle.communitybank;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;


public class MenuFragment extends Fragment {

    Button btnLogout;
    LinearLayout profileLayout, contactLayout;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        init(view);
        return view;
    }

    public void init(View view){
        btnLogout = view.findViewById(R.id.btnLogout);
        profileLayout = view.findViewById(R.id.cardProfile);
        contactLayout = view.findViewById(R.id.cardContactUs);

        btnLogout.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity().getBaseContext(),LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        });

        profileLayout.setOnClickListener(view1 -> {
            Intent intent = new Intent(getActivity().getBaseContext(),ProfileActivity.class);
            startActivity(intent);
        });

        contactLayout.setOnClickListener(view1 -> {

        });
    }


}
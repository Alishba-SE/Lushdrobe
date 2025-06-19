package com.example.lushdrobe.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.example.lushdrobe.R;
import com.example.lushdrobe.activities.ProfileActivity;

public class ProfileFragment extends Fragment {

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        TextView viewFullProfile = view.findViewById(R.id.viewFullProfile);
        viewFullProfile.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), ProfileActivity.class));
        });

        return view;
    }
}
package com.example.cpsc.demoapplication.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.cpsc.demoapplication.R;
import com.example.cpsc.demoapplication.activities.MainActivity;

/**
 * Created by cpsc on 9/16/15.
 */
public class UITestFragment extends Fragment
{
    public UITestFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_ui_test, container, false);

        final Button theButton = (Button) rootView.findViewById(R.id.the_button);
        theButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Animation slideOutAnim = AnimationUtils.loadAnimation(getActivity(), android.R.anim.slide_out_right);
                slideOutAnim.setAnimationListener(new Animation.AnimationListener()
                {
                    @Override
                    public void onAnimationStart(Animation animation)
                    {
                        theButton.setBackground(getActivity().getDrawable(R.drawable.oval_bg_pressed));
                    }

                    @Override
                    public void onAnimationEnd(Animation animation)
                    {
                        theButton.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation)
                    {
                    }
                });
                theButton.startAnimation(slideOutAnim);
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity act)
    {
        super.onAttach(act);
        ((MainActivity) act).onSectionAttached(3);
    }
}

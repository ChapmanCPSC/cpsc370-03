package com.example.cpsc.demoapplication.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.cpsc.demoapplication.R;
import com.example.cpsc.demoapplication.activities.MainActivity;

/**
 * Created by cpsc on 9/16/15.
 */
public class CardsFragment extends Fragment
{
    public CardsFragment()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.fragment_cards, container, false);

        View view0 = rootView.getChildAt(0);
        view0.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                v.animate().x(2000f).y(4000f).scaleX(1.5f).scaleY(3f).setDuration(2000).start();
            }
        });

        View view1 = rootView.getChildAt(1);
        view1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ObjectAnimator spin = ObjectAnimator.ofFloat(v,"rotation",0f,1170f);
                spin.setDuration(1000);
                ObjectAnimator slideRight = ObjectAnimator.ofFloat(v, "translationX", 1000f);
                slideRight.setDuration(500);

                AnimatorSet set = new AnimatorSet();
                set.playSequentially(spin, slideRight);
                set.setInterpolator(new AccelerateInterpolator());
                set.start();
            }
        });

        View view2 = rootView.getChildAt(2);
        view2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ObjectAnimator spin = ObjectAnimator.ofFloat(v,"rotation",0f,1080f);
                spin.setDuration(1000);
                ObjectAnimator slideUp = ObjectAnimator.ofFloat(v, "translationY", -3000);
                slideUp.setDuration(500);

                AnimatorSet set = new AnimatorSet();
                set.playSequentially(spin, slideUp);
                set.setInterpolator(new AccelerateInterpolator());
                set.start();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity act)
    {
        super.onAttach(act);
        ((MainActivity) act).onSectionAttached(0);
    }
}

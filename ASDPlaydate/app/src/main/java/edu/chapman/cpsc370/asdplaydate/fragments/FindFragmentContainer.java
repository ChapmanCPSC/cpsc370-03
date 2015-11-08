package edu.chapman.cpsc370.asdplaydate.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.chapman.cpsc370.asdplaydate.R;


public class FindFragmentContainer extends Fragment {

    private boolean showingResultList = false;

    public FindFragmentContainer()
    {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_find_container, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        // Load the map fragment into the container initially
        if (savedInstanceState == null) {
            getChildFragmentManager()
                    .beginTransaction()
                    .add(R.id.find_container, new FindFragment())
                    .commit();
        }

    }

    public void flipFragment() {

        // Flip back to the map
        if (showingResultList) {
            showingResultList = false;
            getChildFragmentManager().popBackStack();
            return;
        }

        // Flip to result list
        showingResultList = true;

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.find_container, new ResultListFragment())
                .addToBackStack(null)
                /* This part doesn't work with the support.v4 Fragments
                .setCustomAnimations(
                    R.animator.flip_right_in, R.animator.flip_right_out,
                    R.animator.flip_left_in, R.animator.flip_left_out)
                 */
                .commit();
    }
}

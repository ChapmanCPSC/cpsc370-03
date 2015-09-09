package com.example.disneylander;



import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
 
 public class MapFragment extends Fragment implements OnTouchListener{
    //This class shows the map, and allows geture control.  I learned this from this github account: https://github.com/MikeOrtiz/TouchImageView
	 
	ScaleGestureDetector SGD;
	ImageView IMG;
	Matrix matrix;
	 
    public MapFragment(){}
     
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        //Don't change code above
        
        IMG = (ImageView) rootView.findViewById(R.id.disneylandMapImageView);
        matrix = new Matrix();
       SGD = new ScaleGestureDetector(getActivity().getApplicationContext(), new ScaleListener());
        //Leave at bottom
        return rootView;
    }

    
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener
    {
    	@Override
    	public boolean onScale(ScaleGestureDetector detector) {
    		// TODO Auto-generated method stub
    		Float SF = detector.getScaleFactor();
    		SF = Math.max(0.1f, Math.min(SF, 0.5f));
    		matrix.setScale(SF, SF);
    		IMG.setImageMatrix(matrix);
    		return true;	
    	}
    }




	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		SGD.onTouchEvent(event);
		return true;
	}
 }	
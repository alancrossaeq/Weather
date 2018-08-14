package ca.aequilibrium.weather;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class ShowHideBehaviour extends CoordinatorLayout.Behavior<View> {

    private final static String TAG = "CustomBehavior";

    private View child, dependency;

//    public ShowHideBehaviour() {}

    public ShowHideBehaviour(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        this.child = child;
        this.dependency = dependency;
        return dependency instanceof View;
    }

//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
////            int[] dependencyLocation = new int[2];
////            int[] childLocation = new int[2];
////
////            dependency.getLocationInWindow(dependencyLocation);
////            child.getLocationInWindow(childLocation);
////
////            float diff = childLocation[1] - dependencyLocation[1];
////            if(diff > 0) {
////                float scale = diff/(float)childLocation[1];
////                Log.d(TAG, "scale == " + scale);
////                child.setScaleX(1+scale);
////                child.setScaleY(1+scale);
////            }
////            return false;
//
//        child.setVisibility(View.INVISIBLE);
//        return false;
//    }

    @Override
    public boolean onInterceptTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
        System.out.println("here");
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
//            dependency.setVisibility(View.GONE);
//            dependencyBarButton.setVisibility(View.INVISIBLE);
        }
//        return super.onInterceptTouchEvent(parent, child, ev);
        return false;
    }

//    @Override
//    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
//        if (child == dependency) {
//            this.child.setVisibility(View.INVISIBLE);
//        }
//        return super.onTouchEvent(parent, child, ev);
//    }

    //    @Override
//    public boolean onTouchEvent(CoordinatorLayout parent, View child, MotionEvent ev) {
//        System.out.println("here");
//        return super.onTouchEvent(parent, child, ev);
//    }
}

package com.nobrain.materialstudy;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.View;

import com.nobrain.materialstudy.view.MagneticPlateView;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Touch;
import org.androidannotations.annotations.ViewById;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;

/**
 * Created by Steve SeongUg Jung on 15. 1. 4..
 */
@EActivity(R.layout.act_main)
public class MainActivity extends Activity {


    private PublishSubject<MotionEvent> touchDownSubject;
    private PublishSubject<MotionEvent> touchMoveSubject;
    private float lastTouchedX = -1;
    private float lastTouchedY = -1;

    @ViewById(R.id.mpv_main)
    MagneticPlateView magneticPlateView;

    @AfterInject
    void initObject() {

        touchDownSubject = initTouchDownSubject();
        touchMoveSubject = initTouchMoveSubject();

    }

    private PublishSubject<MotionEvent> initTouchMoveSubject() {


        PublishSubject<MotionEvent> touchMoveSubject = PublishSubject.create();

        touchMoveSubject
                .filter(motionEvent -> motionEvent.getAction() == MotionEvent.ACTION_MOVE)
                .map(motionEvent -> {
                    float touchX = motionEvent.getX();
                    float touchY = motionEvent.getY();

                    int[] diffTouchDistance = new int[2];

                    diffTouchDistance[0] = (int) (touchX - lastTouchedX);
                    diffTouchDistance[1] = (int) (touchY - lastTouchedY);

                    lastTouchedX = touchX;
                    lastTouchedY = touchY;
                    return diffTouchDistance;
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(diffXY -> {
                    magneticPlateView.setTouchDiff(diffXY[0], diffXY[1]);
                    magneticPlateView.invalidate();
                });

        return touchMoveSubject;

    }

    private PublishSubject<MotionEvent> initTouchDownSubject() {
        PublishSubject<MotionEvent> touchDownSubject = PublishSubject.create();
        touchDownSubject
                .filter(motionEvent -> motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                .observeOn(Schedulers.io())
                .subscribe(motionEvent -> {
                    lastTouchedX = motionEvent.getX();
                    lastTouchedY = motionEvent.getY();
                });
        return touchDownSubject;
    }

    @Touch(R.id.mpv_main)
    void onPlateTouch(View view, MotionEvent event) {

        touchDownSubject.onNext(event);
        touchMoveSubject.onNext(event);

    }

}

package cu.rm.defibank.parents;

import android.content.Context;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import cu.rm.defibank.R;


/**
 * Una actividad personalizada para hacer mas guiada y sencilla la implementacion de las actividades
 */
public abstract class CustomActivityFullAnimated extends CustomActivity {
    protected TextView labelUi;
    protected ConstraintLayout container;
    protected View borderUp;
    protected View borderRight;
    protected View borderCenter;
    protected TextView labelNameApp;
    protected TextView labelSlogan;

    protected Context contextGlobal;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        contextGlobal = this;
        animationsIn();
    }

    @Override
    protected void findViewByIds() {
        labelUi = findViewById(R.id.label);
        container = findViewById(R.id.container);
        borderUp = findViewById(R.id.borderUp);
        borderRight = findViewById(R.id.borderRight);
        borderCenter = findViewById(R.id.borderCenter);
        labelNameApp = findViewById(R.id.name_label);
        labelSlogan = findViewById(R.id.slogan_label);
    }

    @Override
    protected void animationsIn() {
        if (labelUi != null)
            labelUi.setAnimation(AnimationUtils.loadAnimation(this, R.anim.label_ui_in_delayed));
        if (container != null)
            container.setAnimation(AnimationUtils.loadAnimation(this, R.anim.container_in_delayed));
        if (borderUp != null)
            borderUp.setAnimation(AnimationUtils.loadAnimation(this, R.anim.border_up));
        if (borderRight != null)
            borderRight.setAnimation(AnimationUtils.loadAnimation(this, R.anim.border_right));
        if (borderCenter != null)
            borderCenter.setAnimation(AnimationUtils.loadAnimation(this, R.anim.border_center));
        if (labelNameApp != null)
            labelNameApp.setAnimation(AnimationUtils.loadAnimation(this, R.anim.label_name));
        if (labelSlogan != null)
            labelSlogan.setAnimation(AnimationUtils.loadAnimation(this, R.anim.label_slogan));
    }

    @Override
    protected void animationsOut(View v) {
        animationsClearOut();
        if (labelUi != null)
            labelUi.setAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.label_ui_out));
        if (container != null)
            container.setAnimation(AnimationUtils.loadAnimation(v.getContext(), R.anim.container_out));
    }

    @Override
    protected void animationsClearOut() {
        if (labelUi != null)
            labelUi.clearAnimation();
        if (container != null)
            container.clearAnimation();
    }
}
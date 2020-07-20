package cu.rm.defibank.customsCompatActivity;

import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import cu.rm.defibank.R;


/**
 * Una actividad personalizada para hacer mas guiada y sencilla la implementacion de las actividades
 */
public abstract class CustomActivityAnimated extends CustomActivity {
    TextView labelUi;
    ConstraintLayout container;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        animationsIn();
    }

    @Override
    protected void findViewByIds() {
        labelUi = findViewById(R.id.label);
        container = findViewById(R.id.container);
    }

    @Override
    protected void animationsIn() {
        if (labelUi != null)
            labelUi.setAnimation(AnimationUtils.loadAnimation(this, R.anim.label_ui_in));
        if (container != null)
            container.setAnimation(AnimationUtils.loadAnimation(this, R.anim.container_in));
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
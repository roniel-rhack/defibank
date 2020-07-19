package cu.rm.defibank.parents;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import cu.rm.defibank.R;

public class CustomSplashActivityAnimated extends CustomActivity{

    ImageView circle_0;
    ImageView circle_1;
    ImageView circle_2;
    ImageView circle_3;
    ImageView card;
    ConstraintLayout stars;

    TextView name;
    TextView slogan;

    @Override
    protected void setOnClickListeners() {

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void findViewByIds() {
        super.findViewByIds();
        circle_0 = findViewById(R.id.circle_0);
        circle_1 = findViewById(R.id.circle_1);
        circle_2 = findViewById(R.id.circle_2);
        circle_3 = findViewById(R.id.circle_3);
        card = findViewById(R.id.card);
        stars = findViewById(R.id.splash_stars);
        name = findViewById(R.id.name);
        slogan = findViewById(R.id.slogan);
    }

    @Override
    protected void animationsIn() {
        circle_0.setAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_circle0_in));
        circle_1.setAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_circle1_in));
        circle_2.setAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_circle2_in));
        circle_3.setAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_circle3_in));
        card.setAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_card_in));

        name.setAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_name));
        slogan.setAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_slogan));

//        stars.setAnimation(AnimationUtils.loadAnimation(this, R.anim.splash_star_rotate));
        ObjectAnimator rotar = ObjectAnimator.ofFloat(stars, "rotation", 360).setDuration(3000);
        rotar.setRepeatMode(ValueAnimator.RESTART);
        rotar.setInterpolator(new LinearInterpolator());
        rotar.setRepeatCount(Animation.INFINITE);
        rotar.start();
    }

    @Override
    protected void animationsClearOut() {

    }
}

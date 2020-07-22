package cu.rm.defibank.customsCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Una actividad personalizada para hacer mas guiada y sencilla la implementacion de las actividades
 */
public abstract class CustomActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        findViewByIds();
        animationsIn();
        setOnClickListeners();
    }

    protected abstract void setOnClickListeners();

    protected void findViewByIds() {
    }

    protected abstract void animationsIn();

    protected void animationsOut(View v) {
        animationsClearOut();
    }

    protected abstract void animationsClearOut();

    protected void goActivity(View v, Context packageContext, Class<?> cls) {
        final Intent mainIntent = new Intent(packageContext, cls);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(mainIntent);
                overridePendingTransition(0, 0);
                finish();
            }
        }, 300);

        animationsOut(v);
    }

    protected void goActivity(View v, final Intent intent) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        }, 300);

        animationsOut(v);
    }
}

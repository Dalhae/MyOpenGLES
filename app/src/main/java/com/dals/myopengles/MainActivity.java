package com.dals.myopengles;

import androidx.appcompat.app.AppCompatActivity;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    private GLSurfaceView gLView;
    private GLSurfaceView gLView2;


    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gLView = new MyGLSurfaceView(this);
        gLView2 = new MyGLSurfaceView(this);
        setContentView(R.layout.activity_main);
        //setContentView(gLView);

        LinearLayout boardLayout = findViewById(R.id.GLSurfaceLayout);
        LinearLayout boardLayout2 = findViewById(R.id.GLSurfaceLayout2);
        boardLayout.addView(gLView);
        boardLayout2.addView(gLView2);
    }
}

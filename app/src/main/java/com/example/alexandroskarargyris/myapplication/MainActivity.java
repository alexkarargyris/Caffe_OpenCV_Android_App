package com.example.alexandroskarargyris.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    static {
        System.loadLibrary("MyLib");
        System.loadLibrary("opencv_java3");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView tv = (TextView) findViewById(R.id.testTextView);
        tv.setText("");


        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                long tStart = System.currentTimeMillis();

                TextView tv = (TextView) findViewById(R.id.testTextView);

                String  name_class = NativeClass.getStringFromNative();

                tv.setText(NativeClass.getStringFromNative());

                long tEnd = System.currentTimeMillis();
                long tDelta = tEnd - tStart;
                double elapsedSeconds = tDelta / 1000.0;


                TextView clock = (TextView) findViewById(R.id.clock);
                clock.setText("Classified in "+Double.toString(elapsedSeconds) + " secs");
            }
        });


    }
}

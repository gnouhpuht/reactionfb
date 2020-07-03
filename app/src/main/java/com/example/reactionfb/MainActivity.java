package com.example.reactionfb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    TdhReactionLayout mReactionLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.test);
        mReactionLayout = findViewById(R.id.reaction_layout);
        textView.setOnTouchListener(mReactionLayout.initTouchListener(new TdhReactionLayout.OnReactionSelectListener() {
            @Override
            public void onClick() {
                Toast.makeText(getBaseContext(),"-1", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onReactionSelect(int index) {
                Toast.makeText(getBaseContext(), "select " + index, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDismissReaction() {

            }
        }));
        mReactionLayout.setMarginLeftAnchor(100);
        mReactionLayout.initReactionView();
    }
}

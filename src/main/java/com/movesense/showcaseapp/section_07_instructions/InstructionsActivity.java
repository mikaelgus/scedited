package com.movesense.showcaseapp.section_07_instructions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.movesense.showcaseapp.R;
import com.movesense.showcaseapp.section_00_mainView.MainViewActivity;
import com.movesense.showcaseapp.section_08_info.InfoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InstructionsActivity extends AppCompatActivity {

    @BindView(R.id.instructions_back_button) ImageView Instructions;

    @BindView(R.id.instructions_info_button) RelativeLayout Info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.instructions_back_button, R.id.instructions_info_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.instructions_back_button:
                startActivity(new Intent(InstructionsActivity.this, MainViewActivity.class));
                break;
            case R.id.instructions_info_button:
                startActivity(new Intent(InstructionsActivity.this, InfoActivity.class));
                break;
        }
    }
}
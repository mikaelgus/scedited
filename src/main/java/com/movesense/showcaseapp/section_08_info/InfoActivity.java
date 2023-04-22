package com.movesense.showcaseapp.section_08_info;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.movesense.showcaseapp.R;
import com.movesense.showcaseapp.section_00_mainView.MainViewActivity;
import com.movesense.showcaseapp.section_07_instructions.InstructionsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InfoActivity extends AppCompatActivity {

    @BindView(R.id.info_back_button) ImageView Info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.info_back_button})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.info_back_button:
                startActivity(new Intent(InfoActivity.this, MainViewActivity.class));
                break;
        }
    }
}
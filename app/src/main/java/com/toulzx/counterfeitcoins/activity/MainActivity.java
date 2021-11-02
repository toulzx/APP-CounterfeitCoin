package com.toulzx.counterfeitcoins.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.toulzx.counterfeitcoins.R;
import com.toulzx.counterfeitcoins.databinding.ActivityMainBinding;

import cn.carbswang.android.numberpickerview.library.NumberPickerView;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;
    private View mView;


    /**
     * Perform initialization of all fragments.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // hide the status bar
        getWindow ().addFlags (WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // layout binding
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();
        setContentView(mView);

        // set listener
        initListener();


    }

/*------------------------------------------------------------------------------------------------------*/



    /**
     * 初始化监听器
     * @return void
     * @date 2021/10/25 14:27
     * @author tou
     */
    private void initListener() {
        mBinding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TrichotomyActivity.class);
                Editable editable = mBinding.editTextNumber.getText();
                intent.putExtra("NUM", Integer.valueOf(editable == null ? "12" : editable.toString().equals("") ? "12" : editable.toString()));
                startActivity(intent);
            }
        });
    }
}

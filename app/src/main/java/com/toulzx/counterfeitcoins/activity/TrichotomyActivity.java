package com.toulzx.counterfeitcoins.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Group;
import androidx.core.content.ContextCompat;

import com.toulzx.counterfeitcoins.R;
import com.toulzx.counterfeitcoins.algorithm.Trichotomy;
import com.toulzx.counterfeitcoins.databinding.ActivityTrichotomyBinding;

import java.util.Arrays;

public class TrichotomyActivity extends AppCompatActivity {

    private static final String TAG = TrichotomyActivity.class.getSimpleName();

    private static final int TEST_NUM = 15;
    private int[] bgColors = new int[6];
    private boolean hasRefreshed;
    private boolean hasShowedResult;

    private static final int TARGET_GROUP = Trichotomy.TARGET_GROUP;
    private static final int TARGET_INDEX = Trichotomy.TARGET_INDEX;
    private static final int EXPECTED_GROUP_NUM = Trichotomy.EXPECTED_GROUP_NUM;
    private static final int REMAIN_GROUP_NUM = Trichotomy.REMAIN_GROUP_NUM;

    private Context mContext = this;
    private ActivityTrichotomyBinding mBinding;
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

        int num = getIntent().getIntExtra("NUM", 12);
        Log.i(TAG, "onCreate: getIntent().getIntExtra(\"NUM\", 12)" + getIntent().getIntExtra("NUM", 12));
        // layout binding

        mBinding = ActivityTrichotomyBinding.inflate(getLayoutInflater());
        mView = mBinding.getRoot();
        setContentView(mView);

        // init UI and data
        init(num);

        // set listener

        initListener();

    }

    /**
     * 数据和 UI 初始化
     * @param num:
     * @date 2021/10/26 16:35
     * @author tou
     */
    private void init(int num) {

        // 偶数组色系
        bgColors[0] = ContextCompat.getColor(this, R.color.m_yellow);
        bgColors[1] = ContextCompat.getColor(this, R.color.m_green);
        bgColors[2] = ContextCompat.getColor(this, R.color.m_pink);
        // 奇数组色系
        bgColors[3] = ContextCompat.getColor(this, R.color.m_purple);
        bgColors[4] = ContextCompat.getColor(this, R.color.m_teal);
        bgColors[5] = ContextCompat.getColor(this, R.color.m_red);

        mBinding.tvCoinsContainer1.setMovementMethod(new ScrollingMovementMethod());
        mBinding.tvCoinsContainer2.setMovementMethod(new ScrollingMovementMethod());
        mBinding.tvCoinsContainer3.setMovementMethod(new ScrollingMovementMethod());

        hasRefreshed = false;
        hasShowedResult = false;

        updateUI(Trichotomy.init(num, this));

    }


    /**
     * Called when the activity has detected the user's press of the back
     * key. The {@link #getOnBackPressedDispatcher() OnBackPressedDispatcher} will be given a
     * chance to handle the back button before the default behavior of
     * {@link Activity#onBackPressed()} is invoked.
     *
     * @see #getOnBackPressedDispatcher()
     */
    @Override
    public void onBackPressed() {
        if (Trichotomy.getPointer() == 0) {

            finish();

        } else {

            if (!mBinding.btnNextStep.isEnabled() || !mBinding.btnNextStep.isEnabled()) {
                mBinding.btnNextStep.setEnabled(true);
                mBinding.btnRefreshCoins.setEnabled(true);
            }

            updateUI(Trichotomy.previous());

            // 刷新过后回退时，原有的记录就不用保存了
            if (hasRefreshed) {
                Trichotomy.deleteNextStep();
                hasRefreshed = false;
            }

            mBinding.triLinContainer1.setVisibility(View.VISIBLE);
            mBinding.triLinContainer2.setVisibility(View.VISIBLE);
            mBinding.triLinContainer3.setVisibility(View.VISIBLE);
            hasShowedResult = false;

        }

    }

    /*------------------------------------------------------------------------------------------------------*/



    /**
     * 设置监听器
     * @return void
     * @date 2021/10/25 15:01
     * @author tou
     */
    private void initListener() {

        mBinding.btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 第一次按下 下一步 时候，展示被选择的组，再一次按下时，才进入下一步
                if (!hasShowedResult) {

                    int currentGroup = Trichotomy.getCcurrentTargetGroup();
                    Log.i(TAG, "onClick: currentGroup = " + currentGroup);
                    mBinding.triLinContainer1.setVisibility(currentGroup == 0 ? View.VISIBLE : View.GONE);
                    mBinding.triLinContainer2.setVisibility(currentGroup == 1 ? View.VISIBLE : View.GONE);
                    mBinding.triLinContainer3.setVisibility(currentGroup == 2 ? View.VISIBLE : View.GONE);
                    mBinding.triContainer.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent));

                } else {

                    mBinding.triLinContainer1.setVisibility(View.VISIBLE);
                    mBinding.triLinContainer2.setVisibility(View.VISIBLE);
                    mBinding.triLinContainer3.setVisibility(View.VISIBLE);

                    // 刷新过但继续向下执行，原有的刷新就不再考虑
                    if (hasRefreshed) {
                        hasRefreshed = false;
                    }

                    int[] targetStep = Trichotomy.next();

                    updateUI(targetStep);

                    assert targetStep != null;
                    if(targetStep[EXPECTED_GROUP_NUM] == 1) {
                        if (targetStep[TARGET_GROUP] != 2 || targetStep[REMAIN_GROUP_NUM] == 1) {
                            Toast.makeText( mContext, "找到了！", Toast.LENGTH_SHORT).show();
                            mBinding.btnNextStep.setEnabled(false);
                            mBinding.btnRefreshCoins.setEnabled(false);
                        }
                    }
                }

                hasShowedResult = !hasShowedResult;

            }
        });

        mBinding.btnRefreshCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUI(Trichotomy.refreshCurrentStep());
                hasRefreshed = true;
            }
        });

        mBinding.btnRestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


/*-------------------------------------------------------------------------------------------------------------------*/

    /**
     * 绘制界面 UI 并展示
     * @param targetStep:
     * @return void
     * @date 2021/10/26 14:46
     * @author tou
     */
    private void updateUI(int[] targetStep) {

//        Toast.makeText(this, Arrays.toString(targetStep), Toast.LENGTH_SHORT).show();
//        Log.i(TAG, "updateUI: targetStep = " + Arrays.toString(targetStep));

        int previousTargetGroup = Trichotomy.getPreviousTargetGroup();
        String[] str = {"", "", ""};
        TextView[] tvs = {mBinding.tvCoinsContainer1, mBinding.tvCoinsContainer2, mBinding.tvCoinsContainer3};
        LinearLayout[] tvContainers = {mBinding.triLinContainer1, mBinding.triLinContainer2, mBinding.triLinContainer3};
        ConstraintLayout bgContainer = mBinding.triContainer;

        for (int i = 0; i < 3; i++) {

            int groupNum = (i != 2) ? targetStep[EXPECTED_GROUP_NUM] : targetStep[REMAIN_GROUP_NUM];

            for (int j = 0; j < groupNum; j++) {
                if (targetStep[TARGET_GROUP] == i && targetStep[TARGET_INDEX] == j) {
                    str[i] += "❤";
                } else {
                    str[i] +="🙂";
                }
            }

            tvs[i].setText(str[i]);
            tvContainers[i].setBackgroundColor( Trichotomy.getPointer()%2 == 0 ?
                    bgColors[i] :
                    bgColors[bgColors.length - 1 - i]
            );

            if (previousTargetGroup < 0) {
                bgContainer.setBackgroundColor(ContextCompat.getColor(this, R.color.transparent));
            } else {
                bgContainer.setBackgroundColor(Trichotomy.getPointer() % 2 == 0 ?
                        bgColors[bgColors.length - 1 - Trichotomy.getPreviousTargetGroup()] :
                        bgColors[Trichotomy.getPreviousTargetGroup()]
                );
            }

        }

    }


}

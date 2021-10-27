package com.toulzx.counterfeitcoins.algorithm;

import android.util.Log;

import java.util.ArrayList;
import java.util.Random;

public class Trichotomy {

    private static ArrayList<int[]> stepList;
    private static int pointer = -1;

    private static final String TAG = Trichotomy.class.getSimpleName();
    public static final int TARGET_GROUP = 0;  // 假币当前所在组别
    public static final int TARGET_INDEX = 1;  // 假币所在组内的序号
    public static final int EXPECTED_GROUP_NUM = 2;    // 天平两侧的硬币数
    public static final int REMAIN_GROUP_NUM = 3;      // 剩余硬币数


    /**
     * 首次创建，初始化
     * @return int[] currentStep or null(if error)
     * @date 2021/10/26 14:26
     * @author tou
     */
    public static int[] init(int num) {

        Random rand;
        int randNum;
        int[] currentStep = new int[4];

        pointer = 0;
        stepList = new ArrayList<>();

        currentStep[EXPECTED_GROUP_NUM] = num / 3;

        currentStep[REMAIN_GROUP_NUM] = currentStep[EXPECTED_GROUP_NUM] + num % 3;

        rand = new Random();
        do {
            randNum = rand.nextInt(2 + 1);
        } while (currentStep[TARGET_GROUP] == randNum);
        currentStep[TARGET_GROUP] = randNum;    // 随机生成 0或1或2

        rand = new Random();
        if (currentStep[TARGET_GROUP] != 2) {
            do {
                randNum = rand.nextInt( currentStep[EXPECTED_GROUP_NUM] );
            } while (currentStep[TARGET_GROUP] == randNum);
            currentStep[TARGET_INDEX] = randNum;
        } else {
            do {
                randNum = rand.nextInt( currentStep[REMAIN_GROUP_NUM] );
            } while (currentStep[TARGET_GROUP] == randNum);
            currentStep[TARGET_INDEX] = randNum;
        }

        stepList.add(currentStep);

        return currentStep;

    }

    /**
     * 点击下一步按钮后，分组
     * @return int[] currentStep or null(if error)
     * @date 2021/10/26 10:57
     * @author tou
     */
    public static int[] next() {

        if (pointer == -1) {

            Log.e(TAG, "next: 你不应该在初始化之前执行这个操作！");
            assert pointer == -1;
            return null;

        } else if (pointer != stepList.size() - 1) {

            return stepList.get(++pointer);

        } else {

            int[] currentStep = new int[4];
            int[] previousStep = stepList.get(stepList.size() - 1);
            // 当前用来分组的总硬币数
            int currentNum = ( previousStep[TARGET_GROUP] != 2 ) ?
                    previousStep[EXPECTED_GROUP_NUM] :
                    previousStep[REMAIN_GROUP_NUM];

            if (currentNum == 2) {

                currentStep[EXPECTED_GROUP_NUM] = 1;
                currentStep[REMAIN_GROUP_NUM] = 0;

            } else if (currentNum == 1) {

                return previousStep;

            } else {

                currentStep[EXPECTED_GROUP_NUM] = currentNum / 3;
                currentStep[REMAIN_GROUP_NUM] = currentStep[EXPECTED_GROUP_NUM] + currentNum % 3;

            }

            currentStep[TARGET_GROUP] = ( currentStep[EXPECTED_GROUP_NUM] <= previousStep[TARGET_INDEX] ) ?
                    ( (2 * currentStep[EXPECTED_GROUP_NUM] <= previousStep[TARGET_INDEX]) ? 2 : 1 ) : 0;
            currentStep[TARGET_INDEX] = previousStep[TARGET_INDEX] - currentStep[EXPECTED_GROUP_NUM] * currentStep[TARGET_GROUP];

            stepList.add(currentStep);
            ++pointer;

            return currentStep;

        }

    }


    /**
     * 点击刷新按钮后，刷新当前的硬币（假币）分布
     * @return int[] newCurrentStep or null(if error)
     * @date 2021/10/26 11:18
     * @author tou
     */
    public static int[] refreshCurrentStep() {

        if (pointer == -1) {

            Log.e(TAG, "refreshCurrentStep: 你不应该在初始化之前执行这个操作！");
            assert pointer == -1;
            return null;

        }

        Random rand;
        int[] currentStep = stepList.get(pointer);

        rand = new Random();
        currentStep[TARGET_GROUP] = rand.nextInt(2 + 1);    // 随机生成 0或1或2

        rand = new Random();
        if (currentStep[TARGET_GROUP] != 2) {
            currentStep[TARGET_INDEX] = rand.nextInt( currentStep[EXPECTED_GROUP_NUM] );
        } else {
            currentStep[TARGET_INDEX] = rand.nextInt( currentStep[REMAIN_GROUP_NUM] );
        }

        stepList.set(pointer, currentStep);

        // 刷新后，需删除 pointer 后面的 steps
        while (pointer < stepList.size() - 1) {
            stepList.remove(pointer + 1);
        }

        return currentStep;



    }


    /**
     * 当按返回键返回上一步时，返回上一步信息
     * @return int[] targetStep or null(if pointer = 1)
     * @date 2021/10/26 11:41
     * @author tou
     */
    public static int[] previous() {

        if (pointer == -1) {

            Log.e(TAG, "previous: 你不应该在初始化之前执行这个操作！");
            assert pointer == -1;
            return null;

        } else if (pointer == 0) {

            Log.i(TAG, "previous:  pointer == 0");
            return null;

        } else {

            return stepList.get(--pointer);

        }


    }

    /**
     * 获得指针位置值
     * @return int pointer
     * @date 2021/10/26 14:38
     * @author tou
     */
    public static int getPointer() {
        return pointer;
    }

    /**
     * 获得前一步的假币所在组
     * @return int
     * @date 2021/10/26 17:00
     * @author tou
     */
    public static int getPreviousTargetGroup() {
        if (pointer == -1) {

            Log.e(TAG, "getPreviousTargetGroup: 你不应该在初始化之前执行这个操作！");
            assert pointer == -1;
            return -1;

        } else if (pointer == 0) {

            Log.i(TAG, "previous:  pointer == 0");
            return -1;

        } else {
            return stepList.get(pointer - 1)[TARGET_GROUP];
        }
    }


    /**
     * 获得当前的假币所在组
     * @return int
     * @date 2021/10/27 09:36
     * @author tou
     */
    public static int getCcurrentTargetGroup() {
        if (pointer == -1) {

            Log.e(TAG, "getCurrentTargetGroup: 你不应该在初始化之前执行这个操作！");
            assert pointer == -1;
            return -1;

        } else {
            return stepList.get(pointer)[TARGET_GROUP];
        }
    }


    /**
     * 删除下一条的记录
     * 刷新之后的返回时不需要保留原有记录
     * @return void
     * @date 2021/10/26 19:57
     * @author tou
     */
    public static void deleteNextStep() {
        stepList.remove(pointer + 1);
    }

}

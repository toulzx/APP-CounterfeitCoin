package com.toulzx.counterfeitcoins.algorithm;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class TrichotomyOriginal {

    private static final String TAG = TrichotomyOriginal.class.getSimpleName();

    //输入的总硬币 (测试用例)
    private static int[] all = { 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 0};
    //假币的序号
    private static int indexOfAll = -1;
    //第一堆硬币
    private static int[] a = new int[all.length];
    //第二堆硬币
    private static int[] b = new int[all.length];
    //第三堆硬币
    private static int[] c = new int[all.length];
    //余数堆硬币
    private static int[] d = new int[all.length];

    public static void init(int[] all, Context context){

        indexOfAll = -1;
        a = new int[all.length];
        b = new int[all.length];
        c = new int[all.length];
        d = new int[all.length];

        System.out.println("\n");

        compare(all, all.length);

        if (indexOfAll != -1) {
            Log.i(TAG, "init: \n[real]假币序号是：" + indexOfAll);
            Toast.makeText(context, "假币序号是：" + indexOfAll, Toast.LENGTH_SHORT).show();
        } else {
            Log.i(TAG, "init: [real]完蛋，出错了！");
        }

    }

    /**
     * 计算数组和 / 测量硬币总重量
     * @param coin:
     * @param n:
     * @return int
     * @date 2021/11/2 17:05
     * @author tou
     */
    private static int sum(int[] coin, int n) {
        int result = 0;
        for (int i = 0; i < n; i++) {
            result += coin[i];
        }
        return result;
    }


    /**
     * 判断两个硬币真假
     * @param coin:
     * @return int
     * @date 2021/11/2 17:07
     * @author tou
     */
    private static void judgeTwo(int[] coin) {
        if (coin[0] < coin[1]) {
            indexOfAll += 1;
        } else if (coin[0] > coin[1]) {
            indexOfAll += 2;
        }
    }

    /**
     * 判断三个硬币真假
     * @param coin:
     * @return int
     * @date 2021/11/2 18:28
     * @author tou
     */
    private static void judgeThree(int[] coin) {
        if (coin[0] < coin[1] && coin[0] < coin[2]) {
            indexOfAll += 1;
        } else if (coin[0] > coin[1] && coin[1] < coin[2]) {
            indexOfAll += 2;
        } else if (coin[1] > coin[2] && coin[2] < coin[0]) {
            indexOfAll += 3;
        }
    }


    /**
     * 把硬币分成 3 份和一个余数组
     * @param n:
     * @return void
     * @date 2021/11/2 18:37
     * @author tou
     */
    private static void allot(int[] coin, int n) {
        //第一堆硬币
        a = new int[coin.length];
        //第二堆硬币
        b = new int[coin.length];
        //第三堆硬币
        c = new int[coin.length];
        //余数堆硬币
        d = new int[coin.length];

        Log.i(TAG, "allot: \n[real] " + n + " 枚硬币开始分组...\n\n");

        int groupNum = (n - n%3) / 3;
        int excessNum = n%3;

        for (int i = 0; i < groupNum; i++){
            a[i] = coin[i];
        }
        Log.i(TAG, "allot: [real]第一堆硬币数量为：" + groupNum + " 枚\n");

        for (int w = 0, i = groupNum; i < 2 * groupNum; i++, w++) {
            b[w] = coin[i];
        }
        Log.i(TAG, "allot: [real]第二堆硬币数量为：" + groupNum + " 枚\n");

        for (int w = 0, i = 2*groupNum; i < 3 * groupNum; i++, w++) {
            c[w] = coin[i];
        }
        Log.i(TAG, "allot: [real]第三堆硬币数量为：" + groupNum + " 枚\n");

        if (excessNum!=0) {
            for (int w = 0, i = 3*groupNum; i<coin.length; i++, w++){
                d[w] = coin[i];
            }
            Log.i(TAG, "allot: [real]第一堆硬币数量为：" + excessNum + " 枚\n");
        } else {
            Log.i(TAG, "allot: [real]余数堆硬币数量为 0 枚\n");
        }
        Log.i(TAG, "allot: --------------------------------------------------------------------------");
    }


    /**
     * 分情况讨论各堆质量比较情况（上称）
     * @param coin:
     * @param n:
     * @return void
     * @date 2021/11/2 21:02
     * @author tou
     */
    private static void compare(int[] coin, int n) {
        allot(coin, n);
        // 前 3 堆每一堆硬币数量
        int groupNum = (n-n%3)/3;
        // 余数堆硬币数量
        int excessNum = n%3;
        // 第一堆硬币总质量
        int sum_1 = sum(a,groupNum);
        Log.i(TAG, "compare: [real]第一堆硬币总质量为：" + sum_1 + "\n");
        // 第二堆硬币总质量
        int sum_2 = sum(b,groupNum);
        Log.i(TAG, "compare: [real]第二堆硬币总质量为：" + sum_2 + "\n");
        // 如果 a == b
        if(sum_1 == sum_2) {
            Log.i(TAG, "compare: [real]第一堆硬币总质量等于第二堆硬币总质量，则计算第三堆硬币总质量...\n");
            // 第三堆硬币总质量
            int sum_3 = sum(c, groupNum);
            Log.i(TAG, "compare: [real]第三堆硬币总质量为：" + sum_3 + "\n");
            // 如果 a == c ，假币在d中
            if(sum_1 == sum_3)
            {
                Log.i(TAG, "compare: [real]第一堆硬币总质量等于第三堆硬币总质量,则假币在余数堆中\n");
                indexOfAll += 3*groupNum;
                if(excessNum == 1) {indexOfAll += 1;}
                if(excessNum == 2) {judgeTwo(d);}
                if(excessNum == 3) {judgeThree(d);}
                //用减治法迭代
                if(excessNum > 3)  {System.out.println("temp="+ indexOfAll +"\n");compare(d, excessNum);}
            }
            else{  //如果a不等于c,假币在c中
                Log.i(TAG, "compare: [real]第一堆硬币总质量不等于第三堆硬币总质量,则假币在第三堆中\n");
                indexOfAll += 2*groupNum;
                if(groupNum == 1) {
                    indexOfAll += 1;};
                if(groupNum == 2) {judgeTwo(c);}
                if(groupNum == 3) {judgeThree(c);}
                if(groupNum > 3)  {System.out.println("temp="+ indexOfAll +"\n");compare(c, groupNum);}
            }
        }
        else if(sum_1 > sum_2)
        {// 如果 a 大于 b,假币在 b 中
            Log.i(TAG, "compare: [real]第一堆硬币总质量大于第二堆硬币总质量,则假币在第二堆中\n");
            indexOfAll += groupNum;
            if(groupNum == 1) {
                indexOfAll += 1;};
            if(groupNum == 2) {judgeTwo(b);}
            if(groupNum == 3) {judgeThree(b);}
            //迭代
            if(groupNum > 3)  {
//                Log.i(TAG, "compare: temp="+temp+"\n");
                compare(b, groupNum);
            }
        }
        else
        {//如果a小于b,假币在a中
            System.out.println("[real]第一堆硬币总质量小于第三堆硬币总质量,则假币在第一堆中\n");
            indexOfAll += 0;
            if(groupNum == 1) {
                indexOfAll += 1;};
            if(groupNum == 2) {judgeTwo(a);}
            if(groupNum == 3) {judgeThree(a);}
            //迭代
            if(groupNum > 3)  {
//                Log.i(TAG, "compare: temp="+temp+"\n");
                compare(a, groupNum);
            }
        }
    }

}

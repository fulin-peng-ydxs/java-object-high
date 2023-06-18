package algorithm.sort.extreme;

import org.junit.Test;


/** 求值算法
 * @author PengFuLin
 * @date 11:04 2022/9/24
 **/
public class CountNum {

    @Test
    public void countNum(){
        int[] num = new int[10];
        for (int i = 0; i < num.length; i++)
            //在 [0.0-1.0)
            num[i]=(int)(Math.random()*90+10);
        //求数组元素的最大值
        int maxValue = num[0];
        for(int i = 1;i < num.length;i++){
            if(maxValue < num[i])
                maxValue = num[i];
        }
        System.out.println("最大值为:" + maxValue);
        //求数组元素的最小值
        int minValue = num[0];
        for(int i = 1;i < num.length;i++){
            if(minValue > num[i])
                minValue =num[i];
        }
        System.out.println("最小值为。" +minValue) ;
        //求数组元素的总和
        int sum=0;
        for(int i = 0;i < num.length;i++)
            sum +=num[i];
        System.out.println("总和为:" +sum);
        //求平均值
        int avgValue = sum / num.length;
        System.out.println("平均数为：" + avgValue) ;
    }

}

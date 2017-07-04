package com.data;

/**
 * Created by Demon on 2017/7/4.
 */
public class FindMaxSumOfSubArray {

    public static void main(String[]args){
        int[] arr = { 1, -2, 3, 10, -4, 7, 2, -5 };
        System.out.println(findMaxSum(arr));
    }
     static Integer findMaxSum(int []arr){
         int curSum=0;
         int maxSum=0;
         int len=arr.length;
         if (arr == null || len == 0) {
             return null;
         }
         for(int i=0;i<len;i++){
             curSum+=arr[i];
             if(curSum<0){
                 curSum=0;
             }
             if(curSum>maxSum){
                 maxSum=curSum;
             }
         }
         if(maxSum==0){
             for(int i=0;i<len;i++){
                 if(i==0){
                     maxSum=arr[i];
                 }
                 if(arr[i]>maxSum){
                     maxSum=arr[i];
                 }
             }
         }
         return maxSum;
     }
}

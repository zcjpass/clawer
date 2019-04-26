package com.crawler.task;

public class Mathslary {

    public static Integer[] getSalary(String salary){

        Integer[] sal = new Integer[2];
        String data = salary.substring(salary.length()-1,salary.length()-0);
        String unit = salary.substring(salary.length()-3,salary.length()-2);
        String min = salary.substring(0,salary.indexOf("-"));
        String max = salary.substring(salary.indexOf("-")+1,salary.indexOf("/")-1);
        float flmin = Float.parseFloat(min);
        float flmax = Float.parseFloat(max);
        int mins;
        int maxs;
        if(data.equals("天")){
            if(unit.equals("千")){
                mins = (int)(flmin * 240 * 1000);
                maxs = (int)(flmax * 240 * 1000);
            }else{
                mins = (int)(flmin * 240 * 10000);
                maxs = (int)(flmax * 240 * 10000);
            }
        }else if(data.equals("月")){
            if(unit.equals("千")){
                mins = (int)(flmin * 12 * 1000);
                maxs = (int)(flmax * 12 * 1000);
            }else{
                mins = (int)(flmin * 12 * 10000);
                maxs = (int)(flmax * 12 * 10000);
            }
        }else {
            if(unit.equals("千")){
                mins = (int)(flmin * 1000);
                maxs = (int)(flmax * 1000);
            }else{
                mins = (int)(flmin * 10000);
                maxs = (int)(flmax * 10000);
            }
        }
        sal[0] = Integer.valueOf(mins);
        sal[1] = Integer.valueOf(maxs);
//        System.out.println(sal[0]);
//        System.out.println(sal[1]);
        return sal;
    }
    public static String getString(String str){
        for(int i = 0; i < str.length(); i++){
            if(str.charAt(i)>= 48 && str.charAt(i)<=57 || str.charAt(i) == '无'){
                return str.substring(0,i);
            }
        }
        return null;
    }
    public static  String getTime(String time){
        return  time.substring(time.indexOf("发布")-5,time.indexOf("发布"));
    }
}

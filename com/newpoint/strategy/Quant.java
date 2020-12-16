package com.newpoint.strategy;


public class Quant {
    //calculate simple moving average
      public static double SMA(double[] x) {
                 int m=x.length;
                 double sum=0;
                 for(int i=0;i<m;i++){//求和
                         sum+=x[i];
                     }
                 double dAve=sum/m;//求平均值
                return dAve;
             }

             //calculate standard deviation
             public static double STD(double[] x) {
                 int m=x.length;
                 double sum=0;
                 for(int i=0;i<m;i++){//求和
                         sum+=x[i];
                     }
                 double dAve=sum/m;//求平均值
                 double dVar=0;
                 for(int i=0;i<m;i++){//求方差
                         dVar+=(x[i]-dAve)*(x[i]-dAve);
                     }
                 return Math.sqrt(dVar/m);
             }

    //calculate standard deviation given average
    public static double STD(double[] x, double dAve) {
        int m=x.length;
        double dVar=0;
        for(int i=0;i<m;i++){//求方差
            dVar+=(x[i]-dAve)*(x[i]-dAve);
        }
        return Math.sqrt(dVar/m);
    }
}

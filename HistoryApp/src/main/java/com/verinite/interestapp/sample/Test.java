package com.verinite.interestapp.sample;

public class Test {

    public static void main(String args[]){ 
        System.err.println(stack());
    }

    public static Integer stack(){
        try {
            throw new RuntimeException();
          }
          catch (RuntimeException e) {
            StackTraceElement[] stackTrace = e.getStackTrace();

          
            for (int i = 0;i<stackTrace.length ;i++) {
              String[] packageArray = stackTrace[i].getClassName().split("\\.");
              String subPackage = packageArray[0]+"."+packageArray[1];
              if(subPackage.equalsIgnoreCase("com.verinite")){
                System.out.println("Package:" + subPackage +
                  ", method: "+ stackTrace[i].getMethodName() +
                  ", line:" + stackTrace[i].getLineNumber());
                  return i;
              }
              
            }
            return null;
          }
    }
    
}

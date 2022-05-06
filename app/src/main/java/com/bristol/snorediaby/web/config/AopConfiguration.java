package com.bristol.snorediaby.web.config;

import android.util.Log;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.logging.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Function: AopConfiguration
 * Description:
 * Author: wilso
 * Date: 2022/5/6
 * MaintenancePersonnel: wilso
 */
@Aspect
public class AopConfiguration {

    //@Pointcut("execution(* com.lqr.androidaopdemo.MainActivity.test(..))")
    //public void pointcut() {
    //
    //}
    //
    //@Before("pointcut()")
    //public void before(JoinPoint point) {
    //    System.out.println("@Before");
    //}
    //
    //@Around("pointcut()")
    //public void around(ProceedingJoinPoint joinPoint) throws Throwable {
    //    System.out.println("@Around");
    //}
    //
    //@After("pointcut()")
    //public void after(JoinPoint point) {
    //    System.out.println("@After");
    //}
    //
    //@AfterReturning("pointcut()")
    //public void afterReturning(JoinPoint point, Object returnValue) {
    //    System.out.println("@AfterReturning");
    //}
    //
    //@AfterThrowing(value = "pointcut()", throwing = "ex")
    //public void afterThrowing(Throwable ex) {
    //    System.out.println("@afterThrowing");
    //    System.out.println("ex = " + ex.getMessage());
    //}

    /**
     * log logic service method start run and end run
     */
    //@Around("execution(* com.bristol.snorediaby.logic..*(..))")
    public Object logService(ProceedingJoinPoint pjp) throws Throwable {
        return around(pjp);
    }

    /**
     * log repo method start run and end run
     */
    //@Around("execution(* com.bristol.snorediaby.repo..*(..))")
    public Object logRepo(ProceedingJoinPoint pjp) throws Throwable {
        return around(pjp);
    }

    /**
     * log Web view method start run and end run
     */
    //@Around("execution(* com.bristol.snorediaby.web..*(..))")
    public Object logWebView(ProceedingJoinPoint pjp) throws Throwable {
        return around(pjp);
    }

    private Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        String targetClassName = null;
        String methodName = null;
        long startTime = System.currentTimeMillis();
        try {
            Object target = joinPoint.getTarget();
            String targetClassFullName = target.getClass().getName();
            targetClassName = targetClassFullName.substring(targetClassFullName.lastIndexOf(".") + 1);

            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            methodName = method.getName() + "()";

            Object[] args = joinPoint.getArgs();
            // Print the entry point of joinPoint
            Log.i(targetClassName, "== START " + targetClassName + "." + methodName);

            if (Log.isLoggable(targetClassFullName, Log.DEBUG)) {
                // Print the input arguments
                Log.d(targetClassFullName, methodName + " input: " + Arrays.toString(args));
            }

            // Start running the joinPoint (ex: Class function)
            Object result = joinPoint.proceed();

            // Print result if joinPoint has returned something else
            if (null != result) {
                if (Log.isLoggable(targetClassFullName, Log.DEBUG)) {
                    Log.d(targetClassFullName, methodName + " return: " + result.toString());
                }
            }

            return result;
        } finally {
            // Print the end point of joinPoint
            Log.i(targetClassName,
                "== END " + targetClassName + "." + methodName + "(" + ((System.currentTimeMillis() - startTime) * 1d / 1000) + ")");
        }
    }

}

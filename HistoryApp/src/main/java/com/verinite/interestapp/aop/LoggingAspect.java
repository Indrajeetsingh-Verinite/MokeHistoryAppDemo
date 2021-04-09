package com.verinite.interestapp.aop;

import java.util.Arrays;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.CodeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import com.google.gson.Gson;

@Aspect
@Component
public class LoggingAspect {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Pointcut("within(com.verinite.interestapp..*)")
	public void interestApplication() {
		throw new UnsupportedOperationException();

	}
	
	@AfterThrowing(pointcut = "interestApplication() ", throwing = "ex")
	public void logAfterThrowing(JoinPoint joinPoint, Throwable ex) {
		log.info("Exception in Class Name: {} . Method Name: {} ", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName());

		log.error("Exception in {} {} {}", joinPoint.getSignature().getDeclaringTypeName(),
				joinPoint.getSignature().getName(), ex.getCause() != null ? ex.getCause() : "NULL");
	}

	@Around("interestApplication() ")
	public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
		Object result = null;
		final StopWatch stopWatch = new StopWatch();

		stopWatch.start();

		if (log.isInfoEnabled() && joinPoint.getStaticPart().toShortString() != null) {
			log.info("Interest App Start With Controller .....{}", joinPoint.getStaticPart().toShortString());
		}

		if (log.isInfoEnabled() && Arrays.toString(joinPoint.getArgs()) != null) {
			log.info("Enter: {}.{}() with Parameter[s] = {}", joinPoint.getSignature().getDeclaringTypeName(),
					joinPoint.getSignature().getName(), Arrays.toString(joinPoint.getArgs()));
		}
		CodeSignature methodSignature = (CodeSignature) joinPoint.getSignature();
		String[] sigParamNames = methodSignature.getParameterNames();

		for (String signatureData : sigParamNames) {
			String jsonInString = new Gson().toJson(Arrays.toString(joinPoint.getArgs()));
			log.info("parameter's name: {} ::::Parameter Value {}", signatureData, jsonInString);
		}
		try {
			result = joinPoint.proceed();
			stopWatch.stop();
			String jsonInString = new Gson().toJson(result);
			log.info("Exit: {}.{}() with result = {} Time Taken For Execution  = {}",
					joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName(), result,
					stopWatch.getTotalTimeMillis());

			if (log.isInfoEnabled() && joinPoint.getStaticPart().toShortString() != null) {

				log.info("Interest App End With Controller..... {} \n Result-- {}",
						joinPoint.getStaticPart().toShortString(), jsonInString);
			}
			return result;
		} catch (IllegalArgumentException e) {
			log.error("Illegal argument: {} in {}.{}()", Arrays.toString(joinPoint.getArgs()),
					joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
			throw e;
		}

	}

}
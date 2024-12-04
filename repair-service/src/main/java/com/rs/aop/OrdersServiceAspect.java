package com.rs.aop;

import com.rs.domain.po.Orders;
import com.rs.utils.MailUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class OrdersServiceAspect {
    @Pointcut("execution(* com.repairstation.service.impl.OrdersServiceImpl.addOrder(..))")
    public void addOrderPointCut() {}

    @Autowired
    private MailUtils mailUtils;

    private Orders order;


    @Around(value = "addOrderPointCut()")
    public Object aroundAddOrder(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取参数
        Object[] args = joinPoint.getArgs();

        order = (Orders) args[0];

        return joinPoint.proceed();
    }

    // 后置通知：方法执行之后执行
    @After("addOrderPointCut()")
    public void afterAddOrder() {
        mailUtils.sendEMail("您有新的维修订单，请即使处理！", order.getOrderDescribe());
    }
}

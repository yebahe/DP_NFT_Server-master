package cn.dp.nft.rpc.facade;


import cn.dp.nft.base.response.BaseResponse;
import cn.dp.nft.base.response.ResponseCode;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 *  切面 ： 由切入点 + 通知 组成  ===>做统一的处理
 */
@Component
@Aspect
public class FacadeAnttAspect {

    /**
     * 统一响应结果
     * @param response
     */
    private void enrichObject(Object response) {
        if (response instanceof BaseResponse) {
            if (((BaseResponse) response).getSuccess()) {
                //如果状态是成功的，需要将未设置的responseCode设置成SUCCESS
                if (StringUtils.isEmpty(((BaseResponse) response).getResponseCode())) {
                    ((BaseResponse) response).setResponseCode(ResponseCode.SUCCESS.name());
                }
            } else {
                //如果状态是成功的，需要将未设置的responseCode设置成BIZ_ERROR
                if (StringUtils.isEmpty(((BaseResponse) response).getResponseCode())) {
                    ((BaseResponse) response).setResponseCode(ResponseCode.BIZ_ERROR.name());
                }
            }
        }
    }

    private void printLog(JoinPoint joinPoint) {

    }

    /** 统一的参数检验、
     * 环绕通知
     * @param pjp
     * @return
     * @throws Throwable
     */

    @Around("@annotation(cn.dp.nft.rpc.facade.FacadeAntt)")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        //
        MethodSignature methodSignature = (MethodSignature)pjp.getSignature();
        Method method = methodSignature.getMethod();
        Object[] args = pjp.getArgs();
        // 1、 检查参数是否合法
        for (Object arg : args) {
            //参数校验工具


        }
        // 方法执行前
        // 2、方法的执行
        try{
            Object rpcResponse = pjp.proceed(); // 目标方法的执行
            // 将返回消息配置好
            enrichObject(rpcResponse);

            // 打印日志


        }catch{

        }


        // 方法执行后


    }
    @Before("")
    public void before(JoinPoint joinPoint) {

    }
    @After("")
    public void after(JoinPoint joinPoint) {

    }

    @AfterReturning("")
    public void afterReturning(JoinPoint joinPoint) {

    }


    // 1、日志输出
    // 2、参数校验
    // 3、异常捕获

}

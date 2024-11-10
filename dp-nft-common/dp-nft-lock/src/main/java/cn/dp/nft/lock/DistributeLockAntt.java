package cn.dp.nft.lock;

import cn.dp.nft.lock.domain.Enum.LockTypeEnum;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DistributeLockAntt {

    // 1、持有锁的时间
    public int lockTime() default -1; //默认时间是-1 表示需要主动释放锁
    // 2、等待时间 用于定义获取锁失败后的等待时间。这个时间用于控制在获取锁失败后等待多长时间再尝试下一次获取锁操作
    public int waitTime() default -1; // 等待时间，默认是-1 ，不等待直接返回失败
    // 3、超时重试次数
    public int maxRetryCount() default -1; // 默认不重试
    // 自定义加锁类型
    public LockTypeEnum typeEnum();
    TimeUnit unit() default TimeUnit.MILLISECONDS;
    // 5、
    public String key() default "lock";

}

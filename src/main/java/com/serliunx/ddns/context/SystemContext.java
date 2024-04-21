package com.serliunx.ddns.context;

import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 系统参数上下文
 * @author SerLiunx
 * @since 1.0
 */
@Component
public final class SystemContext {

    /**
     * 本机公网IP
     */
    private volatile String publicIp;

    /**
     * 当前公网ip状态, 为假时说明有线程正在设置新IP, 获取线程应该等待
     * <li> 这么做是尽量保证每次尝试修改解析记录时都是最新的公网IP
     */
    @Getter
    private volatile boolean newestIp = false;

    // 公网状态、公网设置锁(用同一把锁即可)
    private final ReentrantLock ipLock = new ReentrantLock();

    // 自旋获取最新的公网ip, 最多在此处自旋1秒, 每次等待100毫秒.
    public String getPublicIp(){
        int maxRetry = 0;
        while(!newestIp){
            if(maxRetry == 10){
                return null;
            }
            // 非最新IP, 继续自旋
            maxRetry++;
            LockSupport.parkNanos(100 * 1000 * 1000);
        }
        return publicIp;
    }

    /**
     * 设置当前公网IP状态
     * <li> 为保证数据的一致性, 该方法是加锁的. 因为正常情况下不存在多线程竞争去设置本机的公网IP
     * @param newestIp 公网IP状态
     */
    public void setNewestIp(boolean newestIp){
        try {
            ipLock.lock();
            this.newestIp = newestIp;
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            ipLock.unlock();
        }
    }

    /**
     * 设置当前公网IP
     * <li> 为保证数据的一致性, 该方法是加锁的. 因为正常情况下不存在多线程竞争去设置本机的公网IP
     * @param publicIp 公网IP
     */
    public void setPublicIp(String publicIp){
        try {
            ipLock.lock();
            this.publicIp = publicIp;
        }catch (Exception e){
            throw new RuntimeException(e);
        }finally {
            ipLock.unlock();
        }
    }
}

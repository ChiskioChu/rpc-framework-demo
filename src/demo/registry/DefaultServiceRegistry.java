package demo.registry;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
/**
 * 实现服务注册、服务信息的获取
 * */
public class DefaultServiceRegistry implements ServiceRegistry {

    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();
    private final Set<String> registeredService = ConcurrentHashMap.newKeySet();

    @Override
    public synchronized <T> void registry(T service) {
        String serviceName = service.getClass().getCanonicalName();
        if(registeredService.contains(serviceName)) return;
        registeredService.add(serviceName);
        Class<?>[] interfaces = service.getClass().getInterfaces();
        for(Class<?> i : interfaces){
            serviceMap.put(i.getCanonicalName(), service);
        }
    }

    @Override
    public synchronized Object getService(String serviceName) {
        Object service = serviceMap.get(serviceName);
        return service;
    }
}

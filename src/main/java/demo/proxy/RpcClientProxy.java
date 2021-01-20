package demo.proxy;

import demo.dto.RpcRequest;
import demo.dto.RpcResponse;
import demo.netty.NettyClient;
import demo.rpc.RpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 生成动态代理，针对不同的服务调用发送封装后的信息
 * */

public class RpcClientProxy implements InvocationHandler{

    private RpcClient client; // 网络通信的方式

    public RpcClientProxy(RpcClient client) {
        this.client = client;
    }

    @SuppressWarnings("unchecked")
    public <T> T getProxy(Class<T> interfaceClass) {
        if (interfaceClass == null)
            throw new IllegalArgumentException("Interface class == null");
        if (! interfaceClass.isInterface())
            throw new IllegalArgumentException("The " + interfaceClass.getName() + " must be interface class!");

        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) {
        System.out.println("调用方法: " + method.getDeclaringClass().getName() + "#" + method.getName());
        RpcRequest rpcRequest = new RpcRequest(method.getDeclaringClass().getName(),
                method.getName(), args, method.getParameterTypes());
        RpcResponse rpcResponse = null;
        if (client instanceof NettyClient) {
            try {
                rpcResponse = (RpcResponse) client.sendRequest(rpcRequest);
            } catch (Exception e) {
                System.out.println("方法调用请求发送失败:" + e);
                return null;
            }
        }
        return rpcResponse.getData();
    }


}

package demo.client;

import demo.netty.NettyClient;
import demo.rpc.RpcClient;
import demo.proxy.RpcClientProxy;
import demo.service.CalculatorService;

public class ClientDemo {
    public static void main(String[] args) {
        RpcClient client = new NettyClient("127.0.0.1", 1234);
        RpcClientProxy proxy = new RpcClientProxy(client);
        CalculatorService calculatorService = proxy.getProxy(CalculatorService.class);
        int result = calculatorService.add(1, 2);
        System.out.println(result);
    }
}

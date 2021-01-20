package demo.server;

import demo.netty.NettyServer;
import demo.registry.DefaultServiceRegistry;
import demo.registry.ServiceRegistry;
import demo.service.CalculatorService;

import java.io.IOException;

public class RpcServerApplication {
    public static void main(String[] args) throws IOException {
        CalculatorService calculatorService = new CalculatorServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.registry(calculatorService);

        NettyServer server = new NettyServer();
        server.start(1234);
    }
}

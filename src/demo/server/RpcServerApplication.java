package demo.server;

import demo.registry.DefaultServiceRegistry;
import demo.registry.ServiceRegistry;
import demo.rpc.RpcServer;
import demo.service.CalculatorService;
import demo.service.PrintService;

import java.io.IOException;

public class RpcServerApplication {
    public static void main(String[] args) throws IOException {
        CalculatorService calculatorService = new CalculatorServiceImpl();
        PrintService printService = new PrintServiceImpl();
        ServiceRegistry serviceRegistry = new DefaultServiceRegistry();
        serviceRegistry.registry(calculatorService);
        serviceRegistry.registry(printService);
        RpcServer server = new RpcServer(serviceRegistry);
        server.start(1234);
    }
}

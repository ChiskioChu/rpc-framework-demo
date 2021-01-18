package demo.server;

import demo.rpc.RpcServer;
import demo.service.CalculatorService;
import demo.service.PrintService;

import java.io.IOException;

public class RpcServerApplication {
    public static void main(String[] args) throws IOException {
        RpcServer server = new RpcServer();
        CalculatorService calculatorService = new CalculatorServiceImpl();
        server.export(calculatorService, 1234);
    }
}

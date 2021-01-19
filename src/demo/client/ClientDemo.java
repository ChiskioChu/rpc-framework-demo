package demo.client;

import demo.rpc.RpcClient;
import demo.service.CalculatorService;
import demo.service.PrintService;

public class ClientDemo {
    public static void main(String[] args) {
        RpcClient rpcClient = new RpcClient();
        CalculatorService calculatorService = rpcClient.refer(CalculatorService.class, "127.0.0.1", 1234);
        int result = calculatorService.add(1, 2);
        System.out.println(result);
        PrintService printService = rpcClient.refer(PrintService.class, "127.0.0.1", 1234);
        System.out.println(printService.print(5, 3));
    }

}

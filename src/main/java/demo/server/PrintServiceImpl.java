package demo.server;

import demo.service.PrintService;

public class PrintServiceImpl implements PrintService {
    @Override
    public int print(int a, int b) {
        System.out.println("服务端计算" + a + "+" + b + "=" + (a + b));
        return a + b;
    }
}

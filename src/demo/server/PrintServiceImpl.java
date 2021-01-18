package demo.server;

import demo.service.PrintService;

public class PrintServiceImpl implements PrintService {
    @Override
    public int print(int a, int b) {
        System.out.println(a + " " + b);
        return a - b;
    }
}

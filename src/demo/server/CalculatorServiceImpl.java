package demo.server;

import demo.service.CalculatorService;

public class CalculatorServiceImpl implements CalculatorService {
    @Override
    public int add(int a, int b) {
        return a + b;
    }
}

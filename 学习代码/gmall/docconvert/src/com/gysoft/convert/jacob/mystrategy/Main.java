package com.gysoft.convert.jacob.mystrategy;

/**
 * @Description
 * @Author DJZ-WWS
 * @Date 2019/2/26 9:17
 */
public class Main {

    public static void main(String[] args) {

        StrategyContext strategyContext=new StrategyContext( new StrategyA());

        strategyContext.contextInterface();
    }
}

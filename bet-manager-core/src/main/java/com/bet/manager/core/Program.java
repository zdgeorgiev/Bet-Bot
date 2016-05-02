package com.bet.manager.core;

import com.bet.manager.core.data.DataManger;

import java.net.MalformedURLException;

public class Program {

    public static void main(String[] args) throws MalformedURLException, InterruptedException {

        System.out.println(new DataManger()
                .getDataForMatch("1.FC Kaiserslautern", "FC Augsburg", 2011, 34));
    }
}

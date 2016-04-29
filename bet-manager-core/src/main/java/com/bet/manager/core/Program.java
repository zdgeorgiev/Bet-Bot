package com.bet.manager.core;

import com.bet.manager.core.data.DataManger;

import java.net.MalformedURLException;
import java.util.HashMap;

public class Program {

	public static void main(String[] args) throws MalformedURLException, InterruptedException {

		System.out.println(DataManger
				.getDataForMatch("1.FC Kaiserslautern", "FC Augsburg", 2011, 34, new HashMap<>()));
	}
}

package com.bet.manager.core;

import com.bet.manager.core.data.DataManager;

public class Program {

	public static void main(String[] args) throws Exception {

		String a = new DataManager(false).getDataForMatch("FC Bayern München", "VfB Stuttgart", 2012, 2);
		System.out.println(a);
	}
}

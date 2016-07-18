package com.bet.manager.core;

import com.bet.manager.core.data.DataManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Program {

	private static Logger logger = LoggerFactory.getLogger(Program.class);

	public static void main(String[] args) throws Exception {

		String a = new DataManager(false).getDataForMatch("FC Bayern München", "VfB Stuttgart", 2012, 2).toString();
		System.out.println(a);

	}
}

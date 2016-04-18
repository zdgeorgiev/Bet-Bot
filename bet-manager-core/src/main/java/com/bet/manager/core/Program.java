package com.bet.manager.core;

import com.bet.manager.core.data.DataManger;
import org.w3c.dom.Document;

import java.net.URL;
import java.util.HashMap;

public class Program {

	public static void main(String[] args) {

		HashMap<String, Document> docs = new HashMap<>();
		HashMap<URL, String> urls = new HashMap<>();

		try {
			System.out.println(DataManger.getDataForMatch("1.FC Kaiserslautern", "FC Augsburg", 2011, 22, urls, docs));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

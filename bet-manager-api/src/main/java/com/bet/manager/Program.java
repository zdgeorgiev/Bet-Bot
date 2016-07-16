//package com.bet.manager;
//
//import com.bet.manager.web.model.MatchMetaData;
//import com.bet.manager.web.model.PreviousRoundStats;
//import com.bet.manager.web.model.TeamMetaData;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.map.SerializationConfig;
//
//import java.io.IOException;
//
//public class Program {
//	public static void main(String args[]) throws IOException {
//
//		ObjectMapper mapper = new ObjectMapper();
//
//		mapper.enable(SerializationConfig.Feature.INDENT_OUTPUT);
//		System.out.println(mapper.writeValueAsString(new Student("test", 2)));
//		System.out.println(mapper.writeValueAsString(new MatchMetaData("a", -1, -5,
//				new TeamMetaData(1, 2, 3, new PreviousRoundStats(4, 5, 6, 7, 8)),
//				new TeamMetaData(1, 2, 3, new PreviousRoundStats(4, 5, 6, 7, 8)))));
//	}
//}
//
//class Student {
//	private String name;
//	private int age;
//
//	public Student() {
//	}
//
//	public Student(String name, int age) {
//		this.name = name;
//		this.age = age;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public int getAge() {
//		return age;
//	}
//
//	public void setAge(int age) {
//		this.age = age;
//	}
//
//	public String toString() {
//		return "Student [ name: " + name + ", age: " + age + " ]";
//	}
//}
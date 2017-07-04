package com.solar.comtrast.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Testrr {
	public static void main(String[] args) {
		String str1 = "v1.100",str2="v1.11",str3="v1.10";
		List list =new ArrayList();
		list.add(str1);list.add(str2);list.add(str3);
		 
		 
		Collections.sort(list);
		
		for(int i = 0;i < list.size();i++)
			System.out.println(list.get(i));
		
	}
}

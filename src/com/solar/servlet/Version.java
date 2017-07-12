package com.solar.servlet;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.sun.javafx.geom.transform.GeneralTransform3D;

import sun.security.x509.IssuingDistributionPointExtension;

public class Version implements Comparable{
	private String main_Version;
	private final String base_Version = "release";
	private Date date_Version ;
	public String getMain_Version() {
		return main_Version;
	}
	
	public Version(String str){
		int firstIndex = str.indexOf("_");
		int secondIndex = str.indexOf("_", firstIndex+1);
		this.main_Version = str.substring(0,firstIndex);
		//this.base_Version = str.getBase_Version();
		SimpleDateFormat dateFormet = new SimpleDateFormat("yyyyMMdd");
		try {
			this.date_Version = dateFormet.parse(str.substring(secondIndex+1,str.length()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void setMain_Version(String main_Version) {
		this.main_Version = main_Version;
	}
	public Date getDate_Version() {
		return date_Version;
	}
	public void setDate_Version(Date date_Version) {
		this.date_Version = date_Version;
	}
	public String getBase_Version() {
		return base_Version;
	}
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		Version version = (Version)arg0;
		
		int date_result = version.getDate_Version().compareTo(this.getDate_Version());
		if(date_result == 0){
			int length = Math.min(this.getMain_Version().length(), version.getMain_Version().length());
			String mian_Version_1 = this.getMain_Version().substring(0, length);
			String main_Version_2 = version.getMain_Version().substring(0, length);
			int base_result = main_Version_2.compareTo(mian_Version_1);
			 
			return base_result == 0? version.getMain_Version().length() > length ? 1:-1:base_result;
		}
		else
			return date_result;
	}
	
	public String toString(){
		SimpleDateFormat dateFormet = new SimpleDateFormat("yyyyMMdd");
		return this.getMain_Version() + "_" + this.getBase_Version() + "_" + dateFormet.format(this.getDate_Version());
	}
	
	public static void main(String[] args) throws ParseException {
		
		Version version1 = new Version("1.0.2.1_release_20170201");
		Version version2 = new Version("1.0.2.0_release_20170201");
		Version version3 = new Version("1.0.2.1_release_20170203");
		
		Version version4 = new Version("1.0.3_release_20170203");
		Version version5 = new Version("1.0.2_release_20170203");
		Version version6 = new Version("1.0.2_release_20170204");
		Version version7 = new Version("1.0.2.1.1_release_20170203");
		List<Version> list = new ArrayList<Version>();
		list.add(version4);   
		list.add(version3);
		list.add(version1);
		list.add(version2);
		list.add(version5);
		list.add(version6);
		list.add(version7);
	
		
		
		Collections.sort(list); 
		for(Version verion:list){
			System.out.println(verion.toString());
		}
		
//		String str1 = "1.0.1.2.1211";
//		String str2 = "1.0.1.1";
//		
//		int length = Math.min(str1.length(), str2.length());
//		
//		str1 = str1.substring(0,length);System.out.println(str1);
//		str2 = str2.substring(0, length);
//		System.out.println(str2.compareTo(str1));
		
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		String strDate = "2012-3-1";
//		String strDate2 = "2012-02-22";
//		//String dd = "20170712";
////		SimpleDateFormat dateF 
//		Date date = sdf.parse(strDate);
//		Date date1 = sdf.parse(strDate2);
//		System.out.println(date.after(date1));
//		System.out.println(date.before(date1));
//		System.out.println(date.compareTo(date1));//���0 С-1 ��1
	}
	
	
}

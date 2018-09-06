package com.example.duitang.model;

import java.util.ArrayList;


public class CategoryDetail {
	public int status;
	public Data data;
	public class Data {
		public String name;
		public ArrayList<Sub_Cates> sub_cates;
		
	}
	public class Sub_Cates {
		public String name;
		public String theme_name;
	}
}

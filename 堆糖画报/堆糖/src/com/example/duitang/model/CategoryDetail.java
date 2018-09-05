package com.example.duitang.model;

import java.util.ArrayList;


public class CategoryDetail {
	public int status;
	public Data data;
	public class Data {
		public int more;
		public ArrayList<Sub_Cates> sub_cates;
		public long total;	
	}
	public class Sub_Cates {
		public String name;
		public String path;
	}
}

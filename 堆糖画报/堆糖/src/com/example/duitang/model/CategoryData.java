package com.example.duitang.model;

import java.util.ArrayList;

public class CategoryData {
	public int status;
	public ArrayList<Datas> data;
	public class Datas {
		public String group_name;
		public ArrayList<Group_Items> group_items;	
	}
	public class Group_Items {
		public String name;
		public String target;
	}
}

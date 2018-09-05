package com.example.duitang.model;

import java.util.ArrayList;

public class BannerDetailData {
	public Data data;
	public class Data {
		public String name;
		public String count;
		public String like_count;
		public User user;
		public ArrayList<?> covers;
	}
	public class User{
    	public String avatar;
    	public String id;
    	public String username;
	}
}

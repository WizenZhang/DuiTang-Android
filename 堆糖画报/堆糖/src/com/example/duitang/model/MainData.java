package com.example.duitang.model;

import java.util.ArrayList;

import com.example.duitang.model.BannerData.BannerDatas;

/**
 * 主页面下部瀑布流接口
 * @author Wizen
 *
 */
public class MainData {

	public int status;
	public Data data;
	
	public class Data {
		public ArrayList<ObjectList> object_list;
	}
	
	public class ObjectList {
		public Photo photo;
		public Sender sender;
		public Album album;
		public String msg;
    	public String id;
    	public String reply_count;
    	public String like_count;
    	public String favorite_count;
	}
	public class Photo {
		public String path;

		@Override
		public String toString() {
			return "Photo [path=" + path + "]";
		}
		
	}
    public class Sender {
    	public String avatar;
    	public String username;
	}
    public class Album {
    	public String name;
	}
	
}

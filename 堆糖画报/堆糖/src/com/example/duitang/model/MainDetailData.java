package com.example.duitang.model;

import java.util.LinkedList;

import com.example.duitang.model.MainData.Album;
import com.example.duitang.model.MainData.Data;
import com.example.duitang.model.MainData.ObjectList;
import com.example.duitang.model.MainData.Photo;
import com.example.duitang.model.MainData.Sender;

/**
 * 主页面下部瀑布流详情接口
 * @author Wizen
 *
 */
public class MainDetailData {
	public int status;
	public Data data;
	
	public class Data {
		public Photo photo;
		public Sender sender;
		public Album album;
		public String msg;
    	public String id;
    	public String add_datetime_pretty;
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

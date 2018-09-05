package com.example.duitang.model;

import java.util.ArrayList;

/**
 * ��ҳ���²��ٲ�������ӿ�
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
    	public ArrayList<LikeUser> top_like_users;
    	public ArrayList<Related> related_albums;
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
    	public String id;
    	public String like_count;
    	public String count;
    	public ArrayList<?> covers;
	}
    public class LikeUser{
    	public String avatar;
	}
    public class Related{
    	public String id;
    	public String count;
    	public String like_count;
    	public User user;
    	public String name;
    	public ArrayList<?> covers;
	}
    public class Cover{
    	
	}
    public class User{
    	public String avatar;
    	public String id;
    	public String username;
	}
}

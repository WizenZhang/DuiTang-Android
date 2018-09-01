package com.example.duitang.model;

import java.util.ArrayList;

/**
 * Ê×Ò³Í¼Æ¬ÂÖ²¥Êý¾Ý
 * @author Wizen
 *
 */
public class BannerData {
	public int status;
	public ArrayList<BannerDatas> data;
	
	public class BannerDatas {
    	public String description;
    	public String image_url;
    	public String target;
    	public String enabled_at_str;
    	
		@Override
		public String toString() {
			return "BannerDatas [description=" + description + "]";
		}
    	
    }
}

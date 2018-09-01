package com.example.duitang.model;

import java.util.ArrayList;

/**
 * 主页上部图片翻转接口
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

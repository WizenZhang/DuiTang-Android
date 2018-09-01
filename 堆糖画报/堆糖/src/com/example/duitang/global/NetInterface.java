package com.example.duitang.global;

/**
 * 网络接口
 * @author Wizen
 *
 */
public class NetInterface {
	//第一：主页部分接口：
	//1.1主页上部图片翻转接口
	public static final String BANNER ="http://www.duitang.com/napi/ad/banner/week/?platform_version=4.1.2&device_platform=8295&screen_width=540&screen_height=960&app_version=57&platform_name=Android&locale=zh&adid=ANA001&app_code=nayutas";
	//1.2主页面下部瀑布流接口
	public static final String MAIN = "http://www.duitang.com/napi/index/hot/?include_fields=sender%2Calbum%2Cicon_url%2Creply_count%2Clike_count&platform_version=4.1.2&device_platform=8295&screen_width=540&screen_height=960&start=0&app_version=57&platform_name=Android&locale=zh&app_code=nayutas";
	//主页面下部瀑布流详情接口
	public static final String MAINDETAIL = "http://www.duitang.com/napi/blog/detail/?include_fields=share_links_2Ctags%%2Crelated_albums%%2Crelated_albums.covers%%2Ctop_like_users%%2Ctop_comments&blog_id=%@";
}

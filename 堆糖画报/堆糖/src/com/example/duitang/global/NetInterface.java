package com.example.duitang.global;

/**
 * ����ӿ�
 * @author Wizen
 *
 */
public class NetInterface {
	//��һ����ҳ���ֽӿڣ�
	//1.1��ҳ�ϲ�ͼƬ��ת�ӿ�
	public static final String BANNER ="http://www.duitang.com/napi/ad/banner/week/?platform_version=4.1.2&device_platform=8295&screen_width=540&screen_height=960&app_version=57&platform_name=Android&locale=zh&adid=ANA001&app_code=nayutas";
	//1.2��ҳ���²��ٲ����ӿ�
	public static final String MAIN = "http://www.duitang.com/napi/index/hot/?start=";
	//��ҳ���²��ٲ�������ӿ�
	public static final String MAINDETAIL = "http://www.duitang.com/napi/blog/detail/?include_fields=share_links_2%2Ctags%2Crelated_albums%2Crelated_albums.covers%2Ctop_like_users%2Ctop_comments%2Cextra_html&platform_version=4.1.2&device_platform=8295&__dtac=%257B%2522_r%2522%253A%2520%2522297074%2522%257D&screen_width=540&screen_height=960&app_version=57&platform_name=Android&locale=zh&blog_id=";
	//�ϲ���ת����ҳ���ϲ��ӿ�
	public static final String BANNERDETAILUP= "http://www.duitang.com/napi/album/detail/?album_id=";
	//�ϲ���ת����ҳ���²��ӿ�
	public static final String BANNERDETAILDOWN = "http://www.duitang.com/napi/blog/list/by_album/?include_fields=sender%2Calbum%2Cicon_url%2Creply_count%2Clike_count%2Ctop_comments%2Ctop_like_users&platform_version=4.1.2&device_platform=8295&screen_width=540&screen_height=960&start=";
	//����ҳ��ӿ�
	public static final String CATEGORY = "http://www.duitang.com/napi/index/groups/?platform_version=4.1.2&device_platform=8295&screen_width=540&screen_height=960&app_version=57&platform_name=Android&locale=zh&app_code=nayutas";
   
}

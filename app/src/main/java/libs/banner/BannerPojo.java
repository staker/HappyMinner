package libs.banner;

/**
    首页导航对应的图片实体类
 */
public class BannerPojo {

    public int id;
    public String pageUrl; //点击进去的页面地址
    public String imgUrl;//图片地址
    public int type;//对应的类型， 跳转的时候用得到
    public String titleName;
    public String status;//on打开   offer 关闭
    public String url;//如果这个url存在用就这个

}

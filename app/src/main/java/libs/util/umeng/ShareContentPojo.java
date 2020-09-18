package libs.util.umeng;

import com.ailink.db.Configuration;
import com.ailink.db.MyApplication;
import com.ailink.pojo.UserPojo;

public class ShareContentPojo {
    public String title;
    public String content;
    private int random=0;
    public ShareContentPojo(int random){
        this.random=random;
    }

    public String getTitle(){
        return  titles[random];
    }
    public String getContent(){
        UserPojo userPojo= Configuration.getInstance(MyApplication.getContext()).getUserPojo();
        if(userPojo==null){
            return contents[random];
        }
        return contents[random];
//        return userPojo.userName+": "+contents[random];
    }



    private String[] titles = {"开心矿工，开心赚钱！","开心矿工，全新玩法！", "开心做矿主，大手买矿工！", "开心矿工，财大气粗！",
            "开心矿工，邀请得好礼！", "一款神奇的社交游戏", "独家揭秘网络赚钱", "什么挖矿最好玩", "江湖险恶，你敢来吗？",
            "区块链时代，什么最重要"};
    private String[] contents = {"购买他人为你不停挖矿，赚钱多多！","社交挖矿，交友赚钱两不误！", "六度人脉，好友女神任你买！",
            "做最有钱的矿主，买最漂亮的矿工！", "六度人脉好友邀请机制，奖励领不停！", "快来看，你的好友里，身价最高的居然是他！", "为什么他赚这么多？", "能赚钱，能交友，还能...", "我被这个大魔头抢走了，快来拯救我！",
            "是共识！这是我的区块链身价，能得到你的共识吗？"};
}

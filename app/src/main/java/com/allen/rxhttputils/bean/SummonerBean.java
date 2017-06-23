package com.allen.rxhttputils.bean;

import com.allen.library.base.BaseResponse;

import java.util.List;

/**
 * Created by allen on 2017/6/23.
 */

public class SummonerBean extends BaseResponse {

    /**
     * count : 15
     * updated : 2016-01-19 00:00:00
     * data : [{"en_name":"SummonerBoost","name":"净化","img":"/static/lol_imgs/imgs/summoner_6_1_1/SummonerBoost.png","desp":"移除身上的所有限制效果和召唤师技能的减益效果，并且若在接下来的3秒里再次被施加限制效果时，新效果的持续时间会减少65%。","rank":"1","demo_img":"/static/lol_imgs/imgs/full_summoner_6_1_1/SummonerBoost.png"},{"en_name":"SummonerSmite","name":"惩戒","img":"/static/lol_imgs/imgs/summoner_6_1_1/SummonerSmite.png","desp":"对目标史诗野怪、大型野怪或敌方小兵造成390-1000（取决于英雄等级）点真实伤害。","rank":"1","demo_img":"/static/lol_imgs/imgs/full_summoner_6_1_1/SummonerSmite.png"},{"en_name":"SummonerTeleport","name":"传送","img":"/static/lol_imgs/imgs/summoner_6_1_1/SummonerTeleport.png","desp":"在引导3.5秒后，将英雄传送到友方建筑物、小兵或守卫旁边。","rank":"1","demo_img":"/static/lol_imgs/imgs/full_summoner_6_1_1/SummonerTeleport.png"},{"en_name":"SummonerMana","name":"清晰术","img":"/static/lol_imgs/imgs/summoner_6_1_1/SummonerMana.png","desp":"为你的英雄和周围的友军回复40%的最大法力值。","rank":"1","demo_img":"/static/lol_imgs/imgs/full_summoner_6_1_1/SummonerMana.png"},{"en_name":"SummonerDot","name":"引燃","img":"/static/lol_imgs/imgs/summoner_6_1_1/SummonerDot.png","desp":"引燃是对单体敌方目标施放的持续性伤害技能，在5秒的持续时间里造成70-410（取决于英雄等级）真实伤害，获得目标的视野，并减少目标所受的治疗和回复效果。","rank":"1","demo_img":"/static/lol_imgs/imgs/full_summoner_6_1_1/SummonerDot.png"},{"en_name":"SummonerOdinGarrison","name":"卫戍部队","img":"/static/lol_imgs/imgs/summoner_6_1_1/SummonerOdinGarrison.png","desp":"我方防御塔：回复速度得到巨幅提高，持续8秒。敌方防御塔：减少80%的攻击力，持续8秒。","rank":"1","demo_img":"/static/lol_imgs/imgs/full_summoner_6_1_1/SummonerOdinGarrison.png"},{"en_name":"SummonerClairvoyance","name":"洞察","img":"/static/lol_imgs/imgs/summoner_6_1_1/SummonerClairvoyance.png","desp":"将地图上任意一块区域暴露给你的队伍，持续5秒。","rank":"1","demo_img":"/static/lol_imgs/imgs/full_summoner_6_1_1/SummonerClairvoyance.png"},{"en_name":"SummonerBarrier","name":"屏障","img":"/static/lol_imgs/imgs/summoner_6_1_1/SummonerBarrier.png","desp":"为你的英雄套上护盾，吸收115-455（取决于英雄等级）点伤害，持续2秒。","rank":"1","demo_img":"/static/lol_imgs/imgs/full_summoner_6_1_1/SummonerBarrier.png"},{"en_name":"SummonerExhaust","name":"虚弱","img":"/static/lol_imgs/imgs/summoner_6_1_1/SummonerExhaust.png","desp":"虚弱目标敌方英雄，降低目标英雄30%的移动速度和攻击速度，以及10护甲与魔法抗性，并且他们所造成的伤害减少40%，持续2.5秒。","rank":"1","demo_img":"/static/lol_imgs/imgs/full_summoner_6_1_1/SummonerExhaust.png"},{"en_name":"SummonerPoroRecall","name":"护驾！","img":"/static/lol_imgs/imgs/summoner_6_1_1/SummonerPoroRecall.png","desp":"快速位移到魄罗之王旁边。","rank":"1","demo_img":"/static/lol_imgs/imgs/full_summoner_6_1_1/SummonerPoroRecall.png"},{"en_name":"SummonerPoroThrow","name":"魄罗投掷","img":"/static/lol_imgs/imgs/summoner_6_1_1/SummonerPoroThrow.png","desp":"把一个魄罗投向你的敌人。如果它命中了一名敌人，那么你接下来就可以快速位移到被命中的敌人旁边。","rank":"1","demo_img":"/static/lol_imgs/imgs/full_summoner_6_1_1/SummonerPoroThrow.png"},{"en_name":"SummonerSnowball","name":"标记","img":"/static/lol_imgs/imgs/summoner_6_1_1/SummonerSnowball.png","desp":"沿直线扔出一个雪球。如果雪球命中了一个敌人，那么这个敌人会被【标记】，并且你的英雄接下来可以快速突进到被【标记】的目标旁边。","rank":"1","demo_img":"/static/lol_imgs/imgs/full_summoner_6_1_1/SummonerSnowball.png"},{"en_name":"SummonerFlash","name":"闪现","img":"/static/lol_imgs/imgs/summoner_6_1_1/SummonerFlash.png","desp":"使英雄朝着你的指针所停的区域瞬间传送一小段距离。","rank":"1","demo_img":"/static/lol_imgs/imgs/full_summoner_6_1_1/SummonerFlash.png"},{"en_name":"SummonerHaste","name":"幽灵疾步","img":"/static/lol_imgs/imgs/summoner_6_1_1/SummonerHaste.png","desp":"你的英雄在移动时会无视单位的碰撞体积，移动速度增加27%，持续10秒。","rank":"1","demo_img":"/static/lol_imgs/imgs/full_summoner_6_1_1/SummonerHaste.png"},{"en_name":"SummonerHeal","name":"治疗术","img":"/static/lol_imgs/imgs/summoner_6_1_1/SummonerHeal.png","desp":"为你和目标友军英雄回复95-345（取决于英雄等级）生命值，并为你和目标友军英雄提供30%移动速度加成，持续1秒。若目标近期已受到过其它治疗术的影响，则治疗术对目标产生的治疗效果减半。","rank":"1","demo_img":"/static/lol_imgs/imgs/full_summoner_6_1_1/SummonerHeal.png"}]
     */

    private int count;
    private String updated;
    private List<DataBean> data;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getUpdated() {
        return updated;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * en_name : SummonerBoost
         * name : 净化
         * img : /static/lol_imgs/imgs/summoner_6_1_1/SummonerBoost.png
         * desp : 移除身上的所有限制效果和召唤师技能的减益效果，并且若在接下来的3秒里再次被施加限制效果时，新效果的持续时间会减少65%。
         * rank : 1
         * demo_img : /static/lol_imgs/imgs/full_summoner_6_1_1/SummonerBoost.png
         */

        private String en_name;
        private String name;
        private String img;
        private String desp;
        private String rank;
        private String demo_img;

        public String getEn_name() {
            return en_name;
        }

        public void setEn_name(String en_name) {
            this.en_name = en_name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }

        public String getDesp() {
            return desp;
        }

        public void setDesp(String desp) {
            this.desp = desp;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getDemo_img() {
            return demo_img;
        }

        public void setDemo_img(String demo_img) {
            this.demo_img = demo_img;
        }
    }
}

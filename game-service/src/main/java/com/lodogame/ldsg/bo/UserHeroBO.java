package com.lodogame.ldsg.bo;

import java.io.Serializable;
import java.util.List;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;
import com.lodogame.ldsg.helper.HeroHelper;
import com.lodogame.model.SystemHeroUpgradeAddProp;

@Compress
public class UserHeroBO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户武将ID
	 */
	@Mapper(name = "uhid")
	private String userHeroId;

	/**
	 * 用户ID
	 */
	private String userId;

	/**
	 * 武将人物ID
	 */
	private int heroId;

	/**
	 * 系统武将ID
	 */
	@Mapper(name = "shid")
	private int systemHeroId;

	/**
	 * 生命
	 */
	@Mapper(name = "hp")
	private int life;

	@Mapper(name = "rage")
	private int rage;

	@Mapper(name = "mhp")
	private int maxLife;
	/**
	 * 物攻
	 */
	@Mapper(name = "pha")
	private int physicalAttack;

	/**
	 * 物防
	 */
	@Mapper(name = "phd")
	private int physicalDefense;

	@Mapper(name = "sl")
	private int starLevel;

	@Mapper(name = "se")
	private int starExp;

	@Mapper(name = "sna")
	private int starNextAtt;

	@Mapper(name = "snd")
	private int starNextDef;

	@Mapper(name = "snp")
	private int starNextPower;

	@Mapper(name = "star")
	private int star;

	public int getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}

	public int getRage() {
		return rage;
	}

	public void setRage(int rage) {
		this.rage = rage;
	}

	public int getStarLevel() {
		return starLevel;
	}

	public void setStarLevel(int starLevel) {
		this.starLevel = starLevel;
	}

	public int getStarExp() {
		return starExp;
	}

	public void setStarExp(int starExp) {
		this.starExp = starExp;
	}

	public int getStarNextAtt() {
		return starNextAtt;
	}

	public void setStarNextAtt(int starNextAtt) {
		this.starNextAtt = starNextAtt;
	}

	public int getStarNextDef() {
		return starNextDef;
	}

	public void setStarNextDef(int starNextDef) {
		this.starNextDef = starNextDef;
	}

	public int getStarNextPower() {
		return starNextPower;
	}

	public void setStarNextPower(int starNextPower) {
		this.starNextPower = starNextPower;
	}

	/**
	 * 主动技能
	 */
	private int plan;

	/**
	 * 普通攻击
	 */
	private int normalPlan;

	/**
	 * 天赋技能1
	 */
	private int skill1;

	/**
	 * 天赋技能2
	 */
	private int skill2;

	/**
	 * 天赋技能3
	 */
	private int skill3;

	/**
	 * 天赋技能4
	 */
	private int skill4;

	/**
	 * 天赋技能4
	 */
	private int skill5;

	/**
	 * 格挡
	 */
	@Mapper(name = "pry")
	private int parry;

	/**
	 * 暴击
	 */
	@Mapper(name = "cri")
	private int crit;

	/**
	 * 闪避
	 */
	@Mapper(name = "dk")
	private int dodge;

	/**
	 * 命中
	 */
	@Mapper(name = "hit")
	private int hit;

	/**
	 * 韧性
	 */
	@Mapper(name = "thgs")
	private int toughness;

	/**
	 * 破击
	 */
	@Mapper(name = "bgy")
	private int bogey;

	/**
	 * 头像ID
	 */
	@Mapper(name = "img")
	private int imgId;

	/**
	 * 卡牌ID
	 */
	@Mapper(name = "cid")
	private int cardId;

	/**
	 * 武将名字
	 */
	@Mapper(name = "hn")
	private String name;

	/**
	 * 武将站位
	 */
	@Mapper(name = "pos")
	private int pos;

	/**
	 * 武将等级
	 */
	@Mapper(name = "lv")
	private int heroLevel;

	/**
	 * 武将经验
	 */
	@Mapper(name = "exp")
	private int heroExp;

	/**
	 * 武将出售获得的银币
	 */
	@Mapper(name = "sco")
	private int sellCopper;

	/**
	 * 武将出售获得的江山领
	 */
	@Mapper(name = "sjso")
	private int sellJiangshanOrder;

	/**
	 * 武将职业
	 */
	private int career;

	/**
	 * 武将出售获得的功勋(武魂)
	 */
	@Mapper(name = "sexp")
	private int sellExploits;

	/**
	 * 武将卡牌的卡套
	 */
	@Mapper(name = "ol")
	private int outLine;

	/**
	 * 血祭等级
	 */
	@Mapper(name = "stage")
	private int stage;

	/**
	 * 血祭技能
	 */
	@Mapper(name = "bssid")
	private int bloodSacrificeSkillID;

	/**
	 * 武将锁定状态
	 */
	@Mapper(name = "ls")
	private int lockStatus;

	/**
	 * 专属装备技能ID
	 */
	@Mapper(name = "sskid")
	private int suitSkillId;

	/**
	 * 化神节点等级
	 */
	@Mapper(name = "sdfid")
	private int deifyNodeLevel;

	@Mapper(name = "un")
	private String upgradeNode;

	// 攻击增加的百分比
	private double physicalAttackRatio;

	// 防御增加的百分比
	private double physicalDefenseRatio;

	// 生命增加的百分比
	private double lifeRatio;

	/**
	 * 升星获得的经验
	 */
	@Mapper(name = "stpi")
	private int starPoint;

	/**
	 * 达到这个等级后才可以进阶
	 */
	@Mapper(name = "ul")
	private int upgradeLevel;

	/**
	 * 是否可以进阶，0不可以，1可以
	 */
	@Mapper(name = "cu")
	private int canUpgrade;

	public int getToughness() {
		return toughness;
	}

	public void setToughness(int toughness) {
		this.toughness = toughness;
	}

	public int getBogey() {
		return bogey;
	}

	public void setBogey(int bogey) {
		this.bogey = bogey;
	}

	public String getUpgradeNode() {
		return upgradeNode;
	}

	public void setUpgradeNode(String upgradeNode) {
		this.upgradeNode = upgradeNode;
	}

	public int getHeroId() {
		return heroId;
	}

	public void setHeroId(int heroId) {
		this.heroId = heroId;
	}

	public String getUserHeroId() {
		return userHeroId;
	}

	public void setUserHeroId(String userHeroId) {
		this.userHeroId = userHeroId;
	}

	public int getSystemHeroId() {
		return systemHeroId;
	}

	public void setSystemHeroId(int systemHeroId) {
		this.systemHeroId = systemHeroId;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	public int getPhysicalAttack() {
		return physicalAttack;
	}

	public void setPhysicalAttack(int physicalAttack) {
		this.physicalAttack = physicalAttack;
	}

	public int getPhysicalDefense() {
		return physicalDefense;
	}

	public void setPhysicalDefense(int physicalDefense) {
		this.physicalDefense = physicalDefense;
	}

	public int getPlan() {
		return plan;
	}

	public void setPlan(int plan) {
		this.plan = plan;
	}

	public int getSkill1() {
		return skill1;
	}

	public void setSkill1(int skill1) {
		this.skill1 = skill1;
	}

	public int getSkill2() {
		return skill2;
	}

	public void setSkill2(int skill2) {
		this.skill2 = skill2;
	}

	public int getSkill3() {
		return skill3;
	}

	public void setSkill3(int skill3) {
		this.skill3 = skill3;
	}

	public int getSkill4() {
		return skill4;
	}

	public void setSkill4(int skill4) {
		this.skill4 = skill4;
	}

	public int getSkill5() {
		return skill5;
	}

	public void setSkill5(int skill5) {
		this.skill5 = skill5;
	}

	public int getParry() {
		return parry;
	}

	public void setParry(int parry) {
		this.parry = parry;
	}

	public int getCrit() {
		return crit;
	}

	public void setCrit(int crit) {
		this.crit = crit;
	}

	public int getDodge() {
		return dodge;
	}

	public void setDodge(int dodge) {
		this.dodge = dodge;
	}

	public int getHit() {
		return hit;
	}

	public void setHit(int hit) {
		this.hit = hit;
	}

	public int getImgId() {
		return imgId;
	}

	public void setImgId(int imgId) {
		this.imgId = imgId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getHeroLevel() {
		return heroLevel;
	}

	public void setHeroLevel(int heroLevel) {
		this.heroLevel = heroLevel;
	}

	public int getHeroExp() {
		return heroExp;
	}

	public void setHeroExp(int heroExp) {
		this.heroExp = heroExp;
	}

	public int getCardId() {
		return cardId;
	}

	public void setCardId(int cardId) {
		this.cardId = cardId;
	}

	public int getCareer() {
		return career;
	}

	public void setCareer(int career) {
		this.career = career;
	}

	public int getNormalPlan() {
		return normalPlan;
	}

	public void setNormalPlan(int normalPlan) {
		this.normalPlan = normalPlan;
	}

	public int getOutLine() {
		return outLine;
	}

	public void setOutLine(int outLine) {
		this.outLine = outLine;
	}

	public int getStage() {
		return stage;
	}

	public void setStage(int stage) {
		this.stage = stage;
	}

	public int getBloodSacrificeSkillID() {
		return bloodSacrificeSkillID;
	}

	public void setBloodSacrificeSkillID(int bloodSacrificeSkillID) {
		this.bloodSacrificeSkillID = bloodSacrificeSkillID;
	}

	public int getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(int lockStatus) {
		this.lockStatus = lockStatus;
	}

	public int getSuitSkillId() {
		return suitSkillId;
	}

	public void setSuitSkillId(int suitSkillId) {
		this.suitSkillId = suitSkillId;
	}

	public int getDeifyNodeLevel() {
		return deifyNodeLevel;
	}

	public void setDeifyNodeLevel(int deifyNodeLevel) {
		this.deifyNodeLevel = deifyNodeLevel;
	}

	public String getUserId() {
		return userId;
	}

	public double getPhysicalAttackRatio() {
		return physicalAttackRatio;
	}

	public void setPhysicalAttackRatio(double physicalAttackRatio) {
		this.physicalAttackRatio = physicalAttackRatio;
	}

	public double getPhysicalDefenseRatio() {
		return physicalDefenseRatio;
	}

	public void setPhysicalDefenseRatio(double physicalDefenseRatio) {
		this.physicalDefenseRatio = physicalDefenseRatio;
	}

	public double getLifeRatio() {
		return lifeRatio;
	}

	public void setLifeRatio(double lifeRatio) {
		this.lifeRatio = lifeRatio;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void addUpgradeProp(SystemHeroUpgradeAddProp prop) {
		this.life = this.life + prop.getLife();
		this.physicalAttack = this.physicalAttack + prop.getAtt();
		this.physicalDefense = this.physicalDefense + prop.getDef();
		this.crit = this.crit + prop.getCrit();
		this.parry = this.parry + prop.getParry();
		this.hit = this.hit + prop.getHit();
		this.dodge = this.dodge + prop.getDodge();
		this.toughness = this.toughness + prop.getToughness();
		this.bogey = this.bogey + prop.getBogey();
	}

	public void addMeridian(int[] val) {
		physicalAttack += val[0];
		physicalDefense += val[1];
		life += val[2];
		hit += val[3];
		toughness += val[4];
		parry += val[5];
		bogey += val[6];
		dodge += val[7];
		crit += val[8];
	}

	public void addEqui(int[] val) {
		physicalAttack += val[0];
		physicalDefense += val[1];
		life += val[2];
		hit += val[3];
		dodge += val[4];
		bogey += val[5];
		parry += val[6];
		crit += val[7];
		toughness += val[8];
	}

	public void addRefineEqui(List<Property> list) {
		for (Property p : list) {
			switch (p.getType()) {
			case 1:
				physicalAttack += p.getValue();
				break;
			case 2:
				physicalDefense += p.getValue();
				break;
			case 3:
				life += p.getValue();
				break;
			case 4:
				hit += p.getValue();
				break;
			case 5:
				dodge += p.getValue();
				break;
			case 6:
				bogey += p.getValue();
				break;
			case 7:
				parry += p.getValue();
				break;
			case 8:
				crit += p.getValue();
				break;
			case 9:
				toughness += p.getValue();
				break;
			default:
				break;
			}
		}
	}
	public void addEnchantEqui(List<EnchantProty> list) {
		for (EnchantProty p : list) {
			switch (p.getType()) {
			case 0:
				physicalAttack += p.getValue();
				break;
			case 1:
				physicalDefense += p.getValue();
				break;
			case 2:
				life += p.getValue();
				break;
			case 3:
				hit += p.getValue();
				break;
			case 4:
				dodge += p.getValue();
				break;
			case 5:
				bogey += p.getValue();
				break;
			case 6:
				parry += p.getValue();
				break;
			case 7:
				crit += p.getValue();
				break;
			case 8:
				toughness += p.getValue();
				break;
			default:
				break;
			}
		}
	}
	public void addGroupSkillProerty(int type,int value) {
			
			switch (type) {
			case 1:
				physicalAttack += value*physicalAttack/1000;
				break;
			case 2:
				physicalDefense += value*physicalDefense/1000;
				break;
			case 3:
				life += value*life/1000;
				break;
			case 4:
				hit += value*hit/1000;
				break;
			case 5:
				dodge += value*dodge/1000;
				break;
			case 6:
				bogey += value*bogey/1000;
				break;
			case 7:
				parry += value*parry/1000;
				break;
			case 8:
				crit += value*crit/1000;
				break;
			case 9:
				toughness += value*toughness/1000;
				break;
			case 11:
				physicalAttack += value;
				break;
			case 12:
				physicalDefense += value;
				break;
			case 13:
				life += value;
				break;
			case 14:
				hit += value;
				break;
			case 15:
				dodge += value;
				break;
			case 16:
				bogey += value;
				break;
			case 17:
				parry += value;
				break;
			case 18:
				crit += value;
				break;
			case 19:
				toughness += value;
				break;
			default:
				break;
			}
		
	}
	public int getUpgradeLevel() {
		return upgradeLevel;
	}

	public void setUpgradeLevel(int upgradeLevel) {
		this.upgradeLevel = upgradeLevel;
	}

	public int getCanUpgrade() {
		return canUpgrade;
	}

	public void setCanUpgrade(int canUpgrade) {
		this.canUpgrade = canUpgrade;
	}

	public int getSellCopper() {
		return sellCopper;
	}

	public void setSellCopper(int sellCopper) {
		this.sellCopper = sellCopper;
	}

	public int getSellExploits() {
		return sellExploits;
	}

	public void setSellExploits(int sellExploits) {
		this.sellExploits = sellExploits;
	}

	public int getSellJiangshanOrder() {
		return sellJiangshanOrder;
	}

	public void setSellJiangshanOrder(int sellJiangshanOrder) {
		this.sellJiangshanOrder = sellJiangshanOrder;
	}

	public int getStar() {
		int st = HeroHelper.getHeroStar(this.starLevel);
		if (st > 0) {
			return st;
		}
		return this.star;
	}

	public int getStarPoint() {
		return starPoint;
	}

	public void setStarPoint(int starPoint) {
		this.starPoint = starPoint;
	}

	public void setStar(int star) {
		this.star = star;
	}

}

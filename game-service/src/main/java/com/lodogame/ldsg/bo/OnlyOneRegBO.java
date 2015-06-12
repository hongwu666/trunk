package com.lodogame.ldsg.bo;

import com.lodogame.ldsg.annotation.Compress;
import com.lodogame.ldsg.annotation.Mapper;

@Compress
public class OnlyOneRegBO {

	/**
	 * 总的比赛场次
	 */
	@Mapper(name = "ttc")
	private int totalCount;

	/**
	 * 0. 可以报名 1. 不在报时间段 2. 已经在队列中
	 */
	@Mapper(name = "st")
	private int status;

	/**
	 * 当前积分
	 */
	@Mapper(name = "pot")
	private int point;

	/**
	 * 当前精力
	 */
	@Mapper(name = "vig")
	private int vigour;

	/**
	 * 当前排名
	 */
	@Mapper(name = "rank")
	private int rank;

	/**
	 * 精力恢复时间倒数
	 */
	@Mapper(name = "vigas")
	private int vigourAddSecond;

	@Mapper(name = "txt1")
	private String text1;

	@Mapper(name = "txt2")
	private String text2;

	@Mapper(name = "txt3")
	private String text3;

	@Mapper(name = "txt4")
	private String text4;

	@Mapper(name = "txt5")
	private String text5;

	@Mapper(name = "txt6")
	private String text6;

	/**
	 * 轮次
	 */
	@Mapper(name = "round")
	private int round;

	/**
	 * 倍数
	 */
	@Mapper(name = "multi")
	private int multi;

	/**
	 * 下次开始时间
	 */
	@Mapper(name = "nextm")
	private long nextTime;

	/**
	 * 是否有奖励
	 */
	@Mapper(name = "rew")
	private int reward;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public int getVigour() {
		return vigour;
	}

	public void setVigour(int vigour) {
		this.vigour = vigour;
	}

	public int getVigourAddSecond() {
		return vigourAddSecond;
	}

	public void setVigourAddSecond(int vigourAddSecond) {
		this.vigourAddSecond = vigourAddSecond;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getText1() {
		return text1;
	}

	public void setText1(String text1) {
		this.text1 = text1;
	}

	public String getText2() {
		return text2;
	}

	public void setText2(String text2) {
		this.text2 = text2;
	}

	public String getText3() {
		return text3;
	}

	public void setText3(String text3) {
		this.text3 = text3;
	}

	public String getText4() {
		return text4;
	}

	public void setText4(String text4) {
		this.text4 = text4;
	}

	public String getText5() {
		return text5;
	}

	public void setText5(String text5) {
		this.text5 = text5;
	}

	public String getText6() {
		return text6;
	}

	public void setText6(String text6) {
		this.text6 = text6;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getMulti() {
		return multi;
	}

	public void setMulti(int multi) {
		this.multi = multi;
	}

	public long getNextTime() {
		return nextTime;
	}

	public void setNextTime(long nextTime) {
		this.nextTime = nextTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getReward() {
		return reward;
	}

	public void setReward(int reward) {
		this.reward = reward;
	}

}

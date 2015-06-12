package com.ldsg.battle.handle;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ldsg.battle.Context;
import com.ldsg.battle.bo.Buff;
import com.ldsg.battle.bo.Effect;
import com.ldsg.battle.config.EffectConfig;
import com.ldsg.battle.engine.BaseHandle;
import com.ldsg.battle.helper.BattleHeroHelper;
import com.ldsg.battle.helper.BuffHelper;
import com.ldsg.battle.role.Hero;

public class ZhongShiZhiDiHandle extends BaseHandle {

	private static Logger logger = Logger.getLogger(ShareHurtHandle.class);

	public Buff create(Effect effect, Hero hero, Hero target, Context context, int critValue) {
		Buff buff = BuffHelper.Effect2Buff(effect);
		return buff;
	}

	public void handle(Hero hero, Buff buff, Context context) {

		Hero currentBeAttackHero = context.getCurrentBeAttackHero();
		// 不是同阵营，不分担
		if (currentBeAttackHero == null || !BattleHeroHelper.isSameTeamHero(currentBeAttackHero, hero)
				|| StringUtils.equalsIgnoreCase(currentBeAttackHero.getPosition(), hero.getPosition())) {
			logger.debug("不是当前被攻击者,不需要进行伤害分担.当前被攻击武将[" + currentBeAttackHero.getLogName() + "], 当前buff武将[" + hero.getLogName() + "]");
			buff.setReport(false);
			return;
		}

		if (context.isCurrentIsCounter()) {
			logger.debug("反击不需要进行伤害分担");
			buff.setReport(false);
			return;
		}

		// /从上下文中读取当前行动的伤害值
		long currentAttackHurt = context.getCurrentAttackHurt();
		long shareHurt = (long) (currentAttackHurt * buff.getEffect().getParam("a", 1d));

		// /获取掉血伤害的效果
		Effect effectA = EffectConfig.getReduceLifeEffect();
		String funID = effectA.getFunId();

		IBuffHandle handler = BuffHandleFactory.getBuffHandler(funID);

		// /每个分担的武将身上加一个立即触发的掉血buff

		Buff buffA = BuffHelper.Effect2Buff(effectA);
		buffA.setValue(shareHurt);
		buffA.setRound(Buff.ROUND_IMMEDIATELY);
		handler.handle(hero, buffA, context);

		context.setCurrentAttackHurt(0);

	}

	public void handleRemove(Hero hero, Buff buff) {
		// throw new NotImplementedException();
	}

}

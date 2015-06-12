package com.lodogame.ldsg.service.impl;

public class ContestServiceImpl2 {

	// private static final Logger logger =
	// Logger.getLogger(ContestServiceImpl2.class);
	//
	// @Autowired
	// private EventService eventService;
	//
	// private int status = ContestConstant.STATUS_NOT_STAET;
	//
	// private int matchCount = 0;
	//
	// private Date nextStatusTime;
	//
	// // 进入该界面的用户
	// private Map<String, ContestUserBO> userMap = new
	// ConcurrentHashMap<String, ContestUserBO>();
	//
	// // 报名用户
	// private Map<Integer, ContestRegBO> map = new ConcurrentHashMap<Integer,
	// ContestRegBO>();
	//
	// // 战报
	// private Map<String, ContestBattleReport> reportMap = new HashMap<String,
	// ContestBattleReport>();
	//
	// private Map<String, Long> userLockMap = new ConcurrentHashMap<String,
	// Long>();
	//
	// private BlockingQueue<ContestEvent> queue = new
	// ArrayBlockingQueue<ContestEvent>(1024);
	//
	// private Object matchLock = new Object();
	//
	// @Autowired
	// private BattleService battleService;
	//
	// @Autowired
	// private UserService userService;
	//
	// @Autowired
	// private UserDao userDao;
	//
	// @Autowired
	// private HeroService heroService;
	//
	// @Autowired
	// private CommandDao commandDao;
	//
	// @Autowired
	// private SystemContestRewardDao systemContestRewardDao;
	//
	// @Autowired
	// private ContestDao contestDao;
	//
	// @Autowired
	// private MailService mailService;
	//
	// @Autowired
	// private UserMapperDao userMapperDao;
	//
	// @Autowired
	// private UserRecContestRewardLogDao userRecContestRewardLogDao;
	//
	// @Autowired
	// private UserTotalGainLogDao userTotalGainLogDao;
	//
	// @Override
	// public Date getEndCdTime(String userId) {
	//
	// if (userMap.containsKey(userId)) {
	// ContestUserBO contestUserBO = this.userMap.get(userId);
	// return contestUserBO.getEndCoolTime();
	// }
	//
	// return DateUtils.addDays(new Date(), -1);
	// }
	//
	// @Override
	// public void enter(String userId) {
	//
	// if (!userMap.containsKey(userId)) {
	// ContestUserBO user = new ContestUserBO();
	// user.setUserId(userId);
	// user.setEndCoolTime(DateUtils.addDays(new Date(), -1));
	// user.setUserId(userId);
	// user.setActive(true);
	// user.setMaxDeadRound(this.contestDao.getUserMaxDeadRound(userId));
	// userMap.put(userId, user);
	// } else {
	// ContestUserBO user = userMap.get(userId);
	// user.setActive(true);
	// }
	//
	// }
	//
	// @Override
	// public void quit(String userId) {
	//
	// if (userMap.containsKey(userId)) {
	// ContestUserBO user = userMap.get(userId);
	// user.setActive(false);
	// }
	// }
	//
	// @Override
	// public void bet(String userId, int ind, EventHandle handle) {
	//
	// if (!this.isBetTime()) {
	// String message = "擂台赛下注失败,不在下注时间段.userId[" + userId + "]";
	// throw new ServiceException(CONTEST_BET_TIME_ERROR, message);
	// }
	//
	// ContestUserBO contestUserBo = null;
	//
	// if (!userMap.containsKey(userId)) {
	// contestUserBo = new ContestUserBO();
	// contestUserBo.setUserId(userId);
	// contestUserBo.setEndCoolTime(new Date());
	// contestUserBo.setUserId(userId);
	// } else {
	// contestUserBo = userMap.get(userId);
	// if (contestUserBo.getBetInd() > 0) {
	// String message = "擂台赛下注失败，已经下注过.userId[" + userId + "]";
	// throw new ServiceException(CONTEST_BET_HAD_BET, message);
	// }
	// }
	//
	// if (!this.userService.reduceCopper(userId, 1,
	// ToolUseType.REDUCE_CONTEST_BET)) {
	// String message = "擂台赛下注失败，银币不足.userId[" + userId + "]";
	// throw new ServiceException(CONTEST_BET_COPPER_NOT_ENOUGH, message);
	// }
	//
	// userTotalGainLogDao.addUserTotalGain(userId,
	// UserTotalGainLogType.CONTEST_BET, 1);
	//
	// contestUserBo.setBetInd(ind);
	// contestUserBo.setActive(true);
	//
	// userMap.put(userId, contestUserBo);
	// }
	//
	// @Override
	// public boolean reg(final String userId, final EventHandle handle) {
	//
	// if (this.status != ContestConstant.STATUS_REG) {
	// String message = "报名出错，当前不在报名阶段.userId[" + userId + "]";
	// throw new ServiceException(CONTEST_REG_TIME_ERROR, message);
	// }
	//
	// long now = System.currentTimeMillis();
	// if (userLockMap.containsKey(userId)) {
	// long time = userLockMap.get(userId);
	// if (now - time <= 5 * 1000) {// 加锁5秒
	// return false;
	// }
	// }
	// userLockMap.put(userId, now);
	//
	// ContestEvent event = new ContestEvent(userId, handle);
	// this.queue.add(event);
	//
	// return true;
	//
	// }
	//
	// private void debugPrint() {
	//
	// if (Config.ins().isDebug()) {
	//
	// logger.debug("------------------------ round: " +
	// ContestHelper2.getClientRound(status) + "------------------------");
	// for (int i = 0; i <= 31; i++) {
	// if (map.containsKey(i)) {
	// // ContestRegBO bo = map.get(i);
	// // logger.debug("index[" + i + "], deadRound[" +
	// // bo.getDeadRound() + "], username[" + bo.getUsername() +
	// // "]");
	// }
	// }
	// logger.debug("------------------------ end ------------------------");
	// }
	// }
	//
	// /**
	// * 安排赛程
	// */
	// private void scheduling() {
	//
	// List<Integer> indList = new ArrayList<Integer>();
	// for (int i = 0; i <= 31; i++) {
	// indList.add(i);
	// }
	//
	// List<ContestRegBO> list = new ArrayList<ContestRegBO>();
	// for (ContestRegBO bo : this.map.values()) {
	// list.add(bo);
	// }
	//
	// ContestHelper2.scheduling(list, indList);
	//
	// this.map.clear();
	//
	// for (ContestRegBO bo : list) {
	// this.map.put(bo.getIndex(), bo);
	// }
	//
	// for (int i = 0; i < 32; i++) {
	// if (!this.map.containsKey(i)) {
	// ContestRegBO bo = new ContestRegBO();
	// bo.setDeadRound(100);
	// bo.setIndex(i);
	// bo.setUsername("");
	// this.map.put(i, bo);
	// }
	// }
	//
	// }
	//
	// /**
	// * 进行一轮比赛
	// */
	// private void runMatch() {
	//
	// int round = ContestHelper2.getRound(status);
	// if (round <= 0 || round > 5) {
	// logger.debug("错误的比赛轮次.Round[" + round + "]");
	// return;
	// }
	//
	// // 获取比赛赛程
	// Map<String, ContestPairBO> matchSchedule =
	// ContestHelper2.getSchedule(this.map.values(), round);
	//
	// this.matchCount = matchSchedule.size();
	//
	// logger.info("共有比场数[" + this.matchCount + "]");
	//
	// if (this.matchCount > 0) {
	// // 执行赛程比赛
	// this.runSchedule(round, matchSchedule);
	// }
	//
	// }
	//
	// /**
	// * 进行比赛
	// *
	// * @param round
	// * @param matchSchedule
	// */
	// private void runSchedule(int round, Map<String, ContestPairBO>
	// matchSchedule) {
	//
	// // 开始比赛
	// for (Map.Entry<String, ContestPairBO> entry : matchSchedule.entrySet()) {
	//
	// ContestPairBO contestPairBO = entry.getValue();
	//
	// // 进行一场比赛
	// this.runOneSchedule(round, contestPairBO);
	//
	// }
	//
	// }
	//
	// /**
	// * 进行一场比赛
	// *
	// * @param round
	// * @param contestPairBO
	// */
	// private void runOneSchedule(final int round, final ContestPairBO
	// contestPairBO) {
	//
	// // 两支队都有才比赛，不然的话就是轮空
	// if (!contestPairBO.isFullPair()) {
	// synchronized (matchLock) {
	// if (StringUtils.isEmpty(contestPairBO.getUserIdA())) {//
	// 如果没有A队，那么被淘汰的是A队，不然是B队被淘态
	// setDead(contestPairBO.getIndA(), round);
	// } else {
	// setDead(contestPairBO.getIndB(), round);
	// }
	// matchCount -= 1;
	// }
	// return;
	// }
	//
	// final String attackUserID = contestPairBO.getUserIdA();
	// final String defenderUserID = contestPairBO.getUserIdB();
	//
	// this.figth(attackUserID, defenderUserID, 1, new EventHandle() {
	//
	// @Override
	// public boolean handle(Event event) {
	//
	// int flag = event.getInt("flag");
	// String report = event.getString("report");
	// if (flag == 1) {// 进攻方赢
	// setDead(contestPairBO.getIndB(), round);
	// } else {
	// setDead(contestPairBO.getIndA(), round);
	// }
	//
	// synchronized (matchLock) {
	// matchCount -= 1;
	// }
	//
	// saveReport(attackUserID, defenderUserID, flag, 2,
	// contestPairBO.getBaseCode(), report);
	//
	// return false;
	// }
	// });
	//
	// }
	//
	// /**
	// * 设置比赛挂了
	// *
	// * @param ind
	// */
	// private void setDead(int ind, int deadRound) {
	//
	// if (!this.map.containsKey(ind)) {
	// return;
	// }
	//
	// ContestRegBO contestRegBO = this.map.get(ind);
	//
	// contestRegBO.setDeadRound(deadRound);
	//
	// }
	//
	// public ContestRegBO getByUserid(String userId) {
	//
	// for (ContestRegBO contestRegBO : this.map.values()) {
	// if (userId.equals(contestRegBO.getUserId())) {
	// return contestRegBO;
	// }
	// }
	//
	// return null;
	//
	// }
	//
	// /**
	// * 战斗
	// *
	// * @param userIdA
	// * @param userIdB
	// * @param type
	// * @param handle
	// */
	// private void figth(String userIdA, String userIdB, int type, EventHandle
	// handle) {
	//
	// BattleBO attack = new BattleBO();
	// // 英雄列表
	// User userA = this.userService.get(userIdA);
	// List<BattleHeroBO> attackBO =
	// this.heroService.getUserBattleHeroBOList(userIdA);
	// attack.setUserLevel(userA.getLevel());
	// attack.setBattleHeroBOList(attackBO);
	//
	// final User defenseUser = userService.get(userIdB);
	// BattleBO defense = new BattleBO();
	// // 英雄列表
	// List<BattleHeroBO> defenseBO =
	// this.heroService.getUserBattleHeroBOList(userIdB);
	// defense.setBattleHeroBOList(defenseBO);
	// defense.setUserLevel(defenseUser.getLevel());
	//
	// battleService.fight(attack, defense, type, handle);
	//
	// }
	//
	// /**
	// * 状态判断
	// */
	// private void statusCheck() {
	//
	// Date now = new Date();
	//
	// if (now.after(this.getNextStatusTime())) {
	//
	// if (ContestHelper2.isMatchStatus(status)) {// 比赛回合的话要等比赛完
	//
	// if (this.matchCount == 0) {
	// switchStatus();
	// }
	//
	// } else {
	// switchStatus();
	// }
	//
	// }
	//
	// }
	//
	// /**
	// * 推送状态(all)
	// */
	// private void pushStatus() {
	//
	// Command command = new Command();
	// command.setCommand(CommandType.COMMAND_CONTEST_STATUS_CHANGED);
	// command.setType(CommandType.PUSH_ALL);
	// command.setParams(new HashMap<String, String>());
	//
	// commandDao.add(command);
	//
	// }
	//
	// /**
	// * 推送位置被强占
	// */
	// private void pushMsg(String userId) {
	//
	// Command command = new Command();
	// command.setCommand(CommandType.COMMAND_CONTEST_POS_OCCUPIED);
	// command.setType(CommandType.PUSH_USER);
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("userId", userId);
	// map.put("tp", "1");
	// map.put("msg", "大侠你的名额被被人占领了，请再加再励！！！");
	// command.setParams(map);
	//
	// commandDao.add(command);
	//
	// }
	//
	// /**
	// * 状态切换
	// */
	// private void switchStatus() {
	//
	// Date now = new Date();
	//
	// if (this.status == ContestConstant.STATUS_MATCH_2) {// 倒数一场比赛
	//
	// this.status = ContestConstant.STATUS_END;
	//
	// this.nextStatusTime = ContestHelper2.getNextDayStartTime();
	//
	// this.end();
	//
	// } else {
	//
	// if (this.status == ContestConstant.STATUS_NOT_STAET) {// 没开始，则开始
	// this.status += 1;
	// this.nextStatusTime = DateUtils.add(now, Calendar.MILLISECOND,
	// ContestHelper2.getNextStatusTime(status));
	// this.start();
	//
	// } else if (this.status == ContestConstant.STATUS_REG) {
	// this.status += 1;
	// this.nextStatusTime = DateUtils.add(now, Calendar.MILLISECOND,
	// ContestHelper2.getNextStatusTime(status));
	// this.scheduling();// 安排赛程
	//
	// } else if (ContestHelper2.isMatchReadyStatus(status)) {// 是比赛准备状态
	// this.status += 1;
	// this.nextStatusTime = DateUtils.add(now, Calendar.MILLISECOND,
	// ContestHelper2.getNextStatusTime(status));
	// this.runMatch();
	//
	// } else if (this.status == ContestConstant.STATUS_END) {
	// this.status = ContestConstant.STATUS_NOT_STAET;
	// this.nextStatusTime = DateUtils.add(now, Calendar.MILLISECOND,
	// ContestHelper2.getNextStatusTime(status));
	// contestDao.insertPkDate(DateUtils.getDate());
	// } else {
	// this.status += 1;
	// this.nextStatusTime = DateUtils.add(now, Calendar.MILLISECOND,
	// ContestHelper2.getNextStatusTime(status));
	// }
	//
	// }
	//
	// if (ContestHelper2.isNeedPushStatus(status)) {
	// pushStatus();
	// }
	//
	// debugPrint();
	// }
	//
	// /**
	// * 开始
	// */
	// private void start() {
	//
	// userMap.clear();
	//
	// map.clear();
	//
	// if (Config.ins().isDebug()) {
	// // test();
	// }
	//
	// }
	//
	// private void test() {
	//
	// List<User> userList = this.userDao.listOrderByLevelDesc(0, 50);
	//
	// for (int i = 0; i < 31; i++) {
	//
	// String userId = userList.get(i).getUserId();
	// String username = userList.get(i).getUsername();
	//
	// this.enter(userId);
	//
	// logger.debug("开始报名.userId[" + userId + "], username[" + username +
	// "], ind[" + i + "]");
	//
	// this.reg(userId, new EventHandle() {
	//
	// @Override
	// public boolean handle(Event event) {
	//
	// return false;
	// }
	// });
	//
	// }
	//
	// }
	//
	// /**
	// * 结束
	// */
	// private void end() {
	//
	// // 发放排名奖励
	// grantRankReward();
	//
	// // 发放下注奖励
	// grantBetReward();
	//
	// // 发放幸运奖励
	// grantLuckReward();
	//
	// }
	//
	// private void grantLuckReward() {
	// List<ContestUserBO> contestUserBOList = new ArrayList<ContestUserBO>();
	// for (ContestUserBO contestUserBO : this.userMap.values()) {
	// if (contestUserBO.getBetInd() > -1) {
	// contestUserBOList.add(contestUserBO);
	// }
	// }
	//
	// if (!contestUserBOList.isEmpty() && contestUserBOList.size() <= 5) {
	// for (ContestUserBO contestUserBO : contestUserBOList) {
	// if (contestUserBO.getUserId() != null) {
	// sendContestReward(3, contestUserBO.getUserId(), 8, null);
	// }
	// }
	// } else if (contestUserBOList.size() > 5) {
	// List<Integer> randList = getFiveRand(contestUserBOList.size());
	// if (randList.size() > 0) {
	// for (int i = 0; i < randList.size(); i++) {
	// if (contestUserBOList.get(randList.get(i)).getUserId() == null) {
	// continue;
	// }
	// sendContestReward(3, contestUserBOList.get(randList.get(i)).getUserId(),
	// 8, null);
	// }
	// }
	//
	// }
	// }
	//
	// private List<Integer> getFiveRand(int size) {
	// List<Integer> intList = new ArrayList<Integer>();
	// Random rand = new Random();
	// boolean[] bool = new boolean[size];
	// int randInt = 0;
	// for (int i = 0; i < 5; i++) {
	// int j = 0;
	// do {
	// j++;
	// randInt = rand.nextInt(size);
	// // 防止死循环
	// if (j > 1000) {
	// return intList;
	// }
	// } while (bool[randInt]);
	// bool[randInt] = true;
	// intList.add(randInt);
	// }
	// return intList;
	// }
	//
	// private void grantBetReward() {
	// int championInd = 0;
	// for (ContestRegBO contestRegBO : this.map.values()) {
	// if (contestRegBO.getDeadRound() == 100) {
	// championInd = contestRegBO.getIndex();
	// }
	// }
	//
	// for (ContestUserBO contestUserBO : this.userMap.values()) {
	// if (contestUserBO.getBetInd() == championInd) {
	// if (contestUserBO.getUserId() == null) {
	// continue;
	// }
	// sendContestReward(2, contestUserBO.getUserId(), 7, null);
	// }
	// }
	// }
	//
	// private void grantRankReward() {
	//
	// for (ContestRegBO contestRegBO : this.map.values()) {
	// if (contestRegBO.getUserId() == null) {
	// continue;
	// }
	// int rewardId =
	// ContestHelper2.getRoundReward(contestRegBO.getDeadRound());
	//
	// sendContestReward(1, contestRegBO.getUserId(), rewardId,
	// ContestHelper2.getRank(contestRegBO.getDeadRound()));
	//
	// // 保留排名
	// this.contestDao.saveMaxDeadRound(contestRegBO.getUserId(),
	// contestRegBO.getDeadRound());
	//
	// ContestRankEvent e = new ContestRankEvent(contestRegBO.getUserId(),
	// ContestHelper2.getContestRank(contestRegBO.getDeadRound()));
	// eventService.dispatchEvent(e);
	//
	// }
	// }
	//
	// private void sendContestReward(int type, String userId, int rewardId,
	// String rank) {
	//
	// SystemContestReward systemContestReward =
	// this.systemContestRewardDao.getSystemContestReward(rewardId);
	//
	// if (systemContestReward == null) {
	// return;
	// }
	//
	// User user = this.userService.get(userId);
	// String toolIds = "";
	// if (systemContestReward.getGoldNum() > 0) {
	// toolIds = ToolType.GOLD + "," + ToolId.TOOL_GLOD_ID + "," +
	// systemContestReward.getGoldNum() + "|";
	// }
	// if (systemContestReward.getCopperNum() > 0) {
	// toolIds += ToolType.COPPER + "," + ToolId.TOOL_COPPER_ID + "," +
	// systemContestReward.getCopperNum() * user.getLevel() + "|";
	// }
	// if (systemContestReward.getToolIds() != null) {
	// toolIds += systemContestReward.getToolIds();
	// }
	//
	// if (type == 1) {// 排名奖励
	// String content;
	// if (StringUtils.equals(rank, "冠军") || StringUtils.equals(rank, "亚军")) {
	// content = "恭喜您在擂台赛中获得" + rank + ",以下为排行奖励，祝您游戏愉快！";
	// } else {
	// content = "恭喜您在擂台赛中打进" + rank + ",以下为排行奖励，祝您游戏愉快！";
	// }
	// mailService.send("擂台赛排名奖励", content, toolIds, MailTarget.USERS,
	// user.getLodoId() + "", null, new Date(), null);
	// } else if (type == 2) {// 下注奖励
	// mailService.send("擂台赛助威奖励", "恭喜您在擂台赛中为总冠军助威助威奖励。以下为助威奖励，祝您游戏愉快！",
	// toolIds, MailTarget.USERS, user.getLodoId() + "", null, new Date(),
	// null);
	// } else if (type == 3) {// 幸运奖励
	// mailService.send("擂台赛幸运玩家奖励",
	// "恭喜您在擂台赛所有助威玩家中幸运的脱颖而出，成为系统随机抽出的五名幸运玩家之一。以下为助威奖励，祝您游戏愉快！", toolIds,
	// MailTarget.USERS, user.getLodoId() + "", null, new Date(), null);
	// }
	//
	// userRecContestRewardLogDao.addUserRecContestRewardLog(new
	// UserRecContestRewardLog(userId, rewardId, new Date()));
	//
	// try {
	// ContestBetEvent e = new ContestBetEvent(userId);
	// eventService.dispatchEvent(e);
	// } catch (Exception e) {
	// logger.error("cheng jiu err" + this.getClass().getName());
	// }
	// }
	//
	// /**
	// * 获取下一阶段开始的时间
	// *
	// * @return
	// */
	// public Date getNextStatusTime() {
	//
	// if (nextStatusTime == null) {
	// if (Config.ins().isDebug()) {
	// nextStatusTime = DateUtils.add(new Date(), Calendar.MINUTE, 0);
	// new Date();
	// } else {
	// nextStatusTime = DateUtils.str2Date(DateUtils.getDate() + " 12:10:00");
	// }
	// }
	//
	// return nextStatusTime;
	// }
	//
	// @Override
	// public int getClientStatus() {
	// return ContestHelper2.getClientStatus(status);
	// }
	//
	// @Override
	// public int getClientRound() {
	// return ContestHelper2.getClientRound(status);
	// }
	//
	// @Override
	// public int getUserInd(String userId) {
	//
	// for (Map.Entry<Integer, ContestRegBO> entry : this.map.entrySet()) {
	//
	// ContestRegBO contestRegBO = entry.getValue();
	// if (StringUtils.equalsIgnoreCase(userId, contestRegBO.getUserId())) {
	// return contestRegBO.getIndex();
	// }
	//
	// }
	//
	// return -1;
	// }
	//
	// @Override
	// public int getUserBetInd(String userId) {
	//
	// if (userMap.containsKey(userId)) {
	// ContestUserBO user = this.userMap.get(userId);
	// return user.getBetInd();
	// }
	//
	// return -1;
	// }
	//
	// @Override
	// public List<ContestRegBO> getContestRegList() {
	//
	// List<ContestRegBO> list = new ArrayList<ContestRegBO>();
	// for (int i = 0; i < 32; i++) {
	//
	// ContestRegBO contestRegBO = null;
	//
	// if (this.map.containsKey(i)) {
	// contestRegBO = map.get(i);
	// } else {
	// contestRegBO = new ContestRegBO();
	// contestRegBO.setIndex(i);
	// }
	//
	// contestRegBO.setGroupId(ContestHelper2.getGroupId(i));
	//
	// list.add(contestRegBO);
	//
	// }
	//
	// return list;
	// }
	//
	// @Override
	// public ContestStatusRet getUserContestStatus(String userId) {
	//
	// ContestStatusRet ret = new ContestStatusRet();
	//
	// if (this.userMap.containsKey(userId)) {
	//
	// ContestUserBO bo = this.userMap.get(userId);
	// ret.setLastBattleStatus(bo.getLastBattleStatus());
	// ret.setLastBattleType(bo.getLastBattleType());
	// ret.setMaxDeadRound(bo.getMaxDeadRound());
	// ret.setRemainRegCount(32 - this.map.size());
	// ret.setRetStatus(bo.isHadReg() ? 2 : 1);
	// ret.setTargetUserId(bo.getTargetUserId());
	// }
	//
	// return ret;
	// }
	//
	// @Override
	// public boolean isBetTime() {
	// return this.status == ContestConstant.STATUS_BET;
	// }
	//
	// @Override
	// public Collection<ContestUserBO> getContestUserList() {
	// return this.userMap.values();
	// }
	//
	// @Override
	// public String getNextStatusName() {
	// return ContestHelper2.getNextStatusName(status);
	// }
	//
	// private Event take() {
	// try {
	// return this.queue.take();
	// } catch (InterruptedException ie) {
	// throw new ServiceException(3000, ie.getMessage());
	// }
	// }
	//
	// /**
	// * 处理请求
	// *
	// * @param event
	// */
	// private void handle(Event event) {
	//
	// final String userId = event.getUserId();
	//
	// int oldInd = this.getUserInd(userId);
	// if (oldInd >= 0) {
	// String message = "报名出错，已经报过名了.userId[" + userId + "], oldInd[" + oldInd +
	// "]";
	// logger.warn(message);
	// return;
	// }
	//
	// int ind = this.getRegInd();
	//
	// // 设置为已经报过名
	// ContestUserBO attackUserBO = userMap.get(userId);
	// if (attackUserBO != null) {
	// attackUserBO.setHadReg(true);
	// }
	// if (this.map.containsKey(ind)) {// 有人就是干
	// // 回调出去
	// ContestEvent contestEvent = (ContestEvent) event;
	// rob(userId, ind, contestEvent.getHandle());
	// } else {// 没人自己干自己
	// reg(userId, ind);
	// }
	//
	// // 删除锁
	// userLockMap.remove(userId);
	//
	// }
	//
	// /**
	// * 报名，没人时候的报名
	// *
	// * @param userId
	// * @param ind
	// */
	// private void reg(String userId, int ind) {
	//
	// User user = this.userService.get(userId);
	// List<UserHeroBO> userHeroList = this.heroService.getUserHeroList(userId,
	// 1);
	//
	// ContestRegBO contestRegBo = new ContestRegBO();
	// contestRegBo.setDeadRound(100);
	// contestRegBo.setIndex(ind);
	// contestRegBo.setUserId(userId);
	// contestRegBo.setUsername(user.getUsername());
	//
	// SystemHero systmHero =
	// this.heroService.getSysHero(userHeroList.get(0).getSystemHeroId());
	//
	// contestRegBo.setSystemHeroId(systmHero.getImgId());
	//
	// this.map.put(ind, contestRegBo);
	//
	// logger.debug("size:" + this.map.size());
	//
	// // 这种报名会影响到剩余次数，所以要全部推
	// this.pushStatus();
	//
	// }
	//
	// @Override
	// public boolean endCoolTime(String userId) {
	//
	// ContestUserBO bo = this.userMap.get(userId);
	// if (bo == null) {
	// return false;
	// }
	//
	// if (!this.userService.reduceGold(userId, 20,
	// ToolUseType.REDUCE_CONTEST_END_CD)) {
	// throw new ServiceException(CONTEST_END_CD_NOT_MONEY, "擂台赛结束CD失败，元宝不足");
	// }
	//
	// bo.setEndCoolTime(new Date());
	//
	// return true;
	// }
	//
	// /**
	// * 抢位置，有人的时候
	// *
	// * @param userId
	// * @param ind
	// */
	// private void rob(final String userId, int ind, final EventHandle handle)
	// {
	//
	// final ContestRegBO contestRegBo = map.get(ind);
	// final String oldUserId = contestRegBo.getUserId();
	//
	// final ContestUserBO attackUserBO = userMap.get(userId);
	// attackUserBO.setLastBattleType(1);
	// attackUserBO.setTargetUserId(oldUserId);
	//
	// final ContestUserBO defenseUserBO = userMap.get(oldUserId);
	// defenseUserBO.setLastBattleType(2);
	// defenseUserBO.setTargetUserId(userId);
	//
	// this.figth(userId, contestRegBo.getUserId(), BattleType.CONTEST_REG, new
	// EventHandle() {
	//
	// @Override
	// public boolean handle(Event event) {
	//
	// int flag = event.getInt("flag");
	// String report = event.getString("report");
	// if (flag == 1) {// 进攻方赢
	//
	// List<UserHeroBO> userHeroList = heroService.getUserHeroList(userId, 1);
	//
	// SystemHero systmHero =
	// heroService.getSysHero(userHeroList.get(0).getSystemHeroId());
	//
	// User user = userService.get(userId);
	//
	// pushMsg(contestRegBo.getUserId());
	// contestRegBo.setUserId(userId);
	// contestRegBo.setUsername(user.getUsername());
	// contestRegBo.setSystemHeroId(systmHero.getImgId());
	//
	// // 抢夺者的状态
	// attackUserBO.setLastBattleStatus(1);
	// // 被抢夺者的状态
	// defenseUserBO.setLastBattleStatus(2);
	//
	// } else {
	// ContestUserBO contestUserBo = userMap.get(userId);
	// if (contestUserBo != null) {
	// contestUserBo.setEndCoolTime(DateUtils.add(new Date(), Calendar.SECOND,
	// 30));
	// }
	//
	// // 抢夺者的状态
	// attackUserBO.setLastBattleStatus(2);
	//
	// // 被抢夺者的状态
	// defenseUserBO.setLastBattleStatus(1);
	//
	// }
	//
	// // 保存战报
	// saveReport(userId, oldUserId, flag, 1, "", report);
	//
	// event.setObject("targetUserId", oldUserId);
	// handle.handle(event);
	//
	// return false;
	// }
	// });
	//
	// }
	//
	// /**
	// * 获取报名位置
	// *
	// * @return
	// */
	// public int getRegInd() {
	//
	// if (map.size() >= 32) {
	// return RandomUtils.nextInt(32);
	// } else {
	//
	// for (int i = 0; i < 32; i++) {
	// if (!map.containsKey(i)) {
	// return i;
	// }
	// }
	// }
	//
	// return -1;
	//
	// }
	//
	// private void worker() {
	//
	// while (true) {
	//
	// try {
	//
	// Event event = this.take();
	// if (event != null) {
	// handle(event);
	// }
	//
	// } catch (Throwable t) {
	// logger.error(t.getMessage(), t);
	// try {
	// Thread.sleep(1000 * 5);
	// } catch (InterruptedException ie) {
	// logger.debug(ie);
	// }
	// }
	// }
	//
	// }
	//
	// /**
	// * 保存战报
	// *
	// * @param attackUserId
	// * @param defenseUserId
	// * @param flag
	// * @param type
	// * @param baseCode
	// * @param report
	// */
	// private void saveReport(String attackUserId, String defenseUserId, int
	// flag, int type, String baseCode, String report) {
	//
	// User attackUser = this.userService.get(attackUserId);
	// User defenseUser = this.userService.get(defenseUserId);
	//
	// ContestBattleReport contestBattleReport = new ContestBattleReport();
	// contestBattleReport.setAttackUserId(attackUserId);
	// contestBattleReport.setAttackUsername(attackUser.getUsername());
	// contestBattleReport.setDefenseUserId(defenseUserId);
	// contestBattleReport.setDefenseUsername(defenseUser.getUsername());
	// contestBattleReport.setBaseCode(baseCode);
	// contestBattleReport.setCreatedTime(new Date());
	// contestBattleReport.setFlag(flag);
	// contestBattleReport.setReport(report);
	// contestBattleReport.setType(type);
	//
	// this.contestDao.addReport(contestBattleReport);
	//
	// if (type == 1) {
	// reportMap.put(attackUserId, contestBattleReport);
	// reportMap.put(defenseUserId, contestBattleReport);
	// } else {
	//
	// Command command = new Command();
	// command.setCommand(CommandType.COMMAND_CONTEST_PUSH_BATTLE);
	// command.setType(CommandType.PUSH_USER);
	//
	// Map<String, String> params = new HashMap<String, String>();
	// params.put("report", Json.toJson(contestBattleReport));
	//
	// params.put("userId", attackUserId);
	//
	// command.setParams(params);
	// commandDao.add(command);
	//
	// params.put("userId", defenseUserId);
	// commandDao.add(command);
	// }
	// }
	//
	// @Override
	// public ContestBattleReport getReport(String userId) {
	//
	// if (reportMap.containsKey(userId)) {
	// return reportMap.get(userId);
	// }
	//
	// return null;
	//
	// }
	//
	// @Override
	// public ContestBattleReport getReport(String attackUsername, String
	// defenseUsername) {
	// return this.contestDao.getReport(attackUsername, defenseUsername);
	// }
	//
	// @Override
	// public List<ContestBattleBO> getBattleList(String userId) {
	//
	// List<ContestBattleBO> list = new ArrayList<ContestBattleBO>();
	// List<ContestBattleReport> l = this.contestDao.getReportList(userId);
	// for (ContestBattleReport report : l) {
	//
	// ContestBattleBO bo = new ContestBattleBO();
	// String targetUsername = null;
	// int flag = 0;
	//
	// if (StringUtils.equals(userId, report.getAttackUserId())) {
	// targetUsername = report.getDefenseUsername();
	//
	// if (report.getFlag() == 1) {
	// flag = 1;
	// } else {
	// flag = 2;
	// }
	//
	// } else {
	//
	// targetUsername = report.getAttackUsername();
	//
	// if (report.getFlag() == 1) {
	// flag = 2;
	// } else {
	// flag = 1;
	// }
	// }
	//
	// String step = ContestHelper2.getStep(report.getType(),
	// report.getBaseCode());
	//
	// bo.setTime(DateUtils.getDate2(report.getCreatedTime()));
	// bo.setStep(step);
	// bo.setFlag(flag);
	// bo.setTargetUsername(targetUsername);
	//
	// list.add(bo);
	// }
	//
	// return list;
	// }
	//
	// public void init() {
	//
	// if (!Config.ins().isGameServer()) {
	// return;
	// }
	//
	// initNextPkDate();
	//
	// // 状态切换线程
	// new Thread(new Runnable() {
	//
	// public void run() {
	// while (true) {
	// try {
	// statusCheck();
	// } catch (Throwable t) {
	// logger.error(t.getMessage(), t);
	// }
	//
	// try {
	// Thread.sleep(1000);
	// } catch (InterruptedException ie) {
	// logger.error(ie.getMessage(), ie);
	// }
	//
	// }
	// }
	//
	// }).start();
	//
	// new Thread(new Runnable() {
	//
	// public void run() {
	// worker();
	// }
	//
	// }).start();
	//
	// }
	//
	// private void initNextPkDate() {
	// String nowStr = DateUtils.getDate();
	// String last = contestDao.getPkDate();
	// if (!StringUtils.isEmpty(last) && last.equals(nowStr)) { // 今天打过
	// this.nextStatusTime = ContestHelper2.getNextDayStartTime();
	// }
	// }

}

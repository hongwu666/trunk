
import static org.jboss.netty.buffer.ChannelBuffers.buffer;
import static org.jboss.netty.channel.Channels.pipeline;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
* 猎国三合凌天专用的接口测试类
* <br>==========================
* <br> 公司：木屋网络
* <br> 开发：onedear
* <br> 版本：1.0
* <br> 创建时间：Oct 30, 2014 11:12:42 AM
* <br>==========================
*/
public class LieGuoTest extends SimpleChannelUpstreamHandler implements ChannelPipelineFactory {
	
	private static Logger LOGGER = LoggerFactory.getLogger(LieGuoTest.class);
	
	private Channel channel;
	
	private String userId = "000801f6575947da967e24c2ddebebd8";
	
	private String ip = "127.0.0.1";
	
	private int port = 10000;
	
	public LieGuoTest() throws UnsupportedEncodingException{
		ChannelFactory factory = new NioClientSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool());
	    ClientBootstrap bootstrap = new ClientBootstrap(factory);
	    bootstrap.setPipelineFactory(this);
		bootstrap.setOption("child.tcpNoDelay", true);
	    bootstrap.setOption("child.keepAlive", true);
		
		ChannelFuture connectFuture = bootstrap.connect(new InetSocketAddress(ip, port));
		channel = connectFuture.awaitUninterruptibly().getChannel();
	    System.out.println("write request");
		login();
	}
	
	private void login() {
		//{\"reqId\":1,\"dt\":{\"uid\":\""+userId+"\",\"pid\":\"119\",\"ut\":0,\"tk\":\"tk00000100\",\"sid\":\"s1\"},\"act\":\"User.login\"}";
		Message msg = new Message();
		msg.setAct("User.login");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", userId);
		map.put("pid", 119);
		map.put("ut", 0);
		map.put("tk", "tk00000100");
		map.put("sid", "s1");
		msg.setDt(map);
		send(msg);
	}
	
	private void loadDailyTasks() {
		Message msg = new Message();
		msg.setAct("Task.loadDailyTasks");
		send(msg);
	}
	
	private void dailyTaskReward(int tid) {
		Message msg = new Message();
		msg.setAct("Task.dailyReward");
		Map map = new HashMap();
		map.put("tid", tid);
		msg.setDt(map);
		send(msg);
	}
	
	private void testTaskAction(int type, int incr) {
		Message msg = new Message();
		msg.setAct("Task.test");
		Map map = new HashMap();
		map.put("type", type);
		map.put("incr", incr);
		msg.setDt(map);
		send(msg);
	}
	
	private void testTaskLv(int lv) {
		Message msg = new Message();
		msg.setAct("Task.testLv");
		Map map = new HashMap();
		map.put("lv", lv);
		msg.setDt(map);
		send(msg);
	}
	
	
	@Override  
    public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws InterruptedException {
		Message msg = (Message)e.getMessage();
		String json = JsonUtil.encode(msg);
		System.out.println("receive " + json);
		if (msg.getAct().equals("User.loginRq")) {
			//把登录后的代码加这里
			loadDailyTasks();
			
		} 
	}
	
	
	public void send(Message msg) {
		System.out.println("send : " + JsonUtil.encode(msg));
		msg.setReqId(new Random().nextInt(1000) + "");
        byte[] messageBuff = null;
		try {
			messageBuff = JsonUtil.encode(msg).getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
        int len = messageBuff.length;
        int total = 4 + len;
        // 发送 header
        ChannelBuffer buff = buffer(total);
        buff.writeInt(len);
        buff.writeBytes(messageBuff);
        channel.write(buff);
	}

	
	@Override 
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e){
		System.out.println("closed");
		System.exit(0);
	}
	@Override  
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        System.out.println("Exception: "+e.getCause());
        e.getChannel().close();  
    }
	
	public static void main(String[] args) throws InterruptedException, UnsupportedEncodingException{
		new LieGuoTest();
		
		Thread.sleep(30000);
		
		System.exit(0);
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = pipeline();
		pipeline.addFirst("decoder", new LengthFieldBasedFrameDecoder(2147483647,0,4,0,4));
        pipeline.addLast("decoder2", new ProtocalDecoder());
        pipeline.addLast("handler", this);  
        return pipeline;
	}
}

class ProtocalDecoder extends  FrameDecoder {
	@Override
	protected Object decode(ChannelHandlerContext context, Channel channel,
			ChannelBuffer buffer) throws Exception {
		byte[] b = new byte[buffer.readableBytes()];
		buffer.readBytes(b);
		String data = new String(b, Charset.forName("utf8"));
    	Message message = JsonUtil.decode(data, Message.class);
    	return message;
	}
    
}



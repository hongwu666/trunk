import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;


public class Test extends SimpleChannelUpstreamHandler implements ChannelPipelineFactory {

	@Override
	public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws InterruptedException {
		
	}


	@Override
	public ChannelPipeline getPipeline() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}

package com.river.netty.websocket;

import java.time.LocalDateTime;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * ������Ϣ��handler
 * 
 * @author 17629 TextWebSocketFrame �� ��netty�У�������websocketר�Ŵ����ı��Ķ���frame����Ϣ������
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	// ���ڼ�¼�͹���ͻ��˵�channels
	private static ChannelGroup clients = 
			new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

		// ��ȡ�ͻ��˴����������Ϣ
		String content = msg.text();
		System.out.println("���յ�������:" + content);

		for (Channel channel : clients) {
			channel.writeAndFlush(
					new TextWebSocketFrame("[��������:]"+LocalDateTime.now()+"���յ���Ϣ����ϢΪ:"+content));
		}
	}

	/**
	 * ���ͻ������ӷ����֮��(������) ��ȡ�ͻ��˵�channel�����ҷŵ�ChannelGroup�н��й���
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		clients.add(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

		//������handlerRemoved��channelGroup���Զ��Ƴ���Ӧ�ͻ��˵ĵ�channel
		//clients.remove(ctx.channel());
		System.out.println("�ͻ��˶Ͽ�,channel��Ӧ��idΪ"+ctx.channel().id().asLongText());
		System.out.println("�ͻ��˶Ͽ�,channel��Ӧ�Ķ�idΪ"+ctx.channel().id().asShortText());
	}
}
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
 * 处理消息的handler
 * 
 * @author 17629 TextWebSocketFrame ： 在netty中，是用于websocket专门处理文本的对象，frame是消息的载体
 */
public class ChatHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	// 用于记录和管理客户端的channels
	private static ChannelGroup clients = 
			new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {

		// 获取客户端传输过来的消息
		String content = msg.text();
		System.out.println("接收到的数据:" + content);

		for (Channel channel : clients) {
			channel.writeAndFlush(
					new TextWebSocketFrame("[服务器在:]"+LocalDateTime.now()+"接收到消息，消息为:"+content));
		}
	}

	/**
	 * 当客户端连接服务段之后(打开链接) 获取客户端的channel，并且放到ChannelGroup中进行管理
	 */
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		clients.add(ctx.channel());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

		//当触发handlerRemoved，channelGroup会自动移除对应客户端的的channel
		//clients.remove(ctx.channel());
		System.out.println("客户端断开,channel对应的id为"+ctx.channel().id().asLongText());
		System.out.println("客户端断开,channel对应的短id为"+ctx.channel().id().asShortText());
	}
}
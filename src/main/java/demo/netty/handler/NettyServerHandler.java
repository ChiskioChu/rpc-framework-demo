package demo.netty.handler;

import demo.dto.RpcRequest;
import demo.dto.RpcResponse;
import demo.registry.DefaultServiceRegistry;
import demo.registry.ServiceRegistry;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.ReferenceCountUtil;

import java.lang.reflect.Method;

/**
 * 客户端消息处理器
 * */

public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static ServiceRegistry serviceRegistry;

    static {
        serviceRegistry = new DefaultServiceRegistry();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {
        try {
            System.out.println("服务器接收到请求: {}" + msg);
            String interfaceName = msg.getInterfaceName();
            Object service = serviceRegistry.getService(interfaceName);
            if (service == null) {
                throw new IllegalArgumentException("service instance == null");
            }
            Method method = service.getClass().getMethod(msg.getMethodName(), msg.getParamTypes());
            Object result = method.invoke(service, msg.getParameters());
            ChannelFuture future = ctx.writeAndFlush(RpcResponse.success(result));
            future.addListener(ChannelFutureListener.CLOSE);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("处理过程调用时有错误发生:");
        cause.printStackTrace();
        ctx.close();
    }

}

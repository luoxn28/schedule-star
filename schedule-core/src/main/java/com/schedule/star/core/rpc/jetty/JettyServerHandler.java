package com.schedule.star.core.rpc.jetty;

import cn.hutool.core.util.ArrayUtil;
import com.schedule.star.core.rpc.RpcServiceFactory;
import com.schedule.star.core.rpc.encoder.RpcRequest;
import com.schedule.star.core.rpc.encoder.RpcResponse;
import com.schedule.star.core.util.HessianUtil;
import com.schedule.star.core.util.HttpUtil;
import com.schedule.star.core.util.R;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Jetty handler
 *
 * @author xiangnan
 * @date 2018/1/30 22:28
 */
public class JettyServerHandler extends AbstractHandler {
    private static Logger logger = LogManager.getLogger();

    @Override
    public void handle(String s, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        // RCP服务调用
        byte[] responseBytes = HessianUtil.serialize(invoke(request));

        response.setContentType("text/html;charset=utf-8");
        response.setStatus(HttpServletResponse.SC_OK);
        baseRequest.setHandled(true);

        OutputStream out = response.getOutputStream();
        out.write(responseBytes);
        out.flush();
    }

    private RpcResponse invoke(HttpServletRequest request) {

        try {
            byte[] data = HttpUtil.readByte(request);
            if (ArrayUtil.isEmpty(data)) {
                return new RpcResponse(R.status.FAIL, "RpcRequest byte[] is null", null);
            }

            RpcRequest rpcRequest = HessianUtil.deserialize(data);
            return RpcServiceFactory.invoke(rpcRequest);
        } catch (IOException e) {
            logger.error("RPC调用错误: " + e);
            return new RpcResponse(R.status.FAIL, e.getMessage(), null);
        }
    }

}

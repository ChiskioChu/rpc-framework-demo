package demo.rpc;

import demo.dto.RpcRequest;
import demo.dto.RpcResponse;
import demo.registry.ServiceRegistry;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;

public class RequestHandlerThread implements Runnable {

    private Socket socket;
    private ServiceRegistry serviceRegistry;

    public RequestHandlerThread(Socket socket, ServiceRegistry serviceRegistry) {
        this.socket = socket;
        this.serviceRegistry = serviceRegistry;
    }

    @Override
    public void run() {
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
            try {
                RpcRequest rpcRequest = (RpcRequest) objectInputStream.readObject();
                String interfaceName = rpcRequest.getInterfaceName();
                Object service = serviceRegistry.getService(interfaceName);
                if (service == null) {
                    throw new IllegalArgumentException("service instance == null");
                }

                Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParamTypes());
                Object returnObject = method.invoke(service, rpcRequest.getParameters());
                objectOutputStream.writeObject(RpcResponse.success(returnObject));
                objectOutputStream.flush();
            } catch (Throwable t) {
                objectOutputStream.writeObject(t);
            } finally {
                socket.close();
                objectInputStream.close();
                objectOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

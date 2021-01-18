package demo.rpc;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 把服务和端口进行绑定
 * */
public class RpcServer {
    public void export(Object service, int port) throws IOException {
        if (service == null) {
            throw new IllegalArgumentException("service instance == null");
        }
        if (port <= 0 || port > 65535) {
            throw new IllegalArgumentException("Invalid port " + port);
        }

        ServerSocket serverSocket = new ServerSocket(port);
        while(true){
            final Socket socket = serverSocket.accept();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        try {
                            String methodName = objectInputStream.readUTF();
                            Class<?>[] parameterTypes = (Class<?>[]) objectInputStream.readObject();
                            Object[] arguments = (Object[]) objectInputStream.readObject();
                            Method method = service.getClass().getMethod(methodName, parameterTypes);
                            Object result = method.invoke(service, arguments);
                            objectOutputStream.writeObject(result);
                        } catch (Throwable t) {
                            objectOutputStream.writeObject(t);
                        } finally {
                          objectOutputStream.close();
                          objectInputStream.close();
                          socket.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }
}

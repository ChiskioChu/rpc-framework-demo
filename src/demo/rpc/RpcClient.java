package demo.rpc;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class RpcClient {
    private int port;
    private String host;

    public RpcClient(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public RpcClient() {
    }

    public  <T> T refer(final Class<T> interfaceClass, final String host, final int port){
        if (interfaceClass == null)
            throw new IllegalArgumentException("Interface class == null");
        if (! interfaceClass.isInterface())
            throw new IllegalArgumentException("The " + interfaceClass.getName() + " must be interface class!");
        if (host == null || host.length() == 0)
            throw new IllegalArgumentException("Host == null!");
        if (port <= 0 || port > 65535)
            throw new IllegalArgumentException("Invalid port " + port);

        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class<?>[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Socket socket = new Socket(host, port);
                try {
                    ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    try {
                        objectOutputStream.writeUTF(method.getName());
                        objectOutputStream.writeObject(method.getParameterTypes());
                        objectOutputStream.writeObject(args);
                        ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
                        try {
                            Object result = objectInputStream.readObject();
                            if (result instanceof Throwable) {
                                throw (Throwable) result;
                            }
                            return result;
                        } finally {
                            objectInputStream.close();
                        }
                    } finally {
                        objectOutputStream.close();
                    }
                }finally {
                    socket.close();
                }
            }
        });
    }

}

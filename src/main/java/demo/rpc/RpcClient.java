package demo.rpc;

import demo.dto.RpcRequest;

public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}

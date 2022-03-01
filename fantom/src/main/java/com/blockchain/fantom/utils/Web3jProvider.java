package com.blockchain.fantom.utils;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

public class Web3jProvider {

    public static Web3j connect(String rpc){
        return Web3j.build(new HttpService(Environments.RPC_URL));
    }

    public static Web3j connect(){
        return Web3j.build(new HttpService(Environments.LOCAL_URL));
    }
}

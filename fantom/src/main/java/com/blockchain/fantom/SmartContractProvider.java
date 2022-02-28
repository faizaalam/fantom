//package com.blockchain.fantom;
//
//import org.web3j.contracts.token.ERC20Interface;
//import org.web3j.protocol.Web3j;
//import org.web3j.protocol.http.HttpService;
//
//public class SmartContractProvider {
//    Web3j web3b = Web3j.build(new HttpService("https://rpc.testnet.fantom.network"));
//
//    ERC20Interface contract = ERC20.load(tokenAddress, web3j, txManager, gasPriceProvider);
//    BigInteger balance = contract.balanceOf(account).send();
//}

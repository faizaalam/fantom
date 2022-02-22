package com.blockchain.fantom;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketService;

import java.io.IOException;
import java.util.Objects;

@SpringBootTest
class FantomApplicationTests {
	private final String DEFAULT_FTM_ADDRESS = "0xbfaE21DD8C7B846D5e6Aab88CF5F330190dfCF74";

	@Test
	void contextLoads() {
	}

	@Test
	public void testWss() throws Exception {
		Web3j web3j = connect("wss://wsapi.fantom.network/");
		Web3ClientVersion clientVersion = web3j.web3ClientVersion().send();
		System.out.println("version:" + clientVersion.getWeb3ClientVersion());
}


	@Test
	public void testWssFetchBlock() throws Exception {
		Web3j web3j = connect("wss://wsapi.fantom.network/");

		EthBlockNumber ethBlockNumber = web3j.ethBlockNumber().sendAsync().get();
		System.out.println("ethBlockNumber:" + ethBlockNumber.getBlockNumber());
	}

	@Test
	public void testWssFetchAccounts() throws Exception {
		Web3j web3j = connect("wss://wsapi.fantom.network/");

		EthAccounts ethAccounts = web3j.ethAccounts().sendAsync().get();
		System.out.println("ethAccounts:" + ethAccounts.getAccounts());
	}


	@Test
	public void testWssFetchAccountBalance() throws Exception {
		Web3j web3j = connect("wss://wsapi.fantom.network/");

		EthGetBalance result = new EthGetBalance();
			result = web3j.ethGetBalance(DEFAULT_FTM_ADDRESS, DefaultBlockParameter.valueOf("latest")).sendAsync().get();
			System.out.println("ethAccountBalance:" + result.getBalance());
	}


	@Test
	public void testWssFetchAccountTransaction() throws Exception {
		Web3j web3j = connect("wss://wsapi.fantom.network/");

		EthTransaction result = new EthTransaction();
			result = web3j.ethGetTransactionByHash("0xfd62d21dd656f09f7a25b464114174f762b79a9ec6562befb1393f78593a3657").sendAsync().get();
			System.out.println("EthTransaction:" + result.getTransaction().isPresent());


	}


	private static Web3j connect(String url) throws IOException {
		Objects.requireNonNull(url, "ethereum.node.url cannot be null");
		Web3j web3j;
		url = "https://rpc.testnet.fantom.network/";

		if (url.startsWith("ws")) {
			WebSocketService web3jService = new WebSocketService(url, true);
			web3jService.connect();
			web3j = Web3j.build(web3jService);
		} else {
			web3j = Web3j.build(new HttpService(url));
		}
		return web3j;
	}

}

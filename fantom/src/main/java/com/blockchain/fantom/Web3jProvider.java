package com.blockchain.fantom;

import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;


import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class Web3jProvider {

    private final String DEFAULT_LOCAL_ADDRESS = "0x892937b961808818e63253be8A658C06b547d8b5";
    private final String PRIVATE_KEY_LOCAL = "0x71c6f8ed94092390d545cb7c533bfa63a8319dc8079f1d7a0c161dcdf0efa503";

//    private final String DEFAULT_FMT_ADDRESS = "0xbfaE21DD8C7B846D5e6Aab88CF5F330190dfCF74";
//    private final String PRIVATE_KEY_FTM = "0xdae5b07c04a3a083d33e10fb06bd3b23873acbb151b46b09f8bc52ac5cf9d082";
private final String DEFAULT_FMT_ADDRESS = "0xFF3aA6948602aE7Db17A5eF18943041c8F1e5FBB";
    private final String PRIVATE_KEY_FTM = "9d587a9909bc0feed4e5e67c2c4e85fcdd5f0ff4599ba7512cc4acda7d4e7945";

    Web3j web3ba = Web3j.build(new HttpService());
    Web3j web3b = Web3j.build(new HttpService("https://rpc.testnet.fantom.network"));

    public EthBlockNumber getBlockNumber() {
        EthBlockNumber result = new EthBlockNumber();
        try {
            result = web3b.ethBlockNumber().sendAsync().get();
        } catch (Exception ex) {
            System.out.println("Could not get block number");
        }
        return result;
    }

    public EthAccounts getEthAccounts() {
        EthAccounts result = new EthAccounts();
        try {
            result = web3b.ethAccounts().sendAsync().get();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }

    public EthGetTransactionCount getTransactionCount() {
        EthGetTransactionCount result = new EthGetTransactionCount();
        try {
            result = web3b.ethGetTransactionCount(DEFAULT_LOCAL_ADDRESS, DefaultBlockParameter.valueOf("latest")).sendAsync().get();
        } catch (Exception ex) {
            System.out.println("Error");
        }
        return result;
    }

    public EthGetBalance getEthBalance() {
        EthGetBalance result = new EthGetBalance();
        try {
            result = web3b.ethGetBalance(DEFAULT_LOCAL_ADDRESS, DefaultBlockParameter.valueOf("latest")).sendAsync().get();
        } catch (Exception ex) {
            System.out.println("Error getting balance");
        }
        return result;

    }

    public EthTransaction fetchAccountTransaction(String hash) throws Exception {
        EthTransaction result = new EthTransaction();
        result = web3b.ethGetTransactionByHash(hash).sendAsync().get();
        return result;
    }

    public void doTransaction1() throws IOException, InterruptedException {
        Credentials credentials = Credentials.create(PRIVATE_KEY_FTM);
        TransactionManager transactionManager = new RawTransactionManager(
                web3b, credentials, 4002L);
        BigInteger value = Convert.toWei(String.valueOf(0.1), Convert.Unit.ETHER).toBigInteger();
        EthSendTransaction ethTransaction = transactionManager.sendTransaction(
                BigInteger.valueOf(400000000), DefaultGasProvider.GAS_LIMIT, "0x187D724ba4C167E46255Aa4b29557A168E8d8444", "", value);

        String txnHash = ethTransaction.getTransactionHash();

        Optional<TransactionReceipt> transactionReceipt = null;
        do {
            System.out.println("checking if transaction " + txnHash + " is mined....");
            EthGetTransactionReceipt ethGetTransactionReceiptResp = web3b.ethGetTransactionReceipt(txnHash)
                    .send();
            transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();
            Thread.sleep(3000); // Wait for 3 sec
        } while (!transactionReceipt.isPresent());

        System.out.println("Transaction " + txnHash + " was mined in block # "
                + transactionReceipt.get().getBlockNumber());
        System.out.println("Balance: "
                + Convert.fromWei(web3b.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                .send().getBalance().toString(), Convert.Unit.ETHER));

    }

    public void doTransaction() throws TransactionException, IOException, InterruptedException, ExecutionException {
        Credentials credentials = Credentials.create(PRIVATE_KEY_FTM);
        TransactionManager transactionManager = new RawTransactionManager(
                web3b, credentials, 4002L);
        // Gas Parameter
        BigInteger gasLimit = BigInteger.valueOf(21000);
        BigInteger gasPrice = Convert.toWei("1500", Convert.Unit.GWEI).toBigInteger();
        EthSendTransaction ethSendTransaction = transactionManager.sendEIP1559Transaction( 4002L,gasPrice, gasPrice, gasLimit,"0x187D724ba4C167E46255Aa4b29557A168E8d8444","", BigInteger.valueOf(1));
        if (ethSendTransaction.hasError()) {
            throw new RuntimeException(ethSendTransaction.getError().getMessage());
        }

        Optional<TransactionReceipt> transactionReceipt = null;
        String txnHash =  ethSendTransaction.getTransactionHash();
        do {
            System.out.println("checking if transaction " + ethSendTransaction.getTransactionHash() + " is mined....");
            EthGetTransactionReceipt ethGetTransactionReceiptResp = web3b.ethGetTransactionReceipt(txnHash)
                    .send();
            transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();
            Thread.sleep(3000); // Wait for 3 sec
        } while (!transactionReceipt.isPresent());

        System.out.println("Transaction " + txnHash + " was mined in block # "
                + transactionReceipt.get().getBlockNumber());
        System.out.println("Balance: "
                + Convert.fromWei(web3b.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                .send().getBalance().toString(), Convert.Unit.ETHER));

    }


}


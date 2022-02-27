package com.blockchain.fantom;

import org.springframework.stereotype.Component;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
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
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.utils.Convert;


import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.ECGenParameterSpec;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class Web3jProvider {
    private final String pathToWallet =  "/home/faiza/Documents/wallets";


    Web3j web3b = Web3j.build(new HttpService());
    Web3j web3ba = Web3j.build(new HttpService("https://rpc.testnet.fantom.network"));

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

    public EthGetTransactionCount getTransactionCount(String address) {
        EthGetTransactionCount result = new EthGetTransactionCount();
        try {
            result = web3b.ethGetTransactionCount(address, DefaultBlockParameter.valueOf("latest")).sendAsync().get();
        } catch (Exception ex) {
            System.out.println("Error");
        }
        return result;
    }

    public EthGetBalance getEthBalance(String address) {
        EthGetBalance result = new EthGetBalance();
        try {
            result = web3b.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (Exception ex) {
            System.out.println("Error getting balance");
        }
        return result;

    }

    public EthTransaction fetchAccountTransaction(String hash) throws Exception {
        return web3b.ethGetTransactionByHash(hash).sendAsync().get();
    }



    public TransactionReceipt doTransaction() throws TransactionException, IOException, InterruptedException, ExecutionException {
        Credentials credentials = Credentials.create("dae5b07c04a3a083d33e10fb06bd3b23873acbb151b46b09f8bc52ac5cf9d082");
        TransactionManager transactionManager = new RawTransactionManager(
                web3b, credentials, 4002L);
        // Gas Parameter
        BigInteger gasLimit = BigInteger.valueOf(21000);
        BigInteger gasPrice = Convert.toWei("4", Convert.Unit.GWEI).toBigInteger();
        EthSendTransaction ethSendTransaction = transactionManager.sendTransactionEIP1559(gasPrice.multiply(BigInteger.valueOf(2)),  gasPrice, gasLimit,"0x187D724ba4C167E46255Aa4b29557A168E8d8444","", BigInteger.valueOf(1));
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

        return transactionReceipt.orElse(null);

    }

    public String createFtmWallet(String password) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        String walletDirectory = "/home/faiza/Documents/wallets";
        String accountAddress = null;

        try {
            Bip39Wallet wallet =  WalletUtils.generateBip39Wallet(password, new File(walletDirectory));
            Credentials credentials = WalletUtils.loadCredentials(password, wallet.getMnemonic());
            accountAddress = credentials.getAddress();

            System.out.println("wallet location: " + walletDirectory + "/" + wallet.getFilename());
            System.out.println("Account address: " + credentials.getAddress());
            System.out.println(credentials.getEcKeyPair().getPrivateKey());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return accountAddress;
    }

    public String restoreWallet(String password, String mnemonic) throws CipherException, IOException {
        Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);
        return credentials.getAddress();
    }


    public TransactionReceipt doTransaction(String password, String toAddress, BigDecimal value ) throws Exception {
        Credentials credentials = WalletUtils.loadCredentials(password, pathToWallet);

        return TransferToken.transfer(web3b, credentials, toAddress, value) ;
    }







}


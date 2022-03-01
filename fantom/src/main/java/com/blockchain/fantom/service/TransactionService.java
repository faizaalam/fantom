package com.blockchain.fantom.service;

import com.blockchain.fantom.models.UserWallet;
import com.blockchain.fantom.repository.WalletRepository;
import com.blockchain.fantom.utils.TransferToken;
import com.blockchain.fantom.utils.Web3jProvider;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionReceipt;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.exceptions.TransactionException;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Convert;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Service
public class TransactionService {

    private final WalletRepository walletRepository;
    private final Web3j web3j = Web3jProvider.connect();

    public TransactionService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public TransactionReceipt doTransaction() throws TransactionException, IOException, InterruptedException, ExecutionException {
        Credentials credentials = Credentials.create("dae5b07c04a3a083d33e10fb06bd3b23873acbb151b46b09f8bc52ac5cf9d082");
        TransactionManager transactionManager = new RawTransactionManager(
                web3j, credentials, 4002L);
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
            EthGetTransactionReceipt ethGetTransactionReceiptResp = web3j.ethGetTransactionReceipt(txnHash)
                    .send();
            transactionReceipt = ethGetTransactionReceiptResp.getTransactionReceipt();
            Thread.sleep(3000); // Wait for 3 sec
        } while (!transactionReceipt.isPresent());

        System.out.println("Transaction " + txnHash + " was mined in block # "
                + transactionReceipt.get().getBlockNumber());
        System.out.println("Balance: "
                + Convert.fromWei(web3j.ethGetBalance(credentials.getAddress(), DefaultBlockParameterName.LATEST)
                .send().getBalance().toString(), Convert.Unit.ETHER));

        return transactionReceipt.orElse(null);

    }



    public TransactionReceipt doTransaction(String username, String toAddress, BigDecimal value , String privateKey) throws Exception {
        Credentials credentials = null;
        if (privateKey != null) {
            credentials = Credentials.create(privateKey);
        } else  {
            UserWallet userWallet = walletRepository.findByUserName(username).orElseThrow(RuntimeException::new);
            credentials = WalletUtils.loadBip39Credentials(userWallet.getPassword(), userWallet.getMnemonic());

        }

        return TransferToken.transfer(web3j, credentials, toAddress, value) ;
    }


    public EthTransaction fetchAccountTransaction(String hash) throws Exception {
        return web3j.ethGetTransactionByHash(hash).sendAsync().get();
    }
}

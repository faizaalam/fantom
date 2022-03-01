package com.blockchain.fantom.service;

import com.blockchain.fantom.models.UserWallet;
import com.blockchain.fantom.repository.WalletRepository;
import com.blockchain.fantom.utils.Web3jProvider;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Bip39Wallet;
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;

import java.io.File;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;

@Service
public class WalletService {
    private final String pathToWallet =  "/home/faiza/Documents/wallets";

    private final WalletRepository walletRepository;
    private final Web3j web3j = Web3jProvider.connect();


    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }



    public UserWallet createFtmWallet(String password, String username) throws InvalidAlgorithmParameterException, NoSuchAlgorithmException {
        String accountAddress = null;
        UserWallet userWallet = null;
        try {
            Bip39Wallet wallet =  WalletUtils.generateBip39Wallet(password, new File(pathToWallet));
            Credentials credentials = WalletUtils.loadBip39Credentials(password, wallet.getMnemonic());
            accountAddress = credentials.getAddress();

            System.out.println("wallet location: " + pathToWallet + "/" + wallet.getFilename());
            System.out.println("Account address: " + credentials.getAddress());
            System.out.println(credentials.getEcKeyPair().getPrivateKey());


            userWallet = new UserWallet();
            userWallet.setUserName(username);
            userWallet.setPassword(password);
            userWallet.setMnemonic(wallet.getMnemonic());
            userWallet.setAddress(credentials.getAddress());


            walletRepository.save(userWallet);



        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return userWallet;
    }

    public UserWallet restoreWallet(String password, String userName, String mnemonic) throws CipherException, IOException {
        Bip39Wallet wallet =  WalletUtils.generateBip39WalletFromMnemonic(password, mnemonic, new File(pathToWallet));
        Credentials credentials = WalletUtils.loadBip39Credentials(password, mnemonic);
        UserWallet userWallet = new UserWallet();
        userWallet.setUserName(userName);
        userWallet.setPassword(password);
        userWallet.setMnemonic(wallet.getMnemonic());
        userWallet.setAddress(credentials.getAddress());

        // could save the whole wallet with username password

        walletRepository.save(userWallet);

        return userWallet;
    }

    public EthGetTransactionCount getTransactionCount(String address) {
        Web3j web3j = Web3jProvider.connect();
        EthGetTransactionCount result = new EthGetTransactionCount();
        try {
            result = web3j.ethGetTransactionCount(address, DefaultBlockParameter.valueOf("latest")).sendAsync().get();
        } catch (Exception ex) {
            System.out.println("Error");
        }
        return result;
    }

    public EthGetBalance getEthBalance(String address) {

        EthGetBalance result = new EthGetBalance();
        try {
            result = web3j.ethGetBalance(address, DefaultBlockParameterName.LATEST).sendAsync().get();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return result;

    }

    public EthAccounts getEthAccounts() {
        EthAccounts result = new EthAccounts();
        try {
            result = web3j.ethAccounts().sendAsync().get();
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }
}

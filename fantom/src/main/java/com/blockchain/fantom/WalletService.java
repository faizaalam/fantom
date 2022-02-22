package com.blockchain.fantom;

import org.springframework.stereotype.Component;
import org.web3j.crypto.WalletUtils;

import java.io.File;

@Component
public class WalletService {

    public void createWallet() {
        String walletPassword = "trumpshouldbeimpeached";
        String walletDirectory = "/home/batman/wallet/";
        String walletName = null;
        try {
            WalletUtils.generateNewWalletFile(walletPassword, new File(walletDirectory));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        System.out.println(walletName);
    }





}

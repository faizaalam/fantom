package com.blockchain.fantom.controller;

import com.blockchain.fantom.models.ResponseTransfer;
import com.blockchain.fantom.models.UserWallet;
import com.blockchain.fantom.service.ContractService;
import com.blockchain.fantom.service.WalletService;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;


@RestController
@RequestMapping(path = "/fantom/accounts")
public class AccountController {

    private final WalletService walletService;

    public AccountController(WalletService walletService) {
        this.walletService = walletService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public Future<ResponseTransfer<EthAccounts>> getAccounts() {
        ResponseTransfer<EthAccounts> responseTransfer = new ResponseTransfer<>();

        return CompletableFuture.supplyAsync(() -> {
            try {
                EthAccounts result = walletService.getEthAccounts();
                responseTransfer.setMessage(result.toString());
                System.out.println(result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return responseTransfer;

        }).thenApplyAsync(result -> result);
    }

    @RequestMapping(value = "/{address}/balance", method = RequestMethod.GET)
    public Future<ResponseTransfer<EthGetBalance>> getAccountBalance(@PathVariable("address") String address) {
        ResponseTransfer<EthGetBalance> responseTransfer = new ResponseTransfer<>();

        return CompletableFuture.supplyAsync(() -> {
            try {
                EthGetBalance result = walletService.getEthBalance(address);
                responseTransfer.setContent(result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return responseTransfer;

        }).thenApplyAsync(result -> result);
    }

    @RequestMapping(value = "/{address}/transactions-count", method = RequestMethod.GET)
    public Future<ResponseTransfer<EthGetTransactionCount>> getTransactionCounts(@PathVariable(value = "address") String address) {
        ResponseTransfer<EthGetTransactionCount> responseTransfer = new ResponseTransfer<>();
        // Instant start = TimeHelper.start();
        return CompletableFuture.supplyAsync(() -> {
            try {
                EthGetTransactionCount result = walletService.getTransactionCount(address);
                responseTransfer.setMessage(result.toString());
                responseTransfer.setContent(result);
            } catch (Exception e) {
                responseTransfer.setMessage("Error");
            }
            return responseTransfer;
        }).thenApplyAsync(result -> result);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public Future<UserWallet> createOrRestoreWallet(
            @RequestParam("password") String password,
            @RequestParam(value ="userName", required = false) String userName,
            @RequestParam(value ="mnemonic", required = false) String mnemonic) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (mnemonic != null)  {
                    return walletService.restoreWallet(password, userName, mnemonic);
                }
                return walletService.createFtmWallet(password, userName);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).thenApplyAsync(result -> result);
    }


//    @RequestMapping(value = "/accounts/connect", method = RequestMethod.POST)
//    public Future<UserWallet> connectWallet(
//            @RequestParam("address") String address,
//            @RequestParam("password") String password,
//            @RequestParam(value ="userName", required = false) String userName,
//            @RequestParam(value ="mnemonic", required = false) String mnemonic) {
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                return web3jProvider.connectWallet(password,address, userName, mnemonic);
//
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }).thenApplyAsync(result -> result);
//    }


}




package com.blockchain.fantom;

import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.*;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;


@RestController
@RequestMapping(path = "/fantom")
public class FantomRestController {

    private final Web3jProvider web3jProvider;

    public FantomRestController(Web3jProvider web3jProvider) {
        this.web3jProvider = web3jProvider;
    }


    @RequestMapping(value = "/blocks", method = RequestMethod.GET)
    public Future<ResponseTransfer<EthBlockNumber>> getBlock() {
        ResponseTransfer<EthBlockNumber> responseTransfer = new ResponseTransfer<>();

        return CompletableFuture.supplyAsync(() -> {
            try {
                EthBlockNumber result = web3jProvider.getBlockNumber();
                responseTransfer.setMessage(result.getBlockNumber().toString());
            } catch (Exception e) {
                responseTransfer.setMessage("Error");
            }
            return responseTransfer;
        }).thenApplyAsync(result -> result);
    }

    @PostMapping(value = "/transactions")
    public Future<ResponseTransfer<TransactionReceipt>> doTransaction() {
        ResponseTransfer<TransactionReceipt> responseTransfer = new ResponseTransfer<>();

        return CompletableFuture.supplyAsync(() -> {
            try {
                TransactionReceipt result = web3jProvider.doTransaction();
                responseTransfer.setMessage(result.getBlockNumber().toString());
                responseTransfer.setContent(result);
            } catch (Exception e) {
                responseTransfer.setMessage("Error");
                throw new RuntimeException(e);
            }
            return responseTransfer;
        }).thenApplyAsync(result -> result);
    }


    @RequestMapping(value = "/transactions/{txnHash}", method = RequestMethod.GET)
    public Future<ResponseTransfer<EthTransaction>> getTransactionDetails(@PathVariable("txnHash") String txnHash) {
        ResponseTransfer<EthTransaction> responseTransfer = new ResponseTransfer<>();

        return CompletableFuture.supplyAsync(() -> {
            try {
                EthTransaction result = web3jProvider.fetchAccountTransaction(txnHash);
                responseTransfer.setContent(result);
            } catch (Exception e) {
                responseTransfer.setMessage("Error");
                throw new RuntimeException(e);
            }
            return responseTransfer;
        }).thenApplyAsync(result -> result);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public Future<ResponseTransfer<EthAccounts>> getAccounts() {
        ResponseTransfer<EthAccounts> responseTransfer = new ResponseTransfer<>();

        return CompletableFuture.supplyAsync(() -> {
            try {
                EthAccounts result = web3jProvider.getEthAccounts();
                responseTransfer.setMessage(result.toString());
                System.out.println(result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return responseTransfer;

        }).thenApplyAsync(result -> result);
    }

    @RequestMapping(value = "/accounts/{address}/balance", method = RequestMethod.GET)
    public Future<ResponseTransfer<EthGetBalance>> getAccountBalance(@PathVariable("address") String address) {
        ResponseTransfer<EthGetBalance> responseTransfer = new ResponseTransfer<>();

        return CompletableFuture.supplyAsync(() -> {
            try {
                EthGetBalance result = web3jProvider.getEthBalance(address);
                responseTransfer.setContent(result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return responseTransfer;

        }).thenApplyAsync(result -> result);
    }

    @RequestMapping(value = "/transactions/count", method = RequestMethod.GET)
    public Future<ResponseTransfer<EthGetTransactionCount>> getTransactionCounts() {
        ResponseTransfer<EthGetTransactionCount> responseTransfer = new ResponseTransfer<>();
        // Instant start = TimeHelper.start();
        return CompletableFuture.supplyAsync(() -> {
            try {
                EthGetTransactionCount result = web3jProvider.getTransactionCount();
                responseTransfer.setMessage(result.toString());
                responseTransfer.setContent(result);
            } catch (Exception e) {
                responseTransfer.setMessage("Error");
            }
            return responseTransfer;
        }).thenApplyAsync(result -> result);
    }

    @RequestMapping(value = "/accounts", method = RequestMethod.POST)
    public Future<String> createWallet(@RequestParam("password") String password) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return web3jProvider.createFtmWallet(password);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).thenApplyAsync(result -> result);
    }


}




package com.blockchain.fantom;

import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.EthAccounts;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthTransaction;

import java.time.Instant;
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
    public Future<ResponseTransfer> getBlock() {
        ResponseTransfer responseTransfer = new ResponseTransfer();
       // Instant start = TimeHelper.start();

        return CompletableFuture.supplyAsync(() -> {
            try {
                EthBlockNumber result = web3jProvider.getBlockNumber();
                responseTransfer.setMessage(result.getBlockNumber().toString());
            } catch (Exception e) {
                responseTransfer.setMessage("Error");
            }
            return responseTransfer;
        }).thenApplyAsync(result -> {
           // result.setPerformance(TimeHelper.stop(start));
            return result;
        });
    }

    @PostMapping(value = "/transactions")
    public void doTransaction() {
            try {
                web3jProvider.doTransaction();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


            @RequestMapping(value = "/transactions/{txnHash}", method = RequestMethod.GET)
    public Future<ResponseTransfer> getTransactionDetails(@PathVariable("txnHash") String txnHash) {
        ResponseTransfer responseTransfer = new ResponseTransfer();
        // Instant start = TimeHelper.start();

        return CompletableFuture.supplyAsync(() -> {
            try {
                EthTransaction result = web3jProvider.fetchAccountTransaction(txnHash);
                System.out.println(result.getTransaction().get().getNonce());
                responseTransfer.setMessage(result.getTransaction().get().getBlockNumber().toString());
            } catch (Exception e) {
                responseTransfer.setMessage("Error");
            }
            return responseTransfer;
        }).thenApplyAsync(result -> {
            // result.setPerformance(TimeHelper.stop(start));
            return result;
        });
    }
    @RequestMapping(value = "/accounts", method = RequestMethod.GET)
    public Future<ResponseTransfer> getAccounts() {
        ResponseTransfer responseTransfer = new ResponseTransfer();
       // Instant start = TimeHelper.start();

        return CompletableFuture.supplyAsync(() -> {
            try {
                EthAccounts result = web3jProvider.getEthAccounts();
                responseTransfer.setMessage(result.toString());
                System.out.println(result);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            return responseTransfer;

        }).thenApplyAsync(result -> {
           // result.setPerformance(TimeHelper.stop(start));
            return result;
        });
    }

    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    public Future<ResponseTransfer> getTransactions() {
        ResponseTransfer responseTransfer = new ResponseTransfer();
       // Instant start = TimeHelper.start();
        return CompletableFuture.supplyAsync(() -> {
            try {
                EthGetTransactionCount result = web3jProvider.getTransactionCount();
                responseTransfer.setMessage(result.toString());
            } catch (Exception e) {
                responseTransfer.setMessage("Error");
            }
            return responseTransfer;
        }).thenApplyAsync(result -> {
           // result.setPerformance(TimeHelper.stop(start));
            return result;
        });
    }


}




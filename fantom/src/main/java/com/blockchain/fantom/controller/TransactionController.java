package com.blockchain.fantom.controller;

import com.blockchain.fantom.models.ResponseTransfer;
import com.blockchain.fantom.service.ContractService;
import com.blockchain.fantom.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;


@RestController
@RequestMapping(path = "/fantom/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping()
    public TransactionReceipt doTransaction(
            @RequestParam(value = "username", required = false) String username,
            @RequestParam(value = "toAddress", required = false) String toAddress,
            @RequestParam(value = "value", required = false) BigDecimal value,
            @RequestParam(value = "privateKey", required = false) String privateKey) {
        try {
            return transactionService.doTransaction(username, toAddress, value, privateKey);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @RequestMapping(value = "/{txnHash}", method = RequestMethod.GET)
    public Future<ResponseTransfer<EthTransaction>> getTransactionDetails(@PathVariable("txnHash") String txnHash) {
        ResponseTransfer<EthTransaction> responseTransfer = new ResponseTransfer<>();

        return CompletableFuture.supplyAsync(() -> {
            try {
                EthTransaction result = transactionService.fetchAccountTransaction(txnHash);
                responseTransfer.setContent(result);
            } catch (Exception e) {
                responseTransfer.setMessage("Error");
                throw new RuntimeException(e);
            }
            return responseTransfer;
        }).thenApplyAsync(result -> result);
    }


//    @RequestMapping(value = "/blocks", method = RequestMethod.GET)
//    public Future<ResponseTransfer<EthBlockNumber>> getBlock() {
//        ResponseTransfer<EthBlockNumber> responseTransfer = new ResponseTransfer<>();
//
//        return CompletableFuture.supplyAsync(() -> {
//            try {
//                EthBlockNumber result = transactionService.getBlockNumber();
//                responseTransfer.setMessage(result.getBlockNumber().toString());
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//            return responseTransfer;
//        }).thenApplyAsync(result -> result);
//    }

}

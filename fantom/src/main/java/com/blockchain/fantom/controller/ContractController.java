package com.blockchain.fantom.controller;

import com.blockchain.fantom.contracts.Coin;
import com.blockchain.fantom.service.ContractService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/fantom/contracts")
public class ContractController {

    private final ContractService web3jProvider;

    public ContractController(ContractService web3jProvider) {
        this.web3jProvider = web3jProvider;
    }

    @RequestMapping(value = "/contracts", method = RequestMethod.POST)
    public String deployCoinContract() {
        try {
            return web3jProvider.deployContract();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping(value = "/contracts/{address}")
    public void loadContract(@PathVariable(value = "address") String address) throws Exception {
        Coin coin = web3jProvider.interactWithContract(address);
    }

}

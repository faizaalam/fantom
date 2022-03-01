package com.blockchain.fantom.service;

import com.blockchain.fantom.contracts.Coin;
import com.blockchain.fantom.repository.WalletRepository;
import com.blockchain.fantom.utils.Web3jProvider;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.tx.gas.DefaultGasProvider;
import org.web3j.tx.gas.StaticGasProvider;


import java.math.BigInteger;
// generate contracts Java wrappers
//web3j generate truffle  --truffle-json build\contracts\Coin.json --outputDir src\main\java\ --package com.blockchain.fantom
@Service
public class ContractService {

    private final Web3j web3j = Web3jProvider.connect();


    public String  deployContract() throws Exception {
        BigInteger GAS_LIMIT = BigInteger.valueOf(6721975L);
        BigInteger GAS_PRICE = BigInteger.valueOf(20000000000L);
        Credentials credentials = Credentials.create("4ca71a10c70fe00b2c9c8687ec419c81e07e3938cd483506adc1130d327b0fcc");
        Coin coin = Coin.deploy(web3j, credentials, new StaticGasProvider(GAS_PRICE, GAS_LIMIT)).send();
        return coin.getContractAddress();
    }


    public Coin interactWithContract(String contractAddress) throws Exception {
        Credentials credentials = Credentials.create("4ca71a10c70fe00b2c9c8687ec419c81e07e3938cd483506adc1130d327b0fcc");
      Coin coin =  Coin.load(contractAddress, web3j, credentials, new DefaultGasProvider());
     TransactionReceipt receipt = coin.mint("0xa9070e5d76e12184b8ae15380633515253fd1ffc", BigInteger.valueOf(120)).send();
        System.out.println(receipt);
        TransactionReceipt receipt2 = coin.send("0x98fb6ce5995b30563f334115959d97f56aeb4630", BigInteger.valueOf(12)).send();

        System.out.println(receipt2);
      return coin;
   }







}


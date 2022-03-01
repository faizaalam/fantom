package com.blockchain.fantom.repository;

import com.blockchain.fantom.models.UserWallet;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WalletRepository extends MongoRepository<UserWallet, String> {
    Optional<UserWallet> findByUserName(String userName);

}

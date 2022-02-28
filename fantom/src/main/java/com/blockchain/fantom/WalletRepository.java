package com.blockchain.fantom;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface WalletRepository extends MongoRepository<UserWallet, String> {
    Optional<UserWallet> findByUserName(String userName);

}

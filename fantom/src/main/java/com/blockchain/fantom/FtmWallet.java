package com.blockchain.fantom;

import java.io.Serializable;

public class FtmWallet implements Serializable {
    public String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

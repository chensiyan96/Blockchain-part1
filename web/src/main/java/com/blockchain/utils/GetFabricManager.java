package com.blockchain.utils;

import provider.BlockChainServiceImpl;

public class GetFabricManager {
    private static BlockChainServiceImpl blockChainService = null;

    public static BlockChainServiceImpl getBlockChainService() {
        if (blockChainService == null) {
            blockChainService = new BlockChainServiceImpl();
        }
        return blockChainService;
    }
}

package org.poo.main;

import lombok.Getter;

public class TransactionResult {
    @Getter
    private final boolean success;
    private final boolean replaceCard;

    public TransactionResult(boolean success, boolean replaceCard) {
        this.success = success;
        this.replaceCard = replaceCard;
    }

    public boolean shouldReplaceCard() {
        return replaceCard;
    }
}

package org.poo.main;

import lombok.Getter;

public final class TransactionResult {
    @Getter
    private final boolean success;
    private final boolean replaceCard;

    public TransactionResult(final boolean success, final boolean replaceCard) {
        this.success = success;
        this.replaceCard = replaceCard;
    }

    /**
     * @return True if the card should be replaced, otherwise false.
     */
    public boolean shouldReplaceCard() {
        return replaceCard;
    }
}

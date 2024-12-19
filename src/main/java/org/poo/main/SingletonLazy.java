package org.poo.main;


public final class SingletonLazy {
    private static Bank instance = null;
    private SingletonLazy() { }

    /**
     * Provides global access to the single instance of the Bank class
     * @return The single, static instance of the Bank class
     */
    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }
}

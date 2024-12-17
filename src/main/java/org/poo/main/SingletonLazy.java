package org.poo.main;


public class SingletonLazy {
    private static Bank instance = null;
    private SingletonLazy() { }
    public static Bank getInstance() {
        if (instance == null) {
            instance = new Bank();
        }
        return instance;
    }
}

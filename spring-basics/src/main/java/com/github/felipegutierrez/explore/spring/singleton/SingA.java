package com.github.felipegutierrez.explore.spring.singleton;

public class SingA {
    private static SingA instance;

    private SingA() {
        super();
    }

    public static SingA getInstance() {
        if (null == instance) {
            synchronized (SingA.class) {
                if (null == instance) {
                    instance = new SingA();
                }
            }
        }
        return instance;
    }
}

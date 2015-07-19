package com.getMyParking.service.guice;

import com.google.inject.Injector;

/**
 * Created by rahulgupta.s on 19/07/15.
 */
public class GuiceHelper {
    private static Injector injector;

    public static Injector getInjector() {
        return injector;
    }

    public static void setInjector(Injector injector) {
        GuiceHelper.injector = injector;
    }
}

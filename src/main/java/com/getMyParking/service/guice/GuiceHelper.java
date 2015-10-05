package com.getMyParking.service.guice;

import com.google.inject.Injector;

/**
 * Created by rahulgupta.s on 23/08/15.
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

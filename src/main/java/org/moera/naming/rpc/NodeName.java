package org.moera.naming.rpc;

public interface NodeName {

    static NodeName parse(String name) {
        return RegisteredName.parse(name);
    }

    static String shorten(String name) {
        return RegisteredName.shorten(name);
    }

    static String expand(String name) {
        return RegisteredName.expand(name);
    }

}

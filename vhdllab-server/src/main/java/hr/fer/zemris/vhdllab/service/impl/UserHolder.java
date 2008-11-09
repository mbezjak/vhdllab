package hr.fer.zemris.vhdllab.service.impl;

import hr.fer.zemris.vhdllab.entities.Caseless;

public class UserHolder {

    private static ThreadLocal<Caseless> holder = new ThreadLocal<Caseless>();

    public static void setUser(Caseless user) {
        UserHolder.holder.set(user);
    }

    public static Caseless getUser() {
        Caseless user = holder.get();
        if (user == null) {
            user = new Caseless("no-user");
        }
        return user;
    }

}

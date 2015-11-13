package org.wisdom.notejam.controller;

import org.wisdom.api.annotations.Service;
import org.wisdom.api.http.Context;
import org.wisdom.api.http.Result;
import org.wisdom.api.http.Results;
import org.wisdom.api.security.Authenticator;

/**
 * sample
 * User: jennifer
 * Date: 04/09/14
 * Time: 10:24
 */
@Service
public class NoteJamAuth implements Authenticator {

    public String getName() {
        return "myAuth";
    }


    public String getUserName(Context context) {

        if(context.session().get("email") != null){
            return context.session().get("email");
        }
        return null;
    }


    public Result onUnauthorized(Context context) {
        System.out.println("Redirecting...");
        return Results.redirect("/signin");
    }
}

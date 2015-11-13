package org.wisdom.notejam.controller.user;

import org.apache.felix.ipojo.annotations.Requires;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.Controller;
import org.wisdom.api.annotations.FormParameter;
import org.wisdom.api.annotations.Route;
import org.wisdom.api.annotations.View;
import org.wisdom.api.content.Json;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.security.Authenticated;
import org.wisdom.api.templates.Template;
import org.wisdom.notejam.model.User;
import org.wisdom.notejam.service.UserService;

/**
 * TODO: using the back button doesnt rediret if your signed out
 * todo: sign in and sign up buttons are displayed even if your signed in
 */
@Controller
public class UserController extends DefaultController {

    @View("user/signup")
    Template signup;

    @View("user/signin")
    Template signin;

    @View("user/forgot-password")
    Template forgotPassword;

    @View("user/account-settings")
    Template settings;

    @View("base")
    Template mainPage;

    @Requires
    UserService userService;

    @Requires
    Json json;

    private String key = "message";
    private String message = "";

    /**
     *
     * @return
     */
    @Route(method = HttpMethod.GET, uri = "/signup")
    public Result signupDisplay() {
        //flash("message","");
        flash(key, message);
        return ok(render(signup));
    }

    /**
     *
     * @param email
     * @param pwd
     * @param pwd2
     * @return
     */
    private String getErrorMgs(String email, String pwd, String pwd2) {
        String msg = "";
        if (email.isEmpty()) {
            msg = "all fields are required";
        }
        if (pwd.isEmpty()) {
            msg = "all fields are required";
        }
        if (pwd2.isEmpty()) {
            msg = "all fields are required";
        }
        if (!pwd.equals(pwd2)) {
            msg = "Passwords do not match";
        }
        return msg;
    }

    /**
     *
     * @param email
     * @param pwd
     * @param cpwd
     * @return
     */
    @Route(method = HttpMethod.POST, uri = "/signup")
    public Result signupProcess(@FormParameter("email") String email,
                                @FormParameter("password") String pwd,
                                @FormParameter("confirmPassword") String cpwd) {
        message = getErrorMgs(email, pwd, cpwd);
        if (!message.isEmpty()) {
            flash(key, message);
            return ok(render(signup));
        }
        User u = userService.createUser(email, pwd);
        if (u != null) {
            return redirect("/signin");
        } else {
            flash(key, "Password must be atleast 6 characters, " +
                    " and/or user name must be valid email format");
            return ok(render(signup));
        }

    }

    /**
     *
     * @return
     */
    @Route(method = HttpMethod.GET, uri = "/signin")
    public Result signinDisplay() {
        flash("message", "");
        return ok(render(signin, "title", "Sign-in"));
    }

    /**
     *
     * @param email
     * @param pwd
     * @return
     */
    @Route(method = HttpMethod.POST, uri = "/signin")
    public Result signinProcess(@FormParameter("email") String email,
                                @FormParameter("password") String pwd) {

        User u = userService.findUserByEmail(email);
        System.out.println("u is: "+u);

       /* if (u != null) {
            if (u.getPassword().equals(pwd)) {
                //set session
                session("email", email);
                return redirect("/");
            }
        }  */
        if(userService.checkPassword(u,pwd)){
            session("email", email);
            return redirect("/");
        }
        flash(key, "Incorrect user name or password");
        return ok(render(signin, "title", "Sign-in"));
    }

    /**
     *
     * @return
     */
    @Authenticated("myAuth")
    @Route(method = HttpMethod.GET, uri = "/signout")
    public Result signoutProcess() {
        session().clear();
        flash("message", "");
        return ok(render(signin, "title", "You're Signed out"));
    }

    @Authenticated("myAuth")
    @Route(method = HttpMethod.GET, uri = "/forgot-password")
    public Result forgotPasswordDisplay() {
        return ok(render(forgotPassword, "title", "Forgot Password"));
    }

    @Authenticated("myAuth")
    @Route(method = HttpMethod.POST, uri = "/forgot-password")
    public Result forgotPasswordProcess() {
        return ok();
    }

    /**
     *
     * @return
     */
    @Authenticated("myAuth")
    @Route(method = HttpMethod.GET, uri = "/settings")
    public Result settingsDisplay() {
        return ok(render(settings, "title", "Change Password"));
    }

    /**
     * Would be nice to have a suseccsfull msg
     * @param pwd
     * @param newpwd
     * @param confirmpwd
     * @return
     */
    @Authenticated("myAuth")
    @Route(method = HttpMethod.POST, uri = "/settings")
    public Result settingsProcess(@FormParameter("pwd") String pwd,
                                  @FormParameter("newpwd") String newpwd,
                                  @FormParameter("confirmpwd") String confirmpwd) {
        message = getErrorMgs(pwd, newpwd, confirmpwd);
        if (message.isEmpty()) {
            if (userService.findUserByEmail(session().get("email")).getPassword().equals(pwd)) {
                userService.changePassword(userService.findUserByEmail(session().get("email")),
                        newpwd);
                message="";
                return ok(render(settings, "title", "Password has been changed"));
            } else {
                message= "Current password is incorrect";
            }
        }

        flash(key,message);
        return ok(render(settings, "title", "Change Password try again"));
    }


    /**
     * FOR DEBUGGING DELETE ME or add to admin functionality?
     */
    @Route(method = HttpMethod.GET, uri = "/list/users")
    public Result listUsers() {

        return ok(userService.listAllUsers()).json();
    }

    @Route(method = HttpMethod.GET, uri = "/list/users/delete")
    public Result deleteUsers() {

        userService.deleteAll();
        return ok(userService.listAllUsers()).json();
    }

}



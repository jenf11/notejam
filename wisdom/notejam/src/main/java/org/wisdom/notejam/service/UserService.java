package org.wisdom.notejam.service;

import org.wisdom.notejam.model.User;

import java.util.List;

/**
 * org.wisdom.notejam.service
 * User: jennifer
 * Date: 04/09/14
 * Time: 14:48
 */
public interface UserService {

    void changePassword(User user, String pwd);

    User findUserByEmail(String email);

    boolean checkUserName(String name);

    boolean checkPassword(User user, String password);

    User createUser(String email, String password);

    List<User> listAllUsers();//fordebugging


    void deleteAll();
}

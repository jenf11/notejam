package org.wisdom.notejam.controller.user;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import javassist.util.proxy.Proxy;

import org.apache.felix.ipojo.annotations.Requires;
import org.wisdom.api.annotations.Model;
import org.wisdom.api.annotations.Service;
import org.wisdom.api.crypto.Crypto;
import org.wisdom.notejam.model.User;
import org.wisdom.notejam.service.UserService;
import org.wisdom.orientdb.object.OrientDbCrud;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * sample
 * User: jennifer
 * Date: 04/09/14
 * Time: 10:02
 */
@Service
public class UserManager implements UserService {
    static {Class workaround = Proxy.class;}
    @Model(value = User.class)
    private OrientDbCrud<User, String> userCrud;



    public UserManager(){
        //constructor
    }


    public void changePassword(User user, String pwd){

        user.setPassword(pwd);
       user= userCrud.save(user);

    }

    public User findUserByEmail(String email){

        List<User> list = new LinkedList<User>();
        if(email !=null && !email.isEmpty()) {
            List<User> query = userCrud.query(new OSQLSynchQuery<User>("select * " + "from User where" +
                    " email like '" + email + "'"));

            list.addAll(query);
        }
        if(list.isEmpty()){

            return null;
        }
        else{

            return list.get(0);
        }


    }


    public boolean checkUserName(String name){
        return true;

    }

    public boolean checkPassword(User user, String password){
        if(user !=null && !password.isEmpty()) {
            System.out.println(crypto.encryptAES(password));
            if (userCrud.findOne(user.getId()).getPassword().equals(crypto.encryptAES(password))) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Requires
    Validator validator;

    @Requires
    Crypto crypto;


    public User createUser(String email, String password) {

        User u = new User();
        u.setEmail(email);
       u.setPassword(crypto.encryptAES(password));
       //u.setPassword(password);

        Set<ConstraintViolation<User>> list = validator.validate(u);
        if (list.isEmpty()) {
            u = userCrud.save(u);
            System.out.println("user created "+u.getId());
            return u;
        } else {
            return null;
        }
    }


    public List<User> listAllUsers() {

        List<User> list = new LinkedList<User>();
        List<User> query = userCrud.query(new OSQLSynchQuery<User>("select * " + "from User "));

        list.addAll(query);
        return list;
    }


   public void deleteAll(){
       List<User> list = listAllUsers();
       for(int i =0;i<list.size();i++) {
           userCrud.delete(list.get(i).getId());
       }
   }
}

package org.wisdom.notejam.controller.user;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import javassist.util.proxy.Proxy;
import org.apache.felix.ipojo.annotations.Component;
import org.apache.felix.ipojo.annotations.Instantiate;
import org.apache.felix.ipojo.annotations.Provides;
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
@Component
@Provides
@Instantiate

public class UserManager implements UserService {

    @Model(value = User.class)
    private OrientDbCrud<User, String> userCrud;

    Class klass = Proxy.class;

    public UserManager(){
        //constructor
    }

    @Override
    public void changePassword(User user, String pwd){

        user.setPassword(pwd);
       user= userCrud.save(user);

    }
    @Override
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

    @Override
    public boolean checkUserName(String name){
        return true;

    }
    @Override
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

    @Override
    public User createUser(String email, String password) {

        User u = new User();
        u.setEmail(email);
       u.setPassword(crypto.encryptAES(password));
       //u.setPassword(password);

        Set<ConstraintViolation<User>> list = validator.validate(u);
        if (list.isEmpty()) {
            u = userCrud.save(u);

            return u;
        } else {
            return null;
        }
    }

    @Override
    public List<User> listAllUsers() {

        List<User> list = new LinkedList<User>();
        List<User> query = userCrud.query(new OSQLSynchQuery<User>("select * " + "from User "));

        list.addAll(query);
        return list;
    }

   @Override
   public void deleteAll(){
       List<User> list = listAllUsers();
       for(int i =0;i<list.size();i++) {
           userCrud.delete(list.get(i).getId());
       }
   }
}

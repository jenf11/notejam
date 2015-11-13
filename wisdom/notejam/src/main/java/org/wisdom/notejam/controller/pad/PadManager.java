package org.wisdom.notejam.controller.pad;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import javassist.util.proxy.Proxy;
import org.apache.felix.ipojo.annotations.Requires;
import org.wisdom.api.annotations.Model;
import org.wisdom.api.annotations.Service;
import org.wisdom.notejam.model.Pad;
import org.wisdom.notejam.model.User;
import org.wisdom.notejam.service.PadService;
import org.wisdom.notejam.service.UserService;
import org.wisdom.orientdb.object.OrientDbCrud;

import java.util.LinkedList;
import java.util.List;

/**
 * org.wisdom.notejam.controller
 * User: jennifer
 * Date: 04/09/14
 * Time: 15:08
 */
@Service
public class PadManager implements PadService {
    static {Class workaround = Proxy.class;}
    @Model(value = Pad.class)
    private OrientDbCrud<Pad,String> padCrud;
    @Requires
    UserService users;


    public List<Pad> listAllPads() {
        List<Pad> list = new LinkedList<Pad>();
        List<Pad> query = padCrud.query(new OSQLSynchQuery<Pad>("select * " + "from Pad "));

        list.addAll(query);
        return list;
    }



    public Pad createPad(String name, String email) {
       // System.out.println("inside create pad name : "+name + " -email : "+email);
        User user = users.findUserByEmail(email);
        if (user == null) {
            return null;
        } else {
            return createPad(name, user);
        }
    }


    public Pad createPad(String name, User user) {
        if(name.isEmpty() || user == null){
            return null;
        }
        Pad newPad = new Pad();
        newPad.setUser(user);
        newPad.setName(name);
        newPad = padCrud.save(newPad);

        return newPad;
    }


    public void deletePad(String id) {
        padCrud.delete(id);

    }


    public Pad editPad(String name, String id) {
        Pad p = padCrud.findOne(id);
        p.setName(name);
        p = padCrud.save(p);
        return p;
    }


    public Pad getPad(String id) {
        return padCrud.findOne(id);
    }


    public List<Pad> listAllPads(User user) {
        List<Pad> list = new LinkedList<Pad>();
        List<Pad> query = padCrud.query(new OSQLSynchQuery<Pad>("select * " + "from Pad where " +
                "user = '"+user.getId()+"'"));

        list.addAll(query);

        return  list;

    }
}

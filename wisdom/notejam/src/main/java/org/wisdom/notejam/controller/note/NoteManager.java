package org.wisdom.notejam.controller.note;

import com.orientechnologies.orient.core.sql.query.OSQLSynchQuery;
import org.apache.felix.ipojo.annotations.Requires;
import org.wisdom.api.annotations.Model;
import org.wisdom.api.annotations.Service;
import org.wisdom.notejam.model.Note;
import org.wisdom.notejam.model.Pad;
import org.wisdom.notejam.model.User;
import org.wisdom.notejam.service.NoteService;
import org.wisdom.notejam.service.UserService;
import org.wisdom.orientdb.object.OrientDbCrud;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
public class NoteManager implements NoteService {

    @Model(value = Note.class)
    private OrientDbCrud<Note,String> noteCrud;
    @Requires
    UserService users;

    @Requires
    Validator validator;


    @Override
    public Note createNote(String name, String text, Pad pad_id) {
        if(name.isEmpty() || text.isEmpty()){
            return null;
        }
        Date dateobj = new Date();
        Note n = new Note();
        n.setName(name);
        n.setText(text);
        n.setPad(pad_id);
        n.setUser(pad_id.getUser());
        n.setCreated_at(dateobj);
        n = updateNote(n);

        return n;

    }

    @Override
    public void deleteNote(String id) {
        noteCrud.delete(id);

    }

    @Override
    public Note editNote(String note_id, String name, String text, Pad pad) {
        if(name.isEmpty() || text.isEmpty()){
            return null;
        }
        Note n = noteCrud.findOne(note_id);
        n.setName(name);
        n.setPad(pad);
        n.setText(text);
       n.getCreated_at();
        n.getUser();
        n = updateNote(n);

        return n;
    }

    private Note updateNote(Note n){
        Date dateobj = new Date();
        n.setUpdated_at(dateobj);
        Set<ConstraintViolation<Note>> list = validator.validate(n);
        if (list.isEmpty()) {
            n=noteCrud.save(n);
            return n;
        } else {
            ConstraintViolation<Note> violation = list.iterator().next();
//            System.out.println("note validation problem"+violation.getMessage() + " / " +
  //                  violation.getPropertyPath().toString());
            return null;
        }


    }

    @Override
    public Note getNote(String Id) {
        return noteCrud.findOne(Id);
    }

    @Override
    public List<Note> listAllNotes(User user) {
        List<Note> list = new LinkedList<Note>();
        if(user !=null) {
            List<Note> query = noteCrud.query(new OSQLSynchQuery<Note>("select * " + "from Note where" +
                    " " +
                    "user = '" + user.getId() + "'"));

            list.addAll(query);
        }
        return  list;
    }

    @Override
    public List<Note> listNoteByPad(User user, Pad pad) {
        List<Note> list = new LinkedList<Note>();
        if(user != null && pad !=null) {
            List<Note> query = noteCrud.query(new OSQLSynchQuery<Note>("select * " + "from Note where" +
                    " " +
                    "user = '" + user.getId() + "' and pad = '" + pad.getId() + "'"));

            list.addAll(query);
        }
        return  list;

    }

    @Override
    public List<Note> listAllNotes() {

        List<Note> list = new LinkedList<Note>();

        List<Note> query = noteCrud.query(new OSQLSynchQuery<Note>("select * " + "from Note "));

        list.addAll(query);

        return  list;

    }
}

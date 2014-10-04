package org.wisdom.notejam.controller.note;

import org.apache.felix.ipojo.annotations.Requires;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.*;
import org.wisdom.api.http.HttpMethod;
import org.wisdom.api.http.Result;
import org.wisdom.api.security.Authenticated;
import org.wisdom.api.templates.Template;
import org.wisdom.notejam.model.Note;
import org.wisdom.notejam.model.Pad;
import org.wisdom.notejam.model.User;
import org.wisdom.notejam.service.NoteService;
import org.wisdom.notejam.service.PadService;
import org.wisdom.notejam.service.UserService;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

/**
 * sample
 * User: jennifer
 * Date: 03/09/14
 * Time: 10:50
 */

@Controller
@Path("/notes")
public class NoteController extends DefaultController {

    @View("note/create")
    Template create;

    @View("note/delete")
    Template delete;

    @View("note/edit")
    Template edit;

    @View("note/view")
    Template view;

    @Requires
    NoteService noteService;

    @Requires
    UserService userService;

    @Requires
    PadService padService;

    private List<Pad> padList;

    private String successKey ="success";
    private String errorKey ="errormsg";
    private String message = "";

    private void generateList() {
        if (userService.findUserByEmail(session().get
                ("email")) != null) {
            padList = padService.listAllPads(userService.findUserByEmail(session().get
                    ("email")));
        }

    }
    @Authenticated("myAuth")
    @Route(method = HttpMethod.GET, uri = "/create")
    public Result createNoteDisplay() {

        generateList();
        return ok(render(create, "title","create a note", "padList",padList));
    }
    @Authenticated("myAuth")
    @Route(method = HttpMethod.POST, uri = "/create")
    public Result createNoteProcess(@FormParameter("name") String name,
                                    @FormParameter("content") String content,
                                    @FormParameter("pad_id") String pad_id) {

        if(noteService.createNote(name, content, padService.getPad(pad_id))!=null) {
            return redirect("/");
        }
        flash(errorKey,"An Error has occured while updating the note");
        return ok(render(create, "title","create a note", "padList",padList));
    }
    @Authenticated("myAuth")
    @Route(method = HttpMethod.GET, uri = "/view/{note_id}")
    public Result viewNote(@Parameter("note_id") String note_id) {
        generateList();
        return ok(render(view, "title",noteService.getNote(note_id).getName(),"note",
                noteService.getNote(note_id),"padList", padList));
    }
    @Authenticated("myAuth")
    @Route(method = HttpMethod.GET, uri = "/{note_id}/edit")
    public Result editNoteDisplay(@Parameter("note_id") String note_id) {
        generateList();
        return ok(render(edit,"title", "Edit "+noteService.getNote(note_id).getName(),"note",
                noteService.getNote(note_id),"padList", padList));
    }
    @Authenticated("myAuth")
    @Route(method = HttpMethod.POST, uri = "/{note_id}/edit")
    public Result editNoteProcess(@Parameter("note_id") String note_id,
                                  @FormParameter("name") String name,
                                  @FormParameter("content") String content,
                                  @FormParameter("pad_id") String pad_id) throws UnsupportedEncodingException {
       if( noteService.editNote(note_id,name,content,padService.getPad(pad_id))!=null){
           return redirect("/notes/view/"+ URLEncoder.encode(note_id,"UTF-8"));
       }

        flash(errorKey,"OMG");
        return ok(render(edit,"title", "Edit "+noteService.getNote(note_id).getName(),"note",
                noteService.getNote(note_id),"padList", padList));


    }
    @Authenticated("myAuth")
    @Route(method = HttpMethod.GET, uri = "/{note_id}/delete")
    public Result deleteNoteDisplay(@Parameter("note_id") String note_id) {
        noteService.deleteNote(note_id);
        return redirect("/");
    }
    @Authenticated("myAuth")
    @Route(method = HttpMethod.POST, uri = "/{note_id}/delete")
    public Result deleteNoteProcess(@Parameter("note_id") String note_id) {
        return ok();
    }
    @Authenticated("myAuth")
    @Route(method = HttpMethod.GET, uri="/all")
    public Result showAllNotes(){
        return ok();
    }

    /**
     * DEBUGGING
     * @return
     */
    @Route(method = HttpMethod.GET, uri = "/list/notes")
    public Result listNotes() {

        User u = userService.findUserByEmail(session().get("email"));

        return ok(noteService.listAllNotes()).json();

    }

}

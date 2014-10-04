package org.wisdom.notejam.controller.pad;

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
import java.util.LinkedList;
import java.util.List;

/**
 * sample
 * User: jennifer
 * Date: 03/09/14
 * Time: 10:49
 */
@Controller
@Path("/pad")
public class PadController extends DefaultController {

    @View("pad/create-pad")
    Template createPad;

    @View("pad/pad-notes")
    Template padNotes;

    @View("pad/edit-pad")
    Template editPad;

    @Requires
    PadService padService;

    @Requires
    UserService userService;

    @Requires
    NoteService noteService;

    private List<Pad> padList;
    private String successKey ="success";
    private String errorKey ="errormsg";
    private String message = "";


    @Route(method = HttpMethod.GET, uri = "/create")
    @Authenticated("myAuth")
    public Result createPadDisplay() {
        System.out.println("in create");
       generateList();

        return ok(render(createPad, "title", "Create new pad", "padList", padList));
        //return ok();
    }

    @Authenticated("myAuth")
    @Route(method = HttpMethod.POST, uri = "/create")
    public Result createPadProcess(@FormParameter("name") String name) {
        if (session().get("email") != null) {
            if (padService.createPad(name, session().get("email")) != null) {
                return redirect("/pad/create");
            }
            flash("errormsg", "All fields are required.");
        }

        return ok(render(createPad, "title", "Create new pad"));
    }

    @Route(method = HttpMethod.GET, uri = "/view/{pad_id}")
    public Result viewPad(@Parameter("pad_id") String pad_id) {
        generateList();

        List<Note> noteList = noteService.listNoteByPad(userService
                .findUserByEmail
                        (session().get
                                ("email")), padService.getPad(pad_id));

        //should return a page that list all the notes underneath
        return ok(render(padNotes, "title", padService.getPad(pad_id).getName(), "padList", padList,
                "noteList",
                noteList,"padid",pad_id));
    }

    @Route(method = HttpMethod.GET, uri = "/{pad_id}/edit")
    public Result editPadDisplay(@Parameter("pad_id") String pad_id) {
        generateList();
        //edit the name of the pad that changes the name for all the notes also
        return ok(render(editPad, "title","Rename Pad "+padService.getPad(pad_id).getName(),
                "pad_id", pad_id, "padList", padList));
    }

    @Route(method = HttpMethod.POST, uri = "/{pad_id}/edit")
    public Result editPadProcess(@FormParameter("name") String name, @Parameter("pad_id") String
            pad_id) throws UnsupportedEncodingException {

        padService.editPad(name,pad_id);
        //in the show pad page two buttons delete and edit
        return redirect("/pad/"+ URLEncoder.encode(pad_id,"UTF-8"));
    }

    @Route(method = HttpMethod.GET, uri = "/{pad_id}/delete")
    public Result deletePadDisplay(@Parameter("pad_id") String pad_id) {
        //in the show pad page two buttons delete and edit
        padService.deletePad(pad_id);
        return redirect("/");
    }

    @Route(method = HttpMethod.POST, uri = "/{pad_id}/delete")
    public Result deletePadProcess() {
        //delete the pad and delete all of the notes attached
        return redirect("/");
    }


    /**
     * FOR DEBUGGING DELETE ME or add to admin functionality?
     */
    @Route(method = HttpMethod.GET, uri = "/list/pads")
    public Result listUsers() {

        return ok(padService.listAllPads()).json();
    }

    private void generateList() {
        if (userService.findUserByEmail(session().get
                ("email")) != null) {
            padList = padService.listAllPads(userService.findUserByEmail(session().get
                    ("email")));
        }

    }

}

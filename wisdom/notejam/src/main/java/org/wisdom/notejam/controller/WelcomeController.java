/*
 * #%L
 * Wisdom-Framework
 * %%
 * Copyright (C) 2013 - 2014 Wisdom Framework
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package org.wisdom.notejam.controller;

import org.apache.felix.ipojo.annotations.Requires;
import org.wisdom.api.DefaultController;
import org.wisdom.api.annotations.Controller;
import org.wisdom.api.annotations.Route;
import org.wisdom.api.annotations.View;
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

import java.util.LinkedList;
import java.util.List;

/**
 * Your first Wisdom Controller.
 */
@Controller
public class WelcomeController extends DefaultController {

    /**
     * Injects a template named 'welcome'.
     */
    @View("base")
    Template mainPage;

    @Requires
    PadService padService;
    @Requires
    UserService userService;

    @Requires
    NoteService noteService;



    /**
     * The action method returning the welcome page. It handles
     * HTTP GET request on the "/" URL.
     *
     * @return the welcome page
     */
    @Route(method = HttpMethod.GET, uri = "/")
    @Authenticated("myAuth")
    public Result go(){
        List<Pad> padList = new LinkedList<Pad>();
        List<Note> noteList = new LinkedList<Note>();

        String email = session().get("email");


       User u = userService.findUserByEmail(session().get
               ("email"));

        if(userService.findUserByEmail(session().get
                ("email"))!=null){
            padList = padService.listAllPads(u);    //should be user specific
            noteList = noteService.listAllNotes(userService.findUserByEmail(session().get
                    ("email")));

        }

        return ok(render(mainPage, "title","All notes ("+noteList.size()+")", "padList",padList,
                "noteList",
                noteList));
    }

}

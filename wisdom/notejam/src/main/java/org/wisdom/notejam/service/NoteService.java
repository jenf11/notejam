package org.wisdom.notejam.service;

import org.wisdom.notejam.model.Note;
import org.wisdom.notejam.model.Pad;
import org.wisdom.notejam.model.User;

import java.util.List;


public interface NoteService {

    Note createNote(String name, String text, Pad pad_id);
    void deleteNote(String id);
    Note editNote(String note_id, String name, String text, Pad pad);
    Note getNote (String Id);
    List<Note> listAllNotes(User user);
    List<Note> listNoteByPad(User user, Pad pad);
    List<Note> listAllNotes();

}

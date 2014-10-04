package org.wisdom.notejam.service;

import org.wisdom.notejam.model.Note;
import org.wisdom.notejam.model.Pad;
import org.wisdom.notejam.model.User;

import java.util.List;

/**
 * org.wisdom.notejam.service
 * User: jennifer
 * Date: 04/09/14
 * Time: 15:07
 */
public interface PadService {
    List<Pad> listAllPads();
    List<Pad> listAllPads(User user);
    Pad createPad(String name, String email);
    Pad createPad(String name, User user);
    void deletePad(String id);
    Pad editPad(String name, String id);
    Pad getPad (String id);
}


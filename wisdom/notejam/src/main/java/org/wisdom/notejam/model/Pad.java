package org.wisdom.notejam.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * sample
 * User: jennifer
 * Date: 02/09/14
 * Time: 11:50
 */

@Entity
@JsonIgnoreProperties("handler")
public class Pad {

    @Id
    private String id;
    @NotNull
    private User user;
    @NotNull
    private String name;

    public Pad(){
    //constructor
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

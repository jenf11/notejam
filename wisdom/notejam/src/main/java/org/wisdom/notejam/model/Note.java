package org.wisdom.notejam.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * sample
 * User: jennifer
 * Date: 02/09/14
 * Time: 11:51
 */

@Entity
@JsonIgnoreProperties("handler")
public class Note {

    @Id
    private String id;
    @ManyToOne
    private Pad pad ;

    @NotNull
    @ManyToOne
    private User user;
    @NotNull
    private String name;
    @NotNull
    private String text;
    @NotNull
    private Date created_at;
    @NotNull
    private Date updated_at;

    public Note (){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Pad getPad() {
        return pad;
    }

    public void setPad(Pad pad) {
        this.pad = pad;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}

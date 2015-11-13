package org.wisdom.notejam.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;

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
public class User {

    @Id
    private String id;

    @Email
    @NotNull
    @Length(min = 3, max = 50)
    private String email;

    @NotNull
    @Length(min = 6)
    private  String password;

    public User(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

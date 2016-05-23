package mainPackage.Domain;

/**
 * Created by Suryaraj on 5/18/2016.
 */
public class Users {
    public Long id;
    String name;
    String email;

    public Users() {
    }

    public Users(Long id, String name, String email) {
        id = this.id;
        name = this.name;
        email = this.email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

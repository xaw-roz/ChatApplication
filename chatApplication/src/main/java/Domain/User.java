package Domain;

/**
 * Created by Suryaraj on 5/18/2016.
 */
public class User {
    public int Id;
    String Name;
    String email;

    public User() {
    }

    public User(int id, String name, String email) {
        Id = id;
        Name = name;
        this.email = email;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

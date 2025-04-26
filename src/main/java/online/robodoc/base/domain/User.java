package online.robodoc.base.domain;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

public class User
{
    @Id
    @GeneratedValue
    private Long id;

    private String username;
    private String password;

    private boolean isAdmin;

    public Long getId()
    {
        return id;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }

    public boolean getisAdmin()
    {
        return isAdmin;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public void setAdmin(boolean admin)
    {
        isAdmin = admin;
    }
}

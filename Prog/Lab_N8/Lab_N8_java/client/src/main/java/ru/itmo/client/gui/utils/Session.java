package ru.itmo.client.gui.utils;

public class Session
{
    private static Session instance;
    private String login;
    private String password;

    private Session() {}

    public static Session getInstance()
    {
        if( instance == null )
        {
            instance = new Session();
        }
        return instance;
    }

    public void setUser( String login, String password )
    {
        this.login = login;
        this.password = password;
    }

    public String getLogin() { return login; }
    public String getPassword() { return password; }
}
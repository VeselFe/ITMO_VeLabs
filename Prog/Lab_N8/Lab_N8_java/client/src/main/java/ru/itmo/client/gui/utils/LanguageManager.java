package ru.itmo.client.gui.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class LanguageManager
{
    private static LanguageManager instance;
    private ResourceBundle bundle;
    private Locale currentLocale;

    private LanguageManager()
    {
        setLocale(new Locale("ru", "RU"));
    }

    public static LanguageManager getInstance()
    {
        if (instance == null) instance = new LanguageManager();
        return instance;
    }

    public void setLocale(Locale locale)
    {
        this.currentLocale = locale;
        this.bundle = ResourceBundle.getBundle("messages", locale);
    }

    public Locale getLocale() { return currentLocale; }
    public String getString(String key) { return bundle.getString(key); }
}
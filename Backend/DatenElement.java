package Backend;

import java.io.Serializable;

public class DatenElement implements Serializable
{
    private String r;
    private String v;
    private int level;

    public DatenElement(String ruckseite, String vorderseite)
    {
        r = ruckseite;
        v= vorderseite;
        level = 0;
    }

    // Parametern ruckseite und vorderseite
    public String rgeben()
    {
        return r;
    }
    public String vgeben()
    {
        return v;
    }
    public void rsetzen(String ruck)
    {
        r = ruck;
    }
    public void vsetzen(String vorder)
    {
        v = vorder;
    }
    public int lgeben()
    {
        return level;
    }
    public void levelsteigen()
    {
        if(level != 100)
        {
            level = level + 2;
        }
        else
        {
            level = level;
        }
    }
    public void levelsinken()
    {
        if(level == 0)
        {
            level = 0;
        }
        else
        {
            level = level - 2;
        }
    }
}
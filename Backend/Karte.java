package Backend;

import java.io.Serializable;

public class Karte implements Serializable
{

    private DatenElement daten;

    private Karte nachfolger;


    public Karte(DatenElement d)
    {
        daten = d;
    }

    public String ausgebenr()
    {
        return daten.rgeben();
    }
    public String ausgebenv()
    {
        return daten.vgeben();
    }


    public int restLaenge(int anzahl)
    {

        if(nachfolger == null)
        {
            return anzahl;
        }
        else
        {
            anzahl = anzahl + 1;
            return nachfolger.restLaenge(anzahl);
        }
    }
    public void hintenEinfuegen(DatenElement dneu)
    {
        if (nachfolger != null)
        {
            nachfolger.hintenEinfuegen(dneu);
        }
        else
        {
            Karte kneu;
            kneu = new Karte(dneu);
            nachfolger = kneu;
        }
    }

    public Karte einfuegenVor(DatenElement dneu, DatenElement dvergleich)
    {
        if (daten != dvergleich)
        {
            if (nachfolger != null)
            {
                nachfolger.einfuegenVor(dneu, dvergleich);
            }
            else
            {
                new Karte(dneu);
            }
            return this;
        }
        else
        {
            new Karte(dneu);
        }

        return null;
    }
    public Karte besteGeben(Karte beste)
    {
        if(daten.lgeben() > daten.lgeben())
        {
            Karte k = beste;
        }
        else{
            if(daten.lgeben()> daten.lgeben())
            {
                Karte nachfolger = beste;
            }
        }
        return beste;
    }
    public Karte schlechtesteGeben(Karte schlechteste)
    {
        if(daten.lgeben() < daten.lgeben())
        {
            Karte k = schlechteste;
        }
        else{
            if(daten.lgeben() < daten.lgeben())
            {
                Karte nachfolger = schlechteste;
            }
        }
        return schlechteste;
    }

    public void levelsteigen(Karte  nachfolger)
    {
        daten.levelsteigen();
        nachfolger = new Karte(daten);
    }
    public void levelsenken(Karte nachfolger)
    {
        daten.levelsinken();
        nachfolger = new Karte(daten);
    }
    public Karte nächsteGeben()
    {
        return nachfolger;
    }
    public Karte lernstapelgeben(Karte schlechteste)
    {
        for(int i = 1; i == 10; i++)
        {
            return schlechtesteGeben(schlechteste);
        }
        return null;
    }

}
    

package Backend;

import java.io.Serializable;

public class Stapel implements Serializable {   
    
    
    private Karte anfang;
}
    
    public Stapel(DatenElement daten)
    {
      anfang = null;
    }
    
    public Karte KarteGeben()
    {
        return anfang;
    }

    public int laenge(int anzahl)
    
    {
        anzahl = 0;
      if (anfang == null)
      {
        return 0;
        }
        else
        {
        return anfang.restLaenge(anzahl);
        }
    }
    
     public void einfuegenVor(DatenElement dneu, DatenElement dvergleich)
    {
        if (anfang != null)
        {
            anfang = anfang.einfuegenVor(dneu, dvergleich);
        }
        else
        {
            vorneEinfuegen(dneu);
        }
    }
    public void vorneEinfuegen(DatenElement dneu)
    {
      Karte kneu;
      kneu = new Karte(dneu);
      anfang = kneu;
    }
    }

    //wurde geändert
    public String nameGeben(Karte k){
        return k;
    }

}

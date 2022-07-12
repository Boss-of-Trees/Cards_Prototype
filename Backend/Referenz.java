import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

public class Referenz implements OrdnerElement, Serializable {

    Ordner vorgaenger;
    String name;
    String pfad;

    public Referenz(String name, String pfad, Ordner vorgaenger, Stapel stapel)
    {
        this.name = name;
        this.vorgaenger = vorgaenger;
        this.pfad = pfad + name;
        StapelSpeicherer stsp = new StapelSpeicherer();
        stsp.speichern(pfad, stapel);

    }
    

    public void einfuegen(OrdnerElement neuesElement, Ordner vorgaenger)
    {
        System.out.println("Why you calling this on a reference?");
    }
    

    public void ordnerEntfernen(String name) 
    {
        System.out.println("Why you calling this on a reference?");
    }
    

    public void refernzEntfernen(String name) 
    {
        System.out.println("Why you calling this on a reference?");
    }
    

    public Ordner ordnerGeben(String name) 
    {
        return vorgaenger;
    }
    

    public Stapel stapelGeben(String name)
    {
        StapelSpeicherer stapelSpeicherer = new StapelSpeicherer();
        return stapelSpeicherer.laden(pfad);
    }
    

    public ArrayList<String[]> ordnerStrukturAlsStringGeben()
    {
        return null;
    }
    

    public Ordner vogaengerGeben() 
    {
        return vorgaenger;
    }
    

    public String nameGeben()
    {
        return name;
    }
    

    public void vorgaengerSetzten(Ordner ordner)
    {
        vorgaenger = ordner;
    }
    

    public void entfernen()
    {
        File file = new File(pfad + ".stapel");
        file.delete();
    }

}

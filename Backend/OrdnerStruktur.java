import java.io.Serializable;
import java.util.ArrayList;

public class OrdnerStruktur implements Serializable 
{

    private Ordner root;
    private Ordner aktiverOrdner;
    private String name;

    public OrdnerStruktur(String benutzerName)
    {
        root = new Ordner("root_" + benutzerName, null);
        name = benutzerName;
        aktiverOrdner = root;
    }
    

    public void einfuegen(OrdnerElement neuesElement)
    {
        aktiverOrdnerZuRoot();
        aktiverOrdner.einfuegen(neuesElement, aktiverOrdner.vogaengerGeben());
    }
    

    public void ordnerEntfernen(String name)
    {
        aktiverOrdnerZuRoot();
        aktiverOrdner.ordnerEntfernen(name);
    }
    

    public void referenzEntfernen(String name)
    {
        aktiverOrdnerZuRoot();
        aktiverOrdner.refernzEntfernen(name);
    }
    

    public Ordner ordnerGeben(String name)
    {
        aktiverOrdnerZuRoot();
        if(name.equals(aktiverOrdner.nameGeben()))
        {
            return aktiverOrdner;
        }
        return aktiverOrdner.ordnerGeben(name);
    }
    

    public Ordner aktiverOrdnerGeben()
    {
        aktiverOrdnerZuRoot();
        return aktiverOrdner;
    }
    

    public Stapel stapelGeben(String name)
    {
        aktiverOrdnerZuRoot();
        return aktiverOrdner.stapelGeben(name);
    }
    

    public ArrayList<String[]> ordnerStrukturAlsStringArrayListGeben()
    {
        aktiverOrdnerZuRoot();
        return aktiverOrdner.ordnerStrukturAlsStringGeben();
    }
    

    public void ordnerWechselnRunter(String name)
    {
        aktiverOrdnerZuRoot();
        aktiverOrdner = aktiverOrdner.ordnerGeben(name);
    }
    

    public void ordnerWechselnHoch()
    {
        aktiverOrdnerZuRoot();
        aktiverOrdner = aktiverOrdner.vogaengerGeben();
    }
    

    public String nameGeben()
    {
        return name;
    }
    

    private void aktiverOrdnerZuRoot()
    {
        
        if(aktiverOrdner == null)
        {
            aktiverOrdner = root;
        }
        
    }
    
}

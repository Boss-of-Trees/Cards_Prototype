package Backend;

import java.util.ArrayList;

public interface OrdnerElement {

    public abstract void einfuegen(OrdnerElement neuesElement, Ordner vorgaenger);
    
    public abstract void ordnerEntfernen(String name);
    
    public abstract void refernzEntfernen(String name);
    
    public abstract Ordner ordnerGeben(String name);
    
    public abstract Stapel stapelGeben(String name);
    
    public abstract ArrayList<String[]> ordnerStrukturAlsStringGeben();
    
    public abstract Ordner vogaengerGeben();
    
    public abstract String nameGeben();

    public abstract void vorgaengerSetzten(Ordner ordner);

}

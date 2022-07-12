package Backend;

import java.io.Serializable;
import java.util.ArrayList;

public class Ordner implements OrdnerElement, Serializable {

    Ordner vorgaenger;
    String name;
    ArrayList<Ordner> unterordner;
    ArrayList<String> unterordnerNamen;
    ArrayList<Referenz> referenzen;
    ArrayList<String> referenzNamen;

    public Ordner(String name, Ordner vorgaenger)
    {
        this.name = name;
        if(vorgaenger == null)
        {
            this.vorgaenger = this;
        } 
        else
        {
            this.vorgaenger = vorgaenger;
        }
        unterordner = new ArrayList<>();
        unterordnerNamen = new ArrayList<>();
        referenzen = new ArrayList<>();
        referenzNamen = new ArrayList<>();
    }

    
    public void einfuegen(OrdnerElement neuesElement, Ordner vorgaenger) 
    {
        
        if(neuesElement.getClass().getName() == "Referenz")
        {
            
            if (!referenzNamen.contains(neuesElement.nameGeben()))
            {
                neuesElement.vorgaengerSetzten(this);
                referenzen.add((Referenz) neuesElement);
                referenzNamen.add(neuesElement.nameGeben());
            }
            
        }
        else
        {
            
            if(!unterordnerNamen.contains(neuesElement.nameGeben()))
            {
                neuesElement.vorgaengerSetzten(this);
                unterordner.add((Ordner) neuesElement);
                unterordnerNamen.add(neuesElement.nameGeben());
            }
            
        }

    }
    

    public void ordnerEntfernen(String name) 
    {
        
        if(unterordnerNamen.contains(name))
        {
            
            for(int i = 0; i < unterordnerNamen.size(); i++)
            {
                
                if(unterordnerNamen.get(i).equals(name))
                {
                    unterordnerNamen.remove(i);
                    unterordner.remove(i);
                }
                
            }
            
        }
        
    }
    

    public void refernzEntfernen(String name)
    {
        
        if(referenzNamen.contains(name))
        {
            
            for(int i = 0; i < referenzNamen.size(); i++)
            {
                
                if(referenzNamen.get(i).equals(name))
                {
                    referenzNamen.remove(i);
                    referenzen.get(i).entfernen();
                    referenzen.remove(i);
                }
                
            }
            
        }
        
    }
    

    public Ordner ordnerGeben(String name) 
    {
        
        if(unterordnerNamen.contains(name))
        {
            
            for(int i = 0; i < unterordnerNamen.size(); i++)
            {
                
                if(unterordner.get(i).nameGeben().equals(name))
                {
                    return unterordner.get(i);
                }
                
            }
            
        }
        return null;
    }
    

    public Stapel stapelGeben(String name) 
    {
       
        if(referenzNamen.contains(name))
        {
            
            for(int i = 0; i < referenzNamen.size(); i++)
            {
                
                if(referenzNamen.get(i).equals(name))
                {
                    return referenzen.get(i).stapelGeben(name);
                }
                
            }
            
        }
        return null;
    }
    

    public ArrayList<String[]> ordnerStrukturAlsStringGeben()
    {
        ArrayList<String[]> toBeReturned = new ArrayList<>();
        for(int i = 0; i < unterordnerNamen.size(); i++)
        {
            String[] asString = new String[2];
            asString[0] = "ordner";
            asString[1] = unterordnerNamen.get(i);
            toBeReturned.add(asString);
        }
        
        for(int i = 0; i < referenzNamen.size(); i++)
        {
            String[] asString = new String[2];
            asString[0] = "referenz";
            asString[1] = referenzNamen.get(i);
            toBeReturned.add(asString);
        }
        return toBeReturned;
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

}

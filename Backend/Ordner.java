import java.util.ArrayList;

public class Ordner implements OrdnerElement{

    Ordner vorgaenger;
    String name;
    ArrayList<Ordner> unterordner;
    ArrayList<String> unterordnerNamen;
    ArrayList<Referenz> referenzen;
    ArrayList<String> referenzenNamen;

    public Ordner(String name, Ordner vorgaenger){
        this.name = name;
        if(vorgaenger == null){
            this.vorgaenger = this;
        } else {
            this.vorgaenger = vorgaenger;
        }
    }

    public void einfuegen(OrdnerElement neuesElement, Ordner vorgaenger) {
        if(neuesElement.getClass().getName() == "Referenz"){
            if(!referenzenNamen.contains(neuesElement.nameGeben())){
                neuesElement.vorgaengerSetzten(this);
                referenzen.add((Referenz) neuesElement);
                referenzenNamen.add(neuesElement.nameGeben());
            }
        } else {
            if(!unterordnerNamen.contains(neuesElement.nameGeben())){
                neuesElement.vorgaengerSetzten(this);
                unterordner.add((Ordner) neuesElement);
                unterordnerNamen.add(neuesElement.nameGeben());
            }
        }
    }
    
    public void vorgaengerSetzten(Ordner ordner) {
        vorgaenger = ordner;
    }
     
    public void ordnerEntfernen(String name) {
        if (unterordnerNamen.contains(name))
        {
            for(int i = 0; i < unterordnerNamen.size(); i++){
                if(unterordnerNamen.get(i).equals(name)){
                    unterordnerNamen.remove(i);
                    unterordner.remove(i);
                }
            }
        }
    }

    public void refernzEntfernen(String name) {
        if(referenzenNamen.contains(name))
        {
            for(int i = 0; i < referenzenNamen.size(); i++){
                if(referenzenNamen.get(i).equals(name))
                {
                    referenzenNamen.remove(i);
                    referenzen.remove(i);
                }
                }
            }
        }
        
    public Ordner ordnerGeben(String name){
        if(unterordnerNamen.contains(name))
        {
            for(int i = 0; i < unterordnerNamen.size(); i++)
            {
                if(unterordnerNamen.get(i).equals(name))
                {
                    return unterordner.get(i); 
                }
                }
            }
        return null;
            }
    
    public Stapel stapelGeben(String name) {
        if(referenzenNamen.contains(name))
        {
            for(int i = 0; i < referenzenNamen.size(); i++)
            {
                if(referenzenNamen.get(i).equals(name))
                {
                    return referenzen.get(i).stapelGeben(name); 
                }
                }
            }
        return null;
    }

    public ArrayList<String[]> ordnerStrukturAlsStringGeben() {
        ArrayList<String[]> toBeReturned = new ArrayList<>();
        for (int i = 0 ; i <unterordnerNamen.size(); i++)
        {
            String[] asString = new String[2];
            asString[0] = "ordner";
            asString[1] = unterordnerNamen.get(i);
            toBeReturned.add(asString);
        }
        for (int i = 0 ; i <referenzenNamen.size(); i++)
        {
            String[] asString = new String[2];
            asString[0] = "referenz";
            asString[1] = referenzenNamen.get(i);
            toBeReturned.add(asString);
        }
        return toBeReturned;
        }
    

    public Ordner vogaengerGeben() {
        return vorgaenger;
    }

    public String nameGeben() {
        return name;
    }

}

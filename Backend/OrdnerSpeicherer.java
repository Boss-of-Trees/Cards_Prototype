package Backend;

import java.io.*;

public class OrdnerSpeicherer {

    public OrdnerSpeicherer(){}

    public void speichern(String dateiPfadUndNameOhneEndung, OrdnerStruktur zuSpeicherndeOrdnerStruktur){
        try {
            String fileName = dateiPfadUndNameOhneEndung + zuSpeicherndeOrdnerStruktur.nameGeben() + ".ordner";
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(zuSpeicherndeOrdnerStruktur);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public OrdnerStruktur laden(String dateiPfadUndNameOhneEndung){
        OrdnerStruktur obj;
        try {
            FileInputStream fin = new FileInputStream(dateiPfadUndNameOhneEndung + ".ordner");
            ObjectInputStream oit = new ObjectInputStream(fin);
            obj = (OrdnerStruktur) oit.readObject();
            oit.close();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

}

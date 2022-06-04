package Backend;

import java.io.*;

public class StapelSpeicherer {

    public StapelSpeicherer(){}

    public void speichern(String dateiPfad, Stapel zuSpeichernderStapel){
        try {
            String fileName = dateiPfad + zuSpeichernderStapel.nameGeben() + ".stapel";
            FileOutputStream fileOut = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(zuSpeichernderStapel);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    public Stapel laden(String dateiPfadUndNameOhneEndung){
        Stapel obj = null;
        try {
            FileInputStream fin = new FileInputStream(dateiPfadUndNameOhneEndung + ".stapel");
            ObjectInputStream oit = new ObjectInputStream(fin);
            obj = (Stapel) oit.readObject();
            oit.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return obj;
    }

}

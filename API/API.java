package API;

import Backend.Karte;
import Backend.Ordner;
import Backend.OrdnerElement;
import Backend.Stapel;

import java.util.ArrayList;

public class API {

    private Benutzerverwaltung bv;
    private Ordner momentanerOrdner;
    private Stapel momentanerStapel;

    public API(){
        bv = null;
        momentanerOrdner = null;
        momentanerStapel = null;
    }

    /** Bneutzer Bezogen */
    public void benutzerWechseln(String benutzername){}

    public void benutzerHinzufuegen(String benutzername, String passwort){}

    public Boolean passwortEingabePruefen(String benutzername, String passwort){
        return true;
    }

    /** Ordner Bezogen */
    public void zuOrdnerWechseln(String ordnerName){}

    public ArrayList<OrdnerElement> ordnerInhaltGeben(){
        return null;
    }

    public void ordnerLoeschen(String ordnerName){}

    public void ordnerHinzufuegen(String ordnerName){}

    /** Stapel Bezogen */
    public Stapel abfrageStapelGeben(){
        return null;
    }

    public void stapelErstellen(String stapelName){}

    public void karteHinzufuegen(String vorderseite, String rueckseite){}

    public void karteEntfernen(String vorderseite){}

    /** Abfrage Bezogen */
    public Boolean antwortPruefen(String antwort){
        return true;
    }

    public Stapel shuffel(){
        return null;
    }

    public Karte naechsteGeben(){
        return null;
    }

}

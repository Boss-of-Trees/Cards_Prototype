package API;

import Backend.*;

import java.util.ArrayList;

public class API {

    private OrdnerSpeicherer ordnerSpeicherer;
    private StapelSpeicherer stapelSpeicherer;

    private UserManager bv;
    private OrdnerStruktur aktuelleOrdnerStruktur;

    private Stapel aktuellerStapel;
    private Stapel aktuellerAbfrageStapel;
    private Karte aktuelleKarte;

    public API(){
        bv = new UserManager();
        ordnerSpeicherer = new OrdnerSpeicherer();
        stapelSpeicherer = new StapelSpeicherer();
        aktuelleOrdnerStruktur = null;
        aktuellerStapel = null;
        aktuellerAbfrageStapel = null;
        aktuelleKarte = null;
    }

    /** Bneutzer Bezogen */
    public void benutzerWechseln(String benutzerName){
        if(bv.benutzerExistiert(benutzerName)){
            aktuelleOrdnerStruktur = ordnerSpeicherer.laden(bv.ortGeben(benutzerName) + benutzerName);
        } else {
            System.out.println("benutzer existiert nicht");
        }
    }

    public void benutzerHinzufuegen(String benutzername, String passwort){
        bv.registerUser(benutzername, passwort);
    }

    public Boolean passwortEingabePruefen(String benutzername, String passwort){
        return bv.passwortCheck(benutzername, passwort);
    }

    /** Ordner Bezogen */
    public void zuOrdnerWechseln(String ordnerName){
        if(ordnerName.equals("vorgaenger")){
            aktuelleOrdnerStruktur.ordnerWechselnHoch();
        } else {
            aktuelleOrdnerStruktur.ordnerWechselnRunter(ordnerName);
        }
    }

    public void stapelOeffnen(String stapelName){
        aktuellerStapel = aktuelleOrdnerStruktur.stapelGeben(stapelName);
    }

    public ArrayList<String[]> ordnerInhaltGeben(){
        return aktuelleOrdnerStruktur.ordnerStrukturAlsStringArrayListGeben();
    }

    public void ordnerLoeschen(String ordnerName){
        aktuelleOrdnerStruktur.ordnerEntfernen(ordnerName);
    }

    public void ordnerHinzufuegen(String ordnerName){
        if(!ordnerName.equals("vorgaenger")){
            aktuelleOrdnerStruktur.einfuegen(new Ordner(ordnerName, null));
        }
    }

    public void ordnerAktuelleOrdnerstrukturUpdaten(){
        ordnerSpeicherer.speichern(bv.ortGeben(aktuelleOrdnerStruktur.nameGeben()), aktuelleOrdnerStruktur);
    }

    /** Stapel Bezogen */
    public void stapelErstellen(String stapelName){
        Stapel neuerStapel = new Stapel(stapelName);
        Referenz neueReferenz = new Referenz(stapelName, bv.ortGeben(aktuelleOrdnerStruktur.nameGeben()), aktuelleOrdnerStruktur.aktiverOrdnerGeben(), neuerStapel);
        aktuelleOrdnerStruktur.einfuegen(neueReferenz);
    }

    public void karteHinzufuegen(String vorderseite, String rueckseite){
        if(aktuellerStapel != null)
            aktuellerStapel.vorneEinfuegen(new Karte(vorderseite, rueckseite));

        stapelSpeicherer.speichern(bv.ortGeben(aktuelleOrdnerStruktur.nameGeben()), aktuellerStapel);
    }

    public void karteEntfernen(String vorderseite){
        if(aktuellerStapel !=  null)
            //aktuellerStapel.karteEntfernen(vorderseite);
    }

    public void stapelUpdaten(){
        if(aktuellerStapel != null)
            stapelSpeicherer.speichern(bv.ortGeben(aktuelleOrdnerStruktur.nameGeben()), aktuellerStapel);
    }

    public void stapelSchliessen(){
        stapelUpdaten();
        aktuellerStapel = null;
    }

    /** Abfrage Bezogen */
    public void abfrageStarten(){
        aktuellerAbfrageStapel = aktuellerStapel.lernStapelGeben();
        aktuelleKarte = aktuellerAbfrageStapel.KarteGeben();
    }

    public Boolean antwortPruefen(String antwort){
        if(aktuelleKarte != null)
            return aktuelleKarte.antwortAufRueckseitePrüfen(antwort);
        return false;
    }

    public void kartenMischen(){
        if(aktuelleKarte != null)
            aktuellerAbfrageStapel.kartenMischen();
    }

    public void naechsteKarteNehmen(){
        if(aktuelleKarte != null)
            aktuelleKarte = aktuelleKarte.nächsteGeben();
    }

    public String vorderseiteGeben(){
        if(aktuelleKarte != null)
            return aktuelleKarte.ausgebenv();
        return "";
    }

    public String rueckseiteGeben(){
        if(aktuelleKarte != null)
            return aktuelleKarte.ausgebenr();
        return "";
    }

    public void abfrageBeenden(){
        aktuelleKarte = null;
        aktuellerAbfrageStapel = null;

    }

}
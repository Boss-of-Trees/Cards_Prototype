import Backend.OrdnerSpeicherer;
import Backend.OrdnerStruktur;
import Backend.Stapel;

public class Tests {

    Stapel st = new Stapel();
    OrdnerStruktur os = new OrdnerStruktur();

    OrdnerSpeicherer ordnerSpeicherer = new OrdnerSpeicherer();

    ordnerSpeicherer.speichern("test", os);

}

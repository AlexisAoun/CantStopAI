package cantstop;
import strategies.*;


/**
 * Lancement d'un tournoi ou d'une partie
 * @author afleury 
 */
public class Main {
    public static void main(String[] args) {
        // On instancie le jeu
        Jeu j = new Jeu();
        
        // pour lancer une partie classique ou humain vs IA 
        //j.run();
        Strat33 s1 = new Strat33(); 
        Strat69 s2 = new Strat69();
        s1.computeProba();
        s2.calculProba();
        System.out.println("Done ! ");
        // Pour lancer un tournoi avec 10 000 exécutions à chaque fois (entre chaque couple d'IA).
        // Mettez dans ce tableau les strats à tester
        //int[] stratToTest = {69,2};
        //j.runIA(stratToTest,1);
    }
}

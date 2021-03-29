package cantstop;

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
        
        // Pour lancer un tournoi avec 10 000 exécutions à chaque fois (entre chaque couple d'IA).
        // Mettez dans ce tableau les strats à tester
        int[] stratToTest = {0,1,2,3,4,5,6,7};
        j.runIA(stratToTest,10000);
    }
}

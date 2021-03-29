package strategies;

import cantstop.Jeu;

/**
 * Interface à implémenter pour définir une stratégie de jeu
 * @author humeau
 */
public interface Strategie {

    /**
     * Choix d'une combinaison de dés
     * @param j la partie en cours
     * @return un entier compris dans l'interval [0, j.nbChoix[
     */
    public int choix(Jeu j);

    /**
     * Choix d'arrêter ou de continuer son tour de jeu
     * @param j la partie en cours
     * @return true (pour arrêter), ou false (pour continuer)
     */
    public boolean stop(Jeu j);

    /**
     * @return le nom des élèves (sous le format NOM1_NOM2) 
     */
    public String getName();
}

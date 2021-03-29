package strategies;

import cantstop.Jeu;

/**
 * Mauvaise stratégie  
 * @author humeau
 */
public class Strat0 implements Strategie {

    /**
     * @param j le jeu
     * @return toujours le 1er choix
     */
    public int choix(Jeu j){
        return 0;
    }

    /**
     * @param j le jeu
     * @return toujours vrai (pour s'arrêter)
     */
    public boolean stop(Jeu j){
        return true;
    }

    /**
     * @return "Stratégie mauvaise"
     */
    public String getName(){
        return "Stratégie mauvaise";
    }
}

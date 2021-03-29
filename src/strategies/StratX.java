package strategies;

import cantstop.Jeu;
        
/**
 * Votre Stratégie (copie de la Strat0 pour l'instant)
 *
 * Expliquez votre stratégie en une 20aine de lignes maximum.
 *
 * RENDU: Ce fichier, correctement nommé et rempli avec le numéro de la stratégie choisie.
 *
 * @author VOS NOMS (en binome maximum)
 */
public class StratX implements Strategie {

   /**
    * @param j le jeu
    * @return toujours le 1er choix
    */
   public int choix(Jeu j) {
       return 0;
   }

   /**
    * @param j le jeu
    * @return toujours vrai (pour s'arrêter)
    */
   public boolean stop(Jeu j) {
       return true;
   }

   /**
    * @return vos noms
    */
   public String getName() {
       return "VOS NOMS (deux personnes maximum)";
   }
}

package cantstop;

/**
 * Classe Joueur contenant ses informations
 * @author humeau
 */
public class Joueur{
    // son pseudo
    public String pseudo;
    //son avancement validé(avancement aprés avoir décidé d'arreter son tour) sur les colones
    public int[] avancement;
    // son score (entre 0 et 3 (voir plus) pour une victoire)
    public int score;
    // son status: Humain (0), ou IA (1)
    public int status;
}

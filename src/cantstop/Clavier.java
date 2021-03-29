package cantstop;

import java.util.Scanner;

/**
 * Classe contenant des méthodes de saisie clavier
 * @author humeau 
 */
public class Clavier{

    /**
     * Constructeur par défaut
     */
    Clavier(){
	sc=new Scanner(System.in);
    }

    /**
     * Demande de saisie d'un entier compris entre 2 bornes incluses
     * @param _inf la borne inférieure
     * @param _sup la borne supérieure
     * @return l'entier saisi au clavier
     */
    public int getIntBetween(int _inf, int _sup){
        int res = _inf-1;
	while(res<_inf || res>_sup){
	    System.out.println("Veuillez saisir un entier compris entre " + _inf + " et " + _sup + " inclus");
	    while(!sc.hasNextInt()){
		System.out.println("un entier svp.");
		sc.nextLine();
	    }
	    res=sc.nextInt();
	}
        return res;
    }

    /**
     * Demande de saisie d'un oui(o) ou non(n)
     * @return le caractère saisi
     */
    public String YesNo(){
	String res="";
	System.out.println("o/n ?");
	while(!res.equals("n") && !res.equals("N") && !res.equals("o") & !res.equals("O"))
	    res=sc.nextLine();
	return res;
    }
    
    /**
     * Demande de saisie d'une chaîne de caractère
     * @return une chaîne de caractère saisie au clavier
     */
    public String getString(){
        String res=sc.next();
        return res;
    }
    
    //un Scanner utile pour la saisie clavier
    public Scanner sc;
}

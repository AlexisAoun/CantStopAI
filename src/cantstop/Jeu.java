package cantstop;

import strategies.*;
import java.util.Random;
import java.util.Scanner;

/**
 * Le jeu Can't Stop
 * @author humeau
 */
public class Jeu{
    // nb de joueurs (de 2 à 4 au choix de l'utilisateur)
    private int nbJoueurs;
    // les joueurs participants au jeu
    private Joueur[] joueurs;
    // le nombre de case de chaque colone
    private int[] maximum;
    // le positionnement des bonzes [numéro de colone 2 à 12][avancement sur la colone]
    private int[][] bonzes;
    // joueur actif (actif=numéro du joueur-1)
    private int actif;
    // les valeurs des dés (entre 1 et 6)
    private int[] des;
    // les possibilités de regroupement + infos utiles: [somme d'une paire de dé][indication sur la possibilité de choisir cette somme]
    private int[][] possibilite;
    // indication des colones finies et validées
    private boolean[] fini;
    // indication des colones finies mais pas encore validées
    private boolean[] bloque;
    // nb de bonzes restant de coté du joueur actif
    private int bonzesRestants;
    // indique si au moins un choix est réalisable
    private boolean choixPossible;
    // Les choix possibles [Somme1][Somme2 (ou 0 si une seule colone possible)]
    private int[][] lesChoix;
    // nombre de choix possible
    private int nbChoix;
    // indique si c'est le 1er coup du joueur ou non
    private boolean premierCoup;

    private Random rand;

    private int nbCoup;

    private Clavier clavier;

    private Strategie strats[];
    
    private int commence;

    /**
     * Initialisation des valeurs communes aux 2 inits (avec ou sans Humain)
     */
    public void init_commun(){
        //initialisation des valeurs par défaut
        rand = new Random();
	nbCoup=0;
	maximum= new int[11];
	fini = new boolean[11];
	for(int i=0; i<11 ;i++)
	    fini[i]=false;
	bloque = new boolean[11];
	for(int i=0; i<11 ;i++)
	    bloque[i]=false;
	des= new int[4];
	possibilite= new int[6][2];
	lesChoix = new int[6][2];
	choixPossible=false;
	bonzes=new int[3][2];
	bonzesRestants=3;
	maximum[0]=3;
	maximum[1]=5;
	maximum[2]=7;
	maximum[3]=9;
	maximum[4]=11;
	maximum[5]=13;
	maximum[6]=11;
	maximum[7]=9;
	maximum[8]=7;
	maximum[9]=5;
	maximum[10]=3;
    };
    
    /**
     * Initialisation du jeu
     */
    public void init(){
        // Variables temporaires
        String p;
        
        // Initialisation commune
        init_commun();
        
        // Initialisation des joueurs
	clavier= new Clavier();
        System.out.println("Veuillez saisir le nombre de joueurs ");
        nbJoueurs = clavier.getIntBetween(2, 4);

        joueurs = new Joueur[nbJoueurs];
        strats = new Strategie[nbJoueurs];
        
        // Initialisation des valeurs par défaut des joueurs
        for (int i = 0; i < nbJoueurs; i++) {
            joueurs[i] = new Joueur();
            joueurs[i].avancement = new int[11];
            for (int j = 0; j < 11; j++) {
                joueurs[i].avancement[j] = 0;
            }
            joueurs[i].score = 0;
            System.out.println("Veuillez saisir le pseudo du joueur " + (i + 1));
            joueurs[i].pseudo = clavier.getString();
            System.out.println("0: Humain ou 1: IA");
            joueurs[i].status = clavier.getIntBetween(0, 1);
            if(joueurs[i].status == 1) {
                System.out.println("Numéro de la stratégie à charger");
                int s = clavier.getIntBetween(0, 500);
                try {
                    strats[i]=(Strategie) Class.forName("strategies.Strat"+s).newInstance();
                }
                catch(Exception e) {
                    System.exit(-1);
                }
            }
        }
        
        // Determination du 1er joueur
        actif = rand.nextInt(nbJoueurs);
        System.out.println();
        System.out.println("                         -----------------------------------------------");
        System.out.print("                               ");
        System.out.println("J" + (actif+1) + ": " + joueurs[actif].pseudo + " commence la partie.");
        System.out.println("                         -----------------------------------------------");
        System.out.println();
        premierCoup=true;
    }
    
    /**
     * Initialisation du jeu sans Humain
     * @param _nbJoueurs le nombre d'IA
     */
    public void initIA(int _nbJoueurs){
        // Variables temporaires
        int a;
        String p;
        
        // Initialisation commune
        init_commun();
       
	nbJoueurs=_nbJoueurs;
	joueurs= new Joueur[nbJoueurs];
	for(int i=0; i<nbJoueurs; i++){
	    joueurs[i]=new Joueur();
	    joueurs[i].avancement = new int[11];
	    for(int j=0; j<11; j++)
		joueurs[i].avancement[j] = 0;
	    joueurs[i].score=0;
	    joueurs[i].pseudo="IA";
	    joueurs[i].status=1;
	}
	// Determination du 1er joueur
	a=rand.nextInt(nbJoueurs);
	actif=a;
        commence=a;
        premierCoup=true;
    }

    /**
     * Réinitialise les valeurs par défaut pour démarrer une nouvelle partie
     */
    public void newgame(){
	for(int i=0; i<11 ;i++)
	    fini[i]=false;
	choixPossible=false;
	for(int i=0; i<11 ;i++)
	    bloque[i]=false;
        commence=(commence+1)%nbJoueurs;
	actif=commence;
        premierCoup=true;
	for(int i=0; i<nbJoueurs; i++){
	    for(int j=0; j<11; j++)
		joueurs[i].avancement[j] = 0;
	    joueurs[i].score=0;
	}

	nbCoup=0;
	resetChoix();
	bonzes[0][0]=0;
	bonzes[0][1]=0;
	bonzes[1][0]=0;
	bonzes[1][1]=0;
	bonzes[2][0]=0;
	bonzes[2][1]=0;
    }


    /**
     * Affiche l'avancement du jeu
     */
    public void printAvancement(){
        //On affiche les scores
        System.out.print("SCORE: ");
        for(int i=0; i<this.nbJoueurs; i++){
            if(i>0)
                System.out.print("     ");
            System.out.print("J" + (i+1) + ": " + joueurs[i].pseudo + "(" + joueurs[i].score + ")");
        }
        System.out.println();
        System.out.println();

        //On affiche l'avancement des joueurs
        System.out.print("AVANCEMENT");
        for(int i=0; i<11; i++){
            if(i<4 || i==7)
                System.out.print("    " + (i+2) + "(" + maximum[i] + ")");
            else
                System.out.print("   " + (i+2) + "(" + maximum[i] + ")");
        }
        System.out.println();
        for(int i=0; i<nbJoueurs; i++){
            System.out.print("        J" + (i+1));
            for(int j=0; j<11 ; j++){
                if(joueurs[i].avancement[j]<10)
                    System.out.print("      " + joueurs[i].avancement[j] + " ");
                else
                    System.out.print("     " + joueurs[i].avancement[j] + " ");
            }
            System.out.println();
        }
        // On affiche l'avancement du joueur actif
        System.out.print(" actif: J" + (actif + 1));
        for (int i = 0; i < 11; i++){
	    if(fini[i]){
		System.out.print("      X ");
	    }
            else if(bonzes[0][0]==(i+2)){
                if(bonzes[0][1]<10)
                    System.out.print(" ");
                System.out.print("     " + bonzes[0][1] + " ");
            }
            else if(bonzes[1][0]==(i+2)){
                if(bonzes[1][1]<10)
                    System.out.print(" ");
                System.out.print("     " + bonzes[1][1] + " ");
            }
            else if(bonzes[2][0]==(i+2)){
                if(bonzes[2][1]<10)
                    System.out.print(" ");
                System.out.print("     " + bonzes[2][1] + " ");
            }
            else
                System.out.print("      _ ");
        }
        System.out.println();
	System.out.println();
    }

    /**
     * Lance les dés.
     */
    public void jeterLesDes(){
        // Aprés le 1er lancé de dé, ce n'est plus le 1er coup du joueur
        premierCoup=false;
        // Tirage aléatoire des dés
        for(int i=0; i<4; i++)
            des[i]=rand.nextInt(6)+1;
        // Determination des possibilités d'avancement + indication sur les choix possible suivant l'état du jeu
        possibilite[0][0]=des[0]+des[1];
        possibilite[1][0]=des[2]+des[3];
        possibilite[2][0]=des[0]+des[2];
        possibilite[3][0]=des[1]+des[3];
        possibilite[4][0]=des[0]+des[3];
        possibilite[5][0]=des[1]+des[2];
	for (int i=0; i<6; i++)
	    possibilite[i][1]=possible(possibilite[i][0]);
    }

    /**
     * Construction des choix pouvant être joués
     */
    public void construireChoix(){
        int cpt=0;
        int tmp;
        if(choixPossible){
            // Pour chaque combinaison de 2 paires de dé on regarde détermine les choix possibles suivant le nb de bonzes restants
            for(int i=0; i<3; i++){
                tmp=possibilite[2*i][1] + possibilite[2*i+1][1];
                // Cas ou on joue sur 2 colones
                // Soit 2 nouvelle tmp==20 et au moins 2 bonzes restant
                // Soit 2 colones ou on avait déja des bonzes tmp==2
                // Soit 1 nouvelle et 1 "ancienne" tmp==11
                if((tmp==20 && bonzesRestants>1) || tmp==11 || tmp==2){
                    lesChoix[cpt][0]=possibilite[2*i][0];
                    lesChoix[cpt][1]=possibilite[2*i+1][0];
                    cpt++;
                }
                // Cas ou on peut jouer sur 2 nouvelles colones mais 1 seul bonzes restant
                // On propose donc les 2 choix
                else if((tmp==20) && (bonzesRestants<2)){
                    lesChoix[cpt][0]=possibilite[2*i][0];
                    lesChoix[cpt][1]=0;
                    lesChoix[cpt+1][0]=possibilite[2*i+1][0];
                    lesChoix[cpt+1][1]=0;
                    cpt+=2;
                }
                // Cas ou on peut avancer que sur un seule colone déjà prise...
                else if (tmp==1 || (tmp==10 && bonzesRestants>0)){
                    if(possibilite[2*i][1]==0)
                        lesChoix[cpt][0]=possibilite[2*i+1][0];
                    else
                        lesChoix[cpt][0]=possibilite[2*i][0];
                    lesChoix[cpt][1] = 0;
                    cpt++;
                }
            }
        }
        // On stock le nb de choix dispo
        nbChoix=cpt;
    }

    /**
     * Affichage des choix disponibles
     */
    public void printChoix(){
	printDes();
        int cpt=0;
        for(int i=0; i<nbChoix; i++){
            if(lesChoix[i][1]>0)
                System.out.println("Choix " + i + ": Avancer sur " + lesChoix[i][0] + " et " + lesChoix[i][1] + ".");
            else
                System.out.println("Choix " + i + ": Avancer sur " + lesChoix[i][0] + ".");
        }
    }

    /**
     * Affichage des dés.
     */
    public void printDes(){
	System.out.println("Lancé de dés: " + des[0] + " - " + des[1] + " - " + des[2] + " - " + des[3]);
    }

    /**
     * Demande de decision du joueur parmis les choix disponibles
     */
    public void decision(){
	//Si joueur humain on demande de decider
	if(joueurs[actif].status==0){
	    int a=-1;
	    while(a<0 || a>=nbChoix){
		System.out.println("Veuillez choisir parmis les choix suivants: (entrer le numéro du choix)");
		printChoix();
		Scanner sc = new Scanner(System.in);
		a = sc.nextInt();
		if (a < 0 || a >= nbChoix)
		    System.out.println("CHOIX INVALIDE !!!");
		else{
		    appliquerChoix(a);
		}
	    }
	}
        // sinon on utilise la stratégie de l'IA
	else{
	    appliquerChoix(strategieChoix());
	}
    }

    /**
     * Utilise la stratégies A ou B
     * @return le choix fait par l'IA
     */
    public int strategieChoix(){
        try{
           return strats[actif].choix(this);
        }
        catch(Exception e){
            System.out.println(e);
        }
        return 0;
    }

    /**
     * Demande de decision du joueur pour continuer son tour ou arreter et donc validé l'avancement de ses bonzes
     */
    public boolean stop(){
        //Si joueur humain on demande de decider
        boolean res=false;
	if(joueurs[actif].status==0){
	    String tmp="";
	    while(!tmp.equals("O") && !tmp.equals("N")){
		System.out.println("Voulez vous continuer votre tour (O ou N)");
		Scanner sc = new Scanner(System.in);
		tmp = sc.next();
		if(!tmp.equals("O") && !tmp.equals("N"))
		    System.out.println("REPONSE INVALIDE !!!");
		else if(tmp.equals("N"))
		    res=true;
	    }
	}
        // sinon on utilise la stratégie de l'IA
	else
	    res=strategieStop();
        return res;
    }

    /**
     * Utilise la stratégies A ou B
     * @return le choix fait par l'IA pour arreter ou continuer
     */
    public boolean strategieStop(){
        try{
            return strats[actif].stop(this);
        }
        catch(Exception e){
            System.out.println(e);
        }
        return false;
    }

    /**
     * Avancement des bonzes en fonction du choix des dés
     * @param _choix le choix de dés
     */
    public void appliquerChoix(int _choix){
        // on regarde si on avance sur 1 ou 2 colone(s)
        int nbAvancement=2;
        if(lesChoix[_choix][1]==0)
            nbAvancement=1;
        // Pour chaque avancement
        for (int i = 0; i < nbAvancement; i++){
            // On fait avancer un bonze déjà placé
            if(bonzes[0][0]==lesChoix[_choix][i]){
                bonzes[0][1]++;
                if(bonzes[0][1]==maximum[bonzes[0][0]-2])
                    bloque[bonzes[0][0]-2]=true;
            }
            else if(bonzes[1][0]==lesChoix[_choix][i]){
                bonzes[1][1]++;
                if(bonzes[1][1]==maximum[bonzes[1][0]-2])
                    bloque[bonzes[1][0]-2]=true;
            }
            else if(bonzes[2][0]==lesChoix[_choix][i]){
                bonzes[2][1]++;
                if(bonzes[2][1]==maximum[bonzes[2][0]-2])
                    bloque[bonzes[2][0]-2]=true;
            }
            // Sinon on prend le 1er bonze restant pour le faire avancer
            else{
                if(bonzes[0][0]==0){
                    bonzes[0][0] = lesChoix[_choix][i];
                    bonzes[0][1] = joueurs[actif].avancement[lesChoix[_choix][i] - 2] + 1;
                    if (bonzes[0][1] == maximum[bonzes[0][0] - 2])
                        bloque[bonzes[0][0] - 2] = true;
                    bonzesRestants--;
                }
                else if(bonzes[1][0]==0){
                    bonzes[1][0] = lesChoix[_choix][i];
                    bonzes[1][1] = joueurs[actif].avancement[lesChoix[_choix][i] - 2] + 1;
                    if (bonzes[1][1] == maximum[bonzes[1][0] - 2])
                        bloque[bonzes[1][0] - 2] = true;
                    bonzesRestants--;
                }
                else{
                    bonzes[2][0] = lesChoix[_choix][i];
                    bonzes[2][1] = joueurs[actif].avancement[lesChoix[_choix][i] - 2] + 1;
                    if (bonzes[2][1] == maximum[bonzes[2][0] - 2])
                        bloque[bonzes[2][0] - 2] = true;
                    bonzesRestants--;
                }
            }
        }
    }

    /**
     * Détermination de la fin du jeu
     */
    public boolean victoire(){
        boolean res=false;
        // Si un joueur a 3 points il gagne la partie
        if(joueurs[actif].score>=3){
            res = true;        
        }
        // Affichage du gagnant seulement si un Humain joue
        boolean human=false;
        for(int i=0; i<nbJoueurs; i++)
            if(joueurs[i].status==0)
                human=true;
        if(res && human)
            System.out.println("VICTOIRE!!! " + joueurs[actif].pseudo + " a gagné la partie.");
        return res;
    }

    /**
     * Valide l'avancement des bonzes du joueur actif et ajustement de son score si nécessaire
     */
    public void validerArret(){
        for(int i=0; i<3; i++){
            if(bonzes[i][0]!=0){
                joueurs[actif].avancement[bonzes[i][0]-2]=bonzes[i][1];
                if(bonzes[i][1]>=maximum[bonzes[i][0]-2]){
                    fini[bonzes[i][0]-2]=true;
                    joueurs[actif].score++;
                }
            }
        }
    }

    /**
     * Changement de joueur et initialisation des valeurs nécessaires
     */
    public void changementJoueur(){
	resetChoix();
	bonzes[0][0]=0;
	bonzes[0][1]=0;
	bonzes[1][0]=0;
	bonzes[1][1]=0;
	bonzes[2][0]=0;
	bonzes[2][1]=0;
	actif=(actif+1)%nbJoueurs;
	for(int i=0; i<11; i++){
	    bloque[i]=false;
	}
	bonzesRestants=3;
	premierCoup=true;
    }

    /**
     * Le même joueur continue et initialisation des valeurs nécessaires
     */
    public void memeJoueur(){
	resetChoix();
    }

    /**
     * Réinitialisation des choix possibles
     */
    public void resetChoix(){
        for(int i=0; i <6; i++){
                    lesChoix[i][0]=0;
                    lesChoix[i][1]=0;
        }
        choixPossible = false;
        nbChoix=0;
    }

    /**
     * Test la possibilité d'avancement sur la colone "valeur" suivant l'état actuel du jeu
     * @param valeur la colonne à tester
     * @return 0 si pas possible, 1 si c'est possible sur une colone ou un bonze est déja présent, 10 si c'est possible avec un nouveau bonze
     */
    public int possible(int valeur){
        int res=0;
        // Si il nous reste des bonzes et que les colonnes ne sont pas fini (validé ou non)
        // Alors on peut avancer
        if(bonzesRestants>0 && !fini[valeur-2] && !bloque[valeur-2]){
            // 1 quand on avance un bonze déjà placé, 10 dans le cas contraire
            if (bonzes[0][0] == valeur || bonzes[1][0] == valeur || bonzes[2][0] == valeur)
                res = 1;
            else
                res = 10;
            choixPossible=true;
        }
        // Si il ne reste pas de bonze on vérifie que la colones n'est pas bloqué et qu'on a déjà un bonze dessus
        // Si c'est le cas on retourne 1
        else if((bonzes[0][0]==valeur || bonzes[1][0]==valeur || bonzes[2][0]==valeur)&& !bloque[valeur-2] && !fini[valeur-2]){
            res=1;
            choixPossible=true;
        }
        // Sinon on retourne 0
        return res;
    }

    /**
     * Déroulement d'une partie
     */
    public void run(){
	boolean end=false;
        this.init();
        while(!end){
            // si c'est pas le 1er coup du joueur
            if(!premierCoup){
                // on lui demande de continuer ou d'arreter
                if(stop()){
                    // si il arrete on valide et on change de joueur
                    validerArret();
		    end=victoire();
                    changementJoueur();
		    nbCoup=0;
		    if(!end){
			System.out.println();
			System.out.println("                         -----------------------------------------------");
			System.out.print("                                 ");
			System.out.println(joueurs[(actif+nbJoueurs-1)%nbJoueurs].pseudo + " a validé son avancement.");
			System.out.print("                                 ");
			System.out.println("C'est au tour de " + joueurs[actif].pseudo + ".");
			System.out.println("                         -----------------------------------------------");
			System.out.println();
		    }
                }
                // sinon on reste sur le même joueur
                else{
                    memeJoueur();
		    nbCoup++;
		}
            }
	    if(!end){
		// on affiche l'avancement du jeu
		this.printAvancement();
		// on jete les dés
		this.jeterLesDes();
		// on construit et propose les choix dispo si il y en a
		this.construireChoix();
		if(this.choixPossible){
		    // si oui on demande au joueur de choisir
		    this.decision();
		    printAvancement();
		}
		else{
		    // Sinon son tour et perdu on change de joueur sans validé son avancement
		    System.out.println();
		    System.out.println("                         -----------------------------------------------");
		    System.out.print("                                   ");
		    this.printDes();
		    System.out.print("                                   ");
		    System.out.println("Aucun choix possible !");
		    this.changementJoueur();
		    nbCoup=0;
		    System.out.print("                                   ");
		    System.out.println("C'est au tour de " + joueurs[actif].pseudo + ".");
		    System.out.println("                         -----------------------------------------------");
		    System.out.println();
		}
	    }
        }
    }

    /**
     * Lance un tournoi entre plusieurs IA
     * @param groupes les IA à tester en mode tournoi
     * @param _nbAffrontement le nombre de partie joué entre chaque couple d'IA
     * @return les résultats du tournoi
     */
    public void runIA(int[] groupes, int _nbAffrontement){
        try{
            int[][][] stat=new int[groupes.length][groupes.length][4];
            int[][] res= new int[groupes.length][5];
            
            strats = new Strategie[2];
            for(int i=0; i<groupes.length; i++)
                for(int j=0; j<groupes.length; j++)
                    for(int k=0; k<4; k++)
                        stat[i][j][k]=0;

            for(int i=0; i<groupes.length; i++)
                for(int j=0; j<5; j++)
                    res[i][j]=0;

            for(int a=0; a<groupes.length-1; a++){
                for(int b=a+1;b<groupes.length; b++){
                    System.out.print(a + " vs " + b);
                    strats[0]=(Strategie) Class.forName("strategies.Strat"+groupes[a]).newInstance();
                    strats[1]=(Strategie) Class.forName("strategies.Strat"+groupes[b]).newInstance();

                    int[][] victoire=new int[2][4];
                    for(int i=0; i<nbJoueurs; i++)
                        for(int j=0; j<4; j++)
                            victoire[i][j]=0;
                    this.initIA(2);
                    for(int i=0; i<_nbAffrontement; i++){
                        boolean end=false;
                        while(!end){
                            // si c'est pas le 1er coup du joueur
                            if(!premierCoup){
                                // on lui demande de continuer ou d'arreter
                                if(stop()){
                                    // si il arrete on valide et on change de joueur
                                    validerArret();
                                    end=victoire();
                                    changementJoueur();
                                    nbCoup=0;
                                }
                                // sinon on reste sur le même joueur
                                else{
                                    memeJoueur();
                                    nbCoup++;
                                }
                            }
                            if(!end){
                                // on jete les dés
                                this.jeterLesDes();
                                // on construit et propose les choix dispo si il y en a
                                this.construireChoix();
                                if(this.choixPossible){
                                    // si oui on demande au joueur de choisir
                                    this.decision();
                                }
                                else{
                                    // Sinon son tour et perdu on change de joueur sans validé son avancement
                                    this.changementJoueur();
                                    nbCoup=0;
                                }
                            }
                        }
                        for(int j=0; j<nbJoueurs; j++){
                            if(joueurs[j].score>=3){
                                int p=(j+1)%2;
                                victoire[j][0]++;
                                victoire[p][1]++;
                                victoire[j][2]+=3;
                                victoire[j][3]+=joueurs[p].score;
                                victoire[p][2]+=joueurs[p].score;
                                victoire[p][3]+=3;
                            }
                        }
                        this.newgame();
                    }
                    stat[a][b][0]=victoire[0][0];
                    stat[b][a][0]=victoire[1][0];

                    stat[a][b][1]=victoire[0][1];
                    stat[b][a][1]=victoire[1][1];

                    stat[a][b][2]=victoire[0][2];
                    stat[b][a][2]=victoire[1][2];

                    stat[a][b][3]=victoire[0][3];
                    stat[b][a][3]=victoire[1][3];

                    System.out.println(" => " + victoire[0][0] + " - " + victoire[1][0] + " (" + victoire[0][2] + ", " +  victoire[1][2] + ")");

                }
            }

            for(int i=0; i<groupes.length; i++){
                for(int j=0; j<groupes.length; j++){
                    if(i!=j){
                        if(stat[i][j][0]>stat[i][j][1]){
                            res[i][0]++;
                        }
                        else if(stat[i][j][0]==stat[i][j][1]){
                            res[i][1]++;
                        }
                        else{
                            res[i][2]++;
                        }
                        res[i][3]+=stat[i][j][2];
                        res[i][4]+=stat[i][j][3];
                    }
                }
            }

            for(int i=0; i<groupes.length; i++){
                Strategie tmp = (Strategie)Class.forName("strategies.Strat"+groupes[i]).newInstance();

                System.out.println(tmp.getName() + ": " + res[i][0] + " " + res[i][1] + " " + res[i][2] + " " + res[i][3]+ " " + res[i][4]);
            }
        }
        catch (Exception e){
            System.out.println(e);
        }
    }

    /**
     * Permet de récupérer le nombre de joueurs du jeu
     * @param aucun
     * @return le nombre de joueurs
     */
    public int getNbJoueurs() {
        return nbJoueurs;
    }

    /**
     * Permet de récupérer le tableau des maximums de chaque voies
     * @param aucun
     * @return le tableau maximum
     */
    public int[] getMaximum() {
        return maximum.clone();
    }

    /**
     * Permet de récupérer le tableau des bonzes (Voies + positions) 
     * @param aucun
     * @return le tableau des bonzes
     */
    public int[][] getBonzes() {
        int[][] bonzes = new int[this.bonzes.length][this.bonzes[0].length];
        for(int i = 0; i < this.bonzes.length; i++) {
            bonzes[i] = this.bonzes[i].clone();
        }
        return bonzes;
    }
    
    /**
     * Permet de récupérer le joueur actif
     * @param aucun
     * @return le numéro du joueur actif
     */
    public int getActif() {
        return actif;
    }
    /**
     * Permet de récupérer le tableau des possibilités
     * @param aucun
     * @return le tableau possibilite
     */
    public int[][] getPossibilite() {
        int[][] possibilite = new int[this.possibilite.length][this.possibilite[0].length];
        for(int i = 0; i < this.possibilite.length; i++) {
            possibilite[i] = this.possibilite[i].clone();
        }
        return possibilite;
    }
    /**
     * Permet de récupérer le tableau indiquant le status fini (vrai ou faux) de chaque voie
     * @param aucun
     * @return le tableau fini
     */
    public boolean[] getFini() {
        return fini;
    }
    /**
     * Permet de récupérer le tableau indiquant le status bloqué (vrai ou faux) de chaque voie
     * @param aucun
     * @return le tableau bloque
     */
    public boolean[] getBloque() {
        return bloque;
    }
    /**
     * Permet de récupérer le nombre de bonzes disponibles
     * @param aucun
     * @return le nombre de bonzes restants
     */
    public int getBonzesRestants() {
        return bonzesRestants;
    }
    /**
     * Permet de récupérer le fait qu'au moins un choix soit possible
     * @param aucun
     * @return choixPossible
     */
    public boolean isChoixPossible() {
        return choixPossible;
    }
    /**
     * Permet de récupérer le tableau des choix (les coups possibles)
     * @param aucun
     * @return le tableau des choix
     */
    public int[][] getLesChoix() {
        int[][] lesChoix = new int[this.lesChoix.length][this.lesChoix[0].length];
        for(int i = 0; i < this.lesChoix.length; i++) {
            lesChoix[i] = this.lesChoix[i].clone();
        }
        return lesChoix;
    }
    /**
     * Permet de récupérer le nombre de choix possibles
     * @param aucun
     * @return nbChoix
     */
    public int getNbChoix() {
        return nbChoix;
    }
    /**
     * Permet de récupérer le nombre de coups
     * @param aucun
     * @return nbCoup
     */
    public int getNbCoup() {
        return nbCoup;
    }
    /**
     * Permet de l'avancement du joueur actif
     * @param aucun
     * @return le tableau avancement du joueur actif
     */    
    public int[] avancementJoueurEnCours() {
        return joueurs[actif].avancement.clone();
    }
     /**
     * Permet de récupérer le tableau avancement de l'adversaire du joueur actif
     * @param aucun
     * @return le tableau avancement du second joueur
     */   
    public int[] avancementAutreJoueur() {
        return joueurs[(actif+1)%2].avancement.clone();
    }
    /**
     * Permet de récupérer score du joueur en cours
     * @param aucun
     * @return le score du joueur actif
     */    
    public int scoreJoueurEnCours() {
        return joueurs[actif].score;
    }
    /**
     * Permet de récupérer le score de l'adversaire du joueur en cours
     * @param aucun
     * @return le score du second joueur
     */    
    public int scoreAutreJoueur() {
        return joueurs[(actif+1)%2].score;
    }
}

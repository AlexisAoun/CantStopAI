package strategies;

import cantstop.Jeu;

/**
 * Votre Stratégie (copie de la Strat0 pour l'instant)
 *
 * Expliquez votre stratégie en une 20aine de lignes maximum.
 *
 * RENDU: Ce fichier, correctement nommé et rempli avec le numéro de la
 * stratégie choisie.
 *
 * @author AOUN/OURIACHI
 */
public class Strat33 implements Strategie {

    private double probaSingle[], freq[], weigth[];
    private double probaCouple[][];
    private double probaDouble[];
    private double probaTriple[][][];

    private double coefEsperance = 9.0;

    private boolean debug = false;

    public Strat33() {
        this.probaSingle = new double[13];
        this.freq = new double[13];
        this.weigth = new double[13];
        this.probaCouple = new double[13][13];
        this.probaDouble = new double[13];
        this.probaTriple = new double[13][13][13];

        computeProba();
    }

    // Fonction qui calcule toutes les proba dont on aura besoin pour le reste
    public void computeProba() {
        int[][] sumD = new int[3][2];

        // On commence par compter toutes les issues
        for (int a = 1; a < 7; a++) {
            for (int b = 1; b < 7; b++) {
                for (int c = 1; c < 7; c++) {
                    for (int d = 1; d < 7; d++) {
                        sumD = computeSumD(a, b, c, d);

                        for (int i = 2; i <= 12; i++) {
                            int occurences = computeOccurences(sumD, i);

                            this.freq[i] += occurences;
                            if (isCoupleInSum(sumD, i, i))
                                this.probaDouble[i]++;

                            if (occurences > 0)
                                this.probaSingle[i]++;

                            for (int j = i + 1; j <= 12; j++) {
                                if (isCoupleInSum(sumD, i, j))
                                    this.probaCouple[i][j]++;

                                for (int k = j + 1; k <= 12; k++) {
                                    if (isOneOfThreeInSum(sumD, i, j, k))
                                        this.probaTriple[i][j][k]++;
                                }
                            }
                        }

                    }
                }
            }
        }

        double nbTotalIssues = Math.pow(6, 4);

        for (int i = 2; i <= 12; i++) {

            this.freq[i] /= nbTotalIssues;
            this.weigth[i] = 1 / this.freq[i];

            this.probaSingle[i] /= nbTotalIssues;
            this.probaDouble[i] /= nbTotalIssues;

            for (int j = i + 1; j <= 12; j++) {

                this.probaCouple[i][j] /= nbTotalIssues;

                for (int k = j + 1; k <= 12; k++)
                    this.probaTriple[i][j][k] /= nbTotalIssues;

            }
        }
    }

    // Fonction qui return un tableau avec toutes les sommes possbiles de 4 des
    public int[][] computeSumD(int a, int b, int c, int d) {
        int[][] output = new int[3][2];

        output[0][0] = a + b;
        output[0][1] = c + d;
        output[1][0] = a + c;
        output[1][1] = b + d;
        output[2][0] = a + d;
        output[2][1] = b + c;

        return output;
    }

    // Fonction qui retourne le nombre de fois que n est present dans le tableau de
    // somme
    public int computeOccurences(int[][] sumD, int n) {
        int output = 0;
        for (int i = 0; i < 3; i++) {
            if (sumD[i][0] == n && sumD[i][1] == n)
                output = 2;

            else if (sumD[i][0] == n || sumD[i][1] == n)
                output = 1;
        }

        return output;
    }

    // Fonction qui verifie si un couple d entier se trouve dans le tableau de somme
    public boolean isCoupleInSum(int[][] sumD, int n1, int n2) {
        boolean output = false;
        for (int i = 0; i < 3; i++) {
            if ((sumD[i][0] == n1 && sumD[i][1] == n2) || (sumD[i][0] == n2 && sumD[i][1] == n1))
                output = true;
        }
        return output;
    }

    public boolean isOneOfTwoInSum(int[][] sumD, int n1, int n2) {
        boolean output = false;
        if ((computeOccurences(sumD, n1) != 0) || (computeOccurences(sumD, n2) != 0))
            output = true;
        return output;
    }

    // Fonction qui verifie que au moins une des trois valeurs est presente dans le
    // tableau de somme
    public boolean isOneOfThreeInSum(int[][] sumD, int n1, int n2, int n3) {
        boolean output = false;
        if (((computeOccurences(sumD, n1) != 0) || (computeOccurences(sumD, n2) != 0))
                || (computeOccurences(sumD, n3) != 0))
            output = true;
        return output;
    }

    /**
     * @param j le jeu
     * @return
     */
    public int choix(Jeu j) {

        int output = 0;

        if (j.getNbChoix() == 1)
            return 0;

        double scoreMax = 0;
        int[] nbCases = j.getMaximum();

        if (this.debug) {
            System.out.println("--------------------- Choix Debug ---------------------");
            j.printChoix();
        }

        for (int i = 0; i < j.getNbChoix(); i++) {
            double probaColonne1 = 0;
            double probaColonne2 = 0;

            double score = 0;

            int[][] choix = j.getLesChoix();
            int[] avancementActuel = avancementActuel(j.avancementJoueurEnCours(), j.getBonzes());
            int[] avancementAdverse = j.avancementAutreJoueur();

            probaColonne1 = probaFinirUneColonne(j, choix[i][0], avancementActuel[choix[i][0] - 2]);

            if (choix[i][1] != 0) {
                probaColonne2 = probaFinirUneColonne(j, choix[i][1], avancementActuel[choix[i][1] - 2]);

                int casesRestantesAdvC2 = nbCases[choix[i][1] - 2] - avancementAdverse[choix[i][1] - 2];
                int casesRestantesC2 = nbCases[choix[i][1] - 2] - avancementActuel[choix[i][1] - 2];

                if (casesRestantesAdvC2 * weigth[choix[i][1]] - casesRestantesC2 * weigth[choix[i][1]] > 10)
                    probaColonne2 /= 1.5;
            }

            int casesRestantesAdvC1 = nbCases[choix[i][0] - 2] - avancementAdverse[choix[i][0] - 2];
            int casesRestantesC1 = nbCases[choix[i][0] - 2] - avancementActuel[choix[i][0] - 2];

            if (casesRestantesAdvC1 * weigth[choix[i][0]] - casesRestantesC1 * weigth[choix[i][0]] > 10)
                probaColonne1 /= 1.5;

            score = (probaColonne1 + probaColonne2) * 1000;

            if (this.debug) {
                System.out.println("Choix Actuel i = " + i + " proba colonne " + choix[i][0] + " = " + probaColonne1
                        + " proba colonne " + choix[i][1] + " = " + probaColonne2);
                System.out.println("Score du choix = " + score + " scoreMax = " + scoreMax);
            }

            if (score >= scoreMax) {
                scoreMax = score;
                output = i;
            }
        }

        if (this.debug) {
            System.out.println("output = " + output);
            System.out.println("-------------------------------------------------------------------");
        }
        return output;
    }

    public double probaFinirUneColonne(Jeu j, int colonne, int avancement) {
        double output = 0;
        int casesRestantes = j.getMaximum()[colonne - 2] - avancement;

        output = Math.pow(this.probaSingle[colonne], casesRestantes * weigth[colonne]);

        return output;
    }

    public int[] avancementActuel(int[] avancement, int[][] bonzes) {
        int[] output = new int[11];
    
        for (int i = 0; i < avancement.length; i++) {
            for (int j = 0; j < 3; j++) {
                if (bonzes[j][0] == i + 2) 
                    output[i] = avancement[i] + bonzes[j][1];
            }
        }

        if(this.debug){
            System.out.println("----- Debug avancementActuel------");
            afficherTabInt(output);
        }
        return output;
    }

    /**
     * @param j le jeu
     * @return toujours vrai (pour s'arrêter)
     */
    public boolean stop(Jeu j) {

        boolean[] colonnesBloquees = j.getBloque();
        for (int i = 0; i < colonnesBloquees.length; i++) {
            if (colonnesBloquees[i])
                return true;
        }

        if (j.getBonzesRestants() != 0)
            return false;

        boolean output = true;

        double esperance = 0;
        double progresActuelle = 0;

        int[][] bonzes = j.getBonzes();
        double[][] avancement = new double[3][2];

        for(int i = 0; i<3; i++){
            for(int k = 0; k<2; k++)
                avancement[i][k] = (double)bonzes[i][k];
        }

        if (this.debug) {
            System.out.println("----------------------- Debug Stop --------------------------");

        }

        progresActuelle = (double) avancement[0][0] * weigth[bonzes[0][1]]
                + (double) avancement[1][0] * weigth[bonzes[1][1]]
                + (double) avancement[2][0] * weigth[bonzes[2][1]];

        esperance =

                this.probaSingle[bonzes[0][1]] * ((avancement[0][0] + 1) * weigth[bonzes[0][1]]
                        + avancement[1][0] * weigth[bonzes[1][1]] + avancement[2][0] * weigth[bonzes[2][1]])

                        +

                        this.probaSingle[bonzes[1][1]] * (avancement[0][0] * weigth[bonzes[0][1]]
                                + (avancement[1][0] + 1) * weigth[bonzes[1][1]]
                                + avancement[2][0] * weigth[bonzes[2][1]])

                        +

                        this.probaSingle[bonzes[2][1]] * (avancement[0][0] * weigth[bonzes[0][1]]
                                + avancement[1][0] * weigth[bonzes[1][1]]
                                + (avancement[2][0] + 1) * weigth[bonzes[2][1]])

                        +

                        this.probaCouple[bonzes[0][1]][bonzes[1][1]]
                                * ((avancement[0][0] + 1) * weigth[bonzes[0][1]]
                                        + (avancement[1][0] + 1) * weigth[bonzes[1][1]]
                                        + avancement[2][0] * weigth[bonzes[2][1]])
                        +

                        this.probaCouple[bonzes[0][1]][bonzes[2][1]]
                                * ((avancement[0][0] + 1) * weigth[bonzes[0][1]]
                                        + avancement[1][0] * weigth[bonzes[1][1]]
                                        + (avancement[2][0] + 1) * weigth[bonzes[2][1]])
                        +

                        this.probaCouple[bonzes[1][1]][bonzes[2][1]]
                                * (avancement[0][0] * weigth[bonzes[0][1]]
                                        + (avancement[1][0] + 1) * weigth[bonzes[1][1]]
                                        + (avancement[2][0] + 1) * weigth[bonzes[2][1]])

                        +

                        this.probaDouble[bonzes[0][1]] * ((bonzes[0][0] + 2) * weigth[bonzes[0][1]]
                                + avancement[1][0] * weigth[bonzes[1][1]]
                                + avancement[2][0] * weigth[bonzes[2][1]])

                        +

                        this.probaDouble[bonzes[1][1]] * (avancement[0][0] * weigth[bonzes[0][1]]
                                + (avancement[1][0] + 2) * weigth[bonzes[1][1]]
                                + avancement[2][0] * weigth[bonzes[2][1]])

                        +

                        this.probaDouble[bonzes[2][1]] * (avancement[0][0] * weigth[bonzes[0][1]]
                                + avancement[1][0] * weigth[bonzes[1][1]]
                                + (avancement[2][0] + 2) * weigth[bonzes[2][1]]);

        if (esperance >= progresActuelle/coefEsperance)
            output = false;

        if (this.debug) {
            System.out.println("esprance = " + esperance + " progresActuelle = " + progresActuelle/coefEsperance + " diff = "
                    + (esperance - progresActuelle/coefEsperance));
            System.out.println("Stop ? : = " + output);
            System.out.println("-------------------------------------------------------------------");
        }
        return output;

    }

    public static void afficherTabInt(int[] tab) {
        for (int i = 0; i < tab.length; i++)
            System.out.print(tab[i] + " | ");

        System.out.println("");
    }

    /**
     * @return vos noms
     */
    public String getName() {
        return "AOUN/OURIACHI";
    }
}

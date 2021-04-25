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
    private double probaDouble[][];
    private double probaTriple[][][];
    private int playCount;

    private boolean debug = true;

    public Strat33() {
        this.probaSingle = new double[13];
        this.freq = new double[13];
        this.weigth = new double[13];
        this.probaCouple = new double[13][13];
        this.probaDouble = new double[13][13];
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
                            if (occurences > 0)
                                this.probaSingle[i]++;

                            for (int j = i + 1; j <= 12; j++) {
                                if (isCoupleInSum(sumD, i, j))
                                    this.probaCouple[i][j]++;
                                if (isOneOfTwoInSum(sumD, i, j))
                                    this.probaDouble[i][j]++;

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

            for (int j = i + 1; j <= 12; j++) {

                this.probaCouple[i][j] /= nbTotalIssues;
                this.probaDouble[i][j] /= nbTotalIssues;

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

        if (this.debug)
            System.out.println("--------------------- Choix Debug ---------------------");

        for (int i = 0; i < j.getNbChoix(); i++) {
            double probaColonne1 = 0;
            double probaColonne2 = 0;

            double score = 0;

            int[][] choix = j.getLesChoix();
            probaColonne1 = probaFinirUneColonne(j, choix[i][0], j.avancementJoueurEnCours()[choix[i][0] - 2]);

            if (choix[i][0] != 0)
                probaColonne2 = probaFinirUneColonne(j, choix[i][1], j.avancementJoueurEnCours()[choix[i][1] - 2]);

            int[] avancementAdverse = j.avancementAutreJoueur();
            int casesRestantesAdvC1 = nbCases[choix[i][0] - 2] - avancementAdverse[choix[i][0] - 2];
            int casesRestantesAdvC2 = nbCases[choix[i][1] - 2] - avancementAdverse[choix[i][1] - 2];

            int[] avancementIA = j.avancementJoueurEnCours();
            int casesRestantesC1 = nbCases[choix[i][0] - 2] - avancementIA[choix[i][0] - 2];
            int casesRestantesC2 = nbCases[choix[i][1] - 2] - avancementIA[choix[i][1] - 2];

            if (casesRestantesAdvC1 * weigth[choix[i][0]] - casesRestantesC1 * weigth[choix[i][0]] > 5)
                probaColonne1 /= 2;

            if (casesRestantesAdvC2 * weigth[choix[i][1]] - casesRestantesC2 * weigth[choix[i][1]] > 5)
                probaColonne2 /= 2;


            score = (probaColonne1 + probaColonne2) * 10;

            if (this.debug) {
                System.out.println("Choix Actuel i = " + i + " proba colonne " + choix[i][0] + " = " + probaColonne1
                        + " proba colonne " + choix[i][1] + " = " + probaColonne2);
                System.out.println("Score du choix = "+score+" scoreMax = "+scoreMax);
            }

            if (score >= scoreMax) {
                scoreMax = score;
                output = i;
            }
        }

        return output;
    }

    public double probaFinirUneColonne(Jeu j, int colonne, int avancement) {
        double output = 0;
        int casesRestantes = j.getMaximum()[colonne - 2] - avancement;

        output = Math.pow(this.probaSingle[colonne], casesRestantes * weigth[colonne]);

        return output;
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
        return "AOUN/OURIACHI";
    }
}

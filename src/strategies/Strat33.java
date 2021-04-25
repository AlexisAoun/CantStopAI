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
    private double probaTriple[][][];
    private int playCount;


    public Strat33() {
        this.probaSingle = new double[13];
        this.freq = new double[13];
        this.weigth = new double[13];
        this.probaCouple = new double[13][13];
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
            if ((sumD[i][0] == n1 && sumD[i][1] == n2) || (sumD[i][0] == n2 && sumD[i][1] == n1) )
                output = true;
        }
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
        return "AOUN/OURIACHI";
    }
}

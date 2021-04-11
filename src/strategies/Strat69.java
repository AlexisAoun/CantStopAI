package strategies;

import cantstop.Jeu;
import java.util.ArrayList;
import java.util.HashMap;


public class Strat69
  implements Strategie
{
  private boolean log = true;
  public double[] probaSimple;
  public double[][] probaOuDouble;
  public double[][] probaEtDouble;
  public double[][][] probaTriple;
  public double[] vitesseVoie;
  public double[][][][] probaApresNbLancer;
  public int[][][] nbLancerPossible50;
  double probaFaibleGain = 0.15D;
  double nbLancerAdverse = 6.0D;

  
  int nbLancer = 0;

  
  public void calculProba() {
    int[][] sommeDe = new int[3][2];
    
    for (int d1 = 1; d1 <= 6; d1++) {
      for (int d2 = 1; d2 <= 6; d2++) {
        for (int d3 = 1; d3 <= 6; d3++) {
          for (int d4 = 1; d4 <= 6; d4++) {
            sommeDe = sommeDePossible(d1, d2, d3, d4);
            
            for (int i = 2; i <= 12; i++) {
              
              if (presentDansLaSommeDe(sommeDe, i)) this.probaSimple[i] = this.probaSimple[i] + 1.0D; 
              this.vitesseVoie[i] = this.vitesseVoie[i] + nbFoisPresentDansLaSommeDe(sommeDe, i);
              for (int n2 = i + 1; n2 <= 12; n2++)
              { if (ouDoublePresent(sommeDe, i, n2)) this.probaOuDouble[i][n2] = this.probaOuDouble[i][n2] + 1.0D; 
                if (presentExactementDansLaSommeDe(sommeDe, i, n2)) this.probaEtDouble[i][n2] = this.probaEtDouble[i][n2] + 1.0D; 
                for (int n3 = n2 + 1; n3 <= 12; n3++)
                { if (triplePresent(sommeDe, i, n2, n3)) this.probaTriple[i][n2][n3] = this.probaTriple[i][n2][n3] + 1.0D;  }  } 
            } 
          } 
        } 
      } 
    }  double nbLancerPossible = Math.pow(6.0D, 4.0D);
    for (int n1 = 2; n1 <= 12; n1++) {
      this.probaSimple[n1] = this.probaSimple[n1] / nbLancerPossible;
      this.vitesseVoie[n1] = this.vitesseVoie[n1] / nbLancerPossible;
      for (int n2 = n1 + 1; n2 <= 12; n2++) {
        this.probaOuDouble[n1][n2] = this.probaOuDouble[n1][n2] / nbLancerPossible;
        this.probaEtDouble[n1][n2] = this.probaEtDouble[n1][n2] / nbLancerPossible;
        for (int n3 = n2 + 1; n3 <= 12; n3++) {
          this.probaTriple[n1][n2][n3] = this.probaTriple[n1][n2][n3] / nbLancerPossible;
          for (int i = 0; i < 40; i++) {
            this.probaApresNbLancer[n1][n2][n3][i] = Math.pow(this.probaTriple[n1][n2][n3], i);
            
            if (this.probaApresNbLancer[n1][n2][n3][i] < 0.5D && this.nbLancerPossible50[n1][n2][n3] == 0) this.nbLancerPossible50[n1][n2][n3] = i;
          
          } 
        } 
      } 
    } 
  }

  
  public int[][] sommeDePossible(int d1, int d2, int d3, int d4) {
    int[][] sommeDe = new int[3][2];
    
    sommeDe[0][0] = d1 + d2; sommeDe[0][1] = d3 + d4;
    sommeDe[1][0] = d1 + d3; sommeDe[1][1] = d2 + d4;
    sommeDe[2][0] = d1 + d4; sommeDe[2][1] = d2 + d3;
    
    return sommeDe;
  }


  
  public boolean presentDansLaSommeDe(int[][] sommeDe, int nb) {
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 2; j++) {
        if (sommeDe[i][j] == nb) return true; 
      } 
    }  return false;
  }


  
  public boolean presentExactementDansLaSommeDe(int[][] sommeDe, int n1, int n2) {
    for (int i = 0; i < 3; i++) {
      if ((sommeDe[i][0] == n1 && sommeDe[i][1] == n2) || (sommeDe[i][1] == n1 && sommeDe[i][0] == n2)) {
        return true;
      }
    } 

    
    return false;
  }

  
  public double nbFoisPresentDansLaSommeDe(int[][] sommeDe, int n) {
    double nb = 0.0D;
    for (int i = 0; i < 3; i++) {
      if (sommeDe[i][0] == n && sommeDe[i][1] == n) {
        return 2.0D;
      }
      if (sommeDe[i][0] == n || sommeDe[i][1] == n) nb = 1.0D;
    
    } 
    return nb;
  }

  
  public boolean ouDoublePresent(int[][] sommeDe, int n1, int n2) {
    return (presentDansLaSommeDe(sommeDe, n1) || presentDansLaSommeDe(sommeDe, n2));
  }

  
  public boolean triplePresent(int[][] sommeDe, int n1, int n2, int n3) {
    return (presentDansLaSommeDe(sommeDe, n1) || presentDansLaSommeDe(sommeDe, n2) || presentDansLaSommeDe(sommeDe, n3));
  }
  
  public void printProbaTriple() {
    System.out.println("### Proba triple");
    for (int n1 = 2; n1 <= 12; n1++) {
      for (int n2 = n1 + 1; n2 <= 12; n2++) {
        for (int n3 = n2 + 1; n3 <= 12; n3++)
          System.out.println(n1 + "," + n2 + "," + n3 + " : " + this.probaTriple[n1][n2][n3]); 
      } 
    } 
  }
  public void printProbaOuDouble() {
    System.out.println("### Proba OU double");
    for (int n1 = 2; n1 <= 12; n1++) {
      for (int n2 = n1 + 1; n2 <= 12; n2++)
        System.out.println(n1 + "," + n2 + ", : " + this.probaOuDouble[n1][n2]); 
    } 
  }
  
  public void printProbaEtDouble() {
    System.out.println("### Proba ET double");
    for (int n1 = 2; n1 <= 12; n1++) {
      for (int n2 = n1 + 1; n2 <= 12; n2++) {
        System.out.println(n1 + "," + n2 + ", : " + this.probaEtDouble[n1][n2]);
      }
    } 
  }
  
  public void printProbaSimple() {
    System.out.println("### Proba simple");
    for (int n1 = 2; n1 <= 12; n1++) {
      System.out.println(n1 + " " + this.probaSimple[n1]);
    }
  }
  
  public void printprobaApresNbLancer() {
    System.out.println("### Proba apres nb lancer");
    
    for (int n1 = 2; n1 <= 12; n1++) {
      for (int n2 = n1 + 1; n2 <= 12; n2++) {
        for (int n3 = n2 + 1; n3 <= 12; n3++) {
          System.out.print(n1 + "," + n2 + "," + n3 + " : ");
          for (int i = 0; i < 15; i++)
            System.out.println(i + " -> " + this.probaApresNbLancer[n1][n2][n3][i] + "  "); 
        } 
      } 
    } 
  }
  public void printnbLancerPossible50() {
    System.out.println("### Nb Lancer jusque 50");
    for (int n1 = 2; n1 <= 12; n1++) {
      for (int n2 = n1 + 1; n2 <= 12; n2++) {
        for (int n3 = n2 + 1; n3 <= 12; n3++)
          System.out.println(n1 + "," + n2 + "," + n3 + " : " + this.nbLancerPossible50[n1][n2][n3]); 
      } 
    } 
  }
  public void printVitesseVoie() {
    System.out.println("### Vitesse voie");
    for (int n1 = 2; n1 <= 12; n1++) {
      System.out.println(n1 + " " + this.vitesseVoie[n1]);
    }
  }



  
  public Strat69() {
    this.probaSimple = new double[13];
    this.vitesseVoie = new double[13];
    this.probaOuDouble = new double[13][13];
    this.probaEtDouble = new double[13][13];
    this.probaTriple = new double[13][13][13];
    this.probaApresNbLancer = new double[13][13][13][40];
    this.nbLancerPossible50 = new int[13][13][13];
    
    calculProba();
  }





  
  public boolean existeBonzeSurVoie(Jeu jeu, int numVoie) {
    for (int i = 0; i < 3 - jeu.getBonzesRestants(); i++) {
      if (jeu.getBonzes()[i][0] == numVoie) return true; 
    } 
    return false;
  }






  
  public int nombreBonzesPlacer(Jeu jeu, int numChoix) {
    int nbBonzePlacer = 3 - jeu.getBonzesRestants();
    
    if (!existeBonzeSurVoie(jeu, jeu.getLesChoix()[numChoix][0])) nbBonzePlacer++;

    
    if (jeu.getLesChoix()[numChoix][1] != 0 && jeu
      .getLesChoix()[numChoix][1] != jeu.getLesChoix()[numChoix][0] && 
      !existeBonzeSurVoie(jeu, jeu.getLesChoix()[numChoix][1]))
    {
      nbBonzePlacer++;
    }
    
    return nbBonzePlacer;
  }








  
  public int emplacementBonze(Jeu jeu, int numChoix, int numBonze) {
    assert numBonze != 0;
    
    int numero = 0;
    for (int i = 0; i < 11; i++) {
      
      if (existeBonzeSurVoie(jeu, i + 2) || jeu
        .getLesChoix()[numChoix][0] == i + 2 || jeu
        .getLesChoix()[numChoix][1] == i + 2) numero++;
      
      if (numBonze == numero) return i + 2;
    
    } 
    
    assert false;
    return -3;
  }








  
  public ArrayList<HashMap> calculScoreTriplet(Jeu jeu, int numChoix, int voieBonze1) {
    ArrayList<HashMap> res = new ArrayList<>();

    
    for (int v1 = 0; v1 < 11; v1++) {
      if (v1 != voieBonze1 - 2 && !jeu.getBloque()[v1] && !jeu.getFini()[v1]) {
        for (int v2 = v1 + 1; v2 < 11; v2++) {
          if (v2 != voieBonze1 - 2 && !jeu.getBloque()[v2] && !jeu.getFini()[v2]) {
            HashMap<String, Double> infos = infosTriplet(jeu, numChoix, voieBonze1, v1 + 2, v2 + 2);
            infos.put("probaRealise", Double.valueOf(this.probaEtDouble[v1 + 2][v2 + 2]));
            res.add(infos);
          } 
        } 
      }
    } 
    return res;
  }








  
  public ArrayList<HashMap> calculScoreTriplet(Jeu jeu, int numChoix, int voieBonze1, int voieBonze2) {
    ArrayList<HashMap> res = new ArrayList<>();

    
    for (int v = 0; v < 11; v++) {
      if (v != voieBonze1 - 2 && v != voieBonze2 - 2 && !jeu.getBloque()[v] && !jeu.getFini()[v]) {
        HashMap<String, Double> infos = infosTriplet(jeu, numChoix, voieBonze1, voieBonze2, v + 2);
        infos.put("probaRealise", Double.valueOf(this.probaSimple[v + 2]));
        res.add(infos);
      } 
    } 
    
    return res;
  }









  
  public ArrayList<HashMap> calculScoreTriplet(Jeu jeu, int numChoix, int voieBonze1, int voieBonze2, int voieBonze3) {
    ArrayList<HashMap> a = new ArrayList<>();
    
    HashMap<String, Double> h = infosTriplet(jeu, numChoix, voieBonze1, voieBonze2, voieBonze3);
    h.put("probaRealise", Double.valueOf(1.0D));
    
    a.add(h);
    
    return a;
  }










  
  public HashMap infosTriplet(Jeu jeu, int numChoix, int voieBonze1, int voieBonze2, int voieBonze3) {
    HashMap<Object, Object> infosTriplet = new HashMap<>();
    int voieMin = 0;

    
    int[] voieBonze = triVoieBonze(voieBonze1, voieBonze2, voieBonze3);
    
    double nbLancerVoie1 = nbLancerPourGagnerVoie(jeu, numChoix, voieBonze[0]);
    double nbLancerVoie2 = nbLancerPourGagnerVoie(jeu, numChoix, voieBonze[1]);
    double nbLancerVoie3 = nbLancerPourGagnerVoie(jeu, numChoix, voieBonze[2]);

    
    double nbLancerMin = Math.min(nbLancerVoie1, Math.min(nbLancerVoie2, nbLancerVoie3));
    
    if (nbLancerVoie1 == nbLancerMin) voieMin = voieBonze[0]; 
    if (nbLancerVoie2 == nbLancerMin) voieMin = voieBonze[1]; 
    if (nbLancerVoie3 == nbLancerMin) voieMin = voieBonze[2];

    
    infosTriplet.put("voie1", Double.valueOf(voieBonze[0])); infosTriplet.put("voie2", Double.valueOf(voieBonze[1])); infosTriplet.put("voie3", Double.valueOf(voieBonze[2]));
    
    infosTriplet.put("nbLancerVoie1", Double.valueOf(nbLancerVoie1));
    infosTriplet.put("nbLancerVoie2", Double.valueOf(nbLancerVoie2));
    infosTriplet.put("nbLancerVoie3", Double.valueOf(nbLancerVoie3));

    
    infosTriplet.put("voieMin", Double.valueOf(voieMin));
    infosTriplet.put("nbLancerMinGain", Double.valueOf(nbLancerMin));
    infosTriplet.put("NbLancerPossible", Double.valueOf(this.nbLancerPossible50[voieBonze[0]][voieBonze[1]][voieBonze[2]]));

    
    infosTriplet.put("probaDeGagnerVoie1", Double.valueOf(Math.pow(this.probaTriple[voieBonze[0]][voieBonze[1]][voieBonze[2]], nbLancerVoie1)));
    infosTriplet.put("probaDeGagnerVoie2", Double.valueOf(Math.pow(this.probaTriple[voieBonze[0]][voieBonze[1]][voieBonze[2]], nbLancerVoie2)));
    infosTriplet.put("probaDeGagnerVoie3", Double.valueOf(Math.pow(this.probaTriple[voieBonze[0]][voieBonze[1]][voieBonze[2]], nbLancerVoie3)));
    infosTriplet.put("probaDeGagnerMax", Double.valueOf(Math.pow(this.probaTriple[voieBonze[0]][voieBonze[1]][voieBonze[2]], nbLancerMin)));

    
    return infosTriplet;
  }


  public double nbLancerPourGagnerVoie(Jeu jeu, int numChoix, int voie) {
    double resteVoie = (jeu.getMaximum()[voie - 2] - avancementSurLaVoie(jeu, voie, jeu.getActif()));
    
    if (jeu.getLesChoix()[numChoix][0] == voie) resteVoie--; 
    if (jeu.getLesChoix()[numChoix][1] == voie) resteVoie--;

    
    return (resteVoie <= 0.0D) ? 0.0D : (resteVoie / this.vitesseVoie[voie]);
  }

  
  public ArrayList<HashMap> calculScoreTriplet(Jeu jeu, int numChoix) {
    switch (nombreBonzesPlacer(jeu, numChoix)) {
      
      case 1:
        return calculScoreTriplet(jeu, numChoix, emplacementBonze(jeu, numChoix, 1));
      
      case 2:
        return calculScoreTriplet(jeu, numChoix, emplacementBonze(jeu, numChoix, 1), emplacementBonze(jeu, numChoix, 2));
      
      case 3:
        return calculScoreTriplet(jeu, numChoix, emplacementBonze(jeu, numChoix, 1), emplacementBonze(jeu, numChoix, 2), emplacementBonze(jeu, numChoix, 3));
    }  assert false;
    return null;
  }


  
  public void printScoreTriplet(ArrayList<HashMap> scoreTriplet) {
    for (HashMap m : scoreTriplet) {
      System.out.println((new StringBuilder()).append(m.get("voie1")).append(",").append(m.get("voie2")).append(",").append(m.get("voie3")).append(" ").append(m).toString());
    }
  }


  
  public double nbLancerNecessaireGainVoieParAdversaire(Jeu jeu, int voie) {
    int resteVoie = jeu.getMaximum()[voie - 2] - jeu.avancementAutreJoueur()[voie - 2];
    
    return resteVoie / this.vitesseVoie[voie];
  }

  
  public double nbLancerMinAdversaire(Jeu jeu) {
    double nbLancerMin = 100.0D;

    
    for (int i = 0; i < 11; i++) {
      double nbLancerVoie = nbLancerNecessaireGainVoieParAdversaire(jeu, i + 2);
      if (nbLancerVoie < nbLancerMin) {
        nbLancerMin = nbLancerVoie;
      }
    } 
    return nbLancerMin;
  }


  
  public HashMap gainLePlusProbable(Jeu jeu, ArrayList<HashMap> scoreTriplet) {
    double lePlusProbable = 0.0D;
    HashMap best = null;

    
    for (HashMap m : scoreTriplet) {
      if (((Double)m.get("probaRealise")).doubleValue() > lePlusProbable) {
        lePlusProbable = ((Double)m.get("probaRealise")).doubleValue();
        best = m;
      } 
    } 

    
    if (this.log) {
      System.out.println("  --------->" + best.get("voieMin") + " Ma proba : " + (Double)best.get("probaDeGagnerMax") + " nb Lancer adv : " + nbLancerNecessaireGainVoieParAdversaire(jeu, (int)((Double)(best.get("voieMin"))).doubleValue()));
    }
    
    return best;
  }

  
  public void probaGainRealiste(Jeu jeu, HashMap<String, Double> scoreTriplet) {
    double probaVoie;
    int voieProbaMax = ((Double)scoreTriplet.get("voieMin")).intValue();
    double probaMax = -1.0D;
    
    if (((Double)scoreTriplet.get("probaDeGagnerVoie1")).doubleValue() < this.probaFaibleGain && nbLancerNecessaireGainVoieParAdversaire(jeu, (int)((Double)scoreTriplet.get("voie1")).doubleValue()) < this.nbLancerAdverse) {
      probaVoie = 0.0D;
    } else {
      probaVoie = ((Double)scoreTriplet.get("probaDeGagnerVoie1")).doubleValue();
    } 
    if (probaVoie > probaMax) {
      probaMax = probaVoie;
      voieProbaMax = (int)((Double)scoreTriplet.get("voie1")).doubleValue();
    } 
    
    if (((Double)scoreTriplet.get("probaDeGagnerVoie2")).doubleValue() < this.probaFaibleGain && nbLancerNecessaireGainVoieParAdversaire(jeu, (int)((Double)scoreTriplet.get("voie2")).doubleValue()) < this.nbLancerAdverse) {
      probaVoie = 0.0D;
    } else {
      probaVoie = ((Double)scoreTriplet.get("probaDeGagnerVoie2")).doubleValue();
    } 
    if (probaVoie > probaMax) {
      probaMax = probaVoie;
      voieProbaMax = (int)((Double)scoreTriplet.get("voie2")).doubleValue();
    } 

    
    if (((Double)scoreTriplet.get("probaDeGagnerVoie3")).doubleValue() < this.probaFaibleGain && nbLancerNecessaireGainVoieParAdversaire(jeu, (int)((Double)scoreTriplet.get("voie3")).doubleValue()) < this.nbLancerAdverse) {
      probaVoie = 0.0D;
    } else {
      probaVoie = ((Double)scoreTriplet.get("probaDeGagnerVoie3")).doubleValue();
    } 
    if (probaVoie > probaMax) {
      probaMax = probaVoie;
      voieProbaMax = (int)((Double)scoreTriplet.get("voie3")).doubleValue();
    } 
    
    scoreTriplet.put("probaGainRealiste", Double.valueOf(probaMax));
    scoreTriplet.put("voieGainRealiste", Double.valueOf(voieProbaMax));
  }



  
  public int choix(Jeu jeu) {
    int monChoix = 0, nbVoieBestChoix = 0;
    double probaMax = -1.0D;
    HashMap bestGain = null;
    ArrayList<HashMap> debugTriplet = null;
    
    if (jeu.getBonzesRestants() == 3) this.nbLancer = 0;
    
    if (this.log) {
      System.out.println("######## Choix de l'ordi #######");
      jeu.printAvancement();
      jeu.printChoix();
    } 

    
    if (jeu.getNbChoix() == 1) return 0;

    
    for (int i = 0; i < jeu.getNbChoix(); i++) {
      
      if (jeu.getBonzesRestants() != 0) {
        
        debugTriplet = calculScoreTriplet(jeu, i);
        if (this.log) {
          
          System.out.println("------- Debug triplet -------");
          printScoreTriplet(debugTriplet);
        } 
        
        HashMap gainProbable = gainLePlusProbable(jeu, debugTriplet);
        probaGainRealiste(jeu, gainProbable);
        if (this.log) {
          System.out.println(jeu.getLesChoix()[i][0] + "," + jeu.getLesChoix()[i][1] + "  gain : " + gainProbable);
          
          if (((Double)gainProbable.get("probaGainRealiste")).doubleValue() - ((Double)gainProbable.get("probaDeGagnerMax")).doubleValue() != 0.0D) {
            System.out.println("    ====== Pas de CHANCE DE GAGNR LA VOIE  ======= " + gainProbable.get("voieMin"));
          }
        } 

        
        if (((Double)gainProbable.get("probaGainRealiste")).doubleValue() > probaMax) {
          probaMax = ((Double)gainProbable.get("probaGainRealiste")).doubleValue();
          bestGain = gainProbable;
          monChoix = i;
        } 
      } else {
        int nbVoieCurrentChoix;

        
        double probaVoie2, probaVoie1 = Math.pow(this.probaTriple[emplacementBonze(jeu, i, 1)][emplacementBonze(jeu, i, 2)][emplacementBonze(jeu, i, 3)], nbLancerPourGagnerVoie(jeu, i, jeu.getLesChoix()[i][0]));
        System.out.println(" Voie1 : emplacementBonze 1 = "+emplacementBonze(jeu, i, 1)+"  emplacementBonze 2 = "+emplacementBonze(jeu, i, 2)+"  emplacementBonze 3 = "+emplacementBonze(jeu, i, 3)+" nbLancerPourGagner = " + nbLancerPourGagnerVoie(jeu, i, jeu.getLesChoix()[i][0]));
        

        
        if (probaVoie1 < this.probaFaibleGain && nbLancerNecessaireGainVoieParAdversaire(jeu, jeu.getLesChoix()[i][0]) < this.nbLancerAdverse) {
          if (this.log) {
            System.out.println("       ==============  Voie " + jeu.getLesChoix()[i][0] + "  aucune chance  : " + probaVoie1 + " nbLancer adverse  " + nbLancerNecessaireGainVoieParAdversaire(jeu, jeu.getLesChoix()[i][0]));
          }
          probaVoie1 = 0.0D;
        } 



        
        if (jeu.getLesChoix()[i][1] != 0) {

          
          probaVoie2 = Math.pow(this.probaTriple[emplacementBonze(jeu, i, 1)][emplacementBonze(jeu, i, 2)][emplacementBonze(jeu, i, 3)], nbLancerPourGagnerVoie(jeu, i, jeu.getLesChoix()[i][1]));
          System.out.println(" Voie2 : emplacementBonze 1 = "+emplacementBonze(jeu, i, 1)+"  emplacementBonze 2 = "+emplacementBonze(jeu, i, 2)+"  emplacementBonze 3 = "+emplacementBonze(jeu, i, 3)+" nbLancerPourGagner = " + nbLancerPourGagnerVoie(jeu, i, jeu.getLesChoix()[i][1]));
          
          if (probaVoie2 < this.probaFaibleGain && nbLancerNecessaireGainVoieParAdversaire(jeu, jeu.getLesChoix()[i][1]) < this.nbLancerAdverse) {
            if (this.log) {
              System.out.println("       ============== Voie " + jeu.getLesChoix()[i][1] + "  aucune chance  : " + probaVoie2 + " nbLancer adverse  " + nbLancerNecessaireGainVoieParAdversaire(jeu, jeu.getLesChoix()[i][1]));
            }
            
            probaVoie2 = 0.0D;
          } 


          
          nbVoieCurrentChoix = 2;
        } else {
          
          nbVoieCurrentChoix = 1;
          probaVoie2 = -1.0D;
        } 
        if (this.log) System.out.println(jeu.getLesChoix()[i][0] + "," + jeu.getLesChoix()[i][1] + " :  probaVoie1 " + probaVoie1 + " :  probaVoie2 " + probaVoie2);


        
        if (probaVoie1 == 1.0D || probaVoie2 == 1.0D) {
          monChoix = i;
          
          break;
        } 
        if ((probaVoie1 > probaMax || probaVoie2 > probaMax) && nbVoieCurrentChoix >= nbVoieBestChoix) {
          monChoix = i;
          nbVoieBestChoix = nbVoieCurrentChoix;
          probaMax = Math.max(probaVoie1, probaVoie2);
        } else if (nbVoieCurrentChoix > nbVoieBestChoix) {
          monChoix = i;
          nbVoieBestChoix = nbVoieCurrentChoix;
          probaMax = Math.max(probaVoie1, probaVoie2);
        } 
      } 
    } 





    
    return monChoix;
  }




  
  public boolean stop(Jeu jeu) {
    double seuil;
    this.nbLancer++;

    
    if (this.log) {
      System.out.println("######## Stop ou non ordi #######");
      jeu.printAvancement();
    } 

    
    if (jeu.getBonzesRestants() != 0) return false;
    
    if (voieBloque(jeu)) return true;
    
    int[] tri = triVoieBonze(jeu.getBonzes()[0][0], jeu.getBonzes()[1][0], jeu.getBonzes()[2][0]);
    
    double proba = this.probaApresNbLancer[tri[0]][tri[1]][tri[2]][this.nbLancer];
    
    if (this.log) System.out.println(this.nbLancer + " lancer  proba " + proba);


    
    if (jeu.scoreAutreJoueur() == 0) { seuil = 0.1D; } else { seuil = 0.05D; }
    
    if (proba < seuil) {
      if (this.log) System.out.println(" J'arrete"); 
      return true;
    } 
    
    if (this.log) System.out.println(" Je continue"); 
    return false;
  }



  
  public String getName() {
    return "ur mom";
  }



  
  public int getGroupe() {
    return 7;
  }



  
  public int resteSurUneVoie(Jeu jeu, int numVoie, int joueur) {
    return jeu.getMaximum()[numVoie - 2] - avancementSurLaVoie(jeu, numVoie, joueur);
  }



  
  public int avancementSurLaVoie(Jeu j, int numVoie, int joueur) {
    if (joueur == j.getActif()) {
      for (int b = 0; b < 3; b++) {
        if (j.getBonzes()[b][0] == numVoie)
        {
          return j.getBonzes()[b][1];
        }
      } 
    }
    
    return j.avancementJoueurEnCours()[numVoie - 2];
  }

  
  public int adversaire(Jeu j) {
    return (j.getActif() + 1) % j.getNbJoueurs();
  }


  
  public int[] triVoieBonze(int voieBonze1, int voieBonze2, int voieBonze3) {
    int[] tri = new int[3];
    
    tri[0] = Math.min(voieBonze1, Math.min(voieBonze2, voieBonze3));
    tri[2] = Math.max(voieBonze1, Math.max(voieBonze2, voieBonze3));
    
    if (voieBonze1 != tri[0] && voieBonze1 != tri[2]) tri[1] = voieBonze1; 
    if (voieBonze2 != tri[0] && voieBonze2 != tri[2]) tri[1] = voieBonze2; 
    if (voieBonze3 != tri[0] && voieBonze3 != tri[2]) tri[1] = voieBonze3;
    
    return tri;
  }

  
  public boolean voieBloque(Jeu j) {
    for (int i = 0; i < 11; i++) {
      if (j.getBloque()[i]) return true; 
    } 
    return false;
  }
}



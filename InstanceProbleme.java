package rech_op;

import java.util.*;
import java.io.*;
import java.nio.file.Files;
/* /!\ parser à l'envers */
public class InstanceProbleme {
    
    private int nbCentres; // I
    private int nbDepots; // J
    private ArrayList<Float> coutFixe; // f i
    private ArrayList<Float> volume; // d j
    private ArrayList<ArrayList<Float>> distance; // h ij
    private ArrayList<ArrayList<Float>> coutOperationnel; // c ij
    
    private float distanceLimite; //Dmax
    private float proportion; //Cmin

    public InstanceProbleme(String path) {

	nbCentres = 0;
	nbDepots = 0;
	coutFixe = new ArrayList<Float>();
	volume = new ArrayList<Float>();
	distance = new ArrayList<ArrayList<Float>>();
	coutOperationnel = new ArrayList<ArrayList<Float>>();
	

	try {
	    BufferedReader reader = new BufferedReader(new FileReader(path));
	    int etape = 0;
	    // Etapes :
	    // 1. En train de lire nbCentres
	    // 2. En train de lire nbDepots
	    // 3. En train de lire coutFixe
	    // 4. En train de lire volume
	    // 5. En train de lire distance
	    // 6. En train de lire coutOperationnel
	    // 7. En train de lire distanceLimite
	    // 8. En train de lire proportion
	    String line = null;

	    while ((line = reader.readLine()) != null) {
		// Entre dans les étapes
		if (line.startsWith("set I:=")) {
		    etape = 1;
		} else if (line.startsWith("set J:=")) {
		    etape = 2;
		} else if (line.startsWith("param f:=")) {
		    etape = 3;
		} else if (line.startsWith("param d:=")) {
		    etape = 4;
		} else if (line.startsWith("param h:=")) {
		    etape = 5;
		} else if (line.startsWith("param c:=")) {
		    etape = 6;
		} else if (line.startsWith("param Dmax:=")) {
		    etape = 7;
		} else if (line.startsWith("param Cmin:=")) {
		    etape = 8;
		}
		// Sort des étapes
		else if (line.startsWith(";")) {
		    etape = 0;
		}
		// Traitement interne aux étapes
		else {
		    String[] array = line.split("\\s+"); 
		    Float val;
		    int index;
		    switch (etape) {
		    case 1:
			nbCentres++;
			distance.add(new ArrayList<Float>());
			coutOperationnel.add(new ArrayList<Float>());
			System.out.println("NbCentres : " + nbCentres);
			break;
		    case 2:
			nbDepots++;
			System.out.println("NbDepots : " + nbDepots);
			break;
		    case 3:
			coutFixe.add(Float.valueOf(array[1]));
			break;
		    case 4:
			volume.add(Float.valueOf(array[1]));
			break;
		    case 5:
			val = Float.valueOf(array[2]);
			index = Integer.valueOf(array[0]);
			distance.get(index-1).add(val);
			break;
		    case 6:
			val = Float.valueOf(array[2]);
			index = Integer.valueOf(array[0]);
			coutOperationnel.get(index-1).add(val);
			break;
		    case 7:
			distanceLimite = Float.valueOf(array[0]);
			break;
		    case 8:
			
			proportion = Float.valueOf(array[0].replaceAll("%", ""));
			break;
		    default:
			// saute les lignes vides
			break;
		    }
		}
	    }
	    reader.close();
	}
	catch (IOException e) {
	    System.err.format("IOException : %s%n", e);
	}
    } 

    public int nbCentres() {
	return nbCentres;
    }

    public int nbDepots() {
	return nbDepots;
    }

    public float distanceLimite() {
	return distanceLimite;
    }

    public float proportion() {
	return proportion;
    }

    public float coutFixe(int centre) {
	return coutFixe.get(centre-1);
    }

    public float volume(int depot) {
	return volume.get(depot-1);
    }

    public float distance(int centre, int depot) {
	return distance.get(centre-1).get(depot-1);
    }

    public float coutOperationnel(int centre, int depot) {
	return coutOperationnel.get(centre-1).get(depot-1);
    }

}
    

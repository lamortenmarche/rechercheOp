package rech_op;

import java.lang.*;
import java.util.*;

public class Heuristique
{
	public InstanceProbleme ip;
	public boolean[] m;
	public boolean[][] r;

	final static public void main()
	{
		ip = new InstanceProbleme("instances_cafe/A10-25C1.txt");

		m = new boolean[ip.nbDepots()];
		r = new boolean[ip.nbDepots()][ip.nbCentres()];

		boolean[] tout_relie = new boolean[ip.nbCentres()];
		for (int i = 0; i < ip.nbCentres(); i++)
		{
			tout_relie[i] = false;
		}

		float total_production = 0;
		for (int j = 0; j < ip.nbCentres(); j++)
		{
			total_production += ip.volume(j);
		}

		// ouvrir un premier depot de cout minimal
		int premier_depot = 0;
		for (int i = 1; i < ip.nbDepots(); i++)
		{
			// penser a meilleur algo rapport qualite / prix
			if (ip.coutFixe(i) < ip.coutFixe(premier_depot))
			{
				premier_depot = i;
			}
		}
		m[premier_depot] = true;

	
		for (int j = 0; j < ip.nbCentres(); j++)
		{
			if (! tout_relie[j])
			{
				int depotOptimal =0;
				float tmp = coutRattachementA(0, j);
				for (int k=1; k<ip.nbDepots(); k++)
				{
					tmp2 = coutRattachementA(k, j);
					if (tmp2 < tmp)
					{
						tmp = tmp2;
						depotOptimal = k;
					}
				}
				m[depotOptimal]	= true;
				r[depotOptimal][j] = true;
			}
		}
		float proportionActuelle = proportionCourante();
		float proportionCible = (ip.proportion() * total_production);

		while (proportionActuelle < proportionCible)
		{
			ArrayList<Integer> pas_proches = pasProche();
			if (pas_proches.size() > 0)
			{
				int j = pas_proches.get(0);
				ArrayList<Integer> depotsProches = depotsOuvertsProches(j);
				r[ depotRelie(j) ][j] = false;
				r[ depotsProches[j] ][j] = true;
				proportionActuelle += ip.volume(j);
			}
		}

		System.out.print("m = [");
		for (int i = 0; i < ip.nbDepots(); i++)
		{
			System.out.println(" " + m[i] + " ");	
		}
		System.out.println("]");

		System.out.print("r = [");
		for (int i = 0; i < ip.nbDepots(); i++)
		{
			System.out.print("[");
			for (int j = 0; j < ip.nbCentres(); j++)
			{
				System.out.println(" " + r[i][j] + " ");	
			}
			System.out.println("]");
		}
		System.out.println("]");

		/*
		    Vérifier que la contrainte concernant la proportion \(v\) est respectée
		    Si ce n'est pas le cas Alors
		        Effectuer les changements nécessaires dans m et r pour y parvenir
		        \(\rightarrow\) changement de rattachements entre les coopératives et
		           les dépôts déjà ouverts dans m
		        \(\rightarrow\) ouverture de dépôts si réellement nécessaire
		    */

	}

	public boolean toutAVrai(boolean[] array)
	{
		for (boolean b : array)
		{
			if (! b)
				return false;
		}
		return true;
	}

	public float coutRattachementA(int depot, int centre)
	{
		if (m[depot])
		{
			return ip.coutOperationnel(centre, depot);
		}
		else
		{
			return ip.coutFixe(depot) + ip.coutOperationnel(centre, depot);
		}
	}

	public int depotRelie(int centre)
	{
		int i = 0;
		while (!(r[i][centre]))
			i++;
		return i;
	}

	public boolean estProche(int depot, int centre)
	{
		return (ip.distance(centre, depot) < ip.distanceMax());
	}

	public float proportionCourante()
	{
		float production = 0;
		float distanceMax = ip.distanceLimite();
		for (int j = 0; j < ip.nbCentres(); j++)
		{
			if (estProche(j, deportRelie(j, r)))
			{
				production += ip.volume(j);
			}
		}
		return production;
	}

	public class ProdCentreComparator
	{
	    public boolean compare(int centre1, int centre2)
	    {
	        return (ip.volume(centre1) < ip.volume(centre2));
	    }
	}

	public ArrayList<Integer> pasProche()
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int j = 0; j < ip.nbCentres(); j++)
		{
			if (! (estProche(j, deportRelie(j, r))))
			{
				result.add(Integer.valueOf(j));
			}
		}
		Collections.sort(result, new ProdCentreComparator());
		return result;
	}

	public class DepotComparator
	{
		public int centre;

		public DepotComparator(int c)
		{
			centre = c;
		}

	    public boolean compare(int depot1, int depot2)
	    {
	        return (coutRattachementA(depot1, centre) < coutRattachementA(depot2, centre));
	    }
	}

	public ArrayList<Integer> depotsOuvertsProches(int centre)
	{
		ArrayList<Integer> result = new ArrayList<Integer>();
		for (int i = 0; i < ip.nbDepots(); i++)
		{
			if (m[i] && estProche(centre, i))
			{
				result.add(Integer.valueOf(i));
			}
		}
		Collections.sort(result, new DepotComparator(centre));
		return result;
	}
}
package command;

import ihm.Ihm;
import memento.Memento;
import receiver.Enregistreur;
import receiver.Manager;
import receiver.Moteur;
import receiver.MoteurImpl;
import state.State;

/**
 * Concrete Command "Selectionner" implementant l'interface Command
 * 
 * @author Alexis LE MASLE et Fanny PRIEUR
 * 
 */
public class Selectionner implements Command {

	/**
	 * Nouvelle instance de l'interface Moteur declarant la methode selectionner
	 * 
	 * @see Moteur
	 */
	private Moteur moteur;

	private Ihm ihm;

	private Enregistreur enregistreur;

	int deb;

	int fin;

	private SelectionnerMemento memento;

	private Manager manager;

	/**
	 * Constructeur de la classe Selectionner
	 * 
	 * @param moteur
	 * @param ihm
	 * @param enregistreur
	 * @param manager
	 */
	public Selectionner(Moteur moteur, Ihm ihm, Enregistreur enregistreur, Manager manager) {
		this.moteur = moteur;
		this.ihm = ihm;
		this.enregistreur = enregistreur;
		this.manager = manager;
	}

	// Operations

	/**
	 * Appel de la mise en oeuvre de la fonction "selectionner" dans
	 * l'implementation Moteur. Demande les bornes de debut et de fin a
	 * l'utilsateur. Si la commande est en mode replay alors elle utilise les
	 * dernieres bornes.
	 * 
	 * @see MoteurImpl
	 * 
	 */
	@Override
	public void execute() {
		SelectionnerMemento m = getMemento();
		if (enregistreur.getPlay() || manager.getPlay()) {
			deb = memento.getDeb();
			fin = memento.getFin();
			if (deb > fin) {
				int tmp = fin;
				fin = deb;
				deb = tmp;
			}
			moteur.selectionner(deb, fin);
		} else {
			deb = ihm.getDebut();
			fin = ihm.getFin();
			if (deb > fin) {
				int tmp = fin;
				fin = deb;
				deb = tmp;
			}
			moteur.selectionner(deb, fin);
			if (enregistreur.getRecord()) {
				m.setDeb(deb);
				m.setFin(fin);
				enregistreur.addMemento(m);
				enregistreur.addCommand(this);
			}
		}
		if (!manager.getPlay()) {
			State st = manager.getStateCourant();
			m.setDeb(deb);
			m.setFin(fin);
			st.addMem(m);
			st.addCmd(this);
			manager.saveState();
			manager.emptyRedo();
		}
	}

	public void setIhm(Ihm ihm) {
		this.ihm = ihm;
	}

	/**
	 * Cree un nouveau SelectionnerMemento
	 */
	@Override
	public SelectionnerMemento getMemento() {
		return new SelectionnerMemento();
	}

	/**
	 * Classe SelectionnerMemento implementant Memento et ne servant qu'a
	 * Selectionner
	 * 
	 * @author Alexis LE MASLE et Fanny PRIEUR
	 *
	 */
	public class SelectionnerMemento implements Memento<SelectionnerMemento> {

		/**
		 * Les postions de debut et fin de selection a sauvegarder
		 */
		int deb;
		int fin;

		public int getDeb() {
			return deb;
		}

		public void setDeb(int deb) {
			this.deb = deb;
		}

		public int getFin() {
			return fin;
		}

		public void setFin(int fin) {
			this.fin = fin;
		}

		@Override
		public SelectionnerMemento clone() {
			SelectionnerMemento m = new SelectionnerMemento();
			m.setDeb(deb);
			m.setFin(fin);
			return m;
		}

	}

	@Override
	public void setMemento(Memento<?> mem) {
		this.memento = (SelectionnerMemento) mem;
	}

	@Override
	public void setMoteur(Moteur moteur) {
		this.moteur = moteur;
	}

	@Override
	public Moteur getMoteur() {
		return moteur;
	}

	@Override
	public Selectionner clone() {
		Selectionner a = new Selectionner(moteur.clone(), ihm, enregistreur, manager);
		return a;
	}

}

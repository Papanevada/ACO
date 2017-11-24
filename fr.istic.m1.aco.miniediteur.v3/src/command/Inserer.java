package command;

import etats.State;
import ihm.Ihm;
import memento.Memento;
import receiver.Enregistreur;
import receiver.Manager;
import receiver.Moteur;
import receiver.MoteurImpl;

/**
 * Concrete Command "Inserer" implementant l'interface Command
 * 
 * @author Alexis LE MASLE et Fanny PRIEUR
 * 
 */
public class Inserer implements Command {

	/**
	 * Nouvelle instance de l'interface Moteur declarant la methode inserer
	 * 
	 * @see Moteur
	 */
	private Moteur moteur;

	private Enregistreur enregistreur;

	private InsererMemento memento;

	private Manager manager;

	/**
	 * Nouvelle String a inserer
	 */
	private Ihm ihm;

	/**
	 * Constructeur de la classe Inserer
	 * 
	 * @param moteur
	 * @param str
	 */
	public Inserer(Moteur moteur, Ihm ihm, Enregistreur enregistreur, Manager manager) {
		this.moteur = moteur;
		this.ihm = ihm;
		this.enregistreur = enregistreur;
		this.manager = manager;
	}

	/**
	 * Appel de la mise en oeuvre de la fonction "inserer" dans l'implementation
	 * Moteur.
	 * 
	 * @see MoteurImpl
	 * 
	 */
	public void execute() {
		String str = "";
		InsererMemento m = getMemento();
		if (enregistreur.getPlay() || manager.getPlay()) {
			str = memento.getTexte();
			moteur.inserer(str);
		} else {
			str = ihm.getText();
			moteur.inserer(str);
			if (enregistreur.getRecord()) {
				m.setTexte(str);
				enregistreur.addMemento(m);
				enregistreur.addCommand(this);
			}
		}
		State st = manager.getStateCourant();
		st.getLmem().add(m);
		st.getLcmd().add(this);
		manager.saveState();
	}

	public void setIhm(Ihm ihm) {
		this.ihm = ihm;
	}

	@Override
	public InsererMemento getMemento() {
		return new InsererMemento();
	}

	private class InsererMemento implements Memento<InsererMemento> {

		private String texte;

		public String getTexte() {
			return texte;
		}

		public void setTexte(String texte) {
			this.texte = texte;
		}

	}

	@Override
	public void setMemento(Memento<?> mem) {
		this.memento = (InsererMemento) mem;
	}

	@Override
	public void setMoteur(Moteur moteur) {
		this.moteur = moteur;
	}

	@Override
	public Moteur getMoteur() {
		return moteur;
	}

}
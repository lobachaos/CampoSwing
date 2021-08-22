package campominado.visao;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import campominado.logica.Campo;
import campominado.logica.CampoEvento;
import campominado.logica.CampoObservador;

@SuppressWarnings("serial")
public class BotaoCampo extends JButton implements CampoObservador, MouseListener {

	private final Color BG_PADRAO = new Color(190, 190, 190);
	private final Color BG_MARCAR = new Color(8, 179, 247);
	private final Color BG_EXPLODIR = new Color(189, 66, 68);
	private final Color TEXTO_VERDE = new Color(0, 100, 0);
	
	
	
	
	private Campo campo;

	
	
	
	public BotaoCampo(Campo campo) {
		this.campo = campo;
		setBorder(BorderFactory.createBevelBorder(0));
		setBackground(BG_PADRAO);
		addMouseListener(this);
		campo.registrarObserver(this);

	}

	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		switch (evento) {
		case ABRIR:
			aplicarEstiloAbrir();
			break;
		case MARCAR:
			aplicarEstiloMarcar();
			break;
		case EXPLODIR:
			aplicarEstiloExplodir();
			break;
		default:
			aplicarEstiloPadrao();
			setBorder(BorderFactory.createBevelBorder(0));

		}
	}

	private void aplicarEstiloPadrao() {
		setBackground(BG_PADRAO);
		setText("");
	}

	private void aplicarEstiloExplodir() {
		setBackground(BG_EXPLODIR);
		setForeground(Color.WHITE);
		setText("X");

	}

	private void aplicarEstiloMarcar() {
		setBackground(BG_MARCAR);
		setForeground(Color.BLACK);
		setText("M");
	}

	private void aplicarEstiloAbrir() {
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		if(campo.isMinado()){
			setBackground(BG_EXPLODIR);
			setText("X");
			return;
		}
		
		setBackground(BG_PADRAO);

		switch (campo.minasNaVizinhanca()) {
		case 1 -> setForeground(TEXTO_VERDE);
		case 2 -> setForeground(Color.BLUE);
		case 3 -> setForeground(Color.YELLOW);
		case 4, 5, 6 -> setForeground(Color.RED);
		default -> setForeground(Color.PINK);

		}

		String valor = !campo.vizinhacaSafe() ? campo.minasNaVizinhanca() + "" : "";
		setText(valor);

	}

	public void mousePressed(MouseEvent e) {
		if (e.getButton() == 1) {
			campo.abrir();
		} else {
			campo.alterarMarcacao();
		}
	}

	public void mouseClicked(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

}

package campominado.visao;

import javax.swing.JFrame;

import campominado.logica.Tabuleiro;

@SuppressWarnings("serial")
public class TelaPrincipal extends JFrame {
	public TelaPrincipal() {
		Tabuleiro tabuleiro = new Tabuleiro(16, 30, 50);
		PainelTabuleiro painelTabuleiro = new PainelTabuleiro(tabuleiro);
		add(painelTabuleiro);
		
		
		setTitle("Campo Minado");
		setVisible(true);
		setSize(690,438);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
	}
	public static void main(String[] args) {
		new TelaPrincipal();
	}

}

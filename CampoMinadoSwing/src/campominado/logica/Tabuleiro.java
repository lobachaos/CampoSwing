package campominado.logica;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Tabuleiro implements CampoObservador {
	private final int linhas;
	private final int colunas;
	private final int minas;

	private final List<Campo> campos = new ArrayList<Campo>();
	private final List<Consumer<Resultado>> observadores = new ArrayList<>();

	public void registrarObservador(Consumer<Resultado> observador) {
		observadores.add(observador);
	}
	
	
	public void paraCada(Consumer<Campo> funcao) {
		campos.forEach(funcao);
	}
	

	public int getLinhas() {
		return linhas;
	}



	public int getColunas() {
		return colunas;
	}



	public int getMinas() {
		return minas;
	}



	public void notificarObservadores(boolean resultado) {
		observadores.stream().forEach(o -> o.accept(new Resultado(resultado)));
	}

	public Tabuleiro(int linhas, int colunas, int minas) {
		this.linhas = linhas;
		this.colunas = colunas;
		this.minas = minas;

		gerarCampos();
		associarVizinhos();
		sortearMinas();

	}

	public void abrir(int linha, int coluna) {
		campos.stream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst()
				.ifPresent(c -> c.abrir());
	}

	

	public void marcar(int linha, int coluna) {
		campos.stream().filter(c -> c.getLinha() == linha && c.getColuna() == coluna).findFirst()
				.ifPresent(c -> c.alterarMarcacao());
	}

	// i = linhas j = colunas
	private void gerarCampos() {
		for (int i = 0; i < linhas; i++) {
			for (int j = 0; j < colunas; j++) {
				Campo campo = new Campo(i, j);
				campo.registrarObserver(this);
				campos.add(campo);
			}
		}

	}

	private void associarVizinhos() {
		for (Campo c1 : campos) {
			for (Campo c2 : campos) {
				c1.adicionarVizinho(c2);
			}
		}
	}

	private void sortearMinas() {
		int minasArmadas = 0;

		do {
			int aleatorio = (int) (Math.random() * campos.size());
			campos.get(aleatorio).minar();
			minasArmadas = (int) campos.stream().filter(m -> m.isMinado()).count();
		} while (minasArmadas < minas);

	}

	public boolean objetivoAlcancado() {
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}

	public void reiniciarCampo() {
		campos.stream().forEach(c -> c.reiniciar());
		sortearMinas();
	}

	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		if (evento == CampoEvento.EXPLODIR) {
			mostrarMinas();
			notificarObservadores(false);
		} else if (objetivoAlcancado()) {
				System.out.println("Win!");
				notificarObservadores(true);
			}
		}
	
	private void mostrarMinas() {
		campos.stream().filter(c -> c.isMinado()).filter(c-> !c.isMarcado())
		.forEach(c -> c.setAberto(true));
	}

}

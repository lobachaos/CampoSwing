package campominado.logica;

import java.util.ArrayList;
import java.util.List;

public class Campo {
	private final int linha;
	private final int coluna;
	private boolean aberto;
	private boolean minado;
	private boolean marcado = false;
	private List<Campo> vizinhos = new ArrayList<Campo>();
	private List<CampoObservador> observers = new ArrayList<>();

	public void registrarObserver(CampoObservador observer) {
		observers.add(observer);
	}

	public void notificarObservers(CampoEvento evento) {
		observers.stream().forEach(o -> o.eventoOcorreu(this, evento));
	}

	public void setObservers(List<CampoObservador> observers) {
		this.observers = observers;
	}

	Campo(int linha, int coluna) {
		this.linha = linha;
		this.coluna = coluna;
	}

	boolean adicionarVizinho(Campo vizinho) {
		boolean linhaDiferente = linha != vizinho.linha;
		boolean colunaDiferente = coluna != vizinho.coluna;
		boolean diagonal = linhaDiferente && colunaDiferente;

		int deltaLinha = Math.abs(linha - vizinho.linha);
		int deltaColuna = Math.abs(coluna - vizinho.coluna);
		int deltaTotal = deltaLinha + deltaColuna;

		if (deltaTotal == 1 && !diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else if (deltaTotal == 2 && diagonal) {
			vizinhos.add(vizinho);
			return true;
		} else {
			return false;
		}

	}

	public void alterarMarcacao() {
		if (!aberto) {
			marcado = !marcado;

			if (marcado) {
				notificarObservers(CampoEvento.MARCAR);
			} else {
				notificarObservers(CampoEvento.DESMARCAR);
			}
		}

	}

	public boolean abrir() {
		if (!aberto && !marcado) {
			if (minado) {
				notificarObservers(CampoEvento.EXPLODIR);
				return true;
			}

			setAberto(true);

			if (vizinhacaSafe()) {
				vizinhos.forEach(v -> v.abrir());
			}
			return true;

		} else {
			return false;

		}
	}

	 public boolean vizinhacaSafe() {
		return vizinhos.stream().noneMatch(v -> v.minado);

	}

	void minar() {
		minado = true;
	}

	public boolean isMarcado() {
		return marcado;
	}

	public int getLinha() {
		return linha;
	}

	public int getColuna() {
		return coluna;
	}

	boolean objetivoAlcancado() {
		boolean desvendado = !minado && aberto;
		boolean protegido = minado && marcado;
		return desvendado || protegido;
	}

	public int minasNaVizinhanca() {
		return (int) vizinhos.stream().filter(v -> v.minado).count();

	}

	void reiniciar() {
		aberto = false;
		minado = false;
		marcado = false;
		notificarObservers(CampoEvento.REINICIAR);
	}

	public boolean isMinado() {
		return minado;
	}

	void setAberto(boolean aberto) {
		this.aberto = aberto;
		if (aberto) {
			notificarObservers(CampoEvento.ABRIR);
		}
	}

}

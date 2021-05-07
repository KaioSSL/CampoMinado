package br.com.cod3r.cm.modelo;

//Classe responsável por retornar um evento do tipo 
public class ResultadoEvento {
	//Variavel privada, final, que define se o player ganhou ou não.
	private final boolean ganhou;
	//Construtor de um novo evento de resultado do game.
	public ResultadoEvento(Boolean ganhou) {
		//Atribuição padrão
		this.ganhou = ganhou;
	}
	//Retorna se o evento é verdadeiro(ganhou), ou falso(perdeu)
	public Boolean isGanhou() {
		return ganhou;
	}
}

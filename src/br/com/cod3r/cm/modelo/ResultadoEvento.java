package br.com.cod3r.cm.modelo;

//Classe respons�vel por retornar um evento do tipo 
public class ResultadoEvento {
	//Variavel privada, final, que define se o player ganhou ou n�o.
	private final boolean ganhou;
	//Construtor de um novo evento de resultado do game.
	public ResultadoEvento(Boolean ganhou) {
		//Atribui��o padr�o
		this.ganhou = ganhou;
	}
	//Retorna se o evento � verdadeiro(ganhou), ou falso(perdeu)
	public Boolean isGanhou() {
		return ganhou;
	}
}

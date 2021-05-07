package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;
//Objeto de modelo para representar o Campo na l�gica do jogo
public class Campo {
	//Atributo final(N�o pode ser modificado uma vez que foi inicializado), privado(Acesso somente interno), inteiro(Tipo)
	private final int linha;
	//Atributo final(N�o pode ser modificado uma vez que foi inicializado), privado(Acesso somente interno), inteiro(Tipo)
	private final int coluna;
	//Atributo privado(N�o pode ser modificado uma vez que foi iniciailizado)
	private boolean aberto = false;
	//Atributo privado(N�o pode ser modificado uma vez que foi iniciailizado)
	private boolean minado = false;
	//Atributo privado(N�o pode ser modificado uma vez que foi iniciailizado)
	private boolean marcado = false;
	//ArrayList de Objetos do Tipo Campo, � poss�vel criar Arrays de N's tipos devido ao uso do Generics
	//Esse array ser� respons�vel por guardar todos os vizinhos � este campo, valida��o de vizinho ou n�o dever� levar em considera��o a posi��o no plano cartesiano do campo (linha,coluna,)(x,y).
	private List<Campo> vizinhos = new ArrayList<>();
	//Esse  Array ser� respons�vel por guardar todos os objetos que ir�o ficar monitorando os eventos realizados pelo Campo e no Campo.
	private List<CampoObservador> observadores = new ArrayList<>();
	
	//Construtor de Campo, recebe inteiro de linha e coluna
	public Campo(int linha, int coluna) {
		//Simples atribui��o de valores
		this.linha = linha;
		//Simples atribui��o de valores
		this.coluna = coluna;
	}
	//Retorna se o campo est� aberto
	public boolean isAberto() {
		return aberto;
	}
	//Retorna se o campo est� n�o aberto
	public boolean isFechado() {
		return !aberto;
	}
	//Torna um Campo que n�o estava aberto, aberto.
	public void setAberto(boolean aberto) {
		//Atribubi��o normal
		this.aberto = aberto;
		//Verifica se o campo est� aberto
		if(aberto) {
			//Notifica a todos os observadores, que este campo foi aberto, ou que deve ser aberto.
			notificarObservadores(CampoEvento.ABRIR);
		}
	}
	//Retorna se o campo est� minado ou n�o
	public boolean isMinado() {
		return minado;
	}
	//Define um campo como minado ou n�o
	public void setMinado(boolean minado) {
		this.minado = minado;
	}
	//Retorna se um campo est� marcado ou n�o
	public boolean isMarcado() {
		return marcado;
	}
	//Define um campo como marcado ou n�o
	public void setMarcado(boolean marcado) {
		this.marcado = marcado;
	}
	//Retorna a linha(x) deste campo
	public int getLinha() {
		return linha;
	}
	//Retorna a coluna(y) deste campo
	public int getColuna() {
		return coluna;
	}
	//Define um campo como minado;
	public void minar() {
		minado = true;
	}
	
	//Este m�todo � respons�vel por registrar neste Campo, algum objeto que ir� observar os eventos realizados pelo Campo e no Campo. 
	public void registrarObservador(CampoObservador observador) {
		//M�todo add � respons�vel por alocar mais um valor em um ArrayList, nesta linha um novo Objeto do tipo CampoObservador � adicionado no Array observadores
		observadores.add(observador);
	}
	
	//Este m�todo � respons�vel por notificar todos os observadores registrados na variavel "observadores", sempre que um evento for realizado pelo Campo e no Campo.
	private void notificarObservadores(CampoEvento evento) {
		//M�todo .stream() retorna uma Stream em que � poss�vel chamar o m�todo foreach, para cada objeto encontrado dentro de observadores, � passado um lambda
		//neste lambda � recebido um CampoObservador e para cada CampoObservador na variavel observadores, ele notifica que um evento ocorreu passando o Campo em que ocorreu, e o evento que ocorreu.
		observadores.stream().forEach(o -> o.eventoOcorreu(this, evento));
	}
	
	//Um campo pode ter no m�ximo 8 vizinhos, visto que � poss�vel ter 4 diretamente ligados em sua dire��o horizontal e vertical, e 4 em suas diagonais
	//Para isso � realizado o seguinte calculo, caso estejam na mesma linha ou na mesma coluna, a valor absoluto diferen�a entre os pontos sempre ser� 1 Absoluto((x1 - x2)+(y1-y2)).
	//Caso n�o estejam nem na mesma, nem mesma coluna, poder� estar na diagonal, para isso, a diferen�a absoluta entre os pontos sempre ser� 2 
	public boolean adicionarVizinho(Campo vizinho) {
		//Verifica se a coluna do vizinho recebido � diferente da coluna deste Campo
		boolean linhaDif = this.getColuna() != vizinho.getColuna();
		//Verifica se a coluna do vizinho recebido � diferente da coluna deste Campo
		boolean colunaDif = this.getLinha() != vizinho.getLinha();
		//Verifica a partir das demais conclus�es, se est� na diagonal ou n�o.
		boolean diagonal = linhaDif && colunaDif;
		//Realiza a diferen�a absoluta entre a coluna deste campo e a coluna do vizinho, Math.abs() retorna um n�mero absoluto.
		int deltaColuna = Math.abs(this.getColuna() - vizinho.getColuna());
		//Realiza a diferen�a absoluta entrea a linha deste campo e a linha do vizinho.
		int deltaLinha = Math.abs(this.getLinha() - vizinho.getLinha());
		//Calculo geral, soma entre a diferen�a absoluta das linhas e colunas.
		int deltaGeral =  deltaColuna + deltaLinha;
		
		//Caso a diferen�a seja 1, e n�o esteja em uma diagonal, � um vizinho poss�vel
		if(deltaGeral == 1 && !diagonal) {
			//Adiciona o objeto recebido ao Array de vizinhos
			vizinhos.add(vizinho);
			//Retorna resultado verdadeiro para adi��o de vizinho
			return true;
		//Caso n�o esteja na diagonal, ou a diferen�a n�o seja 1, verifica se a diferen�a � 2 e est� na diagonal, caso sim, � um vizinho poss�vel
		}else if(deltaGeral==2 && diagonal) {
			//Adiciona o objeto recebido ao Array de vizinhos
			vizinhos.add(vizinho);
			//Retorna resultado verdadeiro para adi��o de vizinho
			return true;
		//Caso nenhuma das clausulas acima seja atendida, n�o realiza a adi��o do vizinho
		}else {
			//Retorna resultado negativo para adi��o de vizinho
			return false;
		}			
	}

	//M�todo respons�vel por alterar a marca��o de um campo, define se estar� marcado ou n�o.
	public void alternarMarcacao() {
		//Verifica se o campo j� est� aberto, caso j� esteja, n�o poder� marca-lo.
		if(!aberto) {
			//Caso n�o esteja aberto, caso n�o esteja marcado, se torna marcado, caso marcado, se torna n�o marcado.
			marcado = !marcado;
			if(marcado) {
				//Caso a opera��o anterior tenha marcado o campo, notifica os observadores do campo que este campo foi marcado, ou deve ser marcado.
				notificarObservadores(CampoEvento.MARCAR);
			}else {
				//Caso a opera��o anterior tenha desmarcado o campo, notifica os observadores do campo que este campo foi desmarcado, ou deve ser desmarcado.
				notificarObservadores(CampoEvento.DESMARCAR);
			}
		}
		//N�o realiza nada caso campo esteja aberto
	}
	
	//Campo respons�vel por realizar a opera��o de abertura de um Campo
	public boolean abrir() {
		//Verifica se o campo j� n�o est� aberto e n�o est� marcado, caso j� esteja aberto ou marcado, n�o prossegue com m�todo
		if(!aberto && !marcado) {
			//Caso n�o esteja aberto e n�o esteja marcado, verifica se o campo em que est� tentando ser aberto � um campo minado.
			if(minado) {
				//Caso o campo seja um campo que est� minado, ou seja, possui uma mina, notifica a todos os observadores do Campo, que este campo explodiu, ou deve explodir
				notificarObservadores(CampoEvento.EXPLODIR);
				//Retorna resultado positivo para abertura de um campo e finaliza o m�todo, n�o prossegue
				return true;
			}
			//Caso campo n�o esteja minado, prossegue com m�todo, chamando m�todo para abertura do campo.
			setAberto(true);			
			//Verifica se a vizinha est� segura
			if(vizinhancaSegura()) {
				//Caso vizinha esteja segura, para cada vizinho dentro do Array de vizinhos, chama recursivamente o m�todo abrir();
				//Notar que est� sendo passado um Consumer<T>, recebe um parametro e n�o tem retorno.
				vizinhos.forEach(v -> v.abrir());
			}
			//Retorna resultado positivo para abertura de um campo
			return true;
		}else {
			//Retorna resultado negativo para abertura de um campo
			return false;			
		}
	}
	//M�todo respons�vel por verificar se algum vizinho dentre o array de vizinhos, est� minado.
	public boolean vizinhancaSegura() {
		//Chama um stream de vizinhos, e dentro desse stream, verifica se � verdade que nenhum ir� retornar verdade quando for perguntado se est� minado
		//True para que nenhum vizinho possui mina, false para que algum vizinho possui mina
		return vizinhos.stream().noneMatch(v->v.isMinado());
	}
	//Este m�todo verifica se algum objetivo foi alcan�ado, ou o campo n�o est� minado e foi aberto, ou se est� minado e foi marcado
	public boolean objetivoAlcancado() {
		//Verifica se o Campo n�o estava minado e foi aberto.
		 boolean desvendado = !minado && aberto;
		 //Verifica se o Campo estava minado e foi marcado
		 boolean protegido = minado && marcado;
		 //Retorna se o Campo foi desvendado ou foi protegido
		 return desvendado || protegido;
	 }
	//M�todo respons�vel por retornar a quantidade de minas em sua vizinhan�a
	public int minasNaVizinhanca() {
		//Chama um stream() de vizinhos, filtra somente os vizinhos que est�o minados e faz a contagem da quantidade de vizinhos minados
		 return (int) vizinhos.stream().filter(v -> v.isMinado()).count();
	 }
	 //M�todo respons�vel por reiniciar para o default todos os parametros do campo
	 public void reiniciar() {
		 //Seta campo para fechado
		 aberto = false;
		 //Seta campo para n�o minado
		 minado = false;
		 //Seta campo para n�o marcado
		 marcado = false;
		 //Notifica todos os observadores que este campo reiniciou, ou deve ser reiniciado.
		 notificarObservadores(CampoEvento.REINICIAR);
	 }
}


package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;
//Objeto de modelo para representar o Campo na lógica do jogo
public class Campo {
	//Atributo final(Não pode ser modificado uma vez que foi inicializado), privado(Acesso somente interno), inteiro(Tipo)
	private final int linha;
	//Atributo final(Não pode ser modificado uma vez que foi inicializado), privado(Acesso somente interno), inteiro(Tipo)
	private final int coluna;
	//Atributo privado(Não pode ser modificado uma vez que foi iniciailizado)
	private boolean aberto = false;
	//Atributo privado(Não pode ser modificado uma vez que foi iniciailizado)
	private boolean minado = false;
	//Atributo privado(Não pode ser modificado uma vez que foi iniciailizado)
	private boolean marcado = false;
	//ArrayList de Objetos do Tipo Campo, é possível criar Arrays de N's tipos devido ao uso do Generics
	//Esse array será responsável por guardar todos os vizinhos á este campo, validação de vizinho ou não deverá levar em consideração a posição no plano cartesiano do campo (linha,coluna,)(x,y).
	private List<Campo> vizinhos = new ArrayList<>();
	//Esse  Array será responsável por guardar todos os objetos que irão ficar monitorando os eventos realizados pelo Campo e no Campo.
	private List<CampoObservador> observadores = new ArrayList<>();
	
	//Construtor de Campo, recebe inteiro de linha e coluna
	public Campo(int linha, int coluna) {
		//Simples atribuição de valores
		this.linha = linha;
		//Simples atribuição de valores
		this.coluna = coluna;
	}
	//Retorna se o campo está aberto
	public boolean isAberto() {
		return aberto;
	}
	//Retorna se o campo está não aberto
	public boolean isFechado() {
		return !aberto;
	}
	//Torna um Campo que não estava aberto, aberto.
	public void setAberto(boolean aberto) {
		//Atribubição normal
		this.aberto = aberto;
		//Verifica se o campo está aberto
		if(aberto) {
			//Notifica a todos os observadores, que este campo foi aberto, ou que deve ser aberto.
			notificarObservadores(CampoEvento.ABRIR);
		}
	}
	//Retorna se o campo está minado ou não
	public boolean isMinado() {
		return minado;
	}
	//Define um campo como minado ou não
	public void setMinado(boolean minado) {
		this.minado = minado;
	}
	//Retorna se um campo está marcado ou não
	public boolean isMarcado() {
		return marcado;
	}
	//Define um campo como marcado ou não
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
	
	//Este método é responsável por registrar neste Campo, algum objeto que irá observar os eventos realizados pelo Campo e no Campo. 
	public void registrarObservador(CampoObservador observador) {
		//Método add é responsável por alocar mais um valor em um ArrayList, nesta linha um novo Objeto do tipo CampoObservador é adicionado no Array observadores
		observadores.add(observador);
	}
	
	//Este método é responsável por notificar todos os observadores registrados na variavel "observadores", sempre que um evento for realizado pelo Campo e no Campo.
	private void notificarObservadores(CampoEvento evento) {
		//Método .stream() retorna uma Stream em que é possível chamar o método foreach, para cada objeto encontrado dentro de observadores, é passado um lambda
		//neste lambda é recebido um CampoObservador e para cada CampoObservador na variavel observadores, ele notifica que um evento ocorreu passando o Campo em que ocorreu, e o evento que ocorreu.
		observadores.stream().forEach(o -> o.eventoOcorreu(this, evento));
	}
	
	//Um campo pode ter no máximo 8 vizinhos, visto que é possível ter 4 diretamente ligados em sua direção horizontal e vertical, e 4 em suas diagonais
	//Para isso é realizado o seguinte calculo, caso estejam na mesma linha ou na mesma coluna, a valor absoluto diferença entre os pontos sempre será 1 Absoluto((x1 - x2)+(y1-y2)).
	//Caso não estejam nem na mesma, nem mesma coluna, poderá estar na diagonal, para isso, a diferença absoluta entre os pontos sempre será 2 
	public boolean adicionarVizinho(Campo vizinho) {
		//Verifica se a coluna do vizinho recebido é diferente da coluna deste Campo
		boolean linhaDif = this.getColuna() != vizinho.getColuna();
		//Verifica se a coluna do vizinho recebido é diferente da coluna deste Campo
		boolean colunaDif = this.getLinha() != vizinho.getLinha();
		//Verifica a partir das demais conclusões, se está na diagonal ou não.
		boolean diagonal = linhaDif && colunaDif;
		//Realiza a diferença absoluta entre a coluna deste campo e a coluna do vizinho, Math.abs() retorna um número absoluto.
		int deltaColuna = Math.abs(this.getColuna() - vizinho.getColuna());
		//Realiza a diferença absoluta entrea a linha deste campo e a linha do vizinho.
		int deltaLinha = Math.abs(this.getLinha() - vizinho.getLinha());
		//Calculo geral, soma entre a diferença absoluta das linhas e colunas.
		int deltaGeral =  deltaColuna + deltaLinha;
		
		//Caso a diferença seja 1, e não esteja em uma diagonal, é um vizinho possível
		if(deltaGeral == 1 && !diagonal) {
			//Adiciona o objeto recebido ao Array de vizinhos
			vizinhos.add(vizinho);
			//Retorna resultado verdadeiro para adição de vizinho
			return true;
		//Caso não esteja na diagonal, ou a diferença não seja 1, verifica se a diferença é 2 e está na diagonal, caso sim, é um vizinho possível
		}else if(deltaGeral==2 && diagonal) {
			//Adiciona o objeto recebido ao Array de vizinhos
			vizinhos.add(vizinho);
			//Retorna resultado verdadeiro para adição de vizinho
			return true;
		//Caso nenhuma das clausulas acima seja atendida, não realiza a adição do vizinho
		}else {
			//Retorna resultado negativo para adição de vizinho
			return false;
		}			
	}

	//Método responsável por alterar a marcação de um campo, define se estará marcado ou não.
	public void alternarMarcacao() {
		//Verifica se o campo já está aberto, caso já esteja, não poderá marca-lo.
		if(!aberto) {
			//Caso não esteja aberto, caso não esteja marcado, se torna marcado, caso marcado, se torna não marcado.
			marcado = !marcado;
			if(marcado) {
				//Caso a operação anterior tenha marcado o campo, notifica os observadores do campo que este campo foi marcado, ou deve ser marcado.
				notificarObservadores(CampoEvento.MARCAR);
			}else {
				//Caso a operação anterior tenha desmarcado o campo, notifica os observadores do campo que este campo foi desmarcado, ou deve ser desmarcado.
				notificarObservadores(CampoEvento.DESMARCAR);
			}
		}
		//Não realiza nada caso campo esteja aberto
	}
	
	//Campo responsável por realizar a operação de abertura de um Campo
	public boolean abrir() {
		//Verifica se o campo já não está aberto e não está marcado, caso já esteja aberto ou marcado, não prossegue com método
		if(!aberto && !marcado) {
			//Caso não esteja aberto e não esteja marcado, verifica se o campo em que está tentando ser aberto é um campo minado.
			if(minado) {
				//Caso o campo seja um campo que está minado, ou seja, possui uma mina, notifica a todos os observadores do Campo, que este campo explodiu, ou deve explodir
				notificarObservadores(CampoEvento.EXPLODIR);
				//Retorna resultado positivo para abertura de um campo e finaliza o método, não prossegue
				return true;
			}
			//Caso campo não esteja minado, prossegue com método, chamando método para abertura do campo.
			setAberto(true);			
			//Verifica se a vizinha está segura
			if(vizinhancaSegura()) {
				//Caso vizinha esteja segura, para cada vizinho dentro do Array de vizinhos, chama recursivamente o método abrir();
				//Notar que está sendo passado um Consumer<T>, recebe um parametro e não tem retorno.
				vizinhos.forEach(v -> v.abrir());
			}
			//Retorna resultado positivo para abertura de um campo
			return true;
		}else {
			//Retorna resultado negativo para abertura de um campo
			return false;			
		}
	}
	//Método responsável por verificar se algum vizinho dentre o array de vizinhos, está minado.
	public boolean vizinhancaSegura() {
		//Chama um stream de vizinhos, e dentro desse stream, verifica se é verdade que nenhum irá retornar verdade quando for perguntado se está minado
		//True para que nenhum vizinho possui mina, false para que algum vizinho possui mina
		return vizinhos.stream().noneMatch(v->v.isMinado());
	}
	//Este método verifica se algum objetivo foi alcançado, ou o campo não está minado e foi aberto, ou se está minado e foi marcado
	public boolean objetivoAlcancado() {
		//Verifica se o Campo não estava minado e foi aberto.
		 boolean desvendado = !minado && aberto;
		 //Verifica se o Campo estava minado e foi marcado
		 boolean protegido = minado && marcado;
		 //Retorna se o Campo foi desvendado ou foi protegido
		 return desvendado || protegido;
	 }
	//Método responsável por retornar a quantidade de minas em sua vizinhança
	public int minasNaVizinhanca() {
		//Chama um stream() de vizinhos, filtra somente os vizinhos que estão minados e faz a contagem da quantidade de vizinhos minados
		 return (int) vizinhos.stream().filter(v -> v.isMinado()).count();
	 }
	 //Método responsável por reiniciar para o default todos os parametros do campo
	 public void reiniciar() {
		 //Seta campo para fechado
		 aberto = false;
		 //Seta campo para não minado
		 minado = false;
		 //Seta campo para não marcado
		 marcado = false;
		 //Notifica todos os observadores que este campo reiniciou, ou deve ser reiniciado.
		 notificarObservadores(CampoEvento.REINICIAR);
	 }
}


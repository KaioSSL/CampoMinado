package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
//Objeto tabuleiro respons�vel por representar o tabuleiro do jogo.
//Implements CampoObservador, ou seja, Tabuleiro � um observador que observa todos os eventos realizados ou que devem ser realizados no campo.
public class Tabuleiro implements CampoObservador {
	//Variavel privada, final, inteira, guarda a quantidade de linhas do tabuleiro
	private final int linhas;
	//Variavel privada, final, inteira, guarda a quantidade de colunas do tabuleiro
	private final int colunas;
	//Variavel privada, final, inteira, guarda a quantidade de minas do tabuleiro
	private final int minas;
	//Array privado, final, de todos os Campos que ir�o compor o tabuleiro.
	private final List<Campo> campos = new ArrayList<>();
	//Array privado de InterfacesFuncionais do tipo Consumer<ResultadoEvento>. que receber� todos os observadores de um tabuleiro, que ir� observar os eventos realizados ou que devem ser realizados em um tabuleiro
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();
	
	//Construtor de tabuleiro, recebendo a quantidade de linhas, colunas e minas
	public Tabuleiro(int linhas, int colunas, int minas) {
		//Atribui��o padr�o
		this.linhas = linhas;
		//Atribui��o padr�o
		this.colunas = colunas;
		//Atribui��o padr�o
		this.minas = minas;
		//No momento em que o tabuleiro for criado, � chamada a fun��o gerarCampos(), para que seja gerado todos os campos do tabuleiro
		gerarCampos();
		//Chamada fun��o para fazer associa��o de todos os campos que ser�o vizinhos um dos outros, (realiza a amarra de vizinhan�a para todos os campos).
		associarOsVizinhos();
		//Sorteia as minas de acordo com quantidade passada na cria��o.
		sortearMinas();
	}
	//Para cada campo o array de campos, realiza determinada funcao recebida como par�metro
	public void paraCadaCampo(Consumer<Campo> funcao) {
		//Para cada campo no array campos, realiza a funcao recebida como par�mtro
		campos.forEach(funcao);
	}
	//M�todo respons�vel por adicionar no array de observadores um observador que ir� observar eventos realizados pelo tabuleiro, ou que devem ser realizados no tabuleiro.
	public void registrarObservador(Consumer<ResultadoEvento> observador) {
		//Adiciona um observador ao array
		observadores.add(observador);
	}
	//M�todo respons�vel por notificar todos os observadores o evento que ocorreu em tabuleiro.
	private void notificarObservadores(Boolean resultado) {
		//Chama uma stream() do array de observadores, para cada observador passa um Consumer do tipo ResultadoEvento, com o resultado true or false
		observadores.stream().forEach(o->o.accept(new ResultadoEvento(resultado)));
	}
	//M�todo respons�vel por abrir determinado campo do tabuleiro, recebe posi��o da linha e posi��o da coluna (x,y)
	public void abrir(int linha, int coluna) {
		//Recebe um Stream de campos,
		campos.parallelStream()
		// filtra o campo dentre o array que possui a linha e coluna igual � recebida
		.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
		//Pega o primeiro registro encontrado 
		.findFirst()
		//Se existir algum registro, chama o m�todo abrir() interna ao Campo encontrado.
		.ifPresent(c -> c.abrir());
	}
	//M�todo respons�vel por alterar marca��o de determinar campo do tabuleiro, recebe posi��o da linha e posi��o da coluna (x,y)
	public void alternarMarcacao(int linha, int coluna) {
		//Recebe um Stream de campos
		campos.parallelStream()
		//filtra o campo dentre o array que possui a linha e coluna igual � recebida
		.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
		//pega o primeiro registro encontrado.
		.findFirst()
		//Se existir  algum registro, chama o m�todo alterarMarcacao() interna ao Campo encontrado.
		.ifPresent(c -> c.alternarMarcacao());
	}
	//M�todo respons�vel por gerar todos os campos do tabuleiro, de acordo com a quantidade de linhas e colunas (linhas*colunas)
	private void gerarCampos() {		
		//Primeiro for ir� percorrer de 0 ao total de linhas que ser�o poss�veis no Tabuleiro ( Eixo X).
		for(int linha = 0;linha < linhas; linha ++) {
			//Segundo for ir� percorrer de 0 ao total de colunas que ser�o poss�vels no Tabuleiro (Eixo Y).
			for(int coluna = 0; coluna< colunas; coluna++) {
				//Para cada posi��o (linha, coluna) (X,Y) dentro do Tabuleiro, um novo Campo ser� adicionado, passando como parametro para seu construtor sua posi��o dentro do tabuleiro.
				//Notar que a estrutura de campos � um array simples de apenas uma dimens�o, isso foi poss�vel pois a posi��o est� sendo tratada pelos atributos linha e coluna internos ao Campo.
				campos.add(new Campo(linha,coluna));
			}
		}
		//Para cada campo constru�do no tabuleiro, passa um Consumer, que ir� registrar no Campo este tabuleiro como Observador.
		campos.stream().forEach(c -> c.registrarObservador(this));
	}
	//M�todo respons�vel por realizar a associa��o de vizinhos para todos os campos.
	private void associarOsVizinhos() {
		//Ir� percorrer o array de campos duas vezes, chamando para o Campo do primeiro la�o, a tentativa de associa��o de vizinhan�a para todos os campos do array.
		for(Campo c1:campos) {
			//Para cada campo do segundo la�o, chama o m�todo adicionarVizinho(), para verificar se � um poss�vel vizinho e realizar a associa��o.
			for(Campo c2: campos) {
				//Chama o m�todo de associa��o de vizinhan�a.
				c1.adicionarVizinho(c2);
			}
		}
	}
	//M�todo respons�vel por sortear as minas dentre os Campos do tabuleiro, quantidade definida na constru��o do tabuleiro
	private void sortearMinas() {		
		//Contador de quantas minas j� foram armadas no tabuleiro
		long minasArmadas = 0;
		//Predicado que recebe um campo, e retorna se est� minado.
		Predicate<Campo> minado = c -> c.isMinado();
		//Inicio do la�o, neste la�o ser� realizada a arma��o de cada mina, utilizando uma posi��o aleat�ria.
		do {			
			//Math.radom() retorna um numero float entre 0 e 1. Multiplica-se pelo tamanho do array, para ter da posi��o 0 a posi��o m�xima do array de possibilidade de arma��o de mina
			int aleatorio = (int) (Math.random() * campos.size());
			//para o campo na posi��o sorteada, arma uma mina
			campos.get(aleatorio).minar();
			//verifica quantos campos est�o minados dentro do tabuleiro
			minasArmadas = campos.stream().filter(minado).count();
			//Enquanto a quantidade de Campos minados estiver menor que a quantidade de minas poss�veis, arma minas.
		}while(minasArmadas < minas);
	}
	//M�todo respons�vel por retornar se o objetivo do game foi alcan�ado.
	public boolean objetivoAlcando() {		
		//Chama um stream() do array de campos, e verifica se todos os campos est�o com o objetivo alcan�ado.
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	//M�todo respons�vel por reiniciar o tabuleiro.
	public void reinicializarJogo() {
		//Para cada campo do tabuleiro, executa o m�todo de reinicializa��o
		campos.stream().forEach(c -> c.reiniciar());
		//Realiza novamente o sorteio de minas do tabuleiro.
		sortearMinas();
	}
	//Retorna quantidade linhas do tabuleiro.
	public int getLinhas() {
		return linhas;
	}
	//Retorna quantidade de colunas do Tabuleiro.
	public int getColunas() {
		return colunas;
	}
	//Retorna quantidade de minas do tabuleiro
	public int getMinas() {
		return minas;
	}
	//Importante
	@Override
	//M�todo que recebe a notifica��o de que algum evento ocorreu em determinado campo, recebendo o campo e o evento ocorrido.
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		//Verifica se o evento ocorrido � de explos�o.
		if(evento == CampoEvento.EXPLODIR) {
			//Chama o campo mostrar minas, para mostrar todas as minas presentes no tabuleiro. 
			mostrarMinas();
			//Notifica os observadores resultado negativo(perdeu).
			notificarObservadores(false);
		//Caso objetivo tenha sido alcan�ado
		}else if(objetivoAlcando()) {
			//Notifica os observadores do tabuleiro resultado positivo(ganhou).
			notificarObservadores(true);
		}
		//Caso n�o tenha ganhado nem perdido, n�o faz nada.
	}
	//M�todo respons�vel por mostrar todos os campos que est�o minados dentro do tabuleiro.
	private void mostrarMinas() {
		//Percorre o array de campos
		campos.stream()
		//Pega somente os campos que est�o minados
		.filter(c -> c.isMinado())
		//Somente campos que est�o fechados
		.filter(c -> c.isFechado())
		//Abre o campo
		.forEach(c->c.setAberto(true));		
	}
	
}

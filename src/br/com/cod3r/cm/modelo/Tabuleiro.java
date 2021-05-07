package br.com.cod3r.cm.modelo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
//Objeto tabuleiro responsável por representar o tabuleiro do jogo.
//Implements CampoObservador, ou seja, Tabuleiro é um observador que observa todos os eventos realizados ou que devem ser realizados no campo.
public class Tabuleiro implements CampoObservador {
	//Variavel privada, final, inteira, guarda a quantidade de linhas do tabuleiro
	private final int linhas;
	//Variavel privada, final, inteira, guarda a quantidade de colunas do tabuleiro
	private final int colunas;
	//Variavel privada, final, inteira, guarda a quantidade de minas do tabuleiro
	private final int minas;
	//Array privado, final, de todos os Campos que irão compor o tabuleiro.
	private final List<Campo> campos = new ArrayList<>();
	//Array privado de InterfacesFuncionais do tipo Consumer<ResultadoEvento>. que receberá todos os observadores de um tabuleiro, que irá observar os eventos realizados ou que devem ser realizados em um tabuleiro
	private final List<Consumer<ResultadoEvento>> observadores = new ArrayList<>();
	
	//Construtor de tabuleiro, recebendo a quantidade de linhas, colunas e minas
	public Tabuleiro(int linhas, int colunas, int minas) {
		//Atribuição padrão
		this.linhas = linhas;
		//Atribuição padrão
		this.colunas = colunas;
		//Atribuição padrão
		this.minas = minas;
		//No momento em que o tabuleiro for criado, é chamada a função gerarCampos(), para que seja gerado todos os campos do tabuleiro
		gerarCampos();
		//Chamada função para fazer associação de todos os campos que serão vizinhos um dos outros, (realiza a amarra de vizinhança para todos os campos).
		associarOsVizinhos();
		//Sorteia as minas de acordo com quantidade passada na criação.
		sortearMinas();
	}
	//Para cada campo o array de campos, realiza determinada funcao recebida como parâmetro
	public void paraCadaCampo(Consumer<Campo> funcao) {
		//Para cada campo no array campos, realiza a funcao recebida como parâmtro
		campos.forEach(funcao);
	}
	//Método responsável por adicionar no array de observadores um observador que irá observar eventos realizados pelo tabuleiro, ou que devem ser realizados no tabuleiro.
	public void registrarObservador(Consumer<ResultadoEvento> observador) {
		//Adiciona um observador ao array
		observadores.add(observador);
	}
	//Método responsável por notificar todos os observadores o evento que ocorreu em tabuleiro.
	private void notificarObservadores(Boolean resultado) {
		//Chama uma stream() do array de observadores, para cada observador passa um Consumer do tipo ResultadoEvento, com o resultado true or false
		observadores.stream().forEach(o->o.accept(new ResultadoEvento(resultado)));
	}
	//Método responsável por abrir determinado campo do tabuleiro, recebe posição da linha e posição da coluna (x,y)
	public void abrir(int linha, int coluna) {
		//Recebe um Stream de campos,
		campos.parallelStream()
		// filtra o campo dentre o array que possui a linha e coluna igual á recebida
		.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
		//Pega o primeiro registro encontrado 
		.findFirst()
		//Se existir algum registro, chama o método abrir() interna ao Campo encontrado.
		.ifPresent(c -> c.abrir());
	}
	//Método responsável por alterar marcação de determinar campo do tabuleiro, recebe posição da linha e posição da coluna (x,y)
	public void alternarMarcacao(int linha, int coluna) {
		//Recebe um Stream de campos
		campos.parallelStream()
		//filtra o campo dentre o array que possui a linha e coluna igual á recebida
		.filter(c -> c.getLinha() == linha && c.getColuna() == coluna)
		//pega o primeiro registro encontrado.
		.findFirst()
		//Se existir  algum registro, chama o método alterarMarcacao() interna ao Campo encontrado.
		.ifPresent(c -> c.alternarMarcacao());
	}
	//Método responsável por gerar todos os campos do tabuleiro, de acordo com a quantidade de linhas e colunas (linhas*colunas)
	private void gerarCampos() {		
		//Primeiro for irá percorrer de 0 ao total de linhas que serão possíveis no Tabuleiro ( Eixo X).
		for(int linha = 0;linha < linhas; linha ++) {
			//Segundo for irá percorrer de 0 ao total de colunas que serão possívels no Tabuleiro (Eixo Y).
			for(int coluna = 0; coluna< colunas; coluna++) {
				//Para cada posição (linha, coluna) (X,Y) dentro do Tabuleiro, um novo Campo será adicionado, passando como parametro para seu construtor sua posição dentro do tabuleiro.
				//Notar que a estrutura de campos é um array simples de apenas uma dimensão, isso foi possível pois a posição está sendo tratada pelos atributos linha e coluna internos ao Campo.
				campos.add(new Campo(linha,coluna));
			}
		}
		//Para cada campo construído no tabuleiro, passa um Consumer, que irá registrar no Campo este tabuleiro como Observador.
		campos.stream().forEach(c -> c.registrarObservador(this));
	}
	//Método responsável por realizar a associação de vizinhos para todos os campos.
	private void associarOsVizinhos() {
		//Irá percorrer o array de campos duas vezes, chamando para o Campo do primeiro laço, a tentativa de associação de vizinhança para todos os campos do array.
		for(Campo c1:campos) {
			//Para cada campo do segundo laço, chama o método adicionarVizinho(), para verificar se é um possível vizinho e realizar a associação.
			for(Campo c2: campos) {
				//Chama o método de associação de vizinhança.
				c1.adicionarVizinho(c2);
			}
		}
	}
	//Método responsável por sortear as minas dentre os Campos do tabuleiro, quantidade definida na construção do tabuleiro
	private void sortearMinas() {		
		//Contador de quantas minas já foram armadas no tabuleiro
		long minasArmadas = 0;
		//Predicado que recebe um campo, e retorna se está minado.
		Predicate<Campo> minado = c -> c.isMinado();
		//Inicio do laço, neste laço será realizada a armação de cada mina, utilizando uma posição aleatória.
		do {			
			//Math.radom() retorna um numero float entre 0 e 1. Multiplica-se pelo tamanho do array, para ter da posição 0 a posição máxima do array de possibilidade de armação de mina
			int aleatorio = (int) (Math.random() * campos.size());
			//para o campo na posição sorteada, arma uma mina
			campos.get(aleatorio).minar();
			//verifica quantos campos estão minados dentro do tabuleiro
			minasArmadas = campos.stream().filter(minado).count();
			//Enquanto a quantidade de Campos minados estiver menor que a quantidade de minas possíveis, arma minas.
		}while(minasArmadas < minas);
	}
	//Método responsável por retornar se o objetivo do game foi alcançado.
	public boolean objetivoAlcando() {		
		//Chama um stream() do array de campos, e verifica se todos os campos estão com o objetivo alcançado.
		return campos.stream().allMatch(c -> c.objetivoAlcancado());
	}
	//Método responsável por reiniciar o tabuleiro.
	public void reinicializarJogo() {
		//Para cada campo do tabuleiro, executa o método de reinicialização
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
	//Método que recebe a notificação de que algum evento ocorreu em determinado campo, recebendo o campo e o evento ocorrido.
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		//Verifica se o evento ocorrido é de explosão.
		if(evento == CampoEvento.EXPLODIR) {
			//Chama o campo mostrar minas, para mostrar todas as minas presentes no tabuleiro. 
			mostrarMinas();
			//Notifica os observadores resultado negativo(perdeu).
			notificarObservadores(false);
		//Caso objetivo tenha sido alcançado
		}else if(objetivoAlcando()) {
			//Notifica os observadores do tabuleiro resultado positivo(ganhou).
			notificarObservadores(true);
		}
		//Caso não tenha ganhado nem perdido, não faz nada.
	}
	//Método responsável por mostrar todos os campos que estão minados dentro do tabuleiro.
	private void mostrarMinas() {
		//Percorre o array de campos
		campos.stream()
		//Pega somente os campos que estão minados
		.filter(c -> c.isMinado())
		//Somente campos que estão fechados
		.filter(c -> c.isFechado())
		//Abre o campo
		.forEach(c->c.setAberto(true));		
	}
	
}

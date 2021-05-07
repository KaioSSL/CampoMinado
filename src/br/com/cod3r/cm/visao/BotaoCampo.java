package br.com.cod3r.cm.visao;

import java.awt.Color;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;

import br.com.cod3r.cm.modelo.Campo;
import br.com.cod3r.cm.modelo.CampoEvento;
import br.com.cod3r.cm.modelo.CampoObservador;

@SuppressWarnings("serial")
//Classe visual respons�vel por representar o Bot�o do modelo visualmente.
//BotaoCampo extends JButton, BotaoCampo � um JButton
//BotaoCampo implementa CampoObservador, � um observador de Campo.
//BotaoCampo implementa MouseListener, � um observador dos eventos do Mouse(Sempre que ocorrer um evento do mouse, a fun��o respons�vel pelo evento ser� chamada)
public class BotaoCampo extends JButton implements CampoObservador,MouseListener{
	//Variavel privada, final  que define uma constante de uma cor padr�o
	private final Color BG_PADRAO = new Color(150,180,200); // RGB sendo passado para construtor do Color
	//Variavel privada, final  que define uma constante de uma cor padr�o
	private final Color BG_MARCAR = new Color(8,179,247);
	//Variavel privada, final  que define uma constante de uma cor padr�o
	private final Color BG_EXPLODIR = new Color(189,66,68);
	//Variavel privada, final  que define uma constante de uma cor padr�o
	private final Color TEXTO_VERDE = new Color(0,100,0);
	
	//Variavel que define um campo � qual esse modelo visual ser� respons�vel por monitorar.
	private Campo campo;
	
	//Construtor do modelo visual, recebe o campo � qual ser� respons�vel por representar visualmente e monitorar
	public BotaoCampo(Campo campo) {
		//Vincula o campo recebido � esse modelo.
		this.campo = campo;
		//define a cor desse CampoVisual, para a cor criada na variavel BG_PADRAO
		setBackground(BG_PADRAO);
		//Define como opaco a cor do CampoVisual
		setOpaque(true);
		//Define a borda do CampoVisual
		setBorder(BorderFactory.createBevelBorder(0));
		//IMPORTANTE !!!!!!!!!!
		//Adiciona entre os observadores do mouse, este bot�o visual, linha respons�vel por fazer est� classe ouvir e observar o Mouse
		addMouseListener(this);
		//IMPORTANTE
		//Vincula dentro do campo modelo, este CampoVisual como observador, respons�vel por fazer com que este CampoVisual receba todos os eventos ocorridos no Campo.
		campo.registrarObservador(this);
	}
	//M�todo que recebe os eventos ocorridos no Campo, recebendo o Campo em que ocorreu, e o evento.
	@Override
	public void eventoOcorreu(Campo campo, CampoEvento evento) {
		//Switch entre os eventos poss�veis cat�logados no enum CampoEvento.
		switch(evento) {
		//Caso evento de abertura de um campo, chama o m�todo respons�vel pela renderiza��o de um campo aberto.
		case ABRIR:
			//Chamada de renderiza��o de abertura de um campo.
			aplicarEstiloAbrir();
			//Break para que termine o switch
			break;
		//Caso evento de Marca��o de um campo, chama o m�todo respons�vel pela renderiza��o de um campo marcado
		case MARCAR:
			//Chamada de renderiza��o de marca��o de um campo.
			aplicarEstiloMarcar();
			//Break para que termine o switch
			break;
			//Caso evento de Explos�o de um campo, chama o m�todo respons�vel pela renderiza��o de um campo explodido.	
		case EXPLODIR:
			//Chamada de renderiza��o de explos�o de um campo.
			aplicarEstiloExplodir();
			//Break para que termine o switch
			break;
		default:
			//Evento default, n�o explodiu, n�o abriu, nem marcou. Campo retorna ao inicio do tabuleiro.
			aplicarEstiloEstiloPadrao();
			break;
		}
	}
	//M�todo respons�vel por renderizar um Campo padr�o(reinicializado)
	private void aplicarEstiloEstiloPadrao() {
		//Define a cor do background com a cor BG_PADRAO
		setBackground(BG_PADRAO);
		//Define a borda padr�o.
		setBorder(BorderFactory.createBevelBorder(0));
		//Limpa o texto do Campo.
		setText("");		
	}
	//M�todo respons�vel por renderizar um Campo explodido
	private void aplicarEstiloExplodir() {
		//Define a cor do background com a cor BG_EXPLODIR
		setBackground(BG_EXPLODIR);
		//Define a cor da fonte do Campo branca
		setForeground(Color.WHITE);
		//Atribui a letra X ao campo.
		setText("X");
	}
	//M�todo respons�vel por renderizar um Campo marcado
	private void aplicarEstiloMarcar() {
		//Define o background com a cor BG_MARCAR
		setBackground(BG_MARCAR);
		//Define a cor da fonte Preto
		setForeground(Color.BLACK);
		//Atribui a Letra M ao campo
		setText("M");		
	}
	//M�todo respons�vel por renderizar um Campo Aberto
	private void aplicarEstiloAbrir() {
		//seta a borda para padr�o campo aberto.
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		//Verifica se esta realizando a abertura de um Campo Minado.
		if(campo.isMinado()) {
			//Caso seja um campo minado, atribui seu background para padr�o explodido.
			setBackground(BG_EXPLODIR);
			//Finaliza m�todo e n�o processa mais nada
			return;
		}
		//Define background com a cor BG_PADRAO
		setBackground(BG_PADRAO);
		//Switch para verificar a quantidade de minas da vizinhanca e a cor do texto � ser aplicado.
		switch(campo.minasNaVizinhanca()){
		//Caso apenas 1 mina
		case 1:
			//Define cor verde para a fonte do Campo.
			setForeground(TEXTO_VERDE);
			//Finaliza o switch
			break;
		//Caso apenas 2
		case 2:
			//Define a cor azul para a fonte do Campo.
			setForeground(Color.BLUE);
			//Finaliza o switch
			break;
		//Caso apenas 3
		case 3:
			//Define a cor amarelo para a fonte do Campo.
			setForeground(Color.YELLOW);
			//Finaliza o switch
			break;
		//Caso 4, 5 ou 6
		case 4:
		case 5:
		case 6:
			//Define a cor vermelho para fonte do Campo.
			setForeground(Color.RED);
			//Finaliza o switch
			break;
		default:
			//caso nenhum dos casos acima, define rosa a cor da fonte do campo.
			setForeground(Color.PINK);
			break;
		}
		//Verifica se a vizinhanca � segura, caso n�o, retorna a quantidade de minas, caso seja, retorna texto vazio.
		String valor = !campo.vizinhancaSegura()? campo.minasNaVizinhanca() + "":"";
		//Atribui o texto com a quantidade de minas em volta do campo.
		setText(valor);
	} 
	
	//Interface dos eventos do mouse
	@Override
	//M�todo respons�vle por capturar sempre que um bot�o do mouse for pressionado.
	public void mousePressed(java.awt.event.MouseEvent e) {
		//Verifica se o bot�o pressionado � o bot�o esquerdo.
		if(e.getButton() == 1) {
			//Se for o bot�o esquerdo, tenta abrir o campo.
			campo.abrir();
		}else {
			//Se for bot�o direito, tenta marcar ou desmarcar o campo.
			campo.alternarMarcacao();
		}
	}
	//M�todos n�o implementados
	public void mouseClicked(java.awt.event.MouseEvent e) {		
	}
	public void mouseEntered(java.awt.event.MouseEvent e) {
	} 
	public void mouseExited(java.awt.event.MouseEvent e) {		
	}
	public void mouseReleased(java.awt.event.MouseEvent e) {		
	}
}

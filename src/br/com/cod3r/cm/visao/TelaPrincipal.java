package br.com.cod3r.cm.visao;

import javax.swing.JFrame;

import br.com.cod3r.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
//Herança de JFrame, TelaPrincipal é um JFrame
public class TelaPrincipal extends JFrame{
	public TelaPrincipal() {
		//Cria um tabuleiro, passando suas propriedades
		Tabuleiro tabuleiro = new Tabuleiro(16,10,25);
		//Adiciona no JFrame, um JPanel passando um Tabuleiro criado como parametro
		this.add(new PainelTabuleiro(tabuleiro));
		
		//Configurando o JFrame
		//Seta o título do JFrame para Campo Minado
		this.setTitle("Campo Minado");
		//Seta o Tamanho do JFrame para ser Inicialmente Largura 690 e Altura 438
		this.setSize(690,438);
		//Seta para que a abertura do JFrame seja relativa ao desktop da máquina
		this.setLocationRelativeTo(null);
		//Seta para que ao clicar no X de close do JFrame, a aplicação seja finalizada.
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		//Set para que o JFrame seja mostrado
		this.setVisible(true);
	}
	public static void main(String[] args) {
		//Cria uma nova Tela Principal
		new TelaPrincipal();
	}
}

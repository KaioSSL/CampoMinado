package br.com.cod3r.cm.visao;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.cod3r.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
//Heran�a de JPanel, PainelTabuleiro � um JPanel
public class PainelTabuleiro extends JPanel{
	public PainelTabuleiro(Tabuleiro tabuleiro) {
		//Cria um novo Layout, passando como parametro a quatidade de linhas e colunas do tabuleiro, sincronizando as quantidades.
		GridLayout linhasEColunas =new GridLayout(tabuleiro.getLinhas(),tabuleiro.getColunas());
		//Seta para que layout deste JPanel seja o linhasEColunas
		this.setLayout(linhasEColunas);
		//Para cada campo presente dentro do tabuleiro, cria um novo componente visual correspodente, e adiciona o novo componente visual ao JPanel.
		tabuleiro.paraCadaCampo(c -> this.add(new BotaoCampo(c)));
		//Registra no tabuleiro um observador, que ao receber um evento ocorrido ir� interpretar oque dever� ser feito.
		tabuleiro.registrarObservador(e -> {
			//Biblioteca e fun��o utilizada para que s� ap�s renderiza��o o bloco de c�digo seja executado.
			SwingUtilities.invokeLater(()->{
				//Verifica se o evento recebido � de vit�ria do player
				if(e.isGanhou()) {
					//Emite mensagem do resultado do Jogo
					JOptionPane.showMessageDialog(this, "Ganhou :)");
				}else {
					//Emite mensagem do resultado do Jogo
					JOptionPane.showMessageDialog(this, "Perdeu :(");
				}
				//Reinicializa o jogo, para que possa ser jogado novamente
				tabuleiro.reinicializarJogo();
			});
		});
	}
}

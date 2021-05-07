package br.com.cod3r.cm.visao;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import br.com.cod3r.cm.modelo.Tabuleiro;

@SuppressWarnings("serial")
//Herança de JPanel, PainelTabuleiro é um JPanel
public class PainelTabuleiro extends JPanel{
	public PainelTabuleiro(Tabuleiro tabuleiro) {
		//Cria um novo Layout, passando como parametro a quatidade de linhas e colunas do tabuleiro, sincronizando as quantidades.
		GridLayout linhasEColunas =new GridLayout(tabuleiro.getLinhas(),tabuleiro.getColunas());
		//Seta para que layout deste JPanel seja o linhasEColunas
		this.setLayout(linhasEColunas);
		//Para cada campo presente dentro do tabuleiro, cria um novo componente visual correspodente, e adiciona o novo componente visual ao JPanel.
		tabuleiro.paraCadaCampo(c -> this.add(new BotaoCampo(c)));
		//Registra no tabuleiro um observador, que ao receber um evento ocorrido irá interpretar oque deverá ser feito.
		tabuleiro.registrarObservador(e -> {
			//Biblioteca e função utilizada para que só após renderização o bloco de código seja executado.
			SwingUtilities.invokeLater(()->{
				//Verifica se o evento recebido é de vitória do player
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

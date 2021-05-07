package br.com.cod3r.cm.modelo;
@FunctionalInterface
//Interface funcional respons�vel por parametrizar qual m�todo deve ser implementado por todos os Objetos que forem observadores de Campo.
public interface CampoObservador {
	//M�todo abstrato que deve ser chamado sempre que determinado evento ocorreu � um campo ou por um campo, recebe o campo em que o evento ocorreu, e o evento ocorrido.
	public void eventoOcorreu(Campo campo, CampoEvento evento);
}

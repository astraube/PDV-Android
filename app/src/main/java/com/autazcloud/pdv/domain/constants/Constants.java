/**
 * 
 */
package com.autazcloud.pdv.domain.constants;


/**
 * Constantes globais da aplica��o
 * 
 * @author andre
 *
 */
public class Constants {

	public static final String BROADCAST_ACTION = "android.intent.action.BOOT_COMPLETED";


	/**
	 * C�digo tempor�rio
	 */
	public static final int CODE_OP = 1; // TODO C�digo do operador
	public static final int CODE_PDV = 1; // TODO c�digo do ponto de venda do estabelecimento
	
	/**
	 * <p>
	 * String Extras. Constantes para passar string de uma tela para outra
	 * </p>
	 * <b>Exemplo de uso:</b>
	 * 
	 * <pre>
	 * intent.putExtra(Constants.STRING_EXTRA_TITLE_VIEW, "texto");
	 * 
	 * getIntent().getStringExtra(Constants.STRING_EXTRA_TITLE_VIEW);
	 * </pre>
	 */
	public static final String STRING_EXTRA_TITLE_VIEW = "title_view";
	public static final String STRING_EXTRA_CANCEL_SALE = "cancel_sale";
	
	
	// Nomes de atributos de requisi��es ao servidor (POST, PUT, GET...)
	public static final String METHOD = "_METHOD";
	public static final String METHOD_API = "_METHOD"; // Padr�o exigido pela api REST atual 2.0.0

	// TIMERS
	public static final long INTERVAL_UPDATE_LOCATION = 1000*60*8; // Intervalo m�ximo para atualizar a localiza��o. Apenas com o APP aberto
	public static final long INTERVAL_FASTEST_UPDATE_LOCATION = 1000*30*3; // Intervalo mais r�pido para atualizar a localiza��o. Apenas com o APP aberto

	// ACCOUNT MANAGER
	public static final String ACCOUNT_TYPE = "br.com.i9algo.taxiadv";
	public static final String ACCOUNT_TOKEN_TYPE = "provider";

	public static final String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
	public static final String ARG_AUTH_TYPE = "AUTH_TYPE";
	public static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
}

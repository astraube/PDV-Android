/**
 * 
 */
package br.com.i9algo.autaz.pdv.domain.constants;


/**
 * Constantes globais da aplicacao
 * 
 * @author andre
 *
 */
public class Constants {

	public static final String BROADCAST_ACTION = "android.intent.action.BOOT_COMPLETED";

	public static final String TOKEN_MIX_PANEL = "a838086ce2ce696897debbe6ad08be64";
	public static final String GOOGLE_NOTIFICATION_SENDER_ID = "642970038240";


	/**
	 * Request Codes para serem utilizados em "startActivityForResult" e "onActivityResult"
	 */
	public static final int REQ_COD_PHOTO_WAS_PICKED = 222;
	public static final int REQ_COD_DISCOVERY_PRINTER = 333;


	/**
	 * Codigo temporario
	 */
	public static final int CODE_OP = 1; // TODO Codigo do operador
	public static final int CODE_PDV = 1; // TODO codigo do ponto de venda do estabelecimento
	
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
	public static final String EXTRA_TITLE_VIEW = "title_view";
	public static final String EXTRA_CANCEL_SALE = "cancel_sale";
	
	
	// Nomes de atributos de requisi��es ao servidor (POST, PUT, GET...)
	public static final String METHOD = "_METHOD";
	public static final String METHOD_API = "_METHOD"; // Padrao exigido pela api REST atual 2.0.0

	// TIMERS
	public static final long INTERVAL_UPDATE_LOCATION = 1000*60*8; // Intervalo maximo para atualizar a localizacao. Apenas com o APP aberto
	public static final long INTERVAL_FASTEST_UPDATE_LOCATION = 1000*30*3; // Intervalo mais rapido para atualizar a localizacao. Apenas com o APP aberto

	// ACCOUNT MANAGER
	public static final String ACCOUNT_TYPE = "br.com.i9algo.taxiadv";
	public static final String ACCOUNT_TOKEN_TYPE = "provider";

	public static final String ARG_ACCOUNT_TYPE = "ACCOUNT_TYPE";
	public static final String ARG_AUTH_TYPE = "AUTH_TYPE";
	public static final String ARG_ACCOUNT_NAME = "ACCOUNT_NAME";
}

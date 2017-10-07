package br.com.i9algo.autaz.pdv.data.local;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.pddstudio.preferences.encrypted.EncryptedPreferences;

import br.com.i9algo.autaz.pdv.R;
import br.com.i9algo.autaz.pdv.domain.constants.AuthAttr;


public class PreferencesRepository {

	private static final String PASS_ENCRYPT_PREFERENCE = "hd#@85s4d";

	private static Context _context;

	public static void init(Context context) {
		PreferencesRepository._context = context;
	}


	public static final boolean isValueEmpty(final String key) {
		EncryptedPreferences encryptedPreferences = new EncryptedPreferences.Builder(PreferencesRepository._context)
				.withEncryptionPassword(PASS_ENCRYPT_PREFERENCE)
				.build();

		String s = encryptedPreferences.getString(key, "");
		return ((s == null) ? true : s.isEmpty());

		/*SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		String s = mPrefs.getString(key, null);
		return ((s == null) ? true : s.isEmpty());*/

	}
    public static final String getValue(final String key) {
		EncryptedPreferences encryptedPreferences = new EncryptedPreferences.Builder(PreferencesRepository._context).withEncryptionPassword(PASS_ENCRYPT_PREFERENCE).build();
		return encryptedPreferences.getString(key, "");

		/*SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		String s = mPrefs.getString(key, null);
		return (s);*/

	}
	public static final void setValue(final String key, final String newValue) {
		EncryptedPreferences encryptedPreferences = new EncryptedPreferences.Builder(PreferencesRepository._context).withEncryptionPassword(PASS_ENCRYPT_PREFERENCE).build();
		encryptedPreferences.edit().putString(key, newValue).apply();

		/*SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putString(key, newValue);
		editor.commit();*/

	}


	public static final String getUsername() {
		EncryptedPreferences encryptedPreferences = new EncryptedPreferences.Builder(PreferencesRepository._context).withEncryptionPassword(PASS_ENCRYPT_PREFERENCE).build();
		return encryptedPreferences.getString(AuthAttr.USERNAME, "");
	}

	/*public static final boolean isValueEncryptedEmpty(final String key) {
		EncryptedPreferences encryptedPreferences = new EncryptedPreferences.Builder(PreferencesRepository._context).withEncryptionPassword(PASS_ENCRYPT_PREFERENCE).build();
		String s = encryptedPreferences.getString(key, "");
		return ((s == null) ? true : s.isEmpty());

	}
	public static final String getValueEncrypted(final String key) {
		EncryptedPreferences encryptedPreferences = new EncryptedPreferences.Builder(PreferencesRepository._context).withEncryptionPassword(PASS_ENCRYPT_PREFERENCE).build();
		return encryptedPreferences.getString(key, "");

	}
	public static final void setValueEncrypted(final String key, final String newValue) {
		EncryptedPreferences encryptedPreferences = new EncryptedPreferences.Builder(PreferencesRepository._context).withEncryptionPassword(PASS_ENCRYPT_PREFERENCE).build();
		encryptedPreferences.edit().putString(key, newValue).apply();

	}*/

	/**
	 * Para criar documento de SharedPreferences personalizado
	 */
	//final String PREFS_NAME = "MyPrefsFile";
	//SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	
	
	/**
	 * Numero de cupons que o cliente deseja imprimir
	 *
	 * @return
	 */
	public static final int getCuponsPrint() {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		String s = mPrefs.getString(PreferencesRepository._context.getString(R.string.pref_key_num_cupons_print), "1");
		return Integer.parseInt(s);
	}
	public static final void setCuponsPrint(final int newValue) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putInt(PreferencesRepository._context.getString(R.string.pref_key_num_cupons_print), newValue);
		editor.commit();
	}
	
	/**
	 * Quantidade de digitos da senha de pedido
	 * @return
	 */
	public static final int getLengthOrderCode() {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		String s = mPrefs.getString(PreferencesRepository._context.getString(R.string.pref_key_length_order_code), "3");
		return Integer.parseInt(s);
	}
	public static final void setLengthOrderCode(final int newValue) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putInt(PreferencesRepository._context.getString(R.string.pref_key_length_order_code), newValue);
		editor.commit();
	}
	
	/**
	 * Contagem sequencial de vendas efetuadas
	 * @return
	 */
	public static final int getCountSales() { return getCountSales(); }
	/**
	 * Contagem sequencial de vendas efetuadas
	 * @return
	 */
	public static final int getCountSales(boolean increment) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		int count = mPrefs.getInt(PreferencesRepository._context.getString(R.string.pref_key_num_count_sales), 1);
		if (increment) {
			count++;
			
			SharedPreferences.Editor editor = mPrefs.edit();
			editor.putInt(PreferencesRepository._context.getString(R.string.pref_key_num_count_sales), count);
			editor.commit();
		}
		return count;
	}
	
	/**
	 * Exibir produto coringa
	 * 
	 * true - Exibe um produto com o nome diversos.
	 * false - Nao exibe um produto com o nome diversos.
	 * @return
	 */
	public static final boolean isVisibleProductJoker() {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		return mPrefs.getBoolean(PreferencesRepository._context.getString(R.string.pref_key_product_joker), false);
	}
	public static final void setVisibleProductJoker(final Boolean newValue) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putBoolean(PreferencesRepository._context.getString(R.string.pref_key_product_joker), newValue);
		editor.commit();
	}
	
	
	/**
	 * Solicitar senha ao iniciar o programa
	 * 
	 * true = Solicitar Login ao iniciar app
	 * false = Nao Solicitar Login ao iniciar app
	 * @return
	 */
	public static final boolean isLoginRequired() {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		return mPrefs.getBoolean(PreferencesRepository._context.getString(R.string.pref_key_login_required_startup), false);
	}
	public static final void setLoginRequired(final Boolean newValue) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putBoolean(PreferencesRepository._context.getString(R.string.pref_key_login_required_startup), newValue);
		editor.commit();
	}
	
	/**
	 * Solicitar senha ao desligar a tela
	 * 
	 * true = Solicitar senha ao desligar a tela
	 * false = Nao Solicitar senha ao desligar a tela
	 * @return
	 */
	public static final boolean isPassRequiredInactivity() {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		return mPrefs.getBoolean(PreferencesRepository._context.getString(R.string.pref_key_pass_required_inactivity), false);
	}
	public static final void setPassRequiredInactivity(final Boolean newValue) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putBoolean(PreferencesRepository._context.getString(R.string.pref_key_pass_required_inactivity), newValue);
		editor.commit();
	}
	
	
	/**
	 * Exibir Dialog de informa��es da venda. Exibida ao clicar em "Nova Venda"
	 * 
	 * true = Exibir dialog
	 * false = Nao dialog
	 * @return
	 */
	public static final boolean isShowDialogNewSale() {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		return mPrefs.getBoolean(PreferencesRepository._context.getString(R.string.pref_key_display_dialog_new_sale), false);
	}
	public static final void setShowDialogNewSale(final Boolean newValue) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putBoolean(PreferencesRepository._context.getString(R.string.pref_key_display_dialog_new_sale), newValue);
		editor.commit();
	}
	
	/**
	 * Exibir dialog SaleCleanCloseDialog
	 * 
	 * true = Exibir dialog
	 * false = Nao dialog
	 * @return
	 */
	public static final boolean isShowDialogSaleCleanClose() {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		return mPrefs.getBoolean(PreferencesRepository._context.getString(R.string.pref_key_display_dialog_scc), false);
	}
	public static final void setShowDialogSaleCleanClose(final Boolean newValue) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putBoolean(PreferencesRepository._context.getString(R.string.pref_key_display_dialog_scc), newValue);
		editor.commit();
	}
	
	/**
	 * Layout do Sistema
	 * @return
	 */
	public static final int getLayout() {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		return mPrefs.getInt(PreferencesRepository._context.getString(R.string.pref_key_layout_selected_id), 0);
	}
	public static final void setLayout(final int newValue) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putInt(PreferencesRepository._context.getString(R.string.pref_key_layout_selected_id), newValue);
		editor.commit();
	}
	
	/**
	 * Bot�o do p�nico
	 * 
	 * true = Estado de socorro ligado. Bot�o socorro pressionado
	 * false = Estado de socorro desligado.
	 * @return
	 */
	public static final boolean isHelpButtonON() {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		return mPrefs.getBoolean(PreferencesRepository._context.getString(R.string.pref_key_help_button_on), false);
	}
	public static final void setHelpButton(final Boolean newValue) {
		SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(PreferencesRepository._context);
		SharedPreferences.Editor editor = mPrefs.edit();
		editor.putBoolean(PreferencesRepository._context.getString(R.string.pref_key_help_button_on), newValue);
		editor.commit();
	}
}

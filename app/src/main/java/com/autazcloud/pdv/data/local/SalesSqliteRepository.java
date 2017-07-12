package com.autazcloud.pdv.data.local;


public class SalesSqliteRepository {
	
	/*private static final String TAG = "SalesDBManager";
	
	// Verifica se uma determinada venda existe no SQLite
	public static boolean isSaleExisting(String id) {
		String selection = 	SalesTable.COL_CODE_BD + "= ?";
		String[] parametros = {	codeBD };
		String[] colunas = {SalesTable.COL_CODE_BD};
		Cursor cursor = SQLiteHelper.getDB().query(SalesTable.TABLE_NAME, colunas, selection, parametros, null, null,  null);
		boolean r = (cursor.getCount() > 0) ? true : false;
	}
	
	// Busca as vendas em aberto no SQLite
	public static List<SaleModel> getSalesByStatus(SaleStatusEnum status) {
		//Log.w(TAG, "getSalesByStatus()");

		String[] parametros = {	SaleStatusEnum.CANCELED.toString(), SaleStatusEnum.PAID.toString() };
		String sql = 
				"SELECT * FROM " + SalesTable.TABLE_NAME + 
				" WHERE " + SalesTable.COL_STATUS + " <> ?" + 
				" AND " + SalesTable.COL_STATUS + "<> ?" +
				" ORDER BY " + SalesTable.COL_ID + " DESC";
		Cursor cursor = SQLiteHelper.getDB().rawQuery(sql, parametros);
		
		List<SaleModel> saleList = null;
		
		if(cursor.getCount() > 0){
			saleList = new ArrayList<SaleModel>();
			cursor.moveToFirst();
			SaleModel sale;
			
			do{
				sale = SaleModel.newSale(cursor);

				//Log.w(TAG, sale.getName() + " - " + sale.getSaleCodeBD() + " - " + sale.getStatus());
				//Log.w(TAG, "Sale: " + sale.toString());
				saleList.add(sale);
				
			} while(cursor.moveToNext());
			
			//Log.w(TAG, saleList.size() + " vendas");
		}
		return (saleList);
	}
	
	// Busca as vendas com uma data inicial e final no SQLite
	public static List<SaleModel> getSalesDate(String initDate, String finalDate) {
		//Log.w(TAG, "getSalesAll( " + initDate + ", " + finalDate + ")");

		String sql =
				"SELECT * FROM " + SalesTable.TABLE_NAME + 
				" WHERE " + SalesTable.COL_DATE_CREATED + " BETWEEN Datetime('" + initDate + " 00:00:00')" + 
				" AND  Datetime('" + finalDate + " 23:00:59')" +
				" ORDER BY " + SalesTable.COL_DATE_CREATED + " ASC";
		
		String[] colunas = SalesTable.checkColumns();
		Cursor cursor = SQLiteHelper.getDB().rawQuery(sql, null);
		
		//Log.v(TAG, "getSalesDate - sql: " + sql);
		
		List<SaleModel> saleList = new ArrayList<SaleModel>();
		
		if(cursor.getCount() > 0){
			cursor.moveToFirst();
			SaleModel sale;
			
			do{
				sale = SaleModel.newSale(cursor);
				saleList.add(sale);
				//Log.w(TAG, "LOG---> " + sale.getClientName() + " - " + DateFormats.formatDate(sale.getDate(), DateFormats.getDateTimeGlobal()) + " - " + sale.getStatus());
				
			} while(cursor.moveToNext());
			//Log.w(TAG, saleList.size() + " vendas");
		}
		return (saleList);
	}
	
	// Insere OU Atualiza uma venda
	public static synchronized void syncSale(final SaleModel sale) {
		try {
			if (isSaleExisting(sale.getId())) {
				updateSale(sale);
				Log.w(TAG, "Atualizar: " + sale.getClientName());
			} else {
				addSale(sale);
				Log.w(TAG, "Adicionar: " + sale.getClientName());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Insere OU Atualiza varias vendas
	public static synchronized void syncSaleList(final List<SaleModel> saleList) {
		for (SaleModel s : saleList) {
			try {
				if (isSaleExisting(s.getId())) {
					updateSale(s);
					Log.w(TAG, "Atualizar: " + s.getClientName());
				} else {
					addSale(s);
					Log.w(TAG, "Adicionar: " + s.getClientName());
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// Apenas Adiciona uma venda no SQLite
	public static synchronized void addSale(SaleModel sale) {
		ContentValues valores = SalesTable.createContentValues(sale);
		SQLiteHelper.getDB().insertOrThrow(SalesTable.TABLE_NAME, null, valores);
	}

	// Apenas Adiciona varias vendas no SQLite
	public static synchronized void addSaleList(final List<SaleModel> saleList) {
		for (SaleModel m : saleList) {
			try {
				addSale(m);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}*/
}

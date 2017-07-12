package com.autazcloud.pdv.data.local;

public class ProductSqliteRepository {
	
	/*private static final String TAG = ProductSqliteManager.class.getSimpleName();
	
	public static synchronized boolean isProductExisting(String id) {
		String selection = 	ProductsTable.COL_ID + "= ?";
		String[] parametros = {	productID };
		String[] colunas = {ProductsTable.COL_ID};
		Cursor cursor = SQLiteHelper.getDB().query(ProductsTable.TABLE_NAME, colunas, selection, parametros, null, null,  null);
		boolean r = (cursor.getCount() > 0) ? true : false;
	}
	
	public static Product getProductById(String productID) {
		String selection = 	ProductsTable.COL_ID + "= ?";
		String[] parametros = {	productID };
		String[] colunas = ProductsTable.checkColumns();
		Cursor cursor = SQLiteHelper.getDB().query(ProductsTable.TABLE_NAME, colunas, selection, parametros, null, null,  null);
		Product model = null;
		
		if(cursor.getCount() > 0) {
			cursor.moveToFirst();
			model = Product.newProduct(cursor);
			
			model.setStock(10); // TODO ainda nao est� trabalhando com estoque
		}
		return model;
	}
	
	public static List<Product> getProductsAll() {
        String[] colunas = ProductsTable.checkColumns();
		Cursor cursor = SQLiteHelper.getDB().query(ProductsTable.TABLE_NAME, colunas, null, null, null, null,  ProductsTable.COL_NAME + " ASC");

		List<Product> list = new ArrayList<Product>();

		if(cursor.getCount() > 0) {
			cursor.moveToFirst();
			Product model;

			do{
				model = Product.newProduct(cursor);

				//Log.w(TAG, model.getName());

				list.add(model);

				model.setStock(10); // TODO ainda nao est� trabalhando com estoque

			} while(cursor.moveToNext());
		}

		return (list);
	}
	
	public static synchronized void syncItem(final Product product) {
		try {
			//Log.w(TAG, "SQL - syncProduct: " + product.getPublicToken()());
			//Log.w(TAG, "SQL - syncProduct: " + product.getName());

			if (isProductExisting(product.getId())) {
				updateProduct(product);
			} else {
				addProduct(product);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static synchronized void syncItems(final List<Product> productList) {
		//Log.w(TAG, "List<Product>: " + productList.size());

        for (Product s : productList) {
			try {
				//Log.w(TAG, "SQL - syncItems: " + s.getId()());
				//Log.w(TAG, "SQL - syncItems: " + s.getName());
				//Log.w(TAG, "SQL - syncItems: " + s.getRequestPass());

				if (isProductExisting(s.getId()())) {
					updateProduct(s);
				} else {
					addProduct(s);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public static synchronized void addProduct(Product product) {
		//Log.w(TAG, "SQL - Adicionar Produto");
		ContentValues valores = ProductsTable.createContentValues((Product) product);
		SQLiteHelper.getDB().insertOrThrow(ProductsTable.TABLE_NAME, null, valores);
	}
	
	public static synchronized void addProductList(final List<Product> productList) {
		try {
			for (Product m : productList) {
				addProduct(m);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static synchronized void removeProduct(Product product) {
		//Log.w(TAG, "SQL - Remover Produto");

		String selection = 	ProductsTable.COL_ID + "= ?";
		String[] parametros = {	product.getId() };
		ContentValues valores = ProductsTable.createContentValues((Product) product);
		SQLiteHelper.getDB().delete(ProductsTable.TABLE_NAME, selection, parametros);
	}*/
}

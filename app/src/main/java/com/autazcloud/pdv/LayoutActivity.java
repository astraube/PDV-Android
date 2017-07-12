package com.autazcloud.pdv;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import com.autazcloud.pdv.ui.base.BaseActivity;
import com.autazcloud.pdv.domain.interfaces.ItemAdapterInterface;
import com.autazcloud.pdv.domain.models.LayoutModel;
import com.autazcloud.pdv.executor.adapters.LayoutsGridAdapter;
import com.autazcloud.pdv.data.local.PreferencesRepository;

import java.util.ArrayList;
import java.util.List;

public class LayoutActivity extends BaseActivity implements ItemAdapterInterface {
	
	private int mLayoutSelected = 0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_layout);
		
		mLayoutSelected = PreferencesRepository.getLayout();
		
		List<LayoutModel> list = new ArrayList<LayoutModel>();
		
		LayoutModel model1 = new LayoutModel();
		model1.setId(0);
		model1.setName("Várias Vendas");
		model1.setDescription("Ideal para estabelecimentos que efetuam várias vendas ao mesmo tempo, no mesmo ponto. \nEX: Restaurantes, cada venda � uma mesa.");
		model1.setImage(R.drawable.pdv_screen_1);
		list.add(model1);
		
		LayoutModel model2 = new LayoutModel();
		model2.setId(1);
		model2.setName("Apenas uma Venda");
		model2.setDescription("Ideal para estabelecimentos que efetuam apenas uma venda por ponto. \nEX: Cantinas.");
		model2.setImage(R.drawable.pdv_screen_2);
		list.add(model2);
		
		GridView gridView = (GridView) findViewById(R.id.gridView);
		gridView.setAdapter(new LayoutsGridAdapter(this, list, this));
		//gridView.setSelection(mLayoutSelected);
		//gridView.setItemChecked(mLayoutSelected, true);
	}

	@Override
	public void onItemClick(Object item, int position) {
		//PreferencesUtil.setLayout(this, position);
		PreferencesRepository.setLayout(0);
		
		Intent i = new Intent(getBaseContext(), MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
	}
}

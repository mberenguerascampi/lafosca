package com.example.lafoscaplaya;


import android.os.Bundle;
import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class FindChildrenActivity extends Activity {
	private EditText filterText = null;
	ArrayAdapter<String> adapter = null;
	private ListView list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_find_children);
		
		filterText = (EditText) findViewById(R.id.search_editText);
	    filterText.addTextChangedListener(filterTextWatcher);
	    
	    list = (ListView)findViewById(R.id.list);
	    
	    adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,getStringArrayList());
	    list.setAdapter(adapter);
	}
	
	private String[] getStringArrayList() {
		//Cogemos los niños que nos ha pasado la otra actividad
		Bundle b = getIntent().getExtras();
		return b.getStringArray("kids");
		
	}

	private TextWatcher filterTextWatcher = new TextWatcher() {
	    public void beforeTextChanged(CharSequence s, int start, int count,
	            int after) {
	    }

	    public void onTextChanged(CharSequence s, int start, int before,
	            int count) {
	        adapter.getFilter().filter(s);
	    }

		@Override
		public void afterTextChanged(Editable s) {
			
		}

	};

	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    filterText.removeTextChangedListener(filterTextWatcher);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.find_children, menu);
		return true;
	}

}

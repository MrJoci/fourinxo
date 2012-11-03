package jocit.software;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class FourInXOActivity extends ListActivity {
	String menuRows[] = {"StartNewGame", "StartNewFiveInRow"};
	String menuRowNames[] = {"Start new four in row", "Start new five In row"};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, menuRowNames));
	}
	
	@Override
	protected void onListItemClick(ListView list, View view, int position,	long id) {
		super.onListItemClick(list, view, position, id);
		String menuRow = menuRows[position];
		try {
			Class clazz = Class.forName("jocit.software." + menuRow);
			Intent intent = new Intent(this, clazz);
			startActivity(intent);
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
}
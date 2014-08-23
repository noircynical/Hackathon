package gdg.sshhackathon.scv;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout;

public class SCVActivity extends Activity {
	
	private ViewGroup mViewGroup;
	private TextView empty_text;

	private int itemCount= 0;
	
	private ArrayList<RelativeLayout> items= new ArrayList<RelativeLayout>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scv);
		
		createUI();
	}
	
	private void createUI(){
		mViewGroup= (ViewGroup)findViewById(R.id.linear);
		empty_text= (TextView)findViewById(R.id.empty);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_bar, menu);
        return true;
    }
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_item:
                empty_text.setVisibility(View.GONE);
                if(itemCount < 5)
                	addItem();
                else
                	Toast.makeText(this, getResources().getString(R.string.too_many_items), Toast.LENGTH_SHORT).show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
	
	private void addItem() {
        final ViewGroup newView = (ViewGroup) LayoutInflater.from(this).inflate(
                R.layout.list_item, mViewGroup, false);
        
        System.out.println("view id: "+Integer.toString(newView.getId()));

        ((TextView) newView.findViewById(android.R.id.text1)).setText(COUNTRIES[(int) (Math.random() * COUNTRIES.length)]);
        newView.findViewById(R.id.touch).setOnClickListener(click);
        newView.findViewById(R.id.delete_button).setOnClickListener(click);
//        newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//            	
//            }
//        });

        mViewGroup.addView(newView, 0);
        
        itemCount++;
    }
	
	View.OnClickListener click= new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch(v.getId()){
			case R.id.delete_button:
                mViewGroup.removeView(v);
                if (mViewGroup.getChildCount() == 0)
                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
				break;
			case R.id.touch:
				Toast.makeText(SCVActivity.this.getApplicationContext(), ((TextView)v.findViewById(android.R.id.text1)).getText(), Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

    /**
     * A static list of country names.
     */
    private static final String[] COUNTRIES = new String[]{
            "Belgium", "France", "Italy", "Germany", "Spain",
            "Austria", "Russia", "Poland", "Croatia", "Greece",
            "Ukraine",
    };
}

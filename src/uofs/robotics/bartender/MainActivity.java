package uofs.robotics.bartender;


import uofs.robotics.bartender.fragments.BeverageListFragment;
import uofs.robotics.bartender.fragments.BottleListFragment;
import uofs.robotics.bartender.fragments.DrinkListFragment;
import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.ArrayAdapter;

public class MainActivity extends FragmentActivity implements ActionBar.OnNavigationListener {

    /**
     * The serialization (saved instance state) Bundle key representing the
     * current dropdown position.
     */
    private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar to show a dropdown list.
        final ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);

        // Set up the dropdown list navigation in the action bar.
        actionBar.setListNavigationCallbacks(
                // Specify a SpinnerAdapter to populate the dropdown list.
                new ArrayAdapter<String>(
                        actionBar.getThemedContext(),
                        android.R.layout.simple_list_item_1,
                        android.R.id.text1,
                        new String[] {
                                getString(R.string.title_drinks),
                                getString(R.string.title_bottles),
                                getString(R.string.title_beverages),
                        }),
                this);
    }
    
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Restore the previously serialized current dropdown position.
        if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
            getActionBar().setSelectedNavigationItem(
                    savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        // Serialize the current dropdown position.
        outState.putInt(STATE_SELECTED_NAVIGATION_ITEM,
                getActionBar().getSelectedNavigationIndex());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public boolean onNavigationItemSelected(int position, long id) {
        // When the given dropdown item is selected, show its contents in the
        // container view.
    	
    	if (position == 0)
    	{
            Fragment fragment = new DrinkListFragment();
            
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, fragment)
                    .commit();
            
            return true;
    	}
    	else if (position == 1)
    	{
    		Fragment fragment = new BottleListFragment();
    		
    		getSupportFragmentManager().beginTransaction()
        		.replace(R.id.container, fragment)
        		.commit();    		

    		return true;
    	}
    	else if (position == 2)
    	{
    		Fragment fragment = new BeverageListFragment();
           
    		getSupportFragmentManager().beginTransaction()
            	.replace(R.id.container, fragment)
            	.commit();
    		
    		return true;
    	}
    	else
    	{
    		return false;
    	}
    }
}

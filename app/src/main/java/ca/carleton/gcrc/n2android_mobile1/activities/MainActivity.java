package ca.carleton.gcrc.n2android_mobile1.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

import ca.carleton.gcrc.n2android_mobile1.ConnectionInfo;
import ca.carleton.gcrc.n2android_mobile1.CouchDbService;
import ca.carleton.gcrc.n2android_mobile1.NunaliitMobileConstants;
import ca.carleton.gcrc.n2android_mobile1.R;
import ca.carleton.gcrc.n2android_mobile1.activities.ConnectionListActivity;
import ca.carleton.gcrc.n2android_mobile1.activities.EmbeddedCordovaActivity;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainActivity extends ServiceBasedActivity {

    protected String TAG = this.getClass().getSimpleName();

    private List<ConnectionInfo> displayedConnections = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start CouchDb service (and keep it up indefinitely)
        Intent intent = new Intent(this, CouchDbService.class);
        startService(intent);

        setContentView(R.layout.activity_main);

        ListView lv = (ListView)findViewById(R.id.connnections);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ConnectionInfo selectedConnection = null;
                if (null != displayedConnections && position < displayedConnections.size()) {
                    selectedConnection = displayedConnections.get(position);
                }

                if (null != selectedConnection) {
                    startConnectionActivity(selectedConnection);
                }
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_manage_connections:
                startConnectionListActivity(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        drawList();
    }

    @Override
    public void serviceReporting(CouchDbService service) {
        drawList();
    }

    public void drawList() {
        try {
            CouchDbService service = getService();
            if( null != service ){
                List<ConnectionInfo> connectionInfos = service.getConnections();
                ListView listView = (ListView)findViewById(R.id.connnections);

                String[] stringArray = new String[connectionInfos.size()];
                for(int i=0,e=connectionInfos.size(); i<e; ++i){
                    ConnectionInfo connectionInfo = connectionInfos.get(i);
                    stringArray[i] = connectionInfo.toString();
                }

                ArrayAdapter<String> modeAdapter = new ArrayAdapter<String>(this, R.layout.list_connection_item, stringArray);
                listView.setAdapter(modeAdapter);

                displayedConnections = connectionInfos;
            }

        } catch(Exception e) {
            Log.e(TAG, "Error obtaining connection list", e);
        }
    }

    public void startConnectionActivity(ConnectionInfo connInfo){
        Intent intent = new Intent(this, EmbeddedCordovaActivity.class);

        intent.putExtra(NunaliitMobileConstants.EXTRA_CONNECTION_ID, connInfo.getId());

        startActivity(intent);
    }

    public void startConnectionListActivity(View view){
        Intent intent = new Intent(this, ConnectionListActivity.class);
        startActivity(intent);
    }
}

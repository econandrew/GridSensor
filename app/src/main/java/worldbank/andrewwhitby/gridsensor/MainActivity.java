package worldbank.andrewwhitby.gridsensor;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.util.Log;


public class MainActivity extends ActionBarActivity {
    private GridSensorApplication app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Main", "onCreate");

        app = (GridSensorApplication) this.getApplication();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app.statusImage = (ImageView) findViewById(R.id.status_image);
        ListView logHistoryList = (ListView) findViewById(R.id.log_history);
        logHistoryList.setAdapter(app.logHistoryAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        ((GridSensorApplication) super.getApplication()).updateDisplay();
        app.statusImage.post(new Runnable() {

            @Override
            public void run() {
                RelativeLayout.LayoutParams mParams;
                mParams = (RelativeLayout.LayoutParams) app.statusImage.getLayoutParams();
                mParams.height = app.statusImage.getWidth() < app.statusImage.getHeight() ? app.statusImage.getWidth() : app.statusImage.getHeight();
                app.statusImage.setLayoutParams(mParams);
                app.statusImage.postInvalidate();
            }
        });    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}



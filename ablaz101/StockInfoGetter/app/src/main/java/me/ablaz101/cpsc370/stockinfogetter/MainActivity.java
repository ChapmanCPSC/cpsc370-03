package me.ablaz101.cpsc370.stockinfogetter;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity
{

    EditText companyField;
    ListView companyList;
    TextView hiView;
    TextView lowView;
    TextView openView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        companyField = (EditText) findViewById(R.id.et_company_name);
        hiView = (TextView) findViewById(R.id.tv_high);
        lowView = (TextView) findViewById(R.id.tv_low);
        openView = (TextView) findViewById(R.id.tv_open);

        companyList = (ListView) findViewById(R.id.lv_lookups);
        companyList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_symbol = (TextView) view.findViewById(R.id.tv_symbol);
                String symbol = tv_symbol.getText().toString();


                // TODO background task
                new QuoteBackgroundTask(hiView, lowView, openView).execute(symbol);
            }
        });

        Button submitButton = (Button) findViewById(R.id.btn_submit);
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String companyName = companyField.getText().toString();
                if (!companyName.isEmpty()) {
                    // TODO background task
                    new LookupBackgroundTask(v.getContext(), companyList).execute(companyName);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

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

import me.ablaz101.cpsc370.stockinfogetter.db.StockInfoDataProvider;
import me.ablaz101.cpsc370.stockinfogetter.models.QuoteModel;
import me.ablaz101.cpsc370.stockinfogetter.tasks.LookupBackgroundTask;
import me.ablaz101.cpsc370.stockinfogetter.tasks.QuoteBackgroundTask;


public class MainActivity extends ActionBarActivity
{

    EditText companyField;
    ListView companyList;
    ListView preferredStocksList;
    TextView hiView;
    TextView lowView;
    TextView openView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get fields
        companyField = (EditText) findViewById(R.id.et_company_name);
        hiView = (TextView) findViewById(R.id.tv_high);
        lowView = (TextView) findViewById(R.id.tv_low);
        openView = (TextView) findViewById(R.id.tv_open);

        // Get lists
        companyList = (ListView) findViewById(R.id.lv_lookups);
        preferredStocksList = (ListView) findViewById(R.id.lv_preferred_stocks);

        // Set onItemClickListener for companyList (API Call for Quote)
        companyList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tv_symbol = (TextView) view.findViewById(R.id.tv_symbol);
                String symbol = tv_symbol.getText().toString();
                String[] split = symbol.split(",");
                symbol = split[0];
                final QuoteBackgroundTask qbt = new QuoteBackgroundTask(getApplicationContext());
                qbt.onFinish = new Runnable()
                {
                    @Override
                    public void run() {
                        hiView.setText(String.valueOf(qbt._result.High));
                        lowView.setText(String.valueOf(qbt._result.Low));
                        openView.setText(String.valueOf(qbt._result.Open));
                    }
                };
                qbt.execute(symbol);
            }
        });

        // Get submit button
        Button submitButton = (Button) findViewById(R.id.btn_submit);

        // Set onClickListner for submit button (API Call for Lookup List)
        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                String companyName = companyField.getText().toString();
                if (!companyName.isEmpty()) {
                    new LookupBackgroundTask(v.getContext(), companyList, preferredStocksList).execute(companyName);
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

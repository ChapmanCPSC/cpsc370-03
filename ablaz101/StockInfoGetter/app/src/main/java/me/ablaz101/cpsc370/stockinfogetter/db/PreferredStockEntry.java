package me.ablaz101.cpsc370.stockinfogetter.db;

import android.content.ContentValues;
import android.database.Cursor;

import me.ablaz101.cpsc370.stockinfogetter.models.LookupResultModel;
import me.ablaz101.cpsc370.stockinfogetter.models.PreferredStockModel;
import me.ablaz101.cpsc370.stockinfogetter.models.QuoteModel;

/**
 * Created by Xavi on 4/10/15.
 */
public class PreferredStockEntry
{
    public static final String TABLE_NAME = "preferred_stocks";

    public static final String COLUMN_SYMBOL = "symbol";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EXCHANGE = "stock_exchange";

    public static final String COLUMN_HIGH = "high";
    public static final String COLUMN_LOW = "low";
    public static final String COLUMN_OPEN = "open";

    public static final String CREATE_COMMAND = "CREATE TABLE "
            + TABLE_NAME
            + "("
            + COLUMN_SYMBOL
            + " TEXT PRIMARY KEY,"
            + COLUMN_NAME
            + " TEXT NOT NULL,"
            + COLUMN_EXCHANGE
            + " TEXT NOT NULL,"
            + COLUMN_HIGH
            + " DECIMAL(4,2) NOT NULL,"
            + COLUMN_LOW
            + " DECIMAL(4,2) NOT NULL,"
            + COLUMN_OPEN
            + " DECIMAL(4,2) NOT NULL);";

    public static ContentValues MakeContentValues(PreferredStockModel model)
    {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_SYMBOL, model.getLookupResultModel().symbol);
        cv.put(COLUMN_NAME, model.getLookupResultModel().name);
        cv.put(COLUMN_EXCHANGE, model.getLookupResultModel().exchange);
        cv.put(COLUMN_HIGH, model.getQuoteModel().High);
        cv.put(COLUMN_LOW, model.getQuoteModel().Low);
        cv.put(COLUMN_OPEN, model.getQuoteModel().Open);

        return cv;
    }

    public static PreferredStockModel MakeModel(Cursor c)
    {
        String symbol = c.getString(c.getColumnIndex(COLUMN_SYMBOL));
        String name = c.getString(c.getColumnIndex(COLUMN_NAME));
        String exchange = c.getString(c.getColumnIndex(COLUMN_EXCHANGE));
        double high = c.getDouble(c.getColumnIndex(COLUMN_HIGH));
        double low = c.getDouble(c.getColumnIndex(COLUMN_LOW));
        double open = c.getDouble(c.getColumnIndex(COLUMN_OPEN));

        LookupResultModel lookupResultModel = new LookupResultModel(symbol, name, exchange);
        QuoteModel quoteModel = new QuoteModel(high, low, open);

        return new PreferredStockModel(lookupResultModel, quoteModel);
    }
}

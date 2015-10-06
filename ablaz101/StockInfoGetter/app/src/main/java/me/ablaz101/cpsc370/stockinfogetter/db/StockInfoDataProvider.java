package me.ablaz101.cpsc370.stockinfogetter.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import me.ablaz101.cpsc370.stockinfogetter.models.PreferredStockModel;
import me.ablaz101.cpsc370.stockinfogetter.models.QuoteModel;

/**
 * Created by Xavi on 5/10/15.
 */
public class StockInfoDataProvider
{
    private static SQLiteDatabase GetDB(boolean writable, Context ctx)
    {
        StockInfoDatabase helper = new StockInfoDatabase(ctx);
        if (writable)
        {
            return helper.getWritableDatabase();
        }
        else
        {
            return helper.getReadableDatabase();
        }
    }

    public static void InsertPreferredStockEntry(PreferredStockModel model, Context ctx)
    {
        SQLiteDatabase db = GetDB(true, ctx);

        // Check if item exists
        String selection = PreferredStockEntry.COLUMN_SYMBOL+"=?";
        String[] selectionArgs = new String[]{model.getLookupResultModel().symbol};

        Cursor c = db.query(PreferredStockEntry.TABLE_NAME, null, selection, selectionArgs, null, null, null);

        ContentValues cv = PreferredStockEntry.MakeContentValues(model);
        db.insert(PreferredStockEntry.TABLE_NAME, null, cv);

        c.close();
        db.close();
    }

    public static QuoteModel GetStockQuote(Context ctx, String symbol)
    {
        SQLiteDatabase db = GetDB(false, ctx);
//        String[] columns = new String[]{PreferredStockEntry.COLUMN_HIGH, PreferredStockEntry.COLUMN_LOW, PreferredStockEntry.COLUMN_OPEN};
        Cursor c = db.query(PreferredStockEntry.TABLE_NAME, /*columns*/null, null/*PreferredStockEntry.COLUMN_SYMBOL + "=?"*/,null /*new String[]{symbol}*/, null, null, null);

        c.moveToFirst();
        PreferredStockModel stock = PreferredStockEntry.MakeModel(c);
        c.close();
        db.close();
        return stock.getQuoteModel();
    }

    public static List<PreferredStockModel> GetPreferredStocks(Context ctx)
    {
        SQLiteDatabase db = GetDB(false, ctx);

        List<PreferredStockModel> preferredStocks = new ArrayList<PreferredStockModel>();

        Cursor c = db.query(PreferredStockEntry.TABLE_NAME, null, null, null, null, null, null);

        while (c.moveToNext())
        {
            PreferredStockModel stock = PreferredStockEntry.MakeModel(c);
            preferredStocks.add(stock);
        }

        c.close();
        db.close();

        return preferredStocks;
    }

}

package me.ablaz101.cpsc370.stockinfogetter;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import me.ablaz101.cpsc370.stockinfogetter.models.LookupResultModel;
import me.ablaz101.cpsc370.stockinfogetter.models.QuoteModel;

/**
 * Created by Xavi Ablaza on 9/19/15.
 */
public class StockInfoAPIWrapper
{
    private static String baseUrl = "http://dev.markitondemand.com/Api/v2/";
    private static String responseFormat = "json";

    /**
     * Gets an array of LookupResults containing the symbol, name and exchange code of a security.
     *
     * @param input A part of a company name or company symbol.
     *              The API will attempt to match a symbol, a partial symbol, and a partial name,
     *              in that order. The input string must be at the beginning of the symbol or one of
     *              the words in the name.
     * @return LookupResultModel[]
     */
    public static LookupResultModel[] GetLookupResults(String input)
    {
        final String method = "Lookup/";
        String queryString = "?input="+input;
        String fullUrl = baseUrl+method+responseFormat+queryString;

        String response="";
        try
        {
            response = new WebRequest(fullUrl).get();
            JSONArray jsonarray = new JSONArray(response);
            LookupResultModel[] lookupResultModels = new LookupResultModel[jsonarray.length()];
            for (int i = 0; i < lookupResultModels.length; i++) {
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String symbol = jsonobject.getString("Symbol");
                String name = jsonobject.getString("Name");
                String exchange = jsonobject.getString("Exchange");
                lookupResultModels[i] = new LookupResultModel(symbol, name, exchange);
            }
            return lookupResultModels;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * Gets a stock quote for one particular company.
     *
     * @param symbol Ticker symbol
     * @return QuoteModel
     */
    public static QuoteModel GetStockQuote(String symbol)
    {
        String[] split = symbol.split(",");
        symbol = split[0];
        final String method = "Quote/";
        String queryString = "?symbol="+symbol;
        String fullUrl = baseUrl+method+responseFormat+queryString;

        String response="";
        try
        {
            response = new WebRequest(fullUrl).get();
//            QuoteModel model = new Gson().fromJson(response, QuoteModel.class);
            JSONObject jsonobject = new JSONObject(response);
            QuoteModel qm = new QuoteModel();
            qm.High = jsonobject.getDouble("High");
            qm.Low = jsonobject.getDouble("Low");
            qm.Open = jsonobject.getDouble("Open");
            return qm;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }

    }
}

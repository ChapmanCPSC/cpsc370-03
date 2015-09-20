package me.ablaz101.cpsc370.assignment1.models;

/**
 * Created by Xavi Ablaza on 9/19/15.
 */
public class QuoteModel
{
    // Status of quote request
    public String Status;

    // Name of the company
    public String Name;

    // The company's ticker symbol
    public String Symbol;

    // The last price of the company's stock
    public double LastPrice;

    // The change in price of the company's stock since the previous trading day's close
    public float Change;

    // The change percent in price of the company's stock since the previous trading day's close
    public float ChangePercent;

    // The last time the company's stock was traded in exchange-local timezone.
    // Represented as ddd MMM d HH:mm:ss UTCzzzzz yyyy
    public String Timestamp;

    // The last time the company's stock was traded in exchange-local timezone.
    // Represented as an OLE Automation date
    public int MSDate;

    // The company's market cap
    public long MarketCap;

    // The trade volume of the company's stock
    public long Volume;

    // The change in price of the company's stock since the start of the year
    public double ChangeYTD;

    // The change percent in price of the company's stock since the start of the year
    public float ChangePercentYTD;

    // The high price of the company's stock in the trading session
    public double High;

    // The low price of the company's stock in the trading session
    public double Low;

    // The opening price of the company's stock at the start of the trading session
    public double Open;
}

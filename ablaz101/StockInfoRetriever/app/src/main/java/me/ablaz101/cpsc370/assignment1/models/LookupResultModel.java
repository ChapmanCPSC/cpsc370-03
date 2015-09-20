package me.ablaz101.cpsc370.assignment1.models;

/**
 * Created by Xavi Ablaza on 9/19/15.
 */
public class LookupResultModel
{
    // The Symbol for this security in the ExchangeSymbol symbol set
    public String symbol;

    // The Name of the security
    public String name;

    // The Exchange code that the security trades on
    public String exchange;

    public LookupResultModel(String symbol, String name, String exchange)
    {
        this.symbol = symbol;
        this.name = name;
        this.exchange = exchange;
    }

}

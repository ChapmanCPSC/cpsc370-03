package me.ablaz101.cpsc370.stockinfogetter.models;

import me.ablaz101.cpsc370.stockinfogetter.models.LookupResultModel;
import me.ablaz101.cpsc370.stockinfogetter.models.QuoteModel;

/**
 * Created by Xavi on 4/10/15.
 */
public class PreferredStockModel
{
    private LookupResultModel lookupResultModel;
    private QuoteModel quoteModel;

    public PreferredStockModel(LookupResultModel lookupResultModel, QuoteModel quoteModel)
    {
        this.lookupResultModel = lookupResultModel;
        this.quoteModel = quoteModel;
    }

    public LookupResultModel getLookupResultModel() {
        return lookupResultModel;
    }

    public void setLookupResultModel(LookupResultModel lookupResultModel) {
        this.lookupResultModel = lookupResultModel;
    }

    public QuoteModel getQuoteModel() {
        return quoteModel;
    }

    public void setQuoteModel(QuoteModel quoteModel) {
        this.quoteModel = quoteModel;
    }
}

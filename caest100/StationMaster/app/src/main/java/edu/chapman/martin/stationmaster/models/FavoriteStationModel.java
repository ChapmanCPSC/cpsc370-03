package edu.chapman.martin.stationmaster.models;

/**
 * Created by Martin on 10/5/2015.
 */
public class FavoriteStationModel
{
    private String code;
    private int count;

    public FavoriteStationModel(String code, int count){
        this.code = code;
        this.count = count;
    }

    public FavoriteStationModel(String code)
    {
        this.code = code;
        this.count = 1;
    }

    public int getCount()
    {
        return count;
    }

    public void setCount(int count)
    {
        this.count = count;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }
}

package com.http;

import java.util.List;

/**
 * Created by zhaojian on 2018/2/28.
 */

public class ListData<T>
{
    private List<T> list;

    private int size;

    public List<T> getList()
    {
        return list;
    }

    public int getSize()
    {
        return size;
    }

    public void setList(List<T> list)
    {
        this.list = list;
    }

    public void setSize(int size)
    {
        this.size = size;
    }
}

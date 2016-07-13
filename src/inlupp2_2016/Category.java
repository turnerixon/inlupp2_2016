package inlupp2_2016;

import com.sun.deploy.util.ArrayUtil;

import java.util.ArrayList;

/**
 * Created by k.Fredrik.Eriksson on 2016-07-12.
 */
public enum Category {
    Undefined,
    Buss,
    Tunnelbana,
    Tåg;

    public static Category[] getSelectableValues()
    {
        Category[] categories = {Category.Buss,Category.Tunnelbana, Category.Tåg};
        return categories;

    }
}

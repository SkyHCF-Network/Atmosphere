package net.skyhcf.atmosphere.shared.utils;

import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.List;

public class ListUtils {

    public static List reverseArrayList(List list) {
        List revArrayList = Lists.newArrayList();
        for (int i = list.size() - 1; i >= 0; i--) {
            revArrayList.add(list.get(i));
        }
        return revArrayList;
    }

}

package com.mproduction.watchplaces.utils;

import com.mproduction.watchplaces.data.LocationRecord;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseUtils {
    public static int getInteger(HashMap<String, Object> map, String key, int defaultValue) {
        if (map.containsKey(key)) {
            try {
                return Integer.parseInt(map.get(key).toString());
            } catch (Exception e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static double getDouble(HashMap<String, Object> map, String key, double defaultValue) {
        if (map.containsKey(key)) {
            try {
                return Double.parseDouble(map.get(key).toString());
            } catch (Exception e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static float getFloat(HashMap<String, Object> map, String key, float defaultValue) {
        if (map.containsKey(key)) {
            try {
                return Float.parseFloat(map.get(key).toString());
            } catch (Exception e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static boolean getBoolean(HashMap<String, Object> map, String key, boolean defaultValue) {
        if (map.containsKey(key)) {
            try {
                return Boolean.parseBoolean(map.get(key).toString());
            } catch (Exception e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static long getLong(HashMap<String, Object> map, String key, long defaultValue) {
        if (map.containsKey(key)) {
            try {
                return Long.parseLong(map.get(key).toString());
            } catch (Exception e) {
                return defaultValue;
            }
        } else {
            return defaultValue;
        }
    }

    public static ArrayList<String> getStringList(HashMap<String, Object> map, String key) {
        ArrayList<String> result = new ArrayList<>();

        try {
            HashMap<String, String> data = (HashMap<String, String>) map.get(key);

            for (String item : data.keySet()) {
                result.add(item);
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public static HashMap<String, String> getStringListMap(ArrayList<String> data) {
        HashMap<String, String> result = new HashMap<>();

        try {
            for (String item : data) {
                result.put(item, item);
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public static HashMap<String, ArrayList<String>> getStringListMap(HashMap map, String key) {
        HashMap<String, ArrayList<String>> result = new HashMap<>();

        try {
            HashMap<String, HashMap<String, String>> data = (HashMap<String, HashMap<String, String>>) map.get(key);

            for (String id : data.keySet()) {
                HashMap<String, String> item = new HashMap<>();
                ArrayList<String> items = new ArrayList<>();
                for (String itemId: item.keySet()) {
                    items.add(itemId);
                }
                result.put(id, items);
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public static HashMap<String, HashMap<String, String>> getStringMapListMap(HashMap<String, ArrayList<String>> data) {
        HashMap<String, HashMap<String, String>> result = new HashMap<>();

        try {
            for (String id : data.keySet()) {
                HashMap<String, String> items = new HashMap<>();
                for (String key : data.get(id)) {
                    items.put(key, key);
                }
                result.put(id, items);
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public static HashMap<String, Integer> getStrIntegerList(HashMap<String, Object> map, String key) {
        HashMap<String, Integer> result = new HashMap<String, Integer>();

        try {
            HashMap<String, Object> data = (HashMap<String, Object>) map.get(key);

            for (String item : data.keySet()) {
                result.put(item, Integer.parseInt(data.get(item).toString()));
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public static HashMap<String, Double> getStrDoubleList(HashMap<String, Object> map, String key) {
        HashMap<String, Double> result = new HashMap<String, Double>();

        try {
            HashMap<String, Object> data = (HashMap<String, Object>) map.get(key);

            for (String item : data.keySet()) {
                result.put(item, Double.parseDouble(data.get(item).toString()));
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public static boolean checkData(HashMap<String, Object> map, String key, Object value) {
        if (value != null && map.containsKey(key) && map.get(key).equals(value)) {
            return true;
        }
        return false;
    }

    public static double getDoubleStr(String str, int index, double defaultValue) {
        try {
            return Double.parseDouble(str.split(";")[index]);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static long getLongStr(String str, int index, long defaultValue) {
        try {
            return Long.parseLong(str.split(";")[index]);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static int getIntegerStr(String str, int index, int defaultValue) {
        try {
            return Integer.parseInt(str.split(";")[index]);
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getStr(String str, int index, String defaultValue) {
        try {
            return str.split(";")[index];
        } catch (Exception e) {
            return defaultValue;
        }
    }

    public static String getLocationRecordsString(ArrayList<LocationRecord> data) {
        String result = "";

        if (data != null) {
            for (LocationRecord item : data) {
                result += (result.length() > 0 ? "," : "") + item.getStrData();
            }
        }

        if (result.length() > 0) {
            return result;
        } else {
            return null;
        }
    }

    public static ArrayList<LocationRecord> getLocationRecordsFromData(HashMap<String, Object> map, String key) {
        ArrayList<LocationRecord> result = new ArrayList<>();

        try {
            String data = (String) map.get(key);

            for (String item : data.split(",")) {
                result.add(LocationRecord.fromStr(item));
            }
        } catch (Exception e) {
            return result;
        }
        return result;
    }
}

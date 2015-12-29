package co.lujun.shuzhi.util;

import java.security.NoSuchAlgorithmException;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author: lujun(http://blog.lujun.co)
 * Date: 15-12-30 00:42
 */
public class SignatureUtils {

    /**
     * 签名
     * @param token
     * @param map
     * @return
     */
    public static String makeSignature(String token, Map<String, String> map){
        if (map == null || map.isEmpty()){
            return "";
        }
        Map<String, String> sortedMap = new TreeMap<String, String>(new Comparator<String>(){

            @Override
            public int compare(String lhs, String rhs) {
                int intKey1 = 0, intKey2 = 0;
                try {
                    intKey1 = getInt(lhs);
                    intKey2 = getInt(rhs);
                } catch (Exception e) {
                    intKey1 = 0;
                    intKey2 = 0;
                }
                return intKey1 - intKey2;
            }
        });
        sortedMap.putAll(map);
        StringBuilder builder = new StringBuilder();
        Set<Map.Entry<String, String>> set = sortedMap.entrySet();
        for (Iterator<Map.Entry<String, String>> iterator = set.iterator(); iterator.hasNext();) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) iterator.next();
            builder.append(entry.getKey() + entry.getValue());
        }
        builder.append(token);
        try {
            return MD5.getMD5(builder.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    /** */
    private static int getInt(String str){
        int i = 0;
        try {
            Pattern p = Pattern.compile("^\\d+");
            Matcher m = p.matcher(str);
            if (m.find()) {
                i = Integer.valueOf(m.group());
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return i;
    }

}

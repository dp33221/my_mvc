package utils;

/**
 * @author dingpei
 * @Description: todo
 * @date 2020/11/28 10:22 下午
 */
public class HelpUtils {
    //首字母转小写
    public static String toLowerCaseFirstOne(String s){
        if (Character.isLowerCase(s.charAt( 0 ))) {
            return s;
        }
        else {
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
        }
    }
}

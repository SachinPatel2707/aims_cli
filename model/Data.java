package model;

public class Data
{
    private static String userName;
    private static String token;
    private static Integer category;
    private static Integer curSem = 3;
    private static String outputPath;

    public static Integer getCurSem() {
        return curSem;
    }

    public static void setCurSem(Integer curSem) {
        Data.curSem = curSem;
    }

    public static String getOutputPath() {
        return outputPath;
    }

    public static void setOutputPath(String outputPath) {
        Data.outputPath = outputPath;
    }

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String uName) {
        userName = uName;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String t) {
        token = t;
    }

    public static Integer getCategory() {
        return category;
    }

    public static void setCategory(Integer cat) {
        category = cat;
    }

    public static void resetData ()
    {
        setCategory(null);
        setToken(null);
        setUserName(null);
    }
}
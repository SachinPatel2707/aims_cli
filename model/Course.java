package model;

public class Course {
    String code;
    String name;
    Integer credits;
    Integer l;
    Integer t;
    Integer p;

    public Course () {}

    public Course (String code, String name, Integer credits, Integer l, Integer t, Integer p) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.l = l;
        this.t = t;
        this.p = p;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public Integer getL() {
        return l;
    }

    public void setL(Integer l) {
        this.l = l;
    }

    public Integer getT() {
        return t;
    }

    public void setT(Integer t) {
        this.t = t;
    }

    public Integer getP() {
        return p;
    }

    public void setP(Integer p) {
        this.p = p;
    }
}

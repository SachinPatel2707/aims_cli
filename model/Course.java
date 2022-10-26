package model;

import java.util.ArrayList;
import java.util.List;

public class Course {
    String code;
    String name;
    Integer credits;
    Integer l;
    Integer t;
    Integer p;
    List<String> preReq;
    String offeredBy;

    public Course () {}

    public Course (String code, String name, Integer credits, Integer l, Integer t, Integer p) {
        this.code = code;
        this.name = name;
        this.credits = credits;
        this.l = l;
        this.t = t;
        this.p = p;
        preReq = new ArrayList<>();
    }

    public String getOfferedBy() {
        return offeredBy;
    }

    public void setOfferedBy(String offeredBy) {
        this.offeredBy = offeredBy;
    }

    public List<String> getPreReq() {
        return preReq;
    }

    public void setPreReq(List<String> preReq) {
        this.preReq = preReq;
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

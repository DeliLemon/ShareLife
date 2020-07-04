package com.it.database;

public class Attention {

    public String m_user,a_user;

    public Attention(String m_user, String a_user) {
        this.m_user = m_user;
        this.a_user = a_user;
    }

    public String getM_user() {
        return m_user;
    }

    public Attention() {
    }

    public void setM_user(String m_user) {
        this.m_user = m_user;
    }

    public String getA_user() {
        return a_user;
    }

    public void setA_user(String a_user) {
        this.a_user = a_user;
    }
}

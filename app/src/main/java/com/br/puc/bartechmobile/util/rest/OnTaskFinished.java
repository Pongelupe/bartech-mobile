package com.br.puc.bartechmobile.util.rest;

public interface OnTaskFinished {

    <T> void taskfinished(T response, RequestEnum request);
}

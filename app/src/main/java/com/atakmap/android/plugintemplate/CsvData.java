package com.atakmap.android.plugintemplate;

import java.util.Date;

public class CsvData {
    int id_stacji;
    String stacja;
    Date data_pomiaru;
    int godzina_pomiaru;
    double temperatura;
    int predkosc_wiatru;
    int kierunek_wiatru;
    double wilgotnosc_wzgledna;
    double suma_opadu;
    double cisnienie;

    public int getId_stacji() {
        return id_stacji;
    }

    public void setId_stacji(int id_stacji) {
        this.id_stacji = id_stacji;
    }

    public String getStacja() {
        return stacja;
    }

    public void setStacja(String stacja) {
        this.stacja = stacja;
    }

    public Date getData_pomiaru() {
        return data_pomiaru;
    }

    public void setData_pomiaru(Date data_pomiaru) {
        this.data_pomiaru = data_pomiaru;
    }

    public int getGodzina_pomiaru() {
        return godzina_pomiaru;
    }

    public void setGodzina_pomiaru(int godzina_pomiaru) {
        this.godzina_pomiaru = godzina_pomiaru;
    }

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public int getPredkosc_wiatru() {
        return predkosc_wiatru;
    }

    public void setPredkosc_wiatru(int predkosc_wiatru) {
        this.predkosc_wiatru = predkosc_wiatru;
    }

    public int getKierunek_wiatru() {
        return kierunek_wiatru;
    }

    public void setKierunek_wiatru(int kierunek_wiatru) {
        this.kierunek_wiatru = kierunek_wiatru;
    }

    public double getWilgotnosc_wzgledna() {
        return wilgotnosc_wzgledna;
    }

    public void setWilgotnosc_wzgledna(double wilgotnosc_wzgledna) {
        this.wilgotnosc_wzgledna = wilgotnosc_wzgledna;
    }

    public double getSuma_opadu() {
        return suma_opadu;
    }

    public void setSuma_opadu(double suma_opadu) {
        this.suma_opadu = suma_opadu;
    }

    public double getCisnienie() {
        return cisnienie;
    }

    public void setCisnienie(double cisnienie) {
        this.cisnienie = cisnienie;
    }

    @Override
    public String toString() {
        return "CsvData{" +
                "id_stacji=" + id_stacji +
                ", stacja='" + stacja + '\'' +
                ", data_pomiaru=" + data_pomiaru +
                ", godzina_pomiaru=" + godzina_pomiaru +
                ", temperatura=" + temperatura +
                ", predkosc_wiatru=" + predkosc_wiatru +
                ", kierunek_wiatru=" + kierunek_wiatru +
                ", wilgotnosc_wzgledna=" + wilgotnosc_wzgledna +
                ", suma_opadu=" + suma_opadu +
                ", cisnienie=" + cisnienie +
                '}';
    }
}

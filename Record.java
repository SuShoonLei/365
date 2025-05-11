package project2;

import java.io.Serializable;

public class Record implements Serializable {
    String vaersId;
    String recvdDate;
    String state;
    String ageYrs;
    String sex;
    String died;
    String vaxType;
    String vaxName;
    String symptom1;
    String symptom2;


    public Record(String vaersId, String recvdDate, String state, String ageYrs, String sex, String died,
                  String vaxType, String vaxName, String symptom1, String symptom2) {
        this.vaersId = vaersId;
        this.recvdDate = recvdDate;
        this.state = state;
        this.ageYrs = ageYrs;
        this.sex = sex;
        this.died = died;
        this.vaxType = vaxType;
        this.vaxName = vaxName;
        this.symptom1 = symptom1;
        this.symptom2 = symptom2;
    }

    @Override
    public String toString() {
        return vaersId + " | " + recvdDate + " | " + state + " | " + ageYrs + " | " + sex + " | " +
                died + " | " + vaxType + " | " + vaxName + " | " + symptom1 + " | " + symptom2;
    }
}

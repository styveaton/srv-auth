package com.moneyline.srvauth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    /*
     *  sname is firstanme
     */
    private String sname;
    /*
     *  name is lastname
     */
    private String name;
    /*
     *  Adresse email
     */
    private String email;
    /*
     *  ind is country location
     */
    private Integer ind;
    /*
     *  lng is language
     */
    private String lng;
    /*
     *  rsn is Reason why the account is created
     */
    private String rsn;
    /*
     *  phn is phone number
     */
    private String phn;
    /*
     *  scd is secret code as password
     */
    private String scd;
    /*
     *  rscd is repeat form value of secret code
     */
    private String rscd;
    /*
     *  emei is device identification id
     */
    private String emei;
    /*
     *  adress is location adress of customer
     */
    private String adress;
    /*
     *  cp is postal code of customer
     */
    private String cp;
}

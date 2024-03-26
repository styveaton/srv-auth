package com.moneyline.srvauth.utils;


public enum Messagerie {

    AUTH_FIRST_NAME_REQUIS(1107000,"SEC001","AUTH_FIRST_NAME_REQUIS","Votre Prenom est requis pour continuer l'operation"),
    AUTH_LAST_NAME_REQUIS(1107001,"SEC002","AUTH_LAST_NAME_REQUIS","Votre Nom est requis pour continuer l'operation"),
    AUTH_MAIL_REQUIS(1107002,"SEC010","AUTH_MAIL_REQUIS","Votre adresse email est requise pour continuer l'operation"),
    AUTH_COUNTRY_REQUIS(1107003,"SEC003","AUTH_COUNTRY_REQUIS","Pays de residence requis"),
    AUTH_PHONE_REQUIS(1107004,"SEC004","AUTH_PHONE_REQUIS","Numero de Telephone requis"),
    AUTH_ADRESS_REQUIS(1107005,"SEC005","AUTH_ADRESS_REQUIS","Adresse du domicile requise"),
    AUTH_POSTALCODE_REQUIS(1107006,"SEC006","AUTH_POSTALCODE_REQUIS","Code Postal requis"),
    AUTH_RAISONCREATIONCOMPTE_REQUIS(1107007,"SEC007","AUTH_RAISONCREATIONCOMPTE_REQUIS","Veuillez definir la raison principale valide  de votre adhesion"),
    AUTH_CODESECRET_REQUIS(11070018,"SEC008","AUTH_CODESECRET_REQUIS","Code secret requis"),
    AUTH_CODESECRET_INVALIDE(1107009,"SEC009","AUTH_CODESECRET_REQUIS","Le code secret doit contenir ["+Parameter.SECRET_CODE_LENGTH+"] chiffres"),
    AUTH_SUBSCRIBE_DONE(1107010,"SEC011","AUTH_SUBSCRIBE_DONE","Compte enregistre! Veuillez confirmer le code recu par Sms"),
    AUTH_USER_NOTFOUND(1107011,"SEC012","AUTH_USER_NOTFOUND","Utilisateur inconnu du systeme"),
    AUTH_BAD_PASSWORD(1107012,"SEC013","AUTH_BAD_PASSWORD","Mot de passe incorrect"),
    AUTH_ACCOUNT_NEED_ACTIVATION(1107014,"SEC014","AUTH_ACCOUNT_NEED_ACTIVATION","Ce compte n'a pas ete activé. Une activation est requise"),
    AUTH_AUTHENTICATE_DONE(1107015,"SEC015","AUTH_AUTHENTICATE_DONE","Utilisateur authentifie avec succes"),
    AUTH_SENDCODETOPHONE_DONE(1107016,"SEC016","AUTH_SENDCODETOPHONE_DONE","Vous recevrez un code a 6 chiffres valide 10 minutes"),
    AUTH_TOKENVALIDATION_DONE(1107017,"SEC017","AUTH_TOKENVALIDATION_DONE","Le Token est valide"),
    AUTH_TOKENVALIDATION_EXPIRED(1107018,"SEC018","AUTH_TOKENVALIDATION_EXPIRED","Le Token a expiré"),
    AUTH_VERIFICATION_CODE_NOT_MATCH(1107019,"SEC019","AUTH_VERIFICATION_CODE_NOT_MATCH","Code de verification incorrect"),
    AUTH_VERIFICATION_CODE_EXPIRED(1107020,"SEC020","AUTH_VERIFICATION_CODE_EXPIRED","Code de verification a expiré"),
    AUTH_VERIFICATION_CODE_DONE(1107021,"SEC021","AUTH_VERIFICATION_CODE_DONE","Code de verification valide"),
    AUTH_CHANGEPIN_DONE(1107022,"SEC022","AUTH_CHANGEPIN_DONE","Votre pin a été modifié"),
    AUTH_UPDATEACCOUNT_DONE(1107023,"SEC023","AUTH_UPDATEACCOUNT_DONE","La mise a jour a ete enregistre"),
    AUTH_CHANGEPIN_NOTALLOW(1107023,"SEC023","AUTH_CHANGEPIN_NOTALLOW","Erreur 1107023"),
    AUTH_VERIFICATIONCODE_NOTALLOW(1107024,"SEC024","AUTH_VERIFICATIONCODE_NOTALLOW","Erreur 1107024"),
    AUTH_COUNTRY_INCONNU(1107025,"SEC025","AUTH_COUNTRY_INCONNU","Pays non reconnu"),
    AUTH_GET_COUNTRY_DONE(1107026,"SEC026","AUTH_GET_COUNTRY_DONE","Pays reconnu"),
    AUTH_GET_USER_DONE(1107027,"SEC027","AUTH_GET_USER_DONE","Utilisateur Trouve"),
    ;

    private final String KEY;
    private final String mdefault;
    private final String internalcode;
    private final int code;

    Messagerie(int code, String internalcode, String KEY,String mdefault) {
        this.KEY = KEY;
        this.mdefault = mdefault;
        this.code = code;
        this.internalcode = internalcode;
    }

    public String getKEY() {
        return KEY;
    }

    public String getMdefault() {
        return mdefault;
    }

    public int getCode() {
        return code;
    }

    public String getInternalcode() {
        return internalcode;
    }
}

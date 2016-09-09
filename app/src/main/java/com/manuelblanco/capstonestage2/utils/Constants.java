package com.manuelblanco.capstonestage2.utils;

/**
 * Created by manuel on 17/07/16.
 */
public class Constants {

    public static final String TAB_POSITION = "tab_position";
    public static final String FRAGMENT_MAIN = "main";
    public static final String FRAGMENT_COUNTRIES = "countries";
    public static final String FRAGMENT_FAVORITES = "favorites";
    public static final String FRAGMENT_DETAIL_COUNTRY = "detail_country";
    public static final String DIALOG_MAP = "dialog_map";
    public static final String TAB_TYPE = "tab_type";
    public static final String ITEM_MENU = "item_menu";


    //Url builders. Paths for any service.
    public static final String SCHEME_URL = "http";
    public static final String SCHEME_URL_HTTPS = "https";
    public static final String AUTHORITY_URL = "mcomobile.com";
    public static final String AUTHORITY_URL_FACEBOOK = "facebook.com";
    public static final String PATH_FLAGS_ISO = "flags_iso";
    public static final String PATH_FLAGS_ISO_RESOL = "128";
    public static final String PATH_TYPES = "types";
    public static final String PATH_TEST = "test";
    public static final String PATH_FACEBOOK_ID = "mytriptop";

    //Url builders for backendless
    public static final String AUTHORITY_BACKENDLESS = "api.backendless.com";
    public static final String PATH_FILES = "files";
    public static final String PATH_PHOTOS_FOLDER = "trip_photos";

    //Extension for the images.
    public static final String PNG = ".png";
    public static final String JPG = ".jpg";

    //DataBase constants
    public static final String DATABASE_NAME = "triptop.db";
    public static final int DATABASE_VERSION = 1;

    //Keys for countries detail fragment.
    public static final String KEY_TRIPS = "trips";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_LOCATION = "location";
    public static final String DIALOG_TYPES = "dialog_types";
    public static final String FRAGMENT_REGISTER = "register";
    public static final String FRAGMENT_LOGIN = "login";

    //Share Preferences for the Types of tourism
    public static final String PREFERENCE_TYPES = "TripTop_Preferences";
    public static final String DIALOG_AROUNDME = "dialog_aroundme";
    public static final String FRAGMENT_ADD_TRIP = "add_trip";
    public static final String DIALOG_LOGIN = "dialog_login";
    public static final String DIALOG_PHOTO = "dialog_photo";
    public static final String DIALOG_WARNING = "dialog_warning";

    //Camera and Gallery request
    public static final int SELECT_FILE = 0;
    public static final int REQUEST_CAMERA = 1;
    public static final String KEY_DIALOG_TEXT = "text_dialog";
    public static final String DIALOG_INFO = "dialog_info";
    public static final String DIALOG_SPLASH = "dialog_splash";

    //Photo directory backendless
    public static final String PATH_PHOTOS = "trip_photos";
    public static final String DIALOG_POSITION = "dialog_position";
    public static final String KEY_TITLE = "title_trip";
    public static final String KEY_RECOMMEND = "type_recommend";

    //Types of recommends
    public static final String TYPE_EAT = "eat";
    public static final String TYPE_SLEEP = "sleep";
    public static final String DIALOG_RECOMMEND = "dialog_recommend";
    public static final String KEY_IDTRIP = "idTrip";
    public static final String KEY_ORIGIN_POSITION = "origin_position";
    public static final String FROM_RECOMMENDS = "from_recommends";
    public static final String FROM_ADD_TRIP = "from_add_trip";
    public static final String KEY_LOGIN_FROM_MENU = "login_from_menu";
    public static final String KEY_LOGIN_FROM_ADD = "login_from_add";
    public static final String FRAGMENT_DETAIL = "fragment_detail";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_COORDS = "coords";
    public static final String FRAGMENT_TYPES = "fragment_types";
    public static final String SELECTED_TYPE = "selected_type";
    public static final String KEY_CONTINENT = "continent";
    public static final String KEY_TYPE_WARNING = "type_warning";

    public enum AdapterTypes {
        TYPE_FAVORITES,
        TYPE_TRIPS,
        TYPE_ROUTES,
        TYPE_COUNTRIES,
        TYPE_TOURISM
    }

    public static final int warningGPS = 0;
    public static final int warningSave = 1;


    public enum CodeFlags {
        ad,
        ae,
        af,
        ag,
        ai,
        al,
        am,
        an,
        ao,
        aq,
        ar,
        as,
        at,
        au,
        aw,
        ax,
        az,
        ba,
        bb,
        bd,
        be,
        bf,
        bg,
        bh,
        bi,
        bj,
        bl,
        bm,
        bn,
        bo,
        bq,
        br,
        bs,
        bt,
        bv,
        bw,
        by,
        bz,
        ca,
        cc,
        cd,
        cf,
        cg,
        ch,
        ci,
        ck,
        cl,
        cm,
        cn,
        co,
        cr,
        cu,
        cv,
        cw,
        cx,
        cy,
        cz,
        de,
        dj,
        dk,
        dm,
        dom,
        dz,
        ec,
        ee,
        eg,
        eh,
        er,
        es,
        et,
        fi,
        fj,
        fk,
        fm,
        fo,
        fr,
        ga,
        gb,
        gd,
        ge,
        gf,
        gg,
        gh,
        gi,
        gl,
        gm,
        gn,
        gp,
        gq,
        gr,
        gs,
        gt,
        gu,
        gw,
        gy,
        hk,
        hm,
        hn,
        hr,
        ht,
        hu,
        id,
        ie,
        il,
        im,
        in,
        io,
        iq,
        ir,
        is,
        it,
        je,
        jm,
        jo,
        jp,
        ke,
        kg,
        kh,
        ki,
        km,
        kn,
        kp,
        kr,
        kw,
        ky,
        kz,
        la,
        lb,
        lc,
        li,
        lk,
        lr,
        ls,
        lt,
        lu,
        lv,
        ly,
        ma,
        mc,
        md,
        me,
        mf,
        mg,
        mh,
        mk,
        ml,
        mm,
        mn,
        mo,
        mp,
        mq,
        mr,
        ms,
        mt,
        mu,
        mv,
        mw,
        mx,
        my,
        mz,
        na,
        nc,
        ne,
        nf,
        ng,
        ni,
        nl,
        no,
        np,
        nr,
        nu,
        nz,
        om,
        pa,
        pe,
        pf,
        pg,
        ph,
        pk,
        pl,
        pm,
        pn,
        pr,
        ps,
        pt,
        pw,
        py,
        qa,
        re,
        rn,
        ro,
        rs,
        ru,
        rw,
        sa,
        sb,
        sc,
        sd,
        se,
        sg,
        sh,
        si,
        sj,
        sk,
        sl,
        sm,
        sn,
        so,
        sr,
        ss,
        st,
        sv,
        sx,
        sy,
        sz,
        tc,
        td,
        tf,
        tg,
        th,
        tj,
        tk,
        tl,
        tm,
        to,
        tr,
        tt,
        tv,
        tw,
        tz,
        ua,
        ug,
        um,
        us,
        uy,
        uz,
        va,
        vc,
        ve,
        vg,
        vi,
        vn,
        vu,
        wf,
        ws,
        ye,
        yt,
        za,
        zm,
        zw

    }
}
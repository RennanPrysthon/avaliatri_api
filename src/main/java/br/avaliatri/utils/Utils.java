package br.avaliatri.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Utils {
    private static Utils utils;
    private Date dataAtual;
    private SimpleDateFormat sdf;
    private static final String PATTERN = "dd/MM/yyyy";

    public static Utils getInstancia() {
        if(utils == null)
            utils = new Utils();
        return utils;
    }

    public Utils() {
    }

    public Date getDataAtual() {
        dataAtual = new Date(System.currentTimeMillis());
        return dataAtual;
    }

    private Boolean isToday(Date data) {
        Calendar atual = Calendar.getInstance();
        atual.setTime(this.getDataAtual());
        Calendar passada = Calendar.getInstance();
        passada.setTime(data);

        return passada.get(Calendar.DAY_OF_MONTH) == atual.get(Calendar.DAY_OF_MONTH);
    }

    public String getDataFormatada(Date data) {
        if(data == null)
            return null;

        if(this.sdf == null)
            sdf = new SimpleDateFormat(PATTERN);
        return sdf.format(data);
    }
}

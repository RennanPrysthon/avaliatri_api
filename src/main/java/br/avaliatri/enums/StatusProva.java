package br.avaliatri.enums;

public enum StatusProva {
    FEITA(1, "Feita"),
    NAO_FEITA(2, "Nao feita");
    private int cod;
    private String situacao;

    StatusProva(int cod, String situacao) {
        this.cod = cod;
        this.situacao = situacao;
    }

    public int getCod() {
        return cod;
    }

    public String getSituacao() {
        return situacao;
    }

    public static StatusProva toEnum(Integer cod) {
        if(cod == null || cod < 0) {
            return null;
        }

        for (StatusProva s: StatusProva.values()) {
            if(cod.equals(s.getCod())){
                return s;
            }
        }
        throw new IllegalArgumentException("Id invalido: " + cod);
    }
}

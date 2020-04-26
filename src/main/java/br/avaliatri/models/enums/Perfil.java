package br.avaliatri.models.enums;

public enum Perfil {
    ALUNO(1, "Aluno"),
    PROFESSOR(2, "Professor");

    private int cod;
    private String role;

    private Perfil(int cod, String role) {
        this.cod = cod;
        this.role = role;
    }

    public int getCod() {
        return cod;
    }

    public String getRole() {
        return role;
    }

    public static Perfil toEnum(Integer cod) {
        if(cod == null || cod < 0) {
            return null;
        }
        for(Perfil x : Perfil.values()) {
            if(cod.equals(x.getCod())){
                return x;
            }
        }

        throw new IllegalArgumentException("Id invalido: " + cod);
    }
}

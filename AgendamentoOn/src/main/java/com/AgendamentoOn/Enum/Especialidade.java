package com.AgendamentoOn.Enum;

public enum Especialidade {
    CILIOS_VOLUME_BRASILEIRO("Cílios: Volume brasileiro"),
    CILIOS_EFEITO_FOX("Cílios: Efeito fox"),
    CILIOS_EFEITO_MOLHADO("Cílios: Efeito Molhado"),
    CILIOS_VOLUME_5D("Cílios: Volume 5D"),
    SOBRANCELHAS_DESIGN("Sobrancelhas: Design de sobrancelhas"),
    SOBRANCELHAS_DESIGN_HENNA("Sobrancelhas: Design com henna"),
    SOBRANCELHAS_BROW_LAMINATION("Sobrancelhas: Brow lamination"),
    SOBRANCELHAS_LASH_LIFTING("Sobrancelhas: Lash lifting"),
    BLOQUEADO("Horário Bloqueado");

    private final String descricao;

    Especialidade(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static Especialidade fromDescricao(String descricao) {
        for (Especialidade e : values()) {
            if (e.getDescricao().equalsIgnoreCase(descricao)) {
                return e;
            }
        }
        throw new IllegalArgumentException("Especialidade inválida: " + descricao);
    }
}

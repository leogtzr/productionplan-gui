package com.production.domain;

/**
 * @author lgutierr <leogutierrezramirez@gmail.com>
 */
public enum WorkCenterTurns {
    
    // Primer turno: 8.5 hrs
    // Segundo turno: 8.1 hrs
    // Tercer turno: 5.5 hrs
    
    DOBLADO(3), PUNZONADO(3)
    , MAQUINADO_MANUAL(2), LASER(2)         // no se separan por archivos, una sola máquina
    , EMPAQUE_A_PROVEEDOR(0), EMPAQUE_FINAL(0), ENSAMBLE(0), INSERTOS_PEM(0)
    , INSPECCION_DE_ACABADOS(0), LIMPIEZA(0), LIMPIEZA_LUZ_NEGRA(0), MAQUINADO_CNC(0)
    , PINTURA_EN_POLVO(0), PULIDO(0), REBABEO(0), SERIGRAFIA(0), SOLDADURA(0)
    , SPOT_WELD(0), SURTIR_MATERIAL(0), TIME_SAVER(0), TRATAMIENTO_QUIMICO(0)
    ;

    private final int turns;

    WorkCenterTurns(final int turns) {
        this.turns = turns;
    }

    public int turns() {
        return this.turns;
    }

}

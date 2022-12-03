/*------------------------------------------------------------------------------------------
:*                         TECNOLOGICO NACIONAL DE MEXICO
:*                                CAMPUS LA LAGUNA
:*                     INGENIERIA EN SISTEMAS COMPUTACIONALES
:*                             DESARROLLO EN ANDROID "A"
:*
:*                   SEMESTRE: AGO-DIC/2022    HORA: 08-09 HRS
:*
:*                          Clase POJO de los permisos
:*
:*  Archivo     : Imagen.java
:*  Autor       : Mario Alberto Vázquez Anaya (18131289)
:*  Fecha       : 22/11/2022
:*  Compilador  : Android Studio Chipmunk 2021.2.1
:*  Descripción : Esta clase contiene la plantilla para identificar los permisos, su nombre, si es
:*                obligatorio y si se ha otorgado
:*  Ultima modif:
:*  Fecha       Modificó             Motivo
:*==========================================================================================
:*------------------------------------------------------------------------------------------*/
package util.permisos;

public class PermisoApp {

    private String permiso      = "";
    private String nombreCorto  = "";
    private boolean obligatorio = false;
    private boolean otorgado    = false;

    public PermisoApp ( String permiso, String nombreCorto, boolean obligatorio  ) {
        this.permiso = permiso;
        this.nombreCorto = nombreCorto;
        this.obligatorio = obligatorio;
    }

    public String getPermiso() {
        return permiso;
    }

    public void setPermiso(String permiso) {
        this.permiso = permiso;
    }

    public String getNombreCorto() {
        return nombreCorto;
    }

    public void setNombreCorto(String nombreCorto) {
        this.nombreCorto = nombreCorto;
    }

    public boolean isObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public boolean isOtorgado() {
        return otorgado;
    }

    public void setOtorgado(boolean otorgado) {
        this.otorgado = otorgado;
    }
}

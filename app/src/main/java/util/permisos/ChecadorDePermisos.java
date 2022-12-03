/*------------------------------------------------------------------------------------------
:*                         TECNOLOGICO NACIONAL DE MEXICO
:*                                CAMPUS LA LAGUNA
:*                     INGENIERIA EN SISTEMAS COMPUTACIONALES
:*                             DESARROLLO EN ANDROID "A"
:*
:*                   SEMESTRE: AGO-DIC/2022    HORA: 08-09 HRS
:*
:*              Clase que verifica y solicita los permisos de la aplicación
:*
:*  Archivo     : Imagen.java
:*  Autor       : Mario Alberto Vázquez Anaya (18131289)
:*  Fecha       : 22/11/2022
:*  Compilador  : Android Studio Chipmunk 2021.2.1
:*  Descripción : Este activity verifica que la aplicación cuente con los permisos suficientes
:*                para que la aplicación pueda realizar todas las funciones requeridas
:*  Ultima modif:
:*  Fecha       Modificó             Motivo
:*==========================================================================================
:*------------------------------------------------------------------------------------------*/
package util.permisos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ChecadorDePermisos {
    public static final int CODIGO_PEDIR_PERMISOS = 44;

    public static void checarPermisos ( Activity activity, PermisoApp [] permisosReq ) {
        // Si el arreglo de permisos no trae nada retornamos
        if ( permisosReq == null || permisosReq.length == 0 )
            return;

        // Verificar cuales permisos estan otorgados y marcalos
        for ( int i = 0; i < permisosReq.length; i++ ) {
            if ( ContextCompat.checkSelfPermission ( activity, permisosReq [ i ].getPermiso () )
                 == PackageManager.PERMISSION_GRANTED
               )
            {
               permisosReq [ i ].setOtorgado ( true );
            }
        }

        // Determinar la lista de permisos que no estan otorgados
        ArrayList<String> arrTmp = new ArrayList<> ();
        for ( int i = 0; i < permisosReq.length; i++ ) {
            if ( permisosReq [ i ].isOtorgado() == false )
                arrTmp.add ( permisosReq [ i ].getPermiso() );
        }

        // Si hay permisos sin otorgar pedirlos explicitamente al usuario
        if ( arrTmp.size() > 0 ) {
            // Convertir el arraylist temporal a un arreglo de Strings
            String [] permisosPendientes = new String [ arrTmp.size() ];
            arrTmp.toArray ( permisosPendientes );

            ActivityCompat.requestPermissions ( activity, permisosPendientes, CODIGO_PEDIR_PERMISOS );
        }
    }

    public static void verificarPermisosSolicitados (
                 Activity activity, PermisoApp [] permisosReq, String[] permissions, int[] grantResults )
    {
        ArrayList<String> arrTmp = new ArrayList<> ();
        for ( int i = 0; i < permisosReq.length; i++ )
            arrTmp.add ( permisosReq [ i ].getPermiso () );

        String permisosOblitariosNoOtorgados = "";

        if ( grantResults.length > 0  ) {
            // Recorrer el arreglo de resultados de permisos otorgados
            for ( int i = 0; i < grantResults.length; i ++ ) {
                // Buscar el nombre del permiso pendiente en los permisos de la ap
                int index = arrTmp.indexOf ( permissions [ i ] );
                // Si el usuario otorgó el permiso que estaba pendiente
                if ( grantResults [ i ] == PackageManager.PERMISSION_GRANTED )
                    // Registrarlo en el arreglo de permisos de la app
                    permisosReq [ index ].setOtorgado ( true );
                else if ( permisosReq [ index ].isObligatorio () ) {
                    // Si no se otorgo un permiso que es obligatorio anexar al string de no otorgados
                    permisosOblitariosNoOtorgados += permisosReq [ index ].getNombreCorto () + ", ";
                }
            }

            // Checar si hubo permisos obligatorios que no otorgó el usuario
            if (  ! permisosOblitariosNoOtorgados.isEmpty() ) {
                alertarYSalir ( activity, permisosOblitariosNoOtorgados );
            }
        }
    }

    private static void alertarYSalir ( final Activity activity, String noOtorgados ) {
        // Avisar al usuario que no se puede continuar y salimos del Activity
        AlertDialog.Builder builder = new AlertDialog.Builder ( activity );
        builder.setTitle ( "Permisos requeridos" )
                .setMessage  ( "Los siguientes permisos son necesarios para " +
                                "usar esta funcionalidad: \n\n" +
                                noOtorgados + "\n\n" +
                               "Conceda los permisos cuando se soliciten." )
                .setCancelable ( false )
                .setNegativeButton("Salir", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.finish();
                    }
                } )
                .create()
                .show();
    }
}

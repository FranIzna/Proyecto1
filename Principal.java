package com.example.chrno.proyecto2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Principal extends AppCompatActivity {
    private static ClaseAdaptador cl;
    private ListView lv;
    private List<Contacto> agenda;//creo un arraylist que llenare con los contactos y mas tarde le pasare a la clase adaptador
    private TextView telf2, telf3, telf4;
    private Intent i;

    public static void actualizar() {
        Agenda.ordenar();
        cl.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        init();
        telf2=(TextView) findViewById(R.id.edTelefono2);
        telf3=(TextView) findViewById(R.id.edTelefono3);
        telf4=(TextView) findViewById(R.id.edTelefono4);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add){
            añadir();
            return true;
        }
        if(id==R.id.action_ordena_asc) {
            Agenda.ordenar();
            cl.notifyDataSetChanged();
            return true;
        }
        if(id==R.id.action_ordena_desc) {
            Agenda.ordenaInverso();
            cl.notifyDataSetChanged();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void plus(View v){
        cl.plus(v);
    }

    private void init() {
        lv= (ListView) findViewById(R.id.lvContactos);

        Agenda a=new Agenda(this);
        agenda=a.getAgenda();//lleno la lista con todos los contactos que devuelve el metodo getListaContactos
        Contacto c;//creo un contacto auxiliar que llenaré con el contacto que me devuelva em metodo getListaContactos

            for(Contacto aux:agenda)//recorro todos los contactos de mi agenda
                aux.setNumeros(a.getListaNum(this, aux.getId()));//con un set le meto al contacto el numero de telefono

        cl = new ClaseAdaptador(this, R.layout.elemento_lista,agenda);//declaro el adaptador
        lv.setAdapter(cl);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int posicion, long id) {
                llamar(posicion);
            }
        });
        registerForContextMenu(lv);//registramos nuestro menu contextual
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo) {//creamos nuestro menu contextual
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_contextual, menu);
    }

    public boolean onContextItemSelected(MenuItem item){
        AdapterView.AdapterContextMenuInfo vistainfo = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int posicion = vistainfo.position;//cogemos la posicion del elemento pulsado en la vista
        switch(item.getItemId()){//acciones que hara nuestro menu contextual

            case R.id.action_edit:
                editar(posicion);
                return true;
            case R.id.action_remove:
                borrar(posicion);
                return true;
            default: return super.onContextItemSelected(item);
        }
    }

    public void add(View v){
        añadir();
    }

    public void añadir(){
         i = new Intent(this, FormAdd.class);
        startActivity(i);
    }

    public void editar(int pos){
         i = new Intent(this, FormEdit.class);
        Bundle b=new Bundle();
        b.putInt("id", pos);
        i.putExtras(b);

        startActivity(i);

//       actualiza();
    }

    public void borrar(final int pos){
        String s=this.getString(R.string.confBorrar)+agenda.get(pos).getNombre()+this.getString(R.string.interrogacion);
        AlertDialog.Builder dialogo1 = new AlertDialog.Builder(this);
        dialogo1.setTitle(this.getString(R.string.importante));
        dialogo1.setMessage(s);
        dialogo1.setCancelable(false);
        dialogo1.setNegativeButton(this.getString(R.string.cancelar), null);
        dialogo1.setPositiveButton(this.getString(R.string.confirmar), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogo1, int id) {
                aceptar(pos);
            }
        });
        dialogo1.show();
    }

    public void actualiza(){
        cl.notifyDataSetChanged();
    }

    public void Tostada(String s){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }
    public void aceptar(int pos) {
        Tostada(this.getString(R.string.borrasteA) +agenda.get(pos).getNombre());
        agenda.remove(pos);
        actualiza();
    }

    public void llamar(int pos){
        String s=Agenda.getContacto(pos).getNum(0);
        Uri numero = Uri.parse( "tel:" + s.toString() );
        Intent i = new Intent(Intent.ACTION_CALL, numero);
        startActivity(i);
    }

    public void asc(View v) {
        Agenda.ordenar();
        cl.notifyDataSetChanged();
    }

    public void desc(View v) {
        Agenda.ordenaInverso();
        cl.notifyDataSetChanged();
    }

//    public void mostrar2(View v){
//        FormAdd.mostrar2(v);
//        telf2.setVisibility(View.VISIBLE);
//    }
//    public void mostrar3(View v){
//        telf3.setVisibility(View.VISIBLE);
//    }
//    public void mostrar4(View v){
//        telf4.setVisibility(View.VISIBLE);
//
//    }
}



package com.unitec.amigos;
//Comentario de verificacion
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ControladorUsuario {
    //Aqui va un metodo que representa cada uno de los estado que vamos a transferir
    //es decir, VA UN GET, POST, PUT Y DELETE. Como minimo

    //Aqui viene ya el uso de la inversion de control
    @Autowired RepositorioUsuario repositorioUsuario;

    //Implementamos el codigo para guardar un usuario en MongoDB
    @PostMapping("/usuario")
        public Estatus guardar(@RequestBody String json)throws Exception{
            //Primero leemos y convertimos el objeto json a objeto Java
            ObjectMapper mapper=new ObjectMapper();
            Usuario u=mapper.readValue(json,Usuario.class);
            //Este usuario, ya en formato json lo guardamos en mongoDB
            repositorioUsuario.save(u);
            //Creamos un objeto de tipo Estatus y este objeto lo retornamos al cliente (android o psot)
            Estatus estatus=new Estatus();
            estatus.setSucces(true);
            estatus.setMensaje("Tu usuario se guardo con exito");
            return estatus;
    }


    @GetMapping("/usuario/{id}")
        public Usuario obtenerPorId(@PathVariable String id){
            //Leemos un usuario con el metodo findById pasandole como argumento el ID que queremos
            //Apoyandonos del repositorioUsuario
            Usuario u=repositorioUsuario.findById(id).get();
            return  u;
        }

    @GetMapping("/usuario")
    public List<Usuario> buscarTodos(){
        return repositorioUsuario.findAll();
    }

    //Metodo para actualizar
    @PutMapping("/usuario")
    public Estatus actualizar(@RequestBody String json)throws Exception{
            //Primero debemos verificar que exista
            ObjectMapper mapper=new ObjectMapper();
            Usuario u=mapper.readValue(json, Usuario.class);
            Estatus e=new Estatus();
            if (repositorioUsuario.findById(u.getEmail()).isPresent()){
                //Lo volvemos a guardar
                repositorioUsuario.save(u);
                e.setMensaje("El Usuario se actualizo con exito");
                e.setSucces(true);
            }else {
                e.setMensaje("El Usuario no existe");
                e.setSucces(false);
            }
            return e;
    }

    @DeleteMapping("/usuario/{id}")
    public Estatus borrar(@PathVariable String id){
        Estatus estatus=new Estatus();
        if (repositorioUsuario.findById(id).isPresent()){
            repositorioUsuario.deleteById(id);
            estatus.setSucces(true);
            estatus.setMensaje("Usuario borrado con exito");
        }else {
            estatus.setSucces(false);
            estatus.setMensaje("Ese usuario no existe");
        }
        return estatus;
    }

}

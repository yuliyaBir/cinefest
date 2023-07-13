"use strict"
import {byId, toon, verberg} from "./util.js";
byId("toevoegen").onclick = async function(){
    verbergFouten();
    const inputTitel = byId("titel");
    if (!inputTitel.checkValidity()){
        toon("titelFout");
        inputTitel.focus();
    }
    const inputJaar = byId("jaar");
    if (!inputJaar.checkValidity()){
        toon("jaarFout");
        inputJaar.focus();
    }
    const film = {
        titel: inputTitel.value,
        jaar: inputJaar.value
    };
    voegToe(film);
}
function verbergFouten(){
    verberg("titelFout");
    verberg("jaarFout");
    verberg("storing");
}
async function voegToe(film){
    const response = await fetch("films",
        {method: "POST",
        headers: {'Content-Type': "application/json"},
        body: JSON.stringify(film)});
    if (response.ok){
        window.location = "alleFilms.html"
    } else{
        toon("storing");
    }
}
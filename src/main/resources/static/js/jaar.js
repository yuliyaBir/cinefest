"use strict"
import {byId, toon, verberg, verwijderChildElementenVan} from "./util.js";
byId("zoek").onclick = async function (){
    verbergFilmsEnFouten();
    const inputJaar = byId("jaar");
    if (inputJaar.checkValidity()){
        findByJaar(inputJaar.value);
    } else{
        toon("jaarFout");
        inputJaar.focus();
    }
}
function verbergFilmsEnFouten(){
    verberg("filmsTable");
    verberg("jaarFout");
    verberg("storing");
}
async function findByJaar(jaar){
    const response = await fetch(`films?jaarIs=${jaar}`);
    if (response.ok){
        const films = await response.json();
        toon("filmsTable");
        const filmsBody = byId("filmsBody");
        console.log(filmsBody)
        // if (filmsBody.children > 0){
            verwijderChildElementenVan("filmsBody");
        // }
        for (const film of films){
            const tr = filmsBody.insertRow();
            tr.insertCell().innerText = film.id;
            tr.insertCell().innerText = film.titel;
            tr.insertCell().innerText = film.jaar;
            tr.insertCell().innerText = film.vrijePlaatsen;
        }
    } else{
        toon("storing");
    }
}
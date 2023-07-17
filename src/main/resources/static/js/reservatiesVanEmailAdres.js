"use strict"
import {byId, verberg, toon, setText, verwijderChildElementenVan} from "./util.js";

byId("zoek").onclick = async function (){
    verbergFouten();
    const emailInput = byId("email");
    if (emailInput.checkValidity()){
        const reservatiesBody = byId("reservatiesBody");
        verwijderChildElementenVan(reservatiesBody);
        findByEmailAdres(emailInput.value);
    } else{
        toon("emailFout");
        emailInput.focus();
    }
}
function verbergFouten(){
    verberg("emailFout");
    verberg("storing");
}
async function findByEmailAdres(email){
    const response = await fetch(`reservatie?emailAdres=${email}`);
    if (response.ok){
        toon("reservatiesTabel");
        const reservaties = await response.json();
        for (const reservatie of reservaties){
            const tr = reservatiesBody.insertRow();
            tr.insertCell().innerText = reservatie.id;
            tr.insertCell().innerText = reservatie.titel;
            tr.insertCell().innerText = reservatie.plaatsen;
            tr.insertCell().innerText = new Date(reservatie.besteld).toLocaleString("nl-BE");
        }
    } else{
        if (response.status === 404){
            const responseBody = await response.json();
            setText("notFound", responseBody.message);
            toon("notFound");
        } else{
            toon("storing");
        }
    }
}
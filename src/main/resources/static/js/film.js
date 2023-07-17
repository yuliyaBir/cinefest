"use strict";
import {byId, toon, verberg, setText} from "./util.js";
byId("zoek").onclick = async function () {
    verbergFilmEnFouten();
    const zoekIdInput = byId("zoekId");
    if (zoekIdInput.checkValidity()) {
        findById(zoekIdInput.value);
    } else {
        toon("zoekIdFout");
        zoekIdInput.focus();
    }
};
function verbergFilmEnFouten() {
    verberg("film");
    verberg("zoekIdFout");
    verberg("nietGevonden");
    verberg("storing");
    verberg("nieuweTitelFout");
    verberg("emailFout");
    verberg("aantalFout");
    verberg("plaatsenNietGenoeg");
}

async function findById(id) {
    const response = await fetch(`films/${id}`);
    if (response.ok) {
        const film = await response.json();
        toon("film");
        setText("titel", film.titel);
        setText("jaar", film.jaar);
        setText("vrijePlaatsen", film.vrijePlaatsen);
        const button = byId("verwijder");
        button.onclick = async function () {
            const response = await fetch(`films/${film.id}`, {
                method: "DELETE"
            });
            if (response.ok) {
               verbergFilmEnFouten();
            } else {
                toon("storing");
            }
        }
    } else {
        if (response.status === 404) {
            toon("nietGevonden");
        } else {
            toon("storing");
        }
    }
}
byId("bewaar").onclick = async function(){
    var nieuweTitelInput = byId("nieuweTitel");
    if (nieuweTitelInput.checkValidity()){
        verberg("nieuweTitelFout");
        wijzigTitel(nieuweTitelInput.value);
    }else{
        toon("nieuweTitelInput");
        nieuweTitelInput.focus();
    }
}
async function wijzigTitel(nieuweTitel){
    const titelWijziging = {
        titel: nieuweTitel
    }
    const response = await fetch(`films/${byId("zoekId").value}/titel`,
        {
            method: "PATCH",
            headers: {'Content-Type': "application/json"},
            body: JSON.stringify(titelWijziging)
        });
    if (response.ok){
        setText("titel", nieuweTitel);
    }else{
        toon("storing");
    }
}
byId("reserveer").onclick = async function (){
    const emailInput = byId("email");
    const aantalInput = byId("aantal");
    if (!emailInput.checkValidity()){
        toon("emailFout");
        emailInput.focus();
    }
    if (!aantalInput.checkValidity()){
        toon("aantalFout");
        aantalInput.focus();
    }
    const reservatiePoging = {
        email: emailInput.value,
        aantal: aantalInput.value
    }
    console.log(emailInput.value);
    reserveer(reservatiePoging);
}

async function reserveer(reservatiePoging){
    console.log(reservatiePoging);
    const response = await fetch(`films/${byId("zoekId").value}/reservatie`,
        {
            method: "POST",
            headers: {'Content-Type': "application/json"},
            body: JSON.stringify(reservatiePoging)
        });
    console.log(JSON.stringify(reservatiePoging));
    if (response.ok){
        verberg("plaatsenNietGenoeg");
        window.location = "alleFilms.html";
    }else{
        switch (response.status){
            case 404:
                toon("nietGevonden");
                break;
            case 409:
                const responseBody = await response.json();
                setText("plaatsenNietGenoeg", responseBody.message);
                toon("plaatsenNietGenoeg");
                break;
            default:
                toon("storing");
        }
    }
}
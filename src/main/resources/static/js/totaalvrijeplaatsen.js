"use strict";
import {setText, toon} from "./util.js";
const response = await fetch("films/totaalVrijePlaatsen");
if (response.ok){
    const body = await response.text();
    setText("totaal", body);
} else{
    toon("storing");
}
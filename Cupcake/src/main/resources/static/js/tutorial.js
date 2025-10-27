/*

    JavaScript Tutorial / Wiki

    Last updated by: Guacamoleboy
    Updated: 27/10-2025

*/


// ------------------------------------------------------
// document
// ------------------------------------------------------
// Repræsenterer hele HTML-dokumentet i browseren.
// Alt på siden (elementer, tekst, billeder, scripts) findes inde i document-objektet.


// ------------------------------------------------------
// document.getElementById(id)
// ------------------------------------------------------
// Finder og returnerer et element med et specifikt id.
// Returnerer kun ét element, da id’er skal være unikke.


// ------------------------------------------------------
// document.getElementsByClassName(className)
// ------------------------------------------------------
// Returnerer en HTMLCollection af alle elementer med den givne klasse.
// Du kan tilgå elementer ved indeks, fx [0], [1] osv.


// ------------------------------------------------------
// document.getElementsByTagName(tagName)
// ------------------------------------------------------
// Returnerer alle elementer med et bestemt HTML-tag, fx <div>, <p> osv.


// ------------------------------------------------------
// document.querySelector(selector)
// ------------------------------------------------------
// Finder det FØRSTE element i DOM’en, der matcher en CSS-selector (fx .klasse eller #id).


// ------------------------------------------------------
// document.querySelectorAll(selector)
// ------------------------------------------------------
// Finder ALLE elementer, der matcher en selector.
// Returnerer en NodeList, som kan loopes igennem.


// ------------------------------------------------------
// document.addEventListener(event, function)
// ------------------------------------------------------
// Lytter efter en hændelse (fx 'click', 'input', 'keydown') og udfører en funktion, når den sker.
// Første argument = typen af event
// Andet argument = funktionen der skal udføres.


// ------------------------------------------------------
// element.addEventListener(event, function)
// ------------------------------------------------------
// Samme som ovenfor, men bruges direkte på et specifikt element i stedet for hele document.


// ------------------------------------------------------
// element.textContent
// ------------------------------------------------------
// Læser eller ændrer den tekst, der står inde i et element (uden HTML-tags).


// ------------------------------------------------------
// element.innerHTML
// ------------------------------------------------------
// Læser eller ændrer HTML-indholdet inde i et element.
// Tillader HTML-tags (brug forsigtigt ved brugerinput pga. sikkerhed).


// ------------------------------------------------------
// element.style.property
// ------------------------------------------------------
// Giver adgang til at ændre CSS direkte fra JavaScript.
// Fx element.style.color = "red" eller element.style.backgroundColor = "blue".


// ------------------------------------------------------
// element.classList.add("className")
// ------------------------------------------------------
// Tilføjer en CSS-klasse til elementet.


// ------------------------------------------------------
// element.classList.remove("className")
// ------------------------------------------------------
// Fjerner en CSS-klasse fra elementet.


// ------------------------------------------------------
// element.classList.toggle("className")
// ------------------------------------------------------
// Tilføjer klassen, hvis den ikke findes — fjerner den, hvis den allerede findes.


// ------------------------------------------------------
// function navn(parameter) { ... }
// ------------------------------------------------------
// En funktion er et genanvendeligt stykke kode, som kan kaldes når som helst.
// Parameter = input, return = output.


// ------------------------------------------------------
// const navn = (parameter) => { ... }
// ------------------------------------------------------
// En “arrow function”, kortere syntaks for funktioner.
// Har ikke sin egen “this”-værdi (arver fra den omgivende kontekst).


// ------------------------------------------------------
// var / let / const
// ------------------------------------------------------
// var → gammel måde at erklære variabler (funktion-scope).
// let → moderne måde (blok-scope).
// const → konstant (kan ikke gen-tildeles).


// ------------------------------------------------------
// for (let i = 0; i < antal; i++) { ... }
// ------------------------------------------------------
// Loop der gentager kode et bestemt antal gange.


// ------------------------------------------------------
// while (betingelse) { ... }
// ------------------------------------------------------
// Kører så længe betingelsen er sand (true).


// ------------------------------------------------------
// array.forEach(function(item) { ... })
// ------------------------------------------------------
// Løber igennem hvert element i et array og udfører funktionen på hvert element.


// ------------------------------------------------------
// Array-metoder
// ------------------------------------------------------
// arr.push(værdi) → Tilføjer bagpå
// arr.pop() → Fjerner sidste
// arr.shift() → Fjerner første
// arr.unshift() → Tilføjer forrest
// arr.map() → Omdanner hvert element og returnerer et nyt array
// arr.filter() → Returnerer kun elementer, der opfylder en betingelse
// arr.find() → Returnerer det første element, der matcher en betingelse
// arr.length → Antal elementer i arrayet


// ------------------------------------------------------
// Object
// ------------------------------------------------------
// Samling af egenskaber (key:value).
// const person = { navn: "Ali", alder: 21 }
// person.navn → "Ali"
// person["alder"] → 21


// ------------------------------------------------------
// console.log(værdi)
// ------------------------------------------------------
// Udskriver tekst eller værdier i browserens “Console” (til debugging).


// ------------------------------------------------------
// setTimeout(function, millisekunder)
// ------------------------------------------------------
// Udfører funktionen én gang efter et bestemt tidsrum.


// ------------------------------------------------------
// setInterval(function, millisekunder)
// ------------------------------------------------------
// Udfører funktionen gentagne gange med et bestemt tidsinterval.


// ------------------------------------------------------
// fetch(url)
// ------------------------------------------------------
// Henter data fra et API eller en ekstern ressource.
// Returnerer et “Promise”, som man kan håndtere med .then() eller async/await.


// ------------------------------------------------------
// async function navn() { await ... }
// ------------------------------------------------------
// Gør det muligt at bruge “await” inde i funktionen, så man kan vente på async-kald.
// Gør koden lettere at læse end kæder af .then().


// ------------------------------------------------------
// if (betingelse) { ... } else { ... }
// ------------------------------------------------------
// Betinget udførsel – kører kun koden, hvis betingelsen er sand.


// ------------------------------------------------------
// switch (værdi) { case ...: ...; break; }
// ------------------------------------------------------
// Alternativ til mange if/else – bruges når man vil tjekke én værdi mod flere mulige cases.


// ------------------------------------------------------
// Math-objektet
// ------------------------------------------------------
// Indeholder matematiske funktioner som:
// Math.random() → Tilfældig værdi mellem 0 og 1
// Math.floor() → Runder ned
// Math.round() → Runder til nærmeste heltal
// Math.pow(a, b) → a opløftet i b


// ------------------------------------------------------
// class Navn { constructor() { ... } metode() { ... } }
// ------------------------------------------------------
// Bruges til at oprette objekter ud fra en skabelon.
// constructor() kører, når objektet laves med “new”.


// ------------------------------------------------------
// JSON.stringify(objekt)
// ------------------------------------------------------
// Konverterer et JavaScript-objekt til en tekststreng (JSON-format).


// ------------------------------------------------------
// JSON.parse(jsonString)
// ------------------------------------------------------
// Konverterer en JSON-tekst tilbage til et JavaScript-objekt.


// ------------------------------------------------------
// try { ... } catch (error) { ... }
// ------------------------------------------------------
// Bruges til at fange og håndtere fejl uden at programmet stopper.


// ------------------------------------------------------
// typeof værdi
// ------------------------------------------------------
// Returnerer typen af en variabel (string, number, boolean, object, undefined, osv.).


// ------------------------------------------------------
// window
// ------------------------------------------------------
// Globalt objekt der repræsenterer selve browser-vinduet.
// Indeholder fx window.alert(), window.prompt(), window.location, window.scrollTo() osv.


// ------------------------------------------------------
// alert("besked")
// ------------------------------------------------------
// Viser en lille pop-up besked i browseren.


// ------------------------------------------------------
// prompt("besked")
// ------------------------------------------------------
// Viser en pop-up med et tekstfelt og returnerer brugerens input.


// ------------------------------------------------------
// confirm("besked")
// ------------------------------------------------------
// Viser en pop-up med OK / Annuller og returnerer true eller false.


// ------------------------------------------------------
// export / import
// ------------------------------------------------------
// Bruges i moduler (ES Modules) til at dele kode mellem filer.
// export → gør noget tilgængeligt
// import → henter det ind i en anden fil.


// ------------------------------------------------------
// event.target
// ------------------------------------------------------
// Refererer til det element, der udløste eventet (fx det der blev klikket på).


// ------------------------------------------------------
// event.preventDefault()
// ------------------------------------------------------
// Stopper standard-adfærden for et event (fx forhindrer en formular i at reloade siden).


// ------------------------------------------------------
// event.stopPropagation()
// ------------------------------------------------------
// Stopper eventet fra at “boble” videre op til forældre-elementer.


// ------------------------------------------------------
// "use strict";
// ------------------------------------------------------
// Aktiverer “strict mode” – hjælper med at finde fejl og håndhæver bedre kodestandard.


// ------------------------------------------------------
// this
// ------------------------------------------------------
// Refererer til det aktuelle objekt i den sammenhæng, funktionen blev kaldt i.


// ------------------------------------------------------
// NaN / null / undefined
// ------------------------------------------------------
// NaN → “Not a Number” (ugyldig talværdi).
// null → Ingen værdi, sat med vilje.
// undefined → Ingen værdi, ikke sat.

// ____________________________________________________________________________________________
//                         CUSTOM JAVASCRIPT SPECIFIC FOR OUR PROJECTS
// ____________________________________________________________________________________________

// ------------------------------------------------------
// showNotification("besked", "farve")
// ------------------------------------------------------
//  Loader en besked i højre side af skærmen med en specifik farve og besked
//  Kan vælges mellem rød, grøn og orange. Farverne hentes fra .css filen hvorfor
//  Det er vigtigt at de selvfølgelig er der. Dette bør opdateres pr projekt.
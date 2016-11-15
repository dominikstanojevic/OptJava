Budući da RAPGA može koristiti više operatora križanja i operatora mutiranja, možemo dodati različite opcije i tako
poboljšati rad algoritma. Primjerice, dodajući razne logičke operatore za operatore kržanje npr. I, ILI, ekskluzivno ILI.
Ako stopu mutacija izaberemo razumno (dolje objašnjenje), pronalazak rješenja postaje trivijalno.

Napomena: Stopa mutacija mora biti razumna. Primjerice za n = 1000, izabrati stopa = 0.1 (10%) naravno da neće dati
rješenje (u prosjeku 100 bitova će biti mutirano). Stopa mora biti izabrana kako bi za određen n mutirala nekoliko
(jednoznamenkasti broj) bitova. Mutacije možemo čak i zabraniti (staviti stopu na 0), križanje (tj. širok izbor operatora)
je zapravo operator koji dovodi do pronalaska rješenja.

Uzmimo u obzir AND operator kržanja. Nastat će dijete čiji bitovi su računati operacijom AND(prviRoditelj, drugiRoditelj).
To križanje je korisno u slučaju tražnja minimuma. S druge strane, operator OR će pomoći u slučaju da tražimo maksimum.